-- DROP DATABASE projeto;
CREATE DATABASE IF NOT EXISTS projeto;
USE projeto;

CREATE TABLE IF NOT EXISTS info_sistema (
id INT AUTO_INCREMENT PRIMARY KEY,
data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
cpu_cores INT,
ram_total INT,
disco_total INT
);

CREATE TABLE IF NOT EXISTS metricas_tempo_real (
id INT AUTO_INCREMENT PRIMARY KEY,
data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
cpu_percent FLOAT,
ram_percent FLOAT,
disco_percent FLOAT
);

SELECT * FROM info_sistema;
SELECT * FROM metricas_tempo_real;