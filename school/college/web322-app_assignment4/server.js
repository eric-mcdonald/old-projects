/*********************************************************************************
* WEB322 – Assignment 04
* I declare that this assignment is my own work in accordance with Seneca Academic Policy. No part
* of this assignment has been copied manually or electronically from any other source
* (including 3rd party web sites) or distributed to other students.
*
* Name: Eric McDonald Student ID: 153581160 Date: 2019-02-28
*
* Online (Heroku) Link: https://frozen-springs-92199.herokuapp.com/
*
********************************************************************************/ 

const express = require('express');
const dataServ = require(__dirname + '/data-service.js');
const multer = require('multer');
const fs = require('fs');
const bodyParser = require('body-parser');
const path = require('path');
const expHandlebars = require('express-handlebars');

var app = express();
var HTTP_PORT = process.env.PORT || 8080;

app.use(bodyParser.urlencoded({
    extended: true
}));
app.use(express.static('public'));
app.engine('.hbs', expHandlebars({
    extname: '.hbs',
    defaultLayout: 'main',
    helpers: {
    navLink: function(url, options){
        return '<li' +
        ((url == app.locals.activeRoute) ? ' class="active" ' : '') +
        '><a href="' + url + '">' + options.fn(this) + '</a></li>';
       },
       equal: function (lvalue, rvalue, options) {
        if (arguments.length < 3)
            throw new Error("Handlebars Helper equal needs 2 parameters");
        if (lvalue != rvalue) {
            return options.inverse(this);
        } else {
            return options.fn(this);
        }
       },       
    }
}));
app.set('view engine', '.hbs');
app.use(function(req,res,next){
    let route = req.baseUrl + req.path;
    app.locals.activeRoute = (route == "/") ? "/" : route.replace(/\/$/, "");
    next();
});
app.get('/', function (req, res) {
    res.render("home");
});
app.get('/img/background.png', (req, res) => {
    res.sendFile(__dirname + "/img/background.png");
});
app.get('/home', function (req, res) {
    res.render("home");
});
app.get('/about', function (req, res) {
    res.render("about");
});
app.get("/employees/add", (req, res) => {
    res.render("addEmployee");
});
app.post("/employee/update", (req, res) => {
    let promise = dataServ.updateEmployee(req.body);
    promise.then(function(employees) {
        res.redirect("/employees");
    });
   });
   
app.get('/employee/:id', (req, res) => {
    let promise = dataServ.getEmployeeByNum(req.params.id);
    promise.then(function(employees) {
        res.render("employee", {
            employee: employees,
        });
    });
    promise.catch(function(messageVal) {
        res.render("employee", {
            employee: {message: messageVal},
        });
    });
});
app.get('/employees', function (req, res) {
    var promise;
    if (typeof req.query.status !== 'undefined') {
        promise = dataServ.getEmployeesByStatus(req.query.status);
    } else if (typeof req.query.department !== 'undefined') {
        promise = dataServ.getEmployeesByDepartment(req.query.department);
    } else if (typeof req.query.manager !== 'undefined') {
        promise = dataServ.getEmployeesByManager(req.query.manager);
    } else {
        promise = dataServ.getAllEmployees();
    }
    promise.then(function(employees) {
        res.render("employees", {
            employeeList: employees,
        });
    });
    promise.catch(function(messageVal) {
        res.render("employees", {
            employeeList: employees,
            message: messageVal,
        });
    });
});
/*app.get('/managers', function (req, res) {
    var promise = dataServ.getManagers();
    promise.then(function(managers) {
        res.json(managers);
    });
    promise.catch(function(messageVal) {
        res.json({message: messageVal});
    });
});*/
app.get('/departments', function (req, res) {
    var promise = dataServ.getDepartments();
    promise.then(function(departments) {
        res.render("departments", {
            departmentList: departments,
        });
    });
    promise.catch(function(messageVal) {
        res.json({message: messageVal});
    });
});
app.get("/images/add", (req, res) => {
    res.render("addImage");
});
function onHttpStart(){
    console.log('Express http server listening on ' + HTTP_PORT);
};
var storage = multer.diskStorage({
    destination: "./public/images/uploaded",
    filename: function (req, file, cb) {
      cb(null, Date.now() + path.extname(file.originalname));
    }
  });
const upload = multer({
    storage: storage
});
app.post("/images/add", upload.single('imageFile'), (req, res) => {
    res.redirect("/images");
});
app.get("/images", (req, res) => {
    fs.readdir("./public/images/uploaded", (err, items) => {
        res.render('images', {
            images: items
        });
    });
});
app.post("/employees/add", (req, res) => {
    dataServ.addEmployee(req.body).then(() => {
        res.redirect("/employees");
    });
});
app.get('*', function(req, res) {
    res.sendFile(__dirname + "/views/404page.html");
});
var initPromise = dataServ.initialize();
initPromise.then(function(outVal) {
    console.log(outVal);
    app.listen(HTTP_PORT, onHttpStart);
});
initPromise.catch(function(reason) {
    console.log(reason);
});