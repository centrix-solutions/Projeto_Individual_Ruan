import psutil
import time
import pymssql
from mysql.connector import connect
from slack_sdk import WebClient
import threading

# Configurações de conexão e tokens
mysql_cnx = connect(user='root', password='363776', host='localhost', database='centrix')
sql_server_cnx = pymssql.connect(server='44.197.21.59', database='centrix', user='sa', password='centrix')
slack_token = 'xoxb-5806834878417-6181633164562-UNgjvP47AfYcw63CbQhHVGXS'
slack_channel = '#notificacao-ruan'
slack_client = WebClient(token=slack_token)



# Função para monitorar informações do sistema
def monitorar_info_sistema():
    while True:
        cpu_cores = psutil.cpu_count(logical=False)
        ram_total = round(psutil.virtual_memory().total / (1024**3), 3)
        disco_total = round(psutil.disk_usage('/').total / (1024**3), 3)

        # Inserir no MySQL
        bdLocal_cursor = mysql_cnx.cursor()
        add_info_sistema = (
            "INSERT INTO info_sistema (cpu_cores, ram_total, disco_total, fkMaquina) "
            "VALUES (%s, %s, %s, %s)"
        )
        bdLocal_cursor.execute(add_info_sistema, (cpu_cores, ram_total, disco_total, 3))
        bdLocal_cursor.close()
        mysql_cnx.commit()

        # Inserir no SQL Server
        bdServer_cursor = sql_server_cnx.cursor()
        bdServer_cursor.execute(add_info_sistema, (cpu_cores, ram_total, disco_total, 3))
        bdServer_cursor.close()
        sql_server_cnx.commit()

        time.sleep(10)

# Criar thread para a função de monitoramento de info_sistema
thread_info_sistema = threading.Thread(target=monitorar_info_sistema)

# Iniciar a thread
thread_info_sistema.start()

# Aguardar até que a thread termine
thread_info_sistema.join()
