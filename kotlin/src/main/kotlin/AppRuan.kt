import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    println("Digite o e-mail:")
    val email = scanner.nextLine()

    println("Digite a senha:")
    val senha = scanner.nextLine()

    scanner.close()

    val connectionString = "jdbc:mysql://localhost:3306/centrix"
    val username = "root"
    val password = "senhatop"

    try {
        // Conectar ao banco de dados
        Conexao(connectionString, username, password).getConnection().use { connection ->
            // Gerenciar o login
            val login = Login(connection)
            if (login.verificarLogin(email, senha)) {
                println("Login bem-sucedido!")
            } else {
                println("Login falhou. Verifique suas credenciais.")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
