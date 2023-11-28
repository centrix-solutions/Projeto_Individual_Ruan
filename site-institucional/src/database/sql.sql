-- DROP DATABASE projeto;   -- Este comando está comentado, mas se descomentado, apaga o banco de dados "projeto".

CREATE DATABASE IF NOT EXISTS projeto;   -- Cria o banco de dados "projeto" se ele não existir.

USE projeto;   -- Define o banco de dados atual como "projeto".

CREATE TABLE IF NOT EXISTS funcionario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255),
    senha VARCHAR(255)
);

-- Inserindo um funcionário de exemplo.
INSERT INTO funcionario VALUES
    (null, "Ruan", "ruan@gmail.com", "ruan@2004");

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

-- Selecionando todos os registros das tabelas.
SELECT * FROM funcionario;
SELECT * FROM info_sistema;
SELECT * FROM metricas_tempo_real;
