const path = require('path');
const express = require('express');
const authController = require('../controllers/auth');
const router = express.Router();

router.get("/login", authController.getLogin);

router.post("/login", passport.authenticate("local", {
    successRedirect: "/potholes",
    failureRedirect: "/login"
}), authController.postLogin);

router.get("/logout", authController.logout);

router.get("/register", authController.getRegister);

router.post("/register", authController.postRegister);

function isLoggedIn(req, res, next) {
    if (req.isAuthenticated()) {
        return next();
    }
    res.redirect("/login");
}

module.exports = router;