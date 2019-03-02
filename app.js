var bodyParser = require("body-parser"),
	methodOveride = require("method-override"),
	expressSanitizer = require("express-sanitizer"),
	express = require("express"),
	app = express(),
	firebase = require("firebase");

// Initialize Firebase
var config = {
	apiKey: "AIzaSyDSPHnMAGY1iqe12wk_WRakxCoEutxM7dE",
	authDomain: "deep-blue-sample.firebaseapp.com",
	databaseURL: "https://deep-blue-sample.firebaseio.com",
	projectId: "deep-blue-sample",
	storageBucket: "deep-blue-sample.appspot.com",
	messagingSenderId: "1070942423558"
};
firebase.initializeApp(config);
var database = firebase.database();

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

app.get("/", function (req, res) {
	res.redirect("/potholes");
});
var ref = database.ref("result/HDL7SJZlZNRbw452zWZclKgTkTu2");
ref.once("value", function (snapshot) {
		var potholes = snapshot.val();
		//console.log(potholes);
	},
	function (errorObject) {
		console.log("The read failed: " + errorObject.code);
	}
);
// INDEX ROUTE
app.get("/potholes", function (req, res) {
	var user = firebase.auth().currentUser;
	res.render("index");
});
app.get("/login", function (req, res) {
	res.render("login");
});
app.get("/logout", function (req, res) {
	firebase.auth().signOut().then(function () {
		console.log("Logged out!");
	}, function (error) {
		console.log(error.code);
		console.log(error.message);
	});
	res.redirect("/potholes");
});
app.get("/register", function (req, res) {
	res.render("register");
});

function writeUserData(userId, name) {
	database.ref("authorities/" + userId).set({
		username: name
	});
}

// handle sign up logic
app.post("/register", function (req, res) {
	// var newUser = new User({username: req.body.username});
	var email = req.body.email;
	var password = req.body.password;
	var name = req.body.name;
	firebase
		.auth()
		.createUserWithEmailAndPassword(email, password)
		.then(function (user) {})
		.catch(function (error) {
			// Handle Errors here.
			var errorCode = error.code;
			var errorMessage = error.message;
			return res.render("register");
			// ...
		});
	firebase.auth().onAuthStateChanged(function (user) {
		if (user) {
			// User is signed in.
			var uid = user.uid;
			writeUserData(uid, name); // ...
		} else {
			// User is signed out.
			// ...
		}
	});
	// var user = firebase.auth().currentUser;
	// if(user!=null){
	// var  name = user.displayName;
	// var uid= user.uid;

	// }
	//	console.log(req.body);
	// 
	//   if(err) {
	//     // req.flash("error", err.message);
	//     return res.render("register");
	//   }

	res.redirect("/potholes");

	// });
});
app.post("/login", function (req, res) {
	var email = req.body.email;
	var password = req.body.password;
	firebase.auth().signInWithEmailAndPassword(email, password)
		.catch(function (error) {
			// Handle Errors here.
			var errorCode = error.code;
			var errorMessage = error.message;
			if (errorCode === "auth/wrong-password") {
				console.log("Wrong password.");
			} else {
				console.log(errorMessage);
			}
			console.log(error);
			res.redirect("/login");
			// ...
		});
	var user = firebase.auth().currentUser;
	if (user != null) {

		var uid = user.uid;
		console.log(uid);
	}
	res.redirect("/potholes");
});





app.listen(3000, function () {
	console.log("Server has started at PORT 3000");
});