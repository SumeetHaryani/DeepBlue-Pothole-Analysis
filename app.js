const bodyParser = require("body-parser"),
	methodOveride = require("method-override"),
	expressSanitizer = require("express-sanitizer"),
	express = require("express"),
	app = express(),
	firebase = require("firebase");
	database = require('./firebaseConfig');
const authRoutes = require('./routes/auth');	//routes import
	potholeRoutes = require("./routes/pothole");


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

app.get("/",(req,res)=>{
	res.redirect('/potholes');
})
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