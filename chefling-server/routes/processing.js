// 오더에 각 음식에 만들어졌는지 안만들어졌는지에 대한 bool 필요

// servertime ==> 1분을 10초 1/6 , 서버타임 3초 ->18초 ==> 2분
// servertime (int)2 로 넘겨주기
module.exports = function(io){

    const express = require('express');
    const router = express.Router();
    const app = express();
    const fs = require('fs');
    const path = require('path');
    var admin = require('firebase-admin');
    var db = admin.database();
    var ref = db.ref();
    var order_ref = db.ref("order");
    var menu_ref = db.ref("menu")
    var proc_ref = db.ref("processing");
    var proc_ref_fb = db.ref("processing_fb")
    var servertime_ref = db.ref("servertime");

    let {PythonShell} =require("python-shell");
    var options //python shell 작동할 때의 옵션
    var time;
    var wholeData;
    var viewData;
    var proc = new Array();
    var proc_python = new Array();
    var proc_fb = new Array();
    var foods = new Array(); // 메뉴데이터-음식
    var cooks = new Array(); // 쿸리스트
    var serverTime;
    var served_list = new Array();
    
    //메뉴데이터 로드
    menu_ref.once("value", function(snapshot){
        menu_data = snapshot.val();
        console.log("메뉴 데이터 초기화");
        
        for(var key in menu_data["foods"]){ // 메뉴 데이터
            var foodObj = menu_data["foods"][key];
            var temp = new Array();
            temp.push(foodObj["category"])
            temp.push(foodObj["name"]);
            temp.push(foodObj["cooking_time"]);
            temp.push(foodObj["type"]);
            foods.push(temp);
        }

        for(var key in menu_data["cooks"]){
            var cookObj = menu_data["cooks"][key];
            var temp = new Array();
            temp.push(cookObj["name"]);
            temp.push(cookObj["position"]);
            temp.push(cookObj["ability"]);
            temp.push(["None"]); //현재 요리하고있는 음식리스트
            temp.push("None"); //쿸시간
            temp.push("None"); //블락유무
            cooks.push(temp);
        }


    });

    // 오더가 추가 되었을때
    order_ref.on("value", function(snapshot){
        proc = [] // proc 초기화

        if(foods.length != 0 ){ // 메뉴데이터가 있을시.. 실행
            var orders = new Array();
            var ordered_list = new Array();
            order_data = snapshot.val();
            for(var key in order_data){
                var orderObj = order_data[key]["foods"];
                var temp = new Array();
                temp.push(order_data[key]["orderId"]); //오더id
                var food_list = new Array();
               
                for(var sub_key in orderObj){
                    var order_food = orderObj[sub_key];
                    var food_each = new Array();      
                    food_each.push(order_food["name"]);
                    food_each.push(order_food["foodId"]);
                    food_list.push(food_each);
                }
                temp.push(food_list);
                orders.push(temp);
            }

            if(orders.length ==0){

                console.log("오더가 들어오기 전이에요");               
                  
            }
            else if(orders.length == 1){ // 첫 오더일 경우
                console.log("첫 주문 들어와따");             
                time = new Date();             
                ordered_list.push("None");
                proc = [];
                proc.push(foods);
                proc.push(cooks);
                proc.push(orders);
                proc.push(ordered_list)
                
                proc_ref_fb.set(proc); //파이어베이스
            }
            else{ // 첫 오더 아닐경우
                proc_ref.once("value",function(proc_snap){
                    console.log("이후 오더에요");
                    var proc_data = proc_snap.val();
                    ordered_list = proc_data[3];
                    cooks = proc_data[1];

                    //서버타임
                    time = new Date();
                    proc_fb = [];
                    proc_fb.push(foods);
                    proc_fb.push(cooks);
                    proc_fb.push(orders);                    
                    proc_fb.push(ordered_list);

                    console.log("파베넣기전", proc_fb);
                    
                    proc_ref_fb.set(proc_fb);                    
                });
            }
        }  

    });

    proc_ref_fb.on("value",function(snapshot){ 
        var proc_data = snapshot.val();     
        
        if(proc_data != null){
            var new_time = new Date(); // 현재시각
            var duration = (new_time-time)/60 // 분단위
            if(duration <0.5) duration =0;
            console.log("due", duration);
            
            serverTime = duration;
            time = new_time; //전역변수 time 갱신

            var proc_python = new Array();
            proc_python.push(proc_data[0]); //food
            proc_python.push(proc_data[1]); //cook
            proc_python.push(proc_data[2]); //order
            proc_python.push(serverTime); //servertime
            proc_python.push(proc_data[3]); //ordered_list

            console.log("serverTime", serverTime);
            console.log("스케쥴링 전",proc_python);
            options = {
                        mode: 'text',
                        pythonOptions: ['-u'],
                        scriptPath:'',
                        args: [JSON.stringify(proc_python)],
                        pythonPath:''
                    }

                    PythonShell.run("Scheduling_Scheduler.py",options, function(err, result){
                        if(err) throw err;                     
                        var result_json = JSON.parse(result[0]);                        
                        console.log("결과_oredered list\n",result_json[0]);
                        console.log("결과_cook list\n",result_json[1]);                      
                        //ordered list
                        proc_data[3] = result_json[0];
                
                        //cooking list
                        proc_data[1] = result_json[1];
                        //update processing view
                        // io.emit('update_processing',orders);
                        // console.log("파이썬 실행 후",proc);
                        proc_ref.set(proc_data); //파베 적용
                    });

        } 
        
    });


//processing의 변화가 있을 때
// 오더가 추가되고 실행 -1
// 완료한 요리 삭제했을 때 실행 -2
    // proc_ref.on("value",function(snapshot){ 
    //     var proc_data = snapshot.val();
    //     if(proc_data != null){ 
    //         var new_time = new Date(); // 현재시각
    //         time = new_time; //전역변수 time 갱신

    //     } 
        
    // });


    router.get('/', function(req, res, next){
        // console.log("라우팅", proc[1][0][3][0]);        
        res.render("processing",{proc_list:proc}); 
    });

    return router;
}

