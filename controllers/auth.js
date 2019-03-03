const firebase = require('firebase');
const database = require('../firebaseConfig');

exports.getLogin = (req,res)=>{
    res.render('auth/login');
}

exports.getRegister = (req,res)=>{
    res.render('auth/register');
}

exports.postLogin = (req,res)=>{
    const email = req.body.email;
	const password = req.body.password;
	firebase.auth().signInWithEmailAndPassword(email, password)
		.catch(function (error) {
			// Handle Errors here.
			const errorCode = error.code;
			const errorMessage = error.message;
			if (errorCode === "auth/wrong-password") {
				console.log("Wrong password.");
			} else {
				console.log(errorMessage);
			}
			console.log(error);
			res.redirect("/login");
			// ...
		});
	const user = firebase.auth().currentUser;
	if (user != null) {

		const uid = user.uid;
		console.log(uid);
	}
	res.redirect("/");
}

exports.postRegister = (req,res)=>{
    // const newUser = new User({username: req.body.username});
	const email = req.body.email;
	const password = req.body.password;
	const name = req.body.name;
	firebase
		.auth()
		.createUserWithEmailAndPassword(email, password)
		.then(function (user) {})
		.catch(function (error) {
			// Handle Errors here.
			const errorCode = error.code;
			const errorMessage = error.message;
			return res.render("auth/register");
			// ...
		});
	firebase.auth().onAuthStateChanged(function (user) {
		if (user) {
			// User is signed in.
			const uid = user.uid;
			writeUserData(uid, name); // ...
		} else {
			// User is signed out.
			// ...
		}
	});
	// const user = firebase.auth().currentUser;
	// if(user!=null){
	// const  name = user.displayName;
	// const uid= user.uid;

	// }
	//	console.log(req.body);
	// 
	//   if(err) {
	//     // req.flash("error", err.message);
	//     return res.render("register");
	//   }

	res.redirect("/");

	// });
}

exports.logout = (req,res)=>{
    firebase.auth().signOut().then(function () {
		console.log("Logged out!");
	}, function (error) {
		console.log(error.code);
		console.log(error.message);
	});
	res.redirect("/");
}

function writeUserData(userId, name) {
	database.ref("authorities/" + userId).set({
		username: name
	});
}