module.exports = function(io){

const express = require('express');
const router = express.Router();

const app = express();
const fs = require('fs');
const path = require('path');
var admin = require('firebase-admin');
var db = admin.database();
var ref = db.ref("order");
let {PythonShell} =require("python-shell");
var options //python shell 작동할 때의 옵션
var orders = new Array(); //주문 정보를 담는 배열

ref.on("value", function(snapshot) {
    orders = [];
  var len = Object.keys(snapshot.val()).length; // 파이어베이스 주문 배열 길이  
  
  var temp = Object.values(snapshot.val()) // temp는 스트링 배열의 배열
  for(var i =0 ; i<len; i++){    
    orders.push(temp[i]) //order 배열에 주문 정보 담기   
  }
  io.emit('update_orders',orders);
  console.log("파베(order) 업데이트");
  
  

});

router.get('/', function(req, res, next){
    res.render("orders",{order_list:orders});   
    
});



return router;
}
