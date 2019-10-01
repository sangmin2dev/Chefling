const express = require('express');
const router = express.Router();
const fs = require('fs');
const path = require('path');

router.get('/', function(req, res, next){
    res.render('index', {title: 'Implement Local server by JSW :)', project: 'Chefling'});
});

router.get('/firebase', function(req, res, next){
    fs.readFile('./views/firebase.html',(err,data) =>{
        if(err){
            throw err;
        }
        res.end(data);
    });
});

module.exports = router;