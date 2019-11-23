const express = require('express');
const router = express.Router();
const fs = require('fs');
const path = require('path');
var admin = require('firebase-admin');
var serviceAccount = require('../keys/firebase_key_jsw.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://chefling-f122c.firebaseio.com"
  });

var db = admin.database();
var ref = db.ref("menu");

var totalInfo = new Object();
ref.on("value", function(snapshot) {
    const id = Object.keys(snapshot.val())[0];
    // console.log(id);
    var ref_data = db.ref("menu");
    ref_data.on("value",function(snapshot2){
        // console.log(typeof(snapshot2.val()));
        totalInfo = snapshot2.val();
        // console.log(totalInfo);       

    });
    
});

router.get('/', function(req, res, next){
    // res.render("main",{list : JSON.stringify(totalInfo)});
    res.render("main",{list : totalInfo});
});

module.exports = router;