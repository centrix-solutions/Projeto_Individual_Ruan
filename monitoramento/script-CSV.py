import psutil
import pandas as pd
import mysql.connector
from datetime import datetime
import time

# Conectar ao banco de dados MySQL
conn = mysql.connector.connect(
    host='localhost',
    user='root',
    password='363776',
    database='projeto'
)
cursor = conn.cursor()

# Criar uma tabela para armazenar os dados
cursor.execute('''
    CREATE TABLE IF NOT EXISTS monitoramento (
        id INT AUTO_INCREMENT PRIMARY KEY,
        timestamp DATETIME,
        cpu_percent FLOAT,
        ram_percent FLOAT,
        disco_percent FLOAT
    )
''')
conn.commit()

def obter_dados():
    # Obter dados de CPU, RAM e Disco
    cpu_percent = psutil.cpu_percent(interval=1)
    ram_percent = psutil.virtual_memory().percent
    disco_percent = psutil.disk_usage('/').percent

    # Obter timestamp atual
    timestamp = datetime.now()

    return timestamp, cpu_percent, ram_percent, disco_percent

def inserir_dados_no_banco_de_dados(timestamp, cpu_percent, ram_percent, disco_percent):
    # Inserir dados na tabela
    cursor.execute('''
        INSERT INTO monitoramento (timestamp, cpu_percent, ram_percent, disco_percent)
        VALUES (%s, %s, %s, %s)
    ''', (timestamp, cpu_percent, ram_percent, disco_percent))
    conn.commit()

def main():
    while True:
        timestamp, cpu_percent, ram_percent, disco_percent = obter_dados()
        inserir_dados_no_banco_de_dados(timestamp, cpu_percent, ram_percent, disco_percent)
        print(f'Timestamp: {timestamp}, CPU: {cpu_percent}%, RAM: {ram_percent}%, Disco: {disco_percent}%')
        time.sleep(30)  # Espera 30 segundos antes de obter os próximos dados

if __name__ == "__main__":
    main()
