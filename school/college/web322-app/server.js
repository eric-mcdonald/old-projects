/*********************************************************************************
* WEB322 – Assignment 02
* I declare that this assignment is my own work in accordance with Seneca Academic Policy. No part
* of this assignment has been copied manually or electronically from any other source
* (including 3rd party web sites) or distributed to other students.
*
* Name: Eric McDonald Student ID: 153581160 Date: 2019-01-27
*
* Online (Heroku) Link: https://dry-dusk-82172.herokuapp.com/
*
********************************************************************************/

var express = require('express');
var dataServ = require(__dirname + '/data-service.js');
var app = express();
var HTTP_PORT = process.env.PORT || 8080;

app.use(express.static('public'));
app.get('/', function (req, res) {
    res.sendFile(__dirname + "/views/home.html");
});
app.get('/home', function (req, res) {
    res.sendFile(__dirname + "/views/home.html");
});
app.get('/about', function (req, res) {
    res.sendFile(__dirname + "/views/about.html");
});
app.get('/employees', function (req, res) {
    var promise = dataServ.getAllEmployees();
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
app.get('*', function(req, res) {
    res.sendFile(__dirname + "/views/404page.html");
});
function onhttpstart(){
    console.log('Express http server listening on ' + HTTP_PORT);
};
var initPromise = dataServ.initialize();
initPromise.then(function(outVal) {
    console.log(outVal);
    app.listen(HTTP_PORT, onhttpstart);
});
initPromise.catch(function(reason) {
    console.log(reason);
});