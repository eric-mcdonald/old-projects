var fs = require('fs');
var employees = [], departments = [];

module.exports.initialize = function() {
    var readPromise = new Promise((resolve, reject) => {
        var data = fs.readFileSync(__dirname + '/data/employees.json');
        employees = JSON.parse(data);
        data = fs.readFileSync(__dirname + '/data/departments.json');
        departments = JSON.parse(data);
        if (employees.length != 0 && departments.length != 0) {
            resolve("Read operation was successful.");
        } else {
            reject("Unable to read data from files.");
        }
    });
    return readPromise;
};
module.exports.getAllEmployees = function() {
    var promise = new Promise((resolve, reject) => {
        if (employees.length != 0) {
            resolve(employees);
        } else {
            reject("No results returned.");
        }
    });
    return promise;
};
module.exports.getManagers = function() {
    var promise = new Promise((resolve, reject) => {
        if (employees.length != 0) {
            var filteredRes = [];
            for (var i in employees) {
                if (employees[i].isManager) {
                    filteredRes.push(employees[i]);
                }
            }
            if (filteredRes.length == 0) {
                reject("No results returned.");
            } else {
                resolve(filteredRes);
            }
        } else {
            reject("No results returned.");
        }
    });
    return promise;
};
module.exports.getDepartments = function() {
    var promise = new Promise((resolve, reject) => {
        if (departments.length != 0) {
            resolve(departments);
        } else {
            reject("No results returned.");
        }
    });
    return promise;
};