/*********************************************************************************
* WEB322 – Assignment 03
* I declare that this assignment is my own work in accordance with Seneca Academic Policy. No part
* of this assignment has been copied manually or electronically from any other source
* (including 3rd party web sites) or distributed to other students.
*
* Name: Eric McDonald Student ID: 153581160 Date: 2019-02-10
*
* Online (Heroku) Link: https://mysterious-shelf-85237.herokuapp.com/
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
}));
app.set('view engine', '.hbs');
app.get('/', function (req, res) {
    res.sendFile(__dirname + "/views/home.html");
});
app.get('/img/background.png', (req, res) => {
    res.sendFile(__dirname + "/img/background.png");
});
app.get('/home', function (req, res) {
    res.sendFile(__dirname + "/views/home.html");
});
app.get('/about', function (req, res) {
    res.sendFile(__dirname + "/views/about.html");
});
app.get("/employees/add", (req, res) => {
    res.sendFile(__dirname + "/views/addEmployee.html");
});
app.get('/employees/:id', (req, res) => {
    let promise = dataServ.getEmployeeByNum(req.params.id);
    promise.then(function(employees) {
        res.json(employees);
    });
    promise.catch(function(messageVal) {
        res.json({message: messageVal});
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
        res.json(employees);
    });
    promise.catch(function(messageVal) {
        res.json({message: messageVal});
    });
});
app.get('/managers', function (req, res) {
    var promise = dataServ.getManagers();
    promise.then(function(managers) {
        res.json(managers);
    });
    promise.catch(function(messageVal) {
        res.json({message: messageVal});
    });
});
app.get('/departments', function (req, res) {
    var promise = dataServ.getDepartments();
    promise.then(function(departments) {
        res.json(departments);
    });
    promise.catch(function(messageVal) {
        res.json({message: messageVal});
    });
});
app.get("/images/add", (req, res) => {
    res.sendFile(__dirname + "/views/addImage.html");
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
        res.json({images: items});
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