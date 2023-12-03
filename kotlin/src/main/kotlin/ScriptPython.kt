import java.io.File

object scriptPadraoPython {

    var pythonProcesses: List<Process> = listOf()

    fun criarScript(): String {

        val codigoPythonDefaultHard = """
import psutil
import time
import pymssql
from mysql.connector import connect
from datetime import datetime
from slack_sdk import WebClient
import threading

# Configurações de conexão e tokens
mysql_cnx = connect(user='root', password='363776', host='localhost', database='centrix')
sql_server_cnx = pymssql.connect(server='44.197.21.59', database='centrix', user='sa', password='centrix')
slack_token = 'xoxb-5806834878417-6181633164562-6kyLJOI7AbiZnDtq1eDz5u9l'
slack_channel = '#notificacao-ruan'
slack_client = WebClient(token=slack_token)

# Limites de notificação
limite_cpu = 89
limite_ram = 89
limite_disco = 89

def enviar_notificacao(mensagem):
    try:
        slack_client.chat_postMessage(channel=slack_channel, text=mensagem)
    except Exception as e:
        print(f"Erro ao enviar mensagem para o Slack: {e}")

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
        bdLocal_cursor.execute(add_info_sistema, (cpu_cores, ram_total, disco_total, 7))
        bdLocal_cursor.close()
        mysql_cnx.commit()

        # Inserir no SQL Server
        bdServer_cursor = sql_server_cnx.cursor()
        bdServer_cursor.execute(add_info_sistema, (cpu_cores, ram_total, disco_total, 7))
        bdServer_cursor.close()
        sql_server_cnx.commit()

        time.sleep(10)

# Função para monitorar métricas em tempo real
def monitorar_metricas_tempo_real():
    while True:
        cpu_percent = round(psutil.cpu_percent(), 2)
        ram_percent = round(psutil.virtual_memory().percent, 2)
        disco_percent = round(psutil.disk_usage('/').percent, 2)

        # Verificar se ultrapassou os limites e enviar notificação
        if cpu_percent > limite_cpu:
            mensagem_cpu = f"Aviso: Uso de CPU acima do limite! ({cpu_percent}%)"
            enviar_notificacao(mensagem_cpu)

        if ram_percent > limite_ram:
            mensagem_ram = f"Aviso: Uso de RAM acima do limite! ({ram_percent}%)"
            enviar_notificacao(mensagem_ram)

        if disco_percent > limite_disco:
            mensagem_disco = f"Aviso: Uso de Disco acima do limite! ({disco_percent}%)"
            enviar_notificacao(mensagem_disco)

        # Inserir no MySQL
        bdLocal_cursor = mysql_cnx.cursor()
        add_metricas_tempo_real = (
            "INSERT INTO metricas_tempo_real (cpu_percent, ram_percent, disco_percent, fkMaquina) "
            "VALUES (%s, %s, %s, %s)"
        )
        bdLocal_cursor.execute(add_metricas_tempo_real, (cpu_percent, ram_percent, disco_percent, 7))
        bdLocal_cursor.close()
        mysql_cnx.commit()

        # Inserir no SQL Server
        bdServer_cursor = sql_server_cnx.cursor()
        bdServer_cursor.execute(add_metricas_tempo_real, (cpu_percent, ram_percent, disco_percent, 7))
        bdServer_cursor.close()
        sql_server_cnx.commit()

        time.sleep(3)

# Criar threads para as duas funções de monitoramento
thread_info_sistema = threading.Thread(target=monitorar_info_sistema)
thread_metricas_tempo_real = threading.Thread(target=monitorar_metricas_tempo_real)

# Iniciar as threads
thread_info_sistema.start()
thread_metricas_tempo_real.start()

# Aguardar até que ambas as threads terminem
thread_info_sistema.join()
thread_metricas_tempo_real.join()
    """.trimIndent()

        val nomeArquivoPyDefaultHard = "script-python.py"
        File(nomeArquivoPyDefaultHard).writeText(codigoPythonDefaultHard)

        Thread.sleep(2 * 1000L)

        return nomeArquivoPyDefaultHard

    }

    fun executarScript(arquivo1: String) {
        val pythonProcess1 = Runtime.getRuntime().exec("py $arquivo1")
        pythonProcesses = listOf(pythonProcess1)
    }

    fun pararScript() {
        for (process in pythonProcesses) {
            process.destroyForcibly()
        }
    }
}