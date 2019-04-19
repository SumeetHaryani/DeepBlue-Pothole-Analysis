const path = require('path');
const express = require('express');
const homepagecontroller = require('../controllers/homepage');
const router = express.Router();

router.get("/", homepagecontroller.getHomePage);
module.exports = router;