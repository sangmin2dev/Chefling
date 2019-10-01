const express = require('express');
const morgan = require('morgan');
const path = require('path');

const indexRouter = require('./routes/index');

const app = express();

app.set('views', path.join(__dirname,'views'));
app.set('view engine', 'pug');
app.set('port', process.env.port || 8001);

app.use(morgan('dev'));
app.use(express.static(path.join(__dirname, 'public')));
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
