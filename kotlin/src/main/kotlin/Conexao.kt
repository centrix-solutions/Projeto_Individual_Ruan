import java.sql.Connection
import java.sql.DriverManager

class Conexao(private val connectionString: String, private val username: String, private val password: String) {

    fun getConnection(): Connection {
        return DriverManager.getConnection(connectionString, username, password)
    }
}
