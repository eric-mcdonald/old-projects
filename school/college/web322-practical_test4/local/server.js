/*
Establishes a web server that retrieves information from a localhost Postgre database. The localhost database is setup as:
Host: localhost
Port: 5432
Username: postgres
Password: postgres
Database name: postgres

Student name: Eric McDonald
Student ID: 153581160
Date created: 2019-03-12
*/

const express = require("express");
const Sequelize = require("sequelize");

const DB_NAME = "postgres", DB_USER = "postgres", DB_PASS = "postgres",
DB_HOST = "localhost", DB_DIALECT = "postgres", DB_PORT = 5432;
const WEB_SERVER_PORT = process.env.PORT || 8080;

var app = express();
var sequelizeInst = new Sequelize(DB_NAME, DB_USER, DB_PASS, {
    host: DB_HOST,
    dialect: DB_DIALECT,
    port: DB_PORT,
    dialectOptions: {
        ssl: false,
    },
}), sequelizePool = new Sequelize(DB_NAME, DB_USER, DB_PASS, {
    host: DB_HOST,
    dialect: DB_DIALECT,
    port: DB_PORT,
    dialectOptions: {
        ssl: false,
    },
    pool: {
        max: 10,
        min: 0,
        idle: 30000,
    },
});

function sendEmployeeData(res, data) {
    let resBody = "<form>";
    for (let i in data) {
        let row = data[i];
        resBody += "<input type=\"text\" value=\"" + row.empid + "\"/>";
        resBody += "<input type=\"text\" value=\"" + row.name + "\"/>";
        resBody += "<input type=\"text\" value=\"" + row.deptnumber + "\"/>";
        resBody += "<br/>";
    }
    resBody += "</form>";
    res.status(200).send(resBody);
}
sequelizeInst.authenticate().then(function() {
    console.log("Established a connection to the database successfully.");
}).catch(function(err) {
    console.log("Failed to connect to the database with error: ", err);
});
sequelizePool.authenticate().then(function() {
    console.log("Established a connection via pooling to the database successfully.");
}).catch(function(err) {
    console.log("Failed to connect to the database via pooling with error: ", err);
});
app.get("/", function(req, res, next) {
    sequelizeInst.sync().then(function() {
        sequelizeInst.query("SELECT * FROM public.\"Employee\" WHERE empid=1", { type: Sequelize.QueryTypes.SELECT }).then(function(data) {
            sendEmployeeData(res, data);
        }).catch(function(error) {
            console.log("Failed to execute a query with error: ", error);
            res.status(400).send(error);
        });
    });
});
app.get("/sp", function(req, res, next) {
    sequelizeInst.sync().then(function() {
        sequelizeInst.query("SELECT * FROM GetAllEmployee()", { type: Sequelize.QueryTypes.SELECT }).then(function(data) {
            sendEmployeeData(res, data);
        }).catch(function(error) {
            console.log("Failed to execute a query with error: ", error);
            res.status(400).send(error);
        });
    });
});
app.get("/pool", function(req, res, next) {
    sequelizePool.sync().then(function() {
        sequelizePool.query("SELECT * FROM GetAllEmployee()", { type: Sequelize.QueryTypes.SELECT }).then(function(data) {
            sendEmployeeData(res, data);
        }).catch(function(error) {
            console.log("Failed to execute a query via pooling with error: ", error);
            res.status(400).send(error);
        });
    });
});
app.listen(WEB_SERVER_PORT, function() {
    console.log("Listening on port " + WEB_SERVER_PORT + ".");
});
