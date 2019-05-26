/*********************************************************************************
*  WEB222 – Assignment 02 
*  I declare that this assignment is my own work in accordance with Seneca  Academic Policy.   
*  No part of this assignment has been copied manually or electronically from any other source 
*  (including web sites) or distributed to other students. 
*  
*  Name: Eric McDonald Student ID: 153581160 Date: 2018-09-29
* 
********************************************************************************/

/*
 * This is a JavaScript Scratchpad.
 *
 * Enter some JavaScript, then Right Click or choose from the Execute Menu:
 * 1. Run to evaluate the selected text (Ctrl+R),
 * 2. Inspect to bring up an Object Inspector on the result (Ctrl+I), or,
 * 3. Display to insert the result in a comment after the selection. (Ctrl+L)
 */

/**********************************
 *          ALL DATA              *
 *  write your CustomerDB Object  *
 *      BELOW this Object         *
 **********************************/

var allData = [
    {type:"store", data:{store_id: 297, name: "Scotiabank - Main Branch", address_id: 1023}},
    {type:"store", data:{store_id: 614, name: "Scotiabank - Hamilton", address_id: 1984}},
    {type:"store", data:{store_id: 193, name: "Scotiabank - Mississauga", address_id: 1757}},
    {type:"customer", data:{customer_id: 26, store_id:297, first_name: "Dave", last_name: "Bennett", email: "dbennett@gmail.com", address_id: 4536, add_date: null}},
    {type:"customer", data:{customer_id: 59, store_id:193, first_name: "John", last_name: "Stevens", email: "jstevens22@hotmail.com", address_id: 2473, add_date: null}},
    {type:"customer", data:{customer_id: 29, store_id:614, first_name: "Sarah", last_name: "Pym", email: "spym99@hotmail.com", address_id: 1611, add_date: null}},
    {type:"customer", data:{customer_id: 63, store_id:297, first_name: "Steven", last_name: "Edwards", email: "steven2231@hotmail.com", address_id: 1836, add_date: null}},
    {type:"customer", data:{customer_id: 71, store_id:614, first_name: "Martin", last_name: "Scott", email: "mdog33@gmail.com", address_id: 2727, add_date: null}},
    {type:"customer", data:{customer_id: 24, store_id:614, first_name: "Jonathan", last_name: "Pym", email: "jjpym@yahoo.ca", address_id: 1611, add_date: null}},
    {type:"customer", data:{customer_id: 36, store_id:193, first_name: "Kaitlyn", last_name: "Adams", email: "katy38@hotmail.com", address_id: 5464, add_date: null}},
    {type:"customer", data:{customer_id: 73, store_id:297, first_name: "Melissa", last_name: "Bennett", email: "mbennett@gmail.com", address_id: 4536, add_date: null}},         
    {type:"address", data:{address_id: 1023, address: "2895 Yonge St.", city:"Toronto", province:"ON", postal_code:"L4C02G"}},
    {type:"address", data:{address_id: 1984, address: "3611 Main St. West", city:"Hamilton", province:"ON", postal_code:"R5O8H5"}},
    {type:"address", data:{address_id: 1757, address: "1177 Ontario St. Unit 8", city:"Mississauga", province:"ON", postal_code:"L9H6B3"}},
    {type:"address", data:{address_id: 4536, address: "3945 John St.", city: "Ajax", province: "ON", postal_code: "L7M4T9"}},
    {type:"address", data:{address_id: 2473, address: "391 Baker St. Apt 231", city: "Mississauga", province: "ON", postal_code: "M4T8S3"}},
    {type:"address", data:{address_id: 1611, address: "183 City Ct.", city: "Hamilton", province: "ON", postal_code: "J3T9V2"}},
    {type:"address", data:{address_id: 1836, address: "67 Rhymer Ave.", city: "Stouffville", province: "ON", postal_code: "L3C8H4"}},
    {type:"address", data:{address_id: 2727, address: "287 Brant St. Apt 4A", city: "Waterdown", province: "ON", postal_code: "R93G3P"}},
    {type:"address", data:{address_id: 5464, address: "11 New St. Apt 2B", city: "Brampton", province: "ON", postal_code: "L694R7"}},
];




 /*  Write your CustomerDB Object Here.  Do not forget to uncomment the "TEST DATA" section
     when you're ready.  Your code is required to run against these tests before you submit */
