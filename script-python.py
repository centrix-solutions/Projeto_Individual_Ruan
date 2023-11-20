import psutil
import mysql.connector

# Função para obter informações sobre CPU, RAM e disco
def get_system_info():
    cpu_info = psutil.cpu_count(logical=False)  # Obtém o número de núcleos físicos da CPU
    ram_info = psutil.virtual_memory()
    disk_info = psutil.disk_usage('/')

    return {
        'cpu': cpu_info,
        'ram': bytes_to_gb(ram_info.total),
        'disk': bytes_to_gb(disk_info.total)
    }

def bytes_to_gb(bytes_value):
    gb_value = bytes_value / (1024 ** 3)  # 1 gigabyte = 1024 megabytes = 1024^2 kilobytes = 1024^3 bytes
    return round(gb_value, 2)

# Função para conectar ao banco de dados e inserir as informações
def insert_into_database(info):
    try:
        db_connection = mysql.connector.connect(
            host="localhost",
            user="root",
            password="363776",
            database="projeto"
        )

        cursor = db_connection.cursor()

        create_table_query = """
        CREATE TABLE IF NOT EXISTS system_info (
            id INT AUTO_INCREMENT PRIMARY KEY,
            cpu_count INT,
            ram_total BIGINT,
            disk_total BIGINT,
            timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
        """
        cursor.execute(create_table_query)

        insert_query = """
        INSERT INTO system_info (cpu_count, ram_total, disk_total)
        VALUES (%s, %s, %s)
        """
        cursor.execute(insert_query, (info['cpu'], info['ram'], info['disk']))

        db_connection.commit()
        print("Informações inseridas com sucesso no banco de dados.")

    except mysql.connector.Error as err:
        print(f"Erro: {err}")

    finally:
        try:
            # Fecha a conexão apenas se estiver aberta
            if db_connection.is_connected():
                cursor.close()
                db_connection.close()
        except UnboundLocalError:
            # Se ocorrer um erro durante a conexão, db_connection pode não ser definido
            pass

# Obtém as informações do sistema
system_info = get_system_info()

# Insere as informações no banco de dados
insert_into_database(system_info)
