const firebase = require("firebase");
const db = firebase.database();

exports.getDashboard = (req,res)=>{
    complaints = []
    firebase.database().ref('result').once('value')
    .then(snapshot=>{
        snapshot.forEach(childSnapshot=>{
            childSnapshot.forEach((complaint)=>{

                // db.ref().child('/userinfo/'+complaint.val().uid).once('value',(user)=>{
                    
                //     userData = user.val();
                    
                //     complaints.push({
                //         complaint_id : complaint.key,
                //         ...complaint.val(),
                //         name : userData.name,
                //         email: userData.email
                //     });
                // })
                complaints.push({
                
                    complaint_id : complaint.key,
                    ...complaint.val()
                });
                
            });

            console.log(complaints);
            res.render("index",{
                complaints:complaints
            });
            
        });
                
    })
    .catch(e=>{
        console.log("error in fetching results",e);       
    })
            
}
