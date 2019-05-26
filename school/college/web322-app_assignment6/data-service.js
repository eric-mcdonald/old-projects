const Sequelize = require('sequelize');
var sequelize = new Sequelize('dcal6asuikaaja', 'cmiyscuivzeteh', '4fdd228a69d08944f42d445a7479b7b94a12073afda84b7caed873a0b07f592b', {
 host: 'ec2-23-23-195-205.compute-1.amazonaws.com',
 dialect: 'postgres',
 port: 5432,
 dialectOptions: {
 ssl: true
 }
});

var Employee = sequelize.define('Employee', {
    employeeNum: {
        type: Sequelize.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    firstName: Sequelize.STRING,
    last_name: Sequelize.STRING,
    email: Sequelize.STRING,
    SSN: Sequelize.STRING,
    addressStreet: Sequelize.STRING,
    addresCity: Sequelize.STRING,
    addressState: Sequelize.STRING,
    addressPostal: Sequelize.STRING,
    maritalStatus: Sequelize.STRING,
    isManager: Sequelize.BOOLEAN,
    employeeManagerNum: Sequelize.INTEGER,
    status: Sequelize.STRING,
    department: Sequelize.INTEGER,
    hireDate: Sequelize.STRING
});
var Department = sequelize.define('Department', {
    departmentId: {
        type: Sequelize.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    departmentName: Sequelize.STRING
});

module.exports.initialize = function() {
    return new Promise(function(resolve, reject) {
        sequelize.sync().then(() => {
            resolve("Successfully synchronized the database.");
        }).catch((reason) => {
            reject("Failed to synchronize the database with error: " + reason);
        });
    });
};
module.exports.getAllEmployees = function() {
    return new Promise(function(resolve, reject) {
        Employee.findAll().then((data) => {
            if (data.length == 0) {
                reject("No results returned.");
            } else {
                resolve(data);
            }
        }).catch(() => {
            reject("No results returned.");
        });
    });
};
module.exports.getManagers = function() {
    return new Promise(function(resolve, reject) {
        Employee.findAll({
            where: {
                isManager: [true]
            }
        }).then((data) => {
            if (data.length == 0) {
                reject("No results returned.");
            } else {
                resolve(data);
            }
        }).catch(() => {
            reject("No results returned.");
        });
    });
};
module.exports.getDepartments = function() {
    return new Promise(function(resolve, reject) {
        Department.findAll().then((data) => {
            if (data.length == 0) {
                reject("No results returned.");
            } else {
                resolve(data);
            }
        }).catch(() => {
            reject("No results returned.");
        });
    });
};
module.exports.deleteEmployeeByNum = function(num) {
    return new Promise(function(resolve, reject) {
        Employee.destroy({
            where: {
                employeeNum: num 
            }
        }).then(() => {
            resolve();
        }).catch(() => {
            reject("Unable to Remove Employee");
        });
    });
};
module.exports.updateEmployee = function(data) {
    data.isManager = data.isManager ? true : false;
    for (var i in data) {
        if (data[i] == "") {
            data[i] = null;
        }
    }
    return new Promise(function(resolve, reject) {
        Employee.update({
            employeeNum: data.employeeNum,
            firstName: data.firstName,
            last_name: data.lastName,
            email: data.email,
            SSN: data.SSN,
            addressStreet: data.addressStreet,
            addresCity: data.addressCity,
            addressState: data.addressState,
            addressPostal: data.addressPostal,
            maritalStatus: data.maritalStatus,
            isManager: data.isManager,
            employeeManagerNum: data.employeeManagerNum,
            status: data.status,
            department: data.department,
            hireDate: data.hireDate
        }, {
            where: {
                employeeNum: data.employeeNum
            }
        }).then(() => {
            resolve();
        }).catch(() => {
            reject("Unable to update employee.");
        });
    });
};
module.exports.getDepartmentById = function(idIn) {
    return new Promise(function(resolve, reject) {
        Department.findAll({
            where: {
                departmentId: idIn
            }
        }).then((data) => {
            if (data.length == 0) {
                reject("No results returned.");
            } else {
                resolve(data[0]);
            }
        }).catch(() => {
            reject("No results returned.");
        });
    });
};
module.exports.addDepartment = function(data) {
    for (var i in data) {
        if (data[i] == "") {
            data[i] = null;
        }
    }
    return new Promise(function(resolve, reject) {
        Department.create({
            departmentId: data.departmentId,
            departmentName: data.departmentName
        }).then(() => {
            resolve();
        }).catch(() => {
            reject("Unable to create department.");
        });
    });
};
module.exports.updateDepartment = function(data) {
    for (var i in data) {
        if (data[i] == "") {
            data[i] = null;
        }
    }
    return new Promise(function(resolve, reject) {
        Department.update({
            departmentId: data.departmentId,
            departmentName: data.departmentName
        }, {
            where: {
                departmentId: data.departmentId
            }
        }).then(() => {
            resolve();
        }).catch(() => {
            reject("Unable to update department.");
        });
    });
};
module.exports.addEmployee = function(data) {
    data.isManager = data.isManager ? true : false;
    for (var i in data) {
        if (data[i] == "") {
            data[i] = null;
        }
    }
    return new Promise(function(resolve, reject) {
        Employee.create({
            employeeNum: data.employeeNum,
            firstName: data.firstName,
            last_name: data.lastName,
            email: data.email,
            SSN: data.SSN,
            addressStreet: data.addressStreet,
            addresCity: data.addressCity,
            addressState: data.addressState,
            addressPostal: data.addressPostal,
            maritalStatus: data.maritalStatus,
            isManager: data.isManager,
            employeeManagerNum: data.employeeManagerNum,
            status: data.status,
            department: data.department,
            hireDate: data.hireDate
        }).then(() => {
            resolve();
        }).catch(() => {
            reject("Unable to create employee.");
        });
    });
};
module.exports.getEmployeesByStatus = function(statusIn) {
    return new Promise(function(resolve, reject) {
        Employee.findAll({
            where: {
                status: [statusIn]
            }
        }).then((data) => {
            if (data.length == 0) {
                reject("No results returned.");
            } else {
                resolve(data);
            }
        }).catch(() => {
            reject("No results returned.");
        });
    });
};
module.exports.getEmployeesByDepartment = function(departmentIn) {
    return new Promise(function(resolve, reject) {
        Employee.findAll({
            where: {
                department: [departmentIn]
            }
        }).then((data) => {
            if (data.length == 0) {
                reject("No results returned.");
            } else {
                resolve(data);
            }
        }).catch(() => {
            reject("No results returned.");
        });
    });
};
module.exports.getEmployeesByManager = function(managerIn) {
    return new Promise(function(resolve, reject) {
        Employee.findAll({
            where: {
                employeeManagerNum: [managerIn]
            }
        }).then((data) => {
            if (data.length == 0) {
                reject("No results returned.");
            } else {
                resolve(data);
            }
        }).catch(() => {
            reject("No results returned.");
        });
    });
};
module.exports.getEmployeeByNum = function(empNumberIn) {
    return new Promise(function(resolve, reject) {
        Employee.findAll({
            where: {
                employeeNum: [empNumberIn]
            }
        }).then((data) => {
            if (data.length == 0) {
                reject("No results returned.");
            } else {
                resolve(data[0]);
            }
        }).catch(() => {
            reject("No results returned.");
        });
    });
};