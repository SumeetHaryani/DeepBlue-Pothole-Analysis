const bodyParser = require("body-parser"),
	methodOveride = require("method-override"),
	expressSanitizer = require("express-sanitizer"),
	express = require("express"),
	app = express(),
	firebase = require("firebase");
    database = require('./firebaseConfig'),
	mongoose = require('mongoose'),
	passport = require('passport'),
	LocalStrategy = require("passport-local"),
	passportLocalMongoose = require("passport-local-mongoose"),
	authRoutes = require('./routes/auth'); //routes import
	potholeRoutes = require("./routes/pothole");
	homepageRoutes = require("./routes/homepage");
	User = require("./models/user");

app.use(express.static(__dirname + '/public'));

// const MongoClient = require('mongodb').MongoClient;
// const uri = "mongodb+srv://sam:123456@cluster0-1uhjw.mongodb.net/test?retryWrites=true";
// const client = new MongoClient(uri, { useNewUrlParser: true });
// client.connect(err => {
//   const collection = client.db("test").collection("devices");
//   // perform actions on the collection object
//   client.close();
// });

mongoose.connect("mongodb+srv://sam:abcd123@cluster0-1uhjw.mongodb.net/test?retryWrites=true", {
		useNewUrlParser: true
	})
	.then(result => {
		console.log("connected to mongodb");

	})
	.catch(e => {
		console.log("error connecting mongo", e);

	})
app.use(require("express-session")({
	secret: "Rusty is the best and cutest dog in the world",
	resave: false,
	saveUninitialized: false
}));

app.use(passport.initialize());
app.use(passport.session());

passport.use(new LocalStrategy(User.authenticate()));
passport.serializeUser(User.serializeUser());
passport.deserializeUser(User.deserializeUser());

app.use(function (req, res, next) {
	res.locals.currentUser = req.user;
	next();
});
// tell express to use "ejs" as our templating engine
app.set("view engine", "ejs");
//serve public directory
app.use(express.static("public"));
// use body parser to parse req.body into a JS object
app.use(
	bodyParser.urlencoded({
		extended: true
	})
);
// use method-override to override form POST request into PUT request
app.use(methodOveride("_method"));
// use expressSanitizer to sanitize the input given by user
//app.use(expressSanitizer());

app.use("/", homepageRoutes);
app.use("/", potholeRoutes);
app.use("/", authRoutes);

// const ref = database.ref("result/HDL7SJZlZNRbw452zWZclKgTkTu2");
// ref.once("value", function (snapshot) {
// 		const potholes = snapshot.val();
// 		//console.log(potholes);
// 	},
// 	function (errorObject) {
// 		console.log("The read failed: " + errorObject.code);
// 	}
// );


app.listen(3000, function () {
	console.log("Server has started at PORT 3000");
});