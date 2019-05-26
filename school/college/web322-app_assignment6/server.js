/*********************************************************************************
* WEB322 – Assignment 06
* I declare that this assignment is my own work in accordance with Seneca Academic Policy. No part of this
* assignment has been copied manually or electronically from any other source (including web sites) or
* distributed to other students.
*
* Name: Eric McDonald Student ID: 153581160 Date: 2019-04-12
*
* Online (Heroku) Link: https://tranquil-river-90749.herokuapp.com/
*
********************************************************************************/

const express = require('express');
const dataServ = require(__dirname + '/data-service.js');
const multer = require('multer');
const fs = require('fs');
const bodyParser = require('body-parser');
const path = require('path');
const expHandlebars = require('express-handlebars');
const dataServiceAuth = require("./data-service-auth");
const clientSessions = require("client-sessions");

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
app.use(clientSessions({
    cookieName: "session",
    secret: "web322_assignment6-mycoolsecrettokenverylong",
    duration: 2 * 60 * 1000,
    activeDuration: 1000 * 60
  }));
app.use(function(req, res, next) {
    res.locals.session = req.session;
    next();
});
function ensureLogin(req, res, next) {
    if (!req.session.user) {
      res.redirect("/login");
    } else {
      next();
    }
  }

app.get('/', function (req, res) {
    res.render("home");
});
app.get('/img/background.png', (req, res) => {
    res.sendFile(__dirname + "/img/background.png");
});
app.get('/img/navbar_background.png', (req, res) => {
    res.sendFile(__dirname + "/img/navbar_background.png");
});
app.get('/img/rotating_img.png', (req, res) => {
    res.sendFile(__dirname + "/img/rotating_img.png");
});
app.get('/home', function (req, res) {
    res.render("home");
});
app.get('/about', function (req, res) {
    res.render("about");
});
app.get("/employees/add", ensureLogin, (req, res) => {
    var promise = dataServ.getDepartments();
    promise.then((data) => {
        res.render("addEmployee", {departments: data});
    }).catch((reason) => {
        res.render("addEmployee", {departments: []});
    });
});
app.get("/departments/add", ensureLogin, (req, res) => {
    res.render("addDepartment");
});
app.post("/employee/update", ensureLogin, (req, res) => {
    let promise = dataServ.updateEmployee(req.body);
    promise.then(function(employees) {
        res.redirect("/employees");
    });
});
app.post("/department/update", ensureLogin, (req, res) => {
    let promise = dataServ.updateDepartment(req.body);
    promise.then(function(departments) {
        res.redirect("/departments");
    });
});
app.get('/department/:id', ensureLogin, (req, res) => {
    let promise = dataServ.getDepartmentById(req.params.id);
    promise.then(function(departments) {
        res.render("department", {
            department: departments,
        });
    });
    promise.catch(function(messageVal) {
        res.status(404).send("Department Not Found");
    });
});
/*app.get('/employee/:id', (req, res) => {
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
});*/
app.get("/employee/delete/:empNum", ensureLogin, (req, res) => {
    let promise = dataServ.deleteEmployeeByNum(req.params.empNum);
    promise.then(() => {
        res.redirect("/employees");
    }).catch((reason) => {
        res.status(500).send(reason);
    });
});
app.get("/employee/:empNum", ensureLogin, (req, res) => {
    // initialize an empty object to store the values
    let viewData = {};
    dataServ.getEmployeeByNum(req.params.empNum)
    .then((data) => {
    viewData.data = data; //store employee data in the "viewData" object as "data"
    }).catch(()=>{
    viewData.data = null; // set employee to null if there was an error
    }).then(dataServ.getDepartments)
    .then((data) => {
    viewData.departments = data; // store department data in the "viewData" object as "departments"
   
    // loop through viewData.departments and once we have found the departmentId that matches
    // the employee's "department" value, add a "selected" property to the matching
    // viewData.departments object
    for (let i = 0; i < viewData.departments.length; i++) {
    if (viewData.departments[i].departmentId == viewData.data.department) {
    viewData.departments[i].selected = true;
    }
    }
    }).catch(()=>{
    viewData.departments=[]; // set departments to empty if there was an error
    }).then(()=>{
    if(viewData.data == null){ // if no employee - return an error
    res.status(404).send("Employee Not Found");
    }else{
    res.render("employee", { viewData: viewData }); // render the "employee" view
    }
    });
   });
app.get('/employees', ensureLogin, function (req, res) {
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
            employeeList: [],
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
app.get('/departments', ensureLogin, function (req, res) {
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
app.get("/images/add", ensureLogin, (req, res) => {
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
app.post("/images/add", ensureLogin, upload.single('imageFile'), (req, res) => {
    res.redirect("/images");
});
app.get("/images", ensureLogin, (req, res) => {
    fs.readdir("./public/images/uploaded", (err, items) => {
        res.render('images', {
            images: items
        });
    });
});
app.post("/employees/add", ensureLogin, (req, res) => {
    dataServ.addEmployee(req.body).then(() => {
        res.redirect("/employees");
    });
});
app.post("/departments/add", ensureLogin, (req, res) => {
    dataServ.addDepartment(req.body).then(() => {
        res.redirect("/departments");
    });
});
app.get("/login", (req, res) => {
    res.render("login");
});
app.get("/register", (req, res) => {
    res.render("register");
});
app.post("/register", (req, res) => {
    let promise = dataServiceAuth.registerUser(req.body);
    promise.then(() => {
        res.render("register", {successMessage: "User created"});
    }).catch((err) => {
        res.render("register", {errorMessage: err, userName: req.body.userName} );
    });
});
app.post("/login", (req, res) => {
    req.body.userAgent = req.get('User-Agent');
    dataServiceAuth.checkUser(req.body).then((user) => {
        req.session.user = {
        userName: user.userName,
        email: user.email,
        loginHistory: user.loginHistory
        }
        res.redirect('/employees');
       }).catch((err) => {
        res.render("login", {errorMessage: err, userName: req.body.userName} );
    });
});
app.get("/logout", (req, res) => {
    req.session.reset();
    res.redirect("/");
});
app.get("/userHistory", ensureLogin, (req, res) => {
    res.render("userHistory", {session: req.session});
});
app.all('*', function(req, res) {
    res.sendFile(__dirname + "/views/404page.html");
});
var initPromise = dataServ.initialize();
initPromise.then(function(outVal) {
    dataServiceAuth.initialize().then(() => {
        console.log(outVal);
        app.listen(HTTP_PORT, onHttpStart);
    });
});
initPromise.catch(function(reason) {
    console.log(reason);
});