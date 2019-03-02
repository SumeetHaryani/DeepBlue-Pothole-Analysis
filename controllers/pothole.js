const firebase = require("firebase");


exports.getDashboard = (req,res)=>{
    // console.log(firebase.database().ref('/result'));
    
    firebase.database().ref('result').once('value').then(snapshot=>{
        console.log(snapshot.val());
        
    })
	res.render("index");
}
