import psutil
import mysql.connector
from datetime import datetime, timedelta
from slack_sdk import WebClient
from slack_sdk.errors import SlackApiError
import time

# Função para se conectar ao MySQL
def conectar_mysql():
        return mysql.connector.connect(
            host="localhost",
            user="root",
            password="363776",
            database="projeto"
        )

# Função para obter informações do sistema
def obter_info_sistema():
        cpu_cores = psutil.cpu_count(logical=False)
        ram_total = round(psutil.virtual_memory().total / (1024 ** 3))  # Convertendo para GB
        disco_total = round(psutil.disk_usage('/').total / (1024 ** 3))  # Convertendo para GB

        return cpu_cores, ram_total, disco_total

# Função para conectar ao MySQL e criar tabelas se não existirem
def conectar_mysql_criar_tabelas():
        connection = conectar_mysql()
        cursor = connection.cursor()

        # Criação da tabela de informações do sistema
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS info_sistema (
                id INT AUTO_INCREMENT PRIMARY KEY,
                data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                cpu_cores INT,
                ram_total INT,
                disco_total INT
            )
        """)

        # Criação da tabela de métricas em tempo real
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS metricas_tempo_real (
                id INT AUTO_INCREMENT PRIMARY KEY,
                data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                cpu_percent FLOAT,
                ram_percent FLOAT,
                disco_percent FLOAT
            )
        """)

        connection.commit()
        connection.close()

# Função para inserir dados de informações do sistema no MySQL
def inserir_info_sistema_mysql(cpu_cores, ram_total, disco_total):
        connection = conectar_mysql()
        cursor = connection.cursor()

        # Inserindo dados na tabela de informações do sistema
        cursor.execute("""
            INSERT INTO info_sistema (cpu_cores, ram_total, disco_total)
            VALUES (%s, %s, %s)
        """, (cpu_cores, ram_total, disco_total))

        connection.commit()
        connection.close()

# Função para obter e inserir métricas em tempo real no MySQL
def obter_inserir_metricas_tempo_real():
        while True:
            cpu_percent = psutil.cpu_percent(interval=1)
            ram_percent = psutil.virtual_memory().percent
            disco_percent = psutil.disk_usage('/').percent

            # Inserindo dados na tabela de métricas em tempo real
            connection = conectar_mysql()
            cursor = connection.cursor()

            cursor.execute("""
                INSERT INTO metricas_tempo_real (cpu_percent, ram_percent, disco_percent)
                VALUES (%s, %s, %s)
            """, (cpu_percent, ram_percent, disco_percent))

            connection.commit()
            connection.close()

            # Verificando se alguma métrica ultrapassou 90% e notificando no Slack
            if cpu_percent > 90 or ram_percent > 90 or disco_percent > 90:
                notificar_slack(f"Atenção: Uma métrica ultrapassou 90% - CPU: {cpu_percent}%, RAM: {ram_percent}%, Disco: {disco_percent}%")

            time.sleep(3)

# Função para notificar no Slack
def notificar_slack(mensagem):
        token = "xoxb-5806834878417-6181633164562-kyvIcS7NoYBGR1M0zwlWjf2A"
        channel = "#notificacao-ruan"

        client = WebClient(token=token)

        try:
            response = client.chat_postMessage(
                channel=channel,
                text=mensagem
            )
            print("Mensagem enviada para o Slack:", response['ts'])
        except SlackApiError as e:
            print(f"Erro ao enviar mensagem para o Slack: {e.response['error']}")

# Verificando informações do sistema diariamente
def verificar_info_sistema_diariamente():
        while True:
            # Obtendo informações do sistema
            cpu_cores, ram_total, disco_total = obter_info_sistema()

            # Inserindo informações no MySQL
            inserir_info_sistema_mysql(cpu_cores, ram_total, disco_total)

            # Aguardando 24 horas
            time.sleep(24 * 60 * 60)

    # Iniciando as funções em threads separadas
if __name__ == "__main__":
    from threading import Thread

# Thread para verificar informações do sistema diariamente
thread_info_diaria = Thread(target=verificar_info_sistema_diariamente)

# Thread para obter e inserir métricas em tempo real
thread_metricas_tempo_real = Thread(target=obter_inserir_metricas_tempo_real)

# Thread para conectar ao MySQL e criar tabelas se não existirem
thread_conectar_mysql_criar_tabelas = Thread(target=conectar_mysql_criar_tabelas)

# Iniciando as threads
thread_info_diaria.start()
thread_metricas_tempo_real.start()
thread_conectar_mysql_criar_tabelas.start()