import java.sql.Connection
import java.sql.DriverManager

// Classe responsável pela conexão com o banco de dados
class Conexao {
    companion object {
        // Método para estabelecer a conexão com o banco de dados
        fun conectar(): Connection {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/projeto", "root", "363776")
        }

        // Método para criar as tabelas no banco de dados, se não existirem
        fun criarTabelas(connection: Connection) {
            val statement = connection.createStatement()

            // Tabela Empresa
            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS empresa (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nome VARCHAR(255) NOT NULL,
                    cnpj VARCHAR(14) NOT NULL
                )
            """
            )

            // Tabela Endereco
            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS endereco (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    empresa_id INT,
                    FOREIGN KEY (empresa_id) REFERENCES empresa(id),
                    cep VARCHAR(8) NOT NULL,
                    estado VARCHAR(255) NOT NULL,
                    cidade VARCHAR(255) NOT NULL,
                    bairro VARCHAR(255) NOT NULL,
                    rua VARCHAR(255) NOT NULL,
                    numero INT NOT NULL,
                    complemento VARCHAR(255)
                )
            """
            )

            // Tabela Funcionario
            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS funcionario (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nome VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    senha VARCHAR(255) NOT NULL,
                    empresa_id INT,
                    FOREIGN KEY (empresa_id) REFERENCES empresa(id)
                )
            """
            )

            statement.close()
        }
    }
}