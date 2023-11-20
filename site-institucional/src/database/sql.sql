-- DROP DATABASE projeto;

CREATE DATABASE IF NOT EXISTS projeto;

USE projeto;

CREATE TABLE IF NOT EXISTS system_info (
            id INT AUTO_INCREMENT PRIMARY KEY,
            cpu_count INT,
            ram_total BIGINT,
            disk_total BIGINT,
            timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS monitoramento (
        id INT AUTO_INCREMENT PRIMARY KEY,
        timestamp DATETIME,
        cpu_percent FLOAT,
        ram_percent FLOAT,
        disco_percent FLOAT
    );

SELECT * FROM system_info WHERE id = 1;
SELECT * FROM monitoramento;