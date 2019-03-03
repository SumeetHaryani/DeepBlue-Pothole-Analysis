const path = require('path');
const express = require('express');
const potholeController = require('../controllers/pothole');
const router = express.Router();

router.get("/", potholeController.getDashboard);

router.get("/potholes/:pothole_id", potholeController.getIndividualPothole);

module.exports = router;