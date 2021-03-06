// serverjs

// [LOAD PACKAGES]
var express     = require('express');
var app         = express();
var bodyParser  = require('body-parser');
var mongoose    = require('mongoose');

// [ CONFIGURE mongoose ]

// CONNECT TO MONGODB SERVER
var db = mongoose.connection;
db.on('error', console.error);
db.once('open', function(){
    // CONNECTED TO MONGODB SERVER
    console.log("Connected to mongod server");
});

mongoose.connect('mongodb://localhost/project2');

// DEFINE MODEL
var User = require('./models/user');

// [CONFIGURE APP TO USE bodyParser]
app.use(bodyParser.json({limit : '50mb'}))
app.use(bodyParser.urlencoded({ extended: true, limit : '50mb' }));


// [CONFIGURE SERVER PORT]

var port = process.env.PORT || 80;

// [CONFIGURE ROUTER]
var userRouter = require('./routes/userRouter.js')(app, User);

// [RUN SERVER]
var server = app.listen(port, function(){
 console.log("Express server has started on port " + port)
});