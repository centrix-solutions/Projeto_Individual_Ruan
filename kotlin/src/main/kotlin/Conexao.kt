import java.sql.Connection
import java.sql.DriverManager

// Classe para estabelecer a conexão com o banco de dados MySQL
class Conexao {
    // Configurações de conexão
    private val url = "jdbc:mysql://localhost:3306/projeto" // URL do banco de dados
    private val user = "root" // Nome de usuário do banco de dados
    private val password = "363776" // Senha do banco de dados

    // Função para obter uma conexão com o banco de dados
    fun obterConexao(): Connection {
        // Retorna a conexão estabelecida utilizando as configurações fornecidas
        return DriverManager.getConnection(url, user, password)
    }
}