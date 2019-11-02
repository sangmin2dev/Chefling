const express = require('express');
const morgan = require('morgan');
const path = require('path');
const favicon = require('serve-favicon') 
const indexRouter = require('./routes/index');

const app = express();

app.set('views', path.join(__dirname,'views'));
app.set('view engine', 'ejs');
app.set('port', process.env.port || 8001);
// app.use('/js', express.static(__dirname + '/node_modules/bootstrap/dist/js')); // redirect bootstrap JS
// app.use('/js', express.static(__dirname + '/node_modules/jquery/dist')); // redirect JS jQuery
// app.use('/css', express.static(__dirname + '/node_modules/bootstrap/dist/css')); // redirect CSS bootstrap

app.use(morgan('dev'));
app.use(express.static(path.join(__dirname, 'public')));
// app.use(express.static(path.join(__dirname, 'views')));
app.use(favicon(path.join(__dirname,'public/images','favicon.ico')));
app.use(express.json());
app.use(express.urlencoded({extended: false}))

var admin = require('firebase-admin');

var db = admin.database();
var ref = db.ref("order");
let {PythonShell} =require("python-shell");
var options //python shell 작동할 때의 옵션
var orders = new Array(); //주문 정보를 담는 배열

ref.on("value", function(snapshot) {
  // console.log(snapshot.val());
  var len = Object.keys(snapshot).length; // 파이어베이스 주문 배열 길이
  
  var temp = Object.values(snapshot.val()) // temp는 스트링 배열의 배열
  for(var i =0 ; i<len-1; i++){    
    orders.push(temp[i]) //order 배열에 주문 정보 담기
  }
 
  options = {
    mode: 'text',
    pythonOptions: ['-u'],
    scriptPath:'',
    args: [JSON.stringify(orders)],
    pythonPath:''
  }
  // console.log(orders[0]);
  

  PythonShell.run('test.py',options, function(err, result){
    if(err) throw err;
    console.log("result: ",result);
    
  });
}); //프로미스로 동기화 해줘야함

//동기화 시켜야함 .. python 에 먼저 들어가버림




app.use('/', indexRouter);
app.use((req,res,next)=>{
    const err = new Error('Not Found');
    err.status = 404;
    next(err);
});

app.use((err,req,res)=>{
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};
    res.status(err.status || 500);
    res.render('error');
});

app.listen(app.get('port'), ()=> {
    console.log(app.get('port'), '번 포트에서 대기 중');
    
});
