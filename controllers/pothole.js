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

            // console.log(complaints);
            res.render("index",{
                complaints:complaints
            });
            
        });
                
    })
    .catch(e=>{
        console.log("error in fetching results",e);       
    })
            
}

exports.getIndividualPothole = async (req,res)=>{
    pid = req.params.pothole_id;

    try{
        const snapshot = await db.ref('result').once('value');
        snapshot.forEach(childSnapshot=>{
            childSnapshot.forEach(complaint=>{
                complaint_id = complaint.key;
                
                if(complaint_id === pid){
                    console.log(complaint.val());
                    
                    pothole = {
                        complaint_id : complaint.key,
                        ...complaint.val()
                    }
                    res.render('pothole/individualPothole',{
                        pothole : pothole
                    })
                }
            })
        })
    }catch(err){
        console.log(err);
        
    }   
}

exports.changeStatus = async (req,res)=>{
    const status = req.body.statusPicker;
    const uid = req.params.uid;
    const complaint_id = req.params.complaint_id;
    console.log(complaint_id);
    
    const snapshot = await db.ref('result/'+uid+'/'+complaint_id).update({
        status : status
    });
    return res.redirect('/potholes/'+complaint_id);
}