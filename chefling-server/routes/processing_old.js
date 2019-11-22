//파베 전체 참조
ref.on("value", function(snapshot) {

    wholeData = snapshot.val()
    var wholeData_len = Object.keys(wholeData).length
    
    var cooks = new Array();
    var orders = new Array();
    var serverTime;
    var ordered_list = new Array();
    var served_list = new Array();
    //foods
    for(var key in wholeData["menu"]["foods"]){
        var foodObj = wholeData["menu"]["foods"][key];
        var temp = new Array();
        temp.push(foodObj["name"]);
        temp.push(foodObj["cooking_time"]);
        foods.push(temp);
    }    
   
    // a[0] - food: 음식이름, 요리시간(초기화)
    // a[1] - cook 별 (요리사 이름, 포지션, 역량, 현재 쿸 리스트(None), 쿡시간(None), 블락유무(None) (쿡큐) 
                                                //각 요리는 [오더아이디, food_id, (음식이름,소요시간)]
    // a[2] - order: order_id , food배열(이름,ID) (오더)
    // a[3] - servertime
    // a[4] - food 단위의 ordered list(오더 아이디/ food_id /(음식이름,소요시간)/ 우선순위(null)/ 대기가능시간(null)/ 대기시간(null)/ 이벤트유무(null)) (오더큐)
    //   ?? 초기화 이후 파이썬 한번 거치고 난 결과값과 현재 order 리스트 와의 관계?       
    // a[5] - served (음식이름/ 담당한 요리사) (완료된요리; 로그)

    //ordered_list,cooking_list,served_list
    // 메뉴존재 length =1
    // processing
    if(wholeData_len ==2){ //처음 오더가 들어올때
        serverTime = 0;
        time = new Date();
        //orders
        for(var key in wholeData["order"]){
            var orderObj = wholeData["order"][key]["foods"];
            var temp = new Array();
            temp.push(key); //오더id
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
        
        //cook별
        for(var key in wholeData["menu"]["cooks"]){
            var cookObj = wholeData["menu"]["cooks"][key];
            var temp = new Array();
            temp.push(cookObj["name"]);
            temp.push(cookObj["position"]);
            temp.push(cookObj["ability"]);
            temp.push("None"); //현재 요리하고있는 음식리스트
            temp.push("None"); //쿸시간
            temp.push("None"); //블락유무
            cooks.push(temp);
        }
        ordered_list.push(0);
        
    //served_list 
        served_list.push("z");

    }
    else if(wholeData_len ==3){ // 메뉴, 오더, 프로세싱 모두 존재
        //orders
        for(var key in wholeData["order"]){
            var orderObj = wholeData["order"][key]["foods"];
            var temp = new Array();
            temp.push(key); //오더id
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
        // console.log("order_test", orders);
        
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
        cooks = wholeData["processing"][1];    
    //served_list 
        served_list = wholeData["processing"][5]; 

    }
    
       
    proc = [];
    var proc_for_python;
    proc.push(foods);
    proc.push(cooks);
    proc.push(orders);
    proc.push(serverTime);
    proc.push(ordered_list);
    proc_for_python = proc;
    proc.push(served_list);
    
    //파이썬 파일 실행 옵션
    
    if(orders != 0)proc[2][0][0]=1; 
    options = {
        mode: 'text',
        pythonOptions: ['-u'],
        scriptPath:'',
        args: [JSON.stringify(proc_for_python)],
        pythonPath:''
    }
    
    
    if(wholeData_len == 1){ //메뉴만 있을때
        console.log("메뉴만있어요");
        
    }
    else if(wholeData_len == 2){ //메뉴와 processing 있을때
        console.log("첫 오더 들어올때",proc);
        proc_ref.set(proc);
        
    }
    else { // 메뉴, processing, order 모두 있을때        
        console.log("파이썬 실행전",proc);
        //Scheduling_Scheduler.py
        PythonShell.run("Scheduling_Scheduler.py",options, function(err, result){
            if(err) throw err;
            // console.log("python result: ",result);    
            //    console.log(result);
            
            
            var result_json = JSON.parse(result[0]);
            // console.log("파이썬 실행 후", result_json);    
            // 파이썬으로부터 결과 받은 후 proc 재구조
            console.log("result",result_json);
            
            //ordered list
            proc[4] = result_json[0];       
    
            //cooking list
            proc[1] = result_json[1];           
            
            //update processing view
            // io.emit('update_processing',orders);
            // console.log("파이썬 실행 후",proc);           
            
            proc_ref.set(proc); //파베 적용
        });
    }
    
       
    });