const express = require('express');
const morgan = require('morgan');
const path = require('path');
const favicon = require('serve-favicon') 


const app = express();
const http = require('http').createServer(app);
var io = require('socket.io')(http);
const indexRouter = require('./routes/index');

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


// io.on('connection', (socket) => {
//   console.log('a user connected');
//   socket.on('chat message', (msg) => {
//     io.emit('chat message', msg);
//   });
//   socket.on('disconnect', () => {
//   console.log('user disconnected');
//   });
// });
io.on('connection', function(socket) {

  // 접속한 클라이언트의 정보가 수신되면
  socket.on('login', function(data) {
    console.log('Client logged-in:\n name:' + data.name + '\n userid: ' + data.userid);

    // socket에 클라이언트 정보를 저장한다
    socket.name = data.name;
    socket.userid = data.userid;

    // 접속된 모든 클라이언트에게 메시지를 전송한다
    io.emit('login', data.name );
  });

  // 클라이언트로부터의 메시지가 수신되면
  socket.on('chat', function(data) {
    console.log('Message from %s: %s', socket.name, data.msg);

    var msg = {
      from: {
        name: socket.name,
        userid: socket.userid
      },
      msg: data.msg
    };

    // 메시지를 전송한 클라이언트를 제외한 모든 클라이언트에게 메시지를 전송한다
    socket.broadcast.emit('chat', msg);

    // 메시지를 전송한 클라이언트에게만 메시지를 전송한다
    // socket.emit('s2c chat', msg);

    // 접속된 모든 클라이언트에게 메시지를 전송한다
    // io.emit('s2c chat', msg);

    // 특정 클라이언트에게만 메시지를 전송한다
    // io.to(id).emit('s2c chat', data);
  });

  // force client disconnect from server
  socket.on('forceDisconnect', function() {
    socket.disconnect();
  })

  socket.on('disconnect', function() {
    console.log('user disconnected: ' + socket.name);
  });
});

http.listen(app.get('port'), ()=> {
  console.log(app.get('port'), '번 포트에서 대기 중');
  
});



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