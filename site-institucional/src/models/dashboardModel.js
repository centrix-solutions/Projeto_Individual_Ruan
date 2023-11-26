var database = require("../database/config");

function calcular() {

    var instrucao = `SELECT * FROM info_sistema WHERE id = 1;`;

    console.log("Executando a instrução SQL: \n" + instrucao);
    return database.executar(instrucao);
}

function obterMetricasTempoReal() {
    var instrucao = `SELECT * FROM metricas_tempo_real ORDER BY data_hora DESC LIMIT 5;`;
    console.log("Executando a instrução SQL: \n" + instrucao);
    return database.executar(instrucao);
}

module.exports = {
    calcular,
    obterMetricasTempoReal
}
