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

        // var orderObj = order_data[key]["foods"];
        // var temp = new Array();
        // temp.push(order_data[key]["orderId"]); //오더id
        // temp.push(order_data[key]["table"]) // 테이블 넘버
        // var food_list = new Array();
        // temp.push(orderObj);
        // orders.push(temp);
      }


      // var len = Object.keys(snapshot.val()).length; // 파이어베이스 주문 배열 길이  
      // var temp = Object.values(snapshot.val()) // temp는 스트링 배열의 배열
      
      // for(var i =0 ; i<len; i++){    
      //   orders.push(temp[i]["foods"]) //order 배열에 주문 정보 담기   
      // }
    }   
  
  io.emit('update_orders',orders);
  
});

router.get('/', function(req, res, next){
    res.render("orders",{order_list:orders});   
    
});



return router;
}
