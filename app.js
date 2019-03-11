const bodyParser 			= require("body-parser"),
	methodOveride 			= require("method-override"),
	expressSanitizer 		= require("express-sanitizer"),
	express 				= require("express"),
	app 					= express(),
	firebase 				= require("firebase");
	database 				= require('./firebaseConfig'),
	mongoose 				= require('mongoose'),
	passport 				= require('passport'),
	LocalStrategy         	= require("passport-local"),
    passportLocalMongoose 	= require("passport-local-mongoose"),
	 authRoutes 			= require('./routes/auth');	//routes import
	potholeRoutes 			= require("./routes/pothole");
	homepageRoutes          = require("./routes/homepage");
	User					= require("./models/user");


mongoose.connect("mongodb://localhost:27017/PotholeWenApp",{ useNewUrlParser: true })
	.then(result=>{
		console.log("connected to mongodb");
		
	})
	.catch(e=>{
		console.log("error connecting mongo",e);
		
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

app.use(function(req, res, next){
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

app.use("/",homepageRoutes);
app.use("/", potholeRoutes);
app.use("/",authRoutes);

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