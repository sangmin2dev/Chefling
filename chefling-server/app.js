const express = require('express');
const morgan = require('morgan');
const path = require('path');
const favicon = require('serve-favicon') 
const indexRouter = require('./routes/index');

const app = express();

app.set('views', path.join(__dirname,'views'));
app.set('view engine', 'ejs');
app.set('port', process.env.port || 8001);
app.use('/js', express.static(__dirname + '/node_modules/bootstrap/dist/js')); // redirect bootstrap JS
app.use('/js', express.static(__dirname + '/node_modules/jquery/dist')); // redirect JS jQuery
app.use('/css', express.static(__dirname + '/node_modules/bootstrap/dist/css')); // redirect CSS bootstrap

app.use(morgan('dev'));
app.use(express.static(path.join(__dirname, 'public')));
app.use(express.static(path.join(__dirname, 'views')));
app.use(favicon(path.join(__dirname,'public/images','favicon.ico')));
app.use(express.json());
app.use(express.urlencoded({extended: false}))

var admin = require('firebase-admin');
var serviceAccount = require('./keys/firebase_key_jsw.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://chefling-f122c.firebaseio.com"
  });

var db = admin.database();
var ref = db.ref();
ref.on("value", function(snapshot) {
  console.log(snapshot.val());
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

app.listen(app.get('port'), ()=> {
    console.log(app.get('port'), '번 포트에서 대기 중');
    
});
