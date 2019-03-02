const firebase = require("firebase");

exports.getDashboard = (req,res)=>{
    const user = firebase.auth().currentUser;
	res.render("index");
}
