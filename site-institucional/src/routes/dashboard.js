var express = require("express");
var router = express.Router();

var dashboardController = require("../controllers/dashboardController");

router.get("/calcular", function (req, res) {
    dashboardController.calcular(req, res);
});

router.get("/metricas_tempo_real", function (req, res) {
    dashboardController.obterMetricasTempoReal(req, res);
});

module.exports = router;
