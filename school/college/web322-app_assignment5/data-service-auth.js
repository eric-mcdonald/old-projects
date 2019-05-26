const mongoose = require("mongoose");
var Schema = mongoose.Schema;
var companySchema = new Schema({
    "userName":  String,
    "password": String,
    "email": String,
    "loginHistory": [
        { dateTime: Date, userAgent: String }
    ],
    "country": String
  });
  let User; 

module.exports.initialize = function () {
    return new Promise((resolve, reject) => {
        let db = mongoose.createConnection("mongodb+srv://dbUser:<password>@senecaweb-yx7h8.mongodb.net/test?retryWrites=true");
        db.on('error', (err)=> {
            reject(err);
        });
        db.once('open', ()=>{
            User = db.model("users", userSchema);
            resolve();
        });
    });
};
module.exports.registerUser = function(userData) {
    return new Promise((resolve, reject) => {
        if (userData.password != userData.pasword2) {
            reject("Passwords do not match");
            return;
        }
        let newUser = new User(userData);
        newUser.save((err) => {
            if (err) {
                if (err.code == 11000) {
                    reject("User Name already taken");
                } else {
                    reject("There was an error creating the user: " + err);
                }
            } else {
                resolve();
            }
        });
    });
};
modules.export.checkUser = function(userData) {
    return new Promise((resolve, reject) => {
        User.find({ user: userData.userName }).exec().then((users) => {
            if (users.length == 0) {
                reject("Unable to find user: " + user);
            } else if (users[0].password != userData.password) {
                reject("Incorrect Password for user: " + userData.userName);
            } else {
                users[0].loginHistory.push({dateTime: (new Date()).toString(), userAgent: userData.userAgent});
                Company.update({ userName: users[0].userName},
                { $set: { loginHistory: users[0].loginHistory } },
                { multi: false }).exec().then(() => {
                    resolve(users[0]);
                }).catch((err) => {
                    reject( "There was an error verifying the user: " + err);
                });
            }
        }).catch((err) => {
            reject("Unable to find user: " + userData.user);
        });
    });
};
