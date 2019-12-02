module.exports = function(io){

const express = require('express');
const router = express.Router();

const app = express();
const fs = require('fs');
const path = require('path');
var admin = require('firebase-admin');
var db = admin.database();
var ref = db.ref("order");

var orders = new Array(); //주문 정보를 담는 배열

ref.on("value", function(snapshot) {
    orders = [];
    if(snapshot.val()!= null){
      var order_data = snapshot.val();
      for(var key in order_data){
        var orderObj = order_data[key];        
        orders.push(orderObj);

      }

    }   
  
  io.emit('update_orders',orders);
  
});

router.get('/', function(req, res, next){
    res.render("orders",{order_list:orders});   
    
});



return router;
}
