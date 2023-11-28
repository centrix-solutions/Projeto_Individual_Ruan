import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.regex.Pattern

// Classe responsável por realizar operações de login, como autenticação de usuário
class Login {
    // Função para realizar o login com base no email e senha fornecidos
    fun realizarLogin(email: String, senha: String): Funcionario? {
        // Validação do formato do email
        if (!validarEmail(email)) {
            println("Email inválido. Tente novamente.")
            return null
        }

        // Validação da complexidade da senha
        if (!validarSenha(senha)) {
            println("Senha inválida. Tente novamente.")
            return null
        }

        // Inicialização da conexão com o banco de dados
        val conexao = Conexao()
        try {
            // Definição da consulta SQL para obter informações do funcionário com o email e senha fornecidos
            val query = "SELECT nome, email, senha FROM funcionario WHERE email = ? AND senha = ?"
            val preparedStatement: PreparedStatement = conexao.obterConexao().prepareStatement(query)
            preparedStatement.setString(1, email)
            preparedStatement.setString(2, senha)

            // Execução da consulta SQL
            val resultSet: ResultSet = preparedStatement.executeQuery()

            // Verificação se as credenciais são válidas com base nos resultados da consulta
            return if (resultSet.next()) {
                val nome = resultSet.getString("nome")
                val emailResult = resultSet.getString("email")
                val senhaResult = resultSet.getString("senha")
                Funcionario(nome, emailResult, senhaResult)
            } else {
                println("Credenciais inválidas. Tente novamente.")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            // Fechamento da conexão com o banco de dados no bloco 'finally'
            conexao.obterConexao().close()
        }
    }

    // Função para validar o formato do email usando expressão regular
    private fun validarEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val pattern = Pattern.compile(emailRegex)
        return pattern.matcher(email).matches()
    }

    // Função para validar a complexidade da senha
    private fun validarSenha(senha: String): Boolean {
        return senha.length >= 6 && senha.any { it.isDigit() } && senha.any { it.isLetter() } && senha.any { it.isSpecialChar() }
    }

    // Função de extensão para Char que verifica se é um caractere especial
    private fun Char.isSpecialChar(): Boolean {
        return "!@#$%^&*()-_=+[]{}|;:'\",.<>/?".contains(this)
    }
}