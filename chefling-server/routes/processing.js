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
    var served_ref = db.ref("served");

    let {PythonShell} =require("python-shell");
    var options //python shell 작동할 때의 옵션
    var time = 0;
    var wholeData;
    var viewData;
    var view_proc = [];
    var proc = new Array();
    var proc_python = new Array();
    var proc_fb = new Array();
    var foods = new Array(); // 메뉴데이터-음식
    var cooks = new Array(); // 쿸리스트
    var serverTime;
    var served_list = new Array();
    var order_num = 0;

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

    // 오더에 변화가 있을때
    order_ref.on("value", function(snapshot){
        
        var order_flag = 0; 
        // 0 : 첫주문 들어오기전 , 1: 오더개수 변화없고 속성만 변했을 떄, 2: 오더 추가 , 3: 완료된 오더 삭제
        proc = [] // proc 초기화
        // console.log("오더 자식수", snapshot.numChildren());
        var order_num_now = snapshot.numChildren();
        console.log("ordernum",order_num, "ordernumnow",order_num_now);
        
        if(order_num == 0){
            // 첫주문 들어오기 전
            order_flag = 0;
            if(order_num <order_num_now){
                order_flag = 2;
                order_num = order_num_now;
            }
        }
        else if(order_num == order_num_now){ // 오더개수 변화 없고 속성만 변화
            order_flag = 1;
            order_num = order_num_now;
        }
        else if(order_num < order_num_now){ // 오더 추가
            order_flag = 2;
            order_num = order_num_now;
        }
        else{ // 오더 삭제
            order_flag = 3;
            order_num = order_num_now;
        }
        
        if(foods.length != 0 ){ // 메뉴데이터가 있을시.. 실행            
            var orders = new Array();
            var ordered_list = new Array();
            order_data = snapshot.val();
            for(var key in order_data){
                var orderObj = order_data[key]["foods"];                
                var temp = new Array();
                temp.push(order_data[key]["orderId"]); //오더id
                var food_list = new Array();
                var complete_num = 0;
                
                for(var sub_key in orderObj){
                    var order_food = orderObj[sub_key];
                    var food_each = new Array();
                    if(order_food["complete"] == true){
                        complete_num++;
                    }
                    food_each.push(order_food["name"]);
                    food_each.push(order_food["foodId"]);
                    food_list.push(food_each);
                }
                temp.push(food_list);
                orders.push(temp);
                // 완료된 order 삭제: 모든 food가 완료 되었따면 파베에서 삭제하고 orders 배열에도 넣지 않는다.
                // 파이어베이스에서 오더 삭제
                if(complete_num == food_list.length){
                    orders.push(temp);
                    order_ref.child(key).remove();
                }                
            }
            // console.log("order_flag", order_flag, "order_length", orders.length);
            
            if(order_flag == 0 && orders.length ==0){
                console.log("오더가 들어오기 전이에요");
            }
            else if(order_flag == 3 &&orders.length ==0){ // 오더 삭제해서 오더개수 0일때,
                console.log("마지막 남은 오더가 삭제되고 오더가 비었네용~");                
            }
            else if(order_flag == 2 && orders.length == 1){ // 첫 오더일 경우                            
                // time = Date.now();
                console.log("첫 주문 들어와따",time); 
                ordered_list.push("None");
                var time_per_menu = [];
                var time_per_food = [];
                time_per_menu.push("None");
                time_per_food.push("None");
                proc = [];
                proc.push(foods);
                proc.push(cooks);
                proc.push(orders);
                proc.push(ordered_list)
                proc.push(time_per_menu);
                proc.push(time_per_food);
                //첫주문 적용 --> 처음으로 파베에 processing_fb와 processing 생김
                proc_ref_fb.set(proc); //파이어베이스
            }
            else{ // 첫 오더 아닐경우 and 오더 한개일때 속성값이 변하는 경우
                if (order_flag == 3){
                    console.log("완료된 오더 삭제됨");            
                }
                else if(order_flag == 1){ // 오더 개수는 그대로고 속성값만 변한 경우
                    proc_ref.once("value",function(proc_snap){
                        // time = Date.now();
                        console.log("완료된 요리가 삭제되고 processing을 읽음",time);
                        var proc_data = proc_snap.val();                
                        ordered_list = proc_data[3];
                        cooks = proc_data[1];
                        //서버타임
                        
                        proc_fb = [];
                        proc_data[2].push(["None"])
                        proc_fb.push(proc_data[0]);
                        proc_fb.push(proc_data[1]);
                        proc_fb.push(proc_data[2]); // 파이썬이 최신의 order를 보기 때문에 order를 none으로      
                        proc_fb.push(proc_data[3]); 
                        proc_fb.push(proc_data[4]);
                        proc_fb.push(proc_data[5]);
                        // console.log("none확인",proc_fb);
                        
                        // console.log("음식삭제 적용되나욘?", proc_fb[1][0][3]);                        
                        proc_ref_fb.set(proc_fb);        
                    });                                 
                }
                else if(order_flag == 2){ // 오더가 추가되거나 파이어베이스에 변화분 저장
                    proc_ref.once("value",function(proc_snap){
                        // time = Date.now();
                        console.log("이후 오더에요",time);
                        var proc_data = proc_snap.val();                
                        ordered_list = proc_data[3];
                        cooks = proc_data[1];
                        //서버타임
                        
                        proc_fb = [];
                        proc_fb.push(foods);
                        proc_fb.push(cooks);
                        proc_fb.push(orders);              
                        proc_fb.push(ordered_list); 
                        proc_fb.push(proc_data[4]);
                        proc_fb.push(proc_data[5]);
                        // console.log("파베넣기전", proc_fb);                        
                        proc_ref_fb.set(proc_fb);                    
                    });
                }                
            }
        }  

    });
    //오더가 추가되거나, 다 만든 음식을 삭제할 때 실행됨,
    proc_ref_fb.on("value",function(snapshot){ 
        var proc_data = snapshot.val();     
        
        if(proc_data != null){            
             // 현재시각
            // var duration = ((new_time-time)/1000) // 분단위
            if(time ==0 ){
                serverTime = 0;
                time = Date.now();
            }
            else{
                var new_time = Date.now();
                var duration = Math.round((new_time - time)/6000);
                serverTime = duration;
                time = new_time; //전역변수 time 갱신
            }
           
            console.log("타임확인", serverTime);
            

            var proc_python = new Array();
            proc_python.push(proc_data[0]); //food
            proc_python.push(proc_data[1]); //cook
            proc_python.push(proc_data[2]); //order
            proc_python.push(serverTime); //servertime
            proc_python.push(proc_data[3]); //ordered_list
            proc_python.push(proc_data[4]);
            proc_python.push(proc_data[5]);

            
            // console.log("스케쥴링 전",proc_python);
            // console.log("orderedList 확인", proc_data[3]);            
            // console.log("요리사리스트확인",proc_data[1][0][3]);
            
            options = {
                        mode: 'text',
                        pythonOptions: ['-u'],
                        scriptPath:'',
                        args: [JSON.stringify(proc_python)],
                        pythonPath:''
                    }

                    PythonShell.run("Scheduling_Scheduler.py",options, function(err, result){
                        if(err) throw err;
                        // console.log("바로결과", result);
                                    
                        var result_json = JSON.parse(result[0]);
                        if(result_json[0].length == 0){
                            proc_data[3] = ["None"];
                        }
                        else proc_data[3] = result_json[0];
                        // proc_data[3] = result_json[0];//ordered list
                        proc_data[1] = result_json[1];//cooking list 
                        proc_data[4] = result_json[2];//time per menu
                        proc_data[5] = result_json[3];//time per food
                        // proc_data[4] = ["None"];//time per menu
                        // proc_data[5] = ["None"];//time per food
      
                        console.log("파이썬결과_oredered lisdt\n",result_json[0]);
                        console.log("파이썬결과_cook list\n",result_json[1]);
                        console.log("파이썬결과_time menu\n",result_json[2]);
                        console.log("파이썬결과_time food\n",result_json[3]);                      
                        
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
    proc_ref.on("value",function(snapshot){
        view_proc = []
        var proc_data = snapshot.val();
        
        
        if(proc_data != null){
            console.log("processing", proc_data[3]);
            proc_data[1].forEach(cook => {
                cook[3].forEach(food => {
                    if(food == "None"){
                        cook[3] = [];
                    }
                });
            });
            view_proc.push(proc_data[1]) // cook list

            if(proc_data[3][0] =="None"){
                proc_data[3] = [];
            }
            view_proc.push(proc_data[3]) // ordered list
            var served_data = null;
            
            //완료된 요리 파이어베이스에서 읽어오는 곳
            // served_ref.once("value", function(served_snap){                
            //     served_data = served_snap.val();
            //     if(served_data == null){
            //         served_data = [];
            //     }           
            // });
            // view_proc.push(served_data);

            console.log("view",view_proc);
            

        }
        else{
            var temp = new Array();
            // cooks.forEach(cook => {
            //     temp.push(cook[1]);
            // });
            view_proc.push([]);
            view_proc.push([]);
        }
        io.emit('update_proc',view_proc);
        
    });
   

    router.get('/', function(req, res, next){     
        res.render("processing",{proc_list:view_proc}); 
    });

    return router;
}

