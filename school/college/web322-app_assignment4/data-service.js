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
module.exports.updateEmployee = function(data) {
    return new Promise((resolve, reject) => {
        var found = false;
        for (var i in employees) {
            var employee = employees[i];
            if (employee.employeeNum == data.employeeNum) {
                employees[i] = data;
                found = true;
                resolve();
                break;
            }
        }
        if (!found) {
            reject("No results returned");
        }
    });
};
module.exports.addEmployee = function(data) {
    return new Promise((resolve, reject) => {
        data.isManager = typeof data.isManager !== 'undefined';
        data.employeeNum = employees.length + 1;
        employees.push(data);
        resolve();
    });
};
module.exports.getEmployeesByStatus = function(status) {
    return new Promise((resolve, reject) => {
        let matchingEmployees = [];
        if (employees.length != 0) {
            for (var i in employees) {
                if (employees[i].status == status) {
                    matchingEmployees.push(employees[i]);
                }
            }
            resolve(matchingEmployees);
        } else {
            reject("no results returned");
        }
    });
};
module.exports.getEmployeesByDepartment = function(department) {
    return new Promise((resolve, reject) => {
        let matchingEmployees = [];
        if (employees.length != 0) {
            for (var i in employees) {
                if (employees[i].department == department) {
                    matchingEmployees.push(employees[i]);
                }
            }
            resolve(matchingEmployees);
        } else {
            reject("no results returned");
        }
    });
};
module.exports.getEmployeesByManager = function(manager) {
    return new Promise((resolve, reject) => {
        let matchingEmployees = [];
        if (employees.length != 0) {
            for (var i in employees) {
                if (employees[i].employeeManagerNum == manager) {
                    matchingEmployees.push(employees[i]);
                }
            }
            resolve(matchingEmployees);
        } else {
            reject("no results returned");
        }
    });
};
module.exports.getEmployeeByNum = function(empNumber) {
    return new Promise((resolve, reject) => {
        if (employees.length != 0) {
            for (var i in employees) {
                if (employees[i].employeeNum == empNumber) {
                    resolve(employees[i]);
                    break;
                }
            }
        } else {
            reject("no results returned");
        }
    });
};