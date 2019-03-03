const firebase = require("firebase");
const db = firebase.database();

exports.getDashboard = (req, res) => {
    var location = (req.query.city);
    var city = "";
    // console.log(location);
    if (location != undefined) {
        city = capitalize(location.toLowerCase()).split(",")[0];
    }
    //console.log(city);
    complaints = []
    firebase.database().ref('result').once('value')
        .then(snapshot => {
            snapshot.forEach(childSnapshot => {
                childSnapshot.forEach((complaint) => {

                    // db.ref().child('/userinfo/'+complaint.val().uid).once('value',(user)=>{

                    //     userData = user.val();

                    //     complaints.push({
                    //         complaint_id : complaint.key,
                    //         ...complaint.val(),
                    //         name : userData.name,
                    //         email: userData.email
                    //     });
                    // })
                    console.log(complaint.val().location_add);
                    var address = complaint.val().location_add;
                    if (city != "" && address.includes(city)) {

                        complaints.push({

                            complaint_id: complaint.key,
                            ...complaint.val()
                        });
                    }
                    if (city == "") {

                        complaints.push({

                            complaint_id: complaint.key,
                            ...complaint.val()
                        });
                    }
                });

                //   console.log(complaints);
                res.render("index", {
                    complaints: complaints
                });

            });

        }).catch(e => {
            console.log("error in fetching results", e);
        });

}

exports.getIndividualPothole = async (req, res) => {
    pid = req.params.pothole_id;

    try {
        const snapshot = await db.ref('result').once('value');
        snapshot.forEach(childSnapshot => {
            childSnapshot.forEach(complaint => {
                complaint_id = complaint.key;

                if (complaint_id === pid) {
                    console.log(complaint.val());

                    pothole = {
                        complaint_id: complaint.key,
                        ...complaint.val()
                    }
                    res.render('pothole/individualPothole', {
                        pothole: pothole
                    })
                }
            })
        })
    } catch (err) {
        console.log(err);

    }
}

function capitalize(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}