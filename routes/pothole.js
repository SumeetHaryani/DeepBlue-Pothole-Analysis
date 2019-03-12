const path = require('path');
const express = require('express');
const potholeController = require('../controllers/pothole');
const router = express.Router();
var middleware = require("../middleware");
var app = express();

router.get("/potholes", potholeController.getDashboard);

router.get("/potholes/:pothole_id", middleware.isLoggedIn, potholeController.getIndividualPothole);

router.post("/potholes/:uid/:complaint_id", middleware.isLoggedIn, potholeController.changeStatus);
module.exports = router;