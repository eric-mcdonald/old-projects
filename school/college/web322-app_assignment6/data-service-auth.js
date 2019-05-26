const mongoose = require("mongoose");
const bcrypt = require('bcryptjs');

var Schema = mongoose.Schema;
var userSchema = new Schema({
    "userName":  String,
    "password": String,
    "email": String,
    "loginHistory": [
        { dateTime: Date, userAgent: String }
    ],
    "country": String
  });
  let User; 

  userSchema.pre("save", function(next) {
    const user = this;
    if (!user.isModified("password")) {
      return next();
    }
    bcrypt.genSalt(10, (err, salt) => {
      if (err) {
        return next(err);
      }
      bcrypt.hash(user.password, salt, (err, hash) => {
        if (err) {
          return next(err);
        }
        user.password = hash;
        next();
      });
    });
  });

module.exports.initialize = function () {
    return new Promise((resolve, reject) => {
        let db = mongoose.createConnection("mongodb+srv://dbUser:admin@senecaweb-yx7h8.mongodb.net/test?retryWrites=true");
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
        if (userData.password != userData.password2) {
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
module.exports.checkUser = function(userData) {
    return new Promise((resolve, reject) => {
        User.find({ userName: userData.userName }).exec().then((users) => {
            if (users.length == 0) {
                reject("Unable to find user: " + userData.userName);
            } else {
                bcrypt.compare(userData.password, users[0].password).then((res) => {
                    if (!res) {
                        reject("Incorrect Password for user: " + userData.userName);
                    } else {
                        users[0].loginHistory.push({dateTime: (new Date()).toString(), userAgent: userData.userAgent});
                        User.update({ userName: users[0].userName},
                        { $set: { loginHistory: users[0].loginHistory } },
                        { multi: false }).exec().then(() => {
                            resolve(users[0]);
                        }).catch((err) => {
                            reject( "There was an error verifying the user: " + err);
                        });
                    }
                });
            }
        }).catch((err) => {
            reject("Unable to find user: " + userData.user);
        });
    });
};
