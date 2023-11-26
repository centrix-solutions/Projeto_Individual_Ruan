import java.sql.Connection
import java.sql.ResultSet
import java.util.concurrent.TimeUnit

class Login(private val connection: Connection) {

    fun verificarLogin(email: String, senha: String): Boolean {
        val sql = "SELECT * FROM Funcionario WHERE email = ? AND senha = ?"
        try {
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setString(1, email)
                preparedStatement.setString(2, senha)

                val resultSet: ResultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    // Se o login for bem-sucedido, execute o script Python
                    executarScriptPython()
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun executarScriptPython() {
        try {
            val processBuilder = ProcessBuilder("python", "script-python")
                .inheritIO() // Redirecionar a entrada/saída padrão para o console
                .start()

            // Aguardar até que o processo Python termine (timeout de 10 segundos)
            if (!processBuilder.waitFor(10, TimeUnit.SECONDS)) {
                processBuilder.destroy() // Encerrar o processo se demorar muito
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