var CustomerDB = {customers:[], addresses:[], stores:[]};
CustomerDB.addCustomer = function(customer) {
  customer.data.add_date = new Date();
  this.customers.push(customer);
};
CustomerDB.addStore = function(store) {
  this.stores.push(store);
};
CustomerDB.addAddress = function(address) {
  this.addresses.push(address);
};
CustomerDB.getAddressById = function(addrId) {
  var address = null;
  for (var i = 0; i < this.addresses.length; i++) {
    if (this.addresses[i].data.address_id == addrId) {
      address = this.addresses[i];
      break;
    }
  }
  return address;
}
CustomerDB.outputCustomerById = function(customerId) {
  var customer = null;
  for (var i = 0; i < this.customers.length; i++) {
    if (this.customers[i].data.customer_id == customerId) {
      customer = this.customers[i];
      break;
    }
  }
  console.log("Customer " + customer.data.customer_id + ": " + customer.data.first_name + " " + customer.data.last_name + " (" + customer.data.email + ")");
  var address = this.getAddressById(customer.data.address_id);
  console.log("Home Address: " + address.data.address + " " + address.data.city + ", " + address.data.province + ". " + address.data.postal_code);
  console.log("Joined: " + customer.data.add_date);
};
CustomerDB.outputAllCustomers = function() {
  console.log("All Customers\n\n");
  for (var i = 0; i < this.customers.length; i++) {
    this.outputCustomerById(this.customers[i].data.customer_id);
    console.log("\n");
  }
};
CustomerDB.getStoreById = function(storeId) {
  var store = null;
  for (var i = 0; i < this.stores.length; i++) {
    if (this.stores[i].data.store_id == storeId) {
      store = this.stores[i];
      break;
    }
  }
  return store;
};
CustomerDB.outputCustomersByStore = function(storeId) {
  var store = this.getStoreById(storeId);
  console.log("Customers in Store: " + store.data.name + "\n");
  for (var i = 0; i < this.customers.length; i++) {
    if (this.customers[i].data.store_id == storeId) {
      this.outputCustomerById(this.customers[i].data.customer_id);
      console.log("\n");
    }
  }
};
CustomerDB.removeAddressById = function(addrId) {
  var hasAddr = false;
  for (var i = 0; i < this.customers.length; i++) {
    if (this.customers[i].data.address_id == addrId) {
      hasAddr = true;
      break;
    }
  }
  if (!hasAddr) {
    for (var i = 0; i < this.stores.length; i++) {
      if (this.stores[i].data.address_id == addrId) {
        hasAddr = true;
        break;
      }
    }
  }
  if (hasAddr) {
    return false;
  }
  for (var i = 0; i < this.addresses.length; i++) {
    if (this.addresses[i].data.address_id == addrId) {
      this.addresses.splice(i, 1);
      return true;
    }
  }
  return false;
};
CustomerDB.removeCustomerById = function(customerId) {
    for (var i = 0; i < this.customers.length; i++) {
    if (this.customers[i].data.address_id == customerId) {
      this.removeAddressById(this.customers[i].data.address_id);
      this.customers.splice(i, 1);
      return true;
    }
  }
  return false;
};
CustomerDB.addAddress = function(address) {
  return this.addresses.push(address);
};
CustomerDB.outputAllAddresses = function() {
  console.log("All Address\n\n");
  for (var i = 0; i < this.addresses.length ; i++) {
    var address = this.addresses[i];
    console.log("Address " + address.data.address_id + ": " + address.data.address + " " + address.data.city + ", " + address.data.province + ". " + address.data.postal_code);
  }
};
CustomerDB.addStore = function(store) {
  return this.stores.push(store);
};
CustomerDB.outputAllStores = function() {
  console.log("All Stores\n\n");
  for (var i = 0; i < this.stores.length; i++) {
    console.log("Store " + this.stores[i].store_id + ": " + this.stores[i].data.name);
    var address = this.getAddressById(this.stores[i].data.address_id);
    console.log("Location: " + address.data.address_id + ": " + address.data.address + " " + address.data.city + ", " + address.data.province + ". " + address.data.postal_code);
  }
};
CustomerDB.insertData = function(data) {
  for (var i = 0; i < data.length; i++) {
    var value = data[i];
    if (value.type == "store") {
      this.addStore(value);
    } else if (value.type == "customer") {
      this.addCustomer(value);
    } else if (value.type == "address") {
      this.addAddress(value);
    }
  }
};



/**********************************
 *          TEST DATA             *
 *  write your CustomerDB Object  *
 *      ABOVE this code           *
 *                                *
 *  Uncomment this block of code  *
 *  when you're ready to test     *
 *  your CustomerDB Object        *
 *                                *
 *  You MUST Hand in your code    *
 *  with the test data            *
 *  uncommented, as this will     *
 *  help check your code for      *
 *  correctness                   *
 **********************************/



// Insert all Data into the Database

CustomerDB.insertData(allData);

console.log(CustomerDB.customers);

// output all customers

console.log("CustomerDB.outputAllCustomers();\n\n--------------------------\n\n");
CustomerDB.outputAllCustomers();
console.log("--------------------------\n\n");

// output all addresses

console.log("CustomerDB.outputAllAddresses();\n\n--------------------------\n\n");
CustomerDB.outputAllAddresses();
console.log("--------------------------\n\n"); 

// output all stores

console.log("CustomerDB.outputAllStores();\n\n--------------------------\n\n");
CustomerDB.outputAllStores();
console.log("--------------------------\n\n"); 

// output all customers in the "Main Branch"

console.log("CustomerDB.outputCustomersByStore(297);\n\n--------------------------\n\n");
CustomerDB.outputCustomersByStore(297);
console.log("--------------------------\n\n"); 

// remove Customer Dave Bennett (customer_id 26) and Martin Scott (customer_id 71)

console.log("CustomerDB.removeCustomerById(26);\nCustomerDB.removeCustomerById(71);\n\n");
CustomerDB.removeCustomerById(26);
CustomerDB.removeCustomerById(71);
console.log("--------------------------\n\n"); 

// output all customers again
// NOTE: Dave Bennett and Martin Scott should be missing

console.log("CustomerDB.outputAllCustomers();\n\n--------------------------\n\n");
CustomerDB.outputAllCustomers();
console.log("--------------------------\n\n");

// output all addresses again
// NOTE: only addrss 287 Brant St. Apt 4A Waterdown, ON. R93G3P should be missing

console.log("CustomerDB.outputAllAddresses();\n\n--------------------------\n\n");
CustomerDB.outputAllAddresses();
console.log("--------------------------\n\n"); 
