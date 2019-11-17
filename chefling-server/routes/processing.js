//food 단위로 
// 요리사별 푸드 할당 보여주고 (음식이름 / 테이블번호)
// 눌렀을 때 
// 오더에 각 음식에 만들어졌는지 안만들어졌는지에 대한 bool 필요


//###initialize
// a[0] - food: 음식이름, 요리시간(초기화)
// a[1] - cooks: 이름, position, ability(초기화)
// a[2] - order: order_id , food배열(이름) (오더)
// a[3] - servertime
// a[4] -  food 단위의 ordered list(이름/시간) (오더큐)
// a[5] - cook 별 (이름/ 시간) (쿡큐)
// a[6] - served (음식이름/ 담당한 요리사) (완료된요리; 로그)


// servertime ==> 1분을 10초 1/6 , 서버타임 3초 ->18초 ==> 2분
// servertime (int)2 로 넘겨주기


//output --> food 단위의 ordered list(이름/시간)
// cook 별 (이름/ 시간)
//served (음식이름/ 담당한 요리사)

module.exports = function(io){

    const express = require('express');
    const router = express.Router();

    const app = express();
    const fs = require('fs');
    const path = require('path');
    var admin = require('firebase-admin');
    var db = admin.database();
    var ref = db.ref();
    var proc_ref = db.ref("processing");
    let {PythonShell} =require("python-shell");
    var options //python shell 작동할 때의 옵션
    var orders = new Array(); //주문 정보를 담는 배열
    var time;
    var wholeData;
    ref.on("value", function(snapshot) {

    wholeData = snapshot.val()
    var wholeData_len = Object.keys(wholeData).length
    var foods = new Array();
    var cooks = new Array();
    var orders = new Array();
    var serverTime;
    var ordered_list = new Array();
    var cooking_list = new Array();
    var served_list = new Array();
    // a[0] - food: 음식이름, 요리시간(초기화)
    // a[1] - cooks: 이름, position, ability(초기화)
    // a[2] - order: order_id , food배열(이름) (오더)
    // a[3] - servertime

    //foods
    for(var key in wholeData["menu"]["foods"]){
        var foodObj = wholeData["menu"]["foods"][key];
        var temp = new Array();
        temp.push(foodObj["name"]);
        temp.push(foodObj["cooking_time"]);
        foods.push(temp);
    }    
    //cooks
    for(var key in wholeData["menu"]["cooks"]){
        var cookObj = wholeData["menu"]["cooks"][key];
        var temp = new Array();
        temp.push(cookObj["name"]);
        temp.push(cookObj["position"]);
        temp.push(cookObj["ability"]);
        cooks.push(temp);
    }
    //orders
    for(var key in wholeData["order"]){
        var orderObj = wholeData["order"][key];
        var temp = new Array();
        temp.push(key); //오더id
        var temp_food = new Array();
        for(var sub_key in orderObj){
            var order_food = orderObj[sub_key];            
            temp_food.push(order_food["name"]);
        }
        temp.push(temp_food);
        orders.push(temp);
    }
    
    
    // a[4] -  food 단위의 ordered list(오더 아이디/ 음식이름/ 우선순위(null)/ 대기가능시간(null)/ 대기시간(null)/ 이벤트유무(null)) (오더큐)
    // ?? 초기화 이후 파이썬 한번 거치고 난 결과값과 현재 order 리스트 와의 관계?
    // a[5] - cook 별 (쿸아이디, 포지션, 역량, 현재 쿸 리스트(null), 쿡시간(null), 블락유무(null) (쿡큐)
    // a[6] - served (음식이름/ 담당한 요리사) (완료된요리; 로그)

    //ordered_list,cooking_list,served_list
    
    if(wholeData_len ==2){ // 초기화 일때 
        serverTime = 0;
        time = new Date();
    //ordered_list
        for(var key in wholeData["order"]){
            var orderObj = wholeData["order"][key];
            var temp = new Array();
            temp.push(key); //오더id
            for(var sub_key in orderObj){
                var order_food = orderObj[sub_key];            
                temp.push(order_food["name"]); // 음식이름
                temp.push(null); // 우선순위
                temp.push(null); // 대기가능 시간
                temp.push(null); // 대기시간
                temp.push(null); // 이벤트 유무
            }
            ordered_list.push(temp);
        }
    //cooking_list
        for(var key in wholeData["menu"]["cooks"]){
            var cookObj = wholeData["menu"]["cooks"][key];
            var temp = new Array();
            temp.push(key); // cook id
            temp.push(cookObj["position"]); //포지션
            temp.push(cookObj["ability"]); //역량
            temp.push(null); // 현재 쿸 리스트
            temp.push(null); //쿸시간
            temp.push(null); //블락유무
            cooking_list.push(temp);
        }

    //served_list 
        var served_temp = new Array();
        served_temp.push("init");
        served_list.push(served_temp);

    }
    else{ //프로세싱 구조가 적용 된 후
    //serverTime
        serverTime = 0;
        // console.log("이전시각",time);
        // var new_time = new Date(); // 현재시각 
        // var duration = (new_time-time)/60 // 분단위
        // serverTime = duration;
        // time = new_time; //전역변수 time 갱신
        // console.log("현재시각",time);
        
    //ordered_list
        ordered_list = wholeData["processing"][4];       
    //cooking_list
        cooking_list = wholeData["processing"][5];    
    //served_list 
        served_list = wholeData["processing"][6]; 

    }
    
    var proc = new Array();   

    proc.push(foods);
    proc.push(cooks);
    proc.push(orders);
    proc.push(serverTime);
    proc.push(ordered_list);
    proc.push(cooking_list);
    proc.push(served_list);
    console.log(proc);     
    
    
    proc_ref.set(proc); //파싱한 배열 적용
   
    });

    router.get('/', function(req, res, next){
        // res.render("orders",{order_list:orders}); 
        console.log(fire);
          
        
    });

    return router;
}

