const path = require('path');
const express = require('express');
const potholeController = require('../controllers/pothole');
const router = express.Router();
var app=express();

router.get("/potholes", potholeController.getDashboard);

router.get("/potholes/:pothole_id", potholeController.getIndividualPothole);

router.post("/potholes/:uid/:complaint_id",potholeController.changeStatus);
module.exports = router;