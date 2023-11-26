import java.sql.Connection
import java.sql.PreparedStatement
import java.util.*

class Cadastro {
    companion object {
        // Método para cadastrar uma nova Empresa
        fun cadastrarEmpresa(scanner: Scanner, connection: Connection): Empresa {
            println("Cadastro empresarial")

            // Limpar o buffer do scanner antes de ler o nome
            scanner.nextLine()

            print("Nome da empresa: ")
            val nome = scanner.nextLine()

            // Validação do CNPJ
            var cnpj: String
            do {
                print("CNPJ da empresa (14 números): ")
                cnpj = scanner.nextLine()
                if (cnpj.length != 14 || !cnpj.matches(Regex("\\d{14}"))) {
                    println("CNPJ inválido. Certifique-se de inserir 14 números.")
                }
            } while (cnpj.length != 14 || !cnpj.matches(Regex("\\d{14}")))

            // Insere os dados da Empresa no banco de dados
            val insertEmpresaStatement = connection.prepareStatement(
                """
        INSERT INTO empresa (nome, cnpj)
        VALUES (?, ?)
    """, PreparedStatement.RETURN_GENERATED_KEYS
            )

            insertEmpresaStatement.setString(1, nome)
            insertEmpresaStatement.setString(2, cnpj)

            insertEmpresaStatement.executeUpdate()

            // Obtém o ID gerado automaticamente para a Empresa
            val empresaIdResultSet = insertEmpresaStatement.generatedKeys
            val empresaId = if (empresaIdResultSet.next()) {
                empresaIdResultSet.getInt(1)
            } else {
                throw RuntimeException("Erro ao obter ID da empresa")
            }

            insertEmpresaStatement.close()

            // Cadastro de Endereço associado à Empresa
            val endereco = cadastrarEndereco(scanner, connection, empresaId)

            return Empresa(empresaId, nome, cnpj)
        }


        // Método para cadastrar um novo Endereço
        fun cadastrarEndereco(scanner: Scanner, connection: Connection, empresaId: Int): Endereco {
            println("Cadastre o endereço")

            // Validação do CEP
            var cep: String
            do {
                print("CEP (8 números): ")
                cep = scanner.nextLine()
                if (cep.length != 8 || !cep.matches(Regex("\\d{8}"))) {
                    println("CEP inválido. Certifique-se de inserir 8 números.")
                }
            } while (cep.length != 8 || !cep.matches(Regex("\\d{8}")))

            // Solicitação dos dados de endereço
            print("Estado: ")
            val estado = scanner.nextLine()

            print("Cidade: ")
            val cidade = scanner.nextLine()

            print("Bairro: ")
            val bairro = scanner.nextLine()

            print("Rua: ")
            val rua = scanner.nextLine()

            print("Número: ")
            val numero = scanner.nextInt()
            scanner.nextLine() // Limpar o buffer

            print("Complemento: ")
            val complemento = scanner.nextLine()

            // Insere os dados do Endereço no banco de dados
            val insertEnderecoStatement = connection.prepareStatement(
                """
                INSERT INTO endereco (empresa_id, cep, estado, cidade, bairro, rua, numero, complemento)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """
            )

            insertEnderecoStatement.setInt(1, empresaId)
            insertEnderecoStatement.setString(2, cep)
            insertEnderecoStatement.setString(3, estado)
            insertEnderecoStatement.setString(4, cidade)
            insertEnderecoStatement.setString(5, bairro)
            insertEnderecoStatement.setString(6, rua)
            insertEnderecoStatement.setInt(7, numero)
            insertEnderecoStatement.setString(8, complemento)

            insertEnderecoStatement.executeUpdate()

            insertEnderecoStatement.close()

            return Endereco(0, empresaId, cep, estado, cidade, bairro, rua, numero, complemento)
        }

        // Método para cadastrar um novo Funcionário
        fun cadastrarFuncionario(scanner: Scanner, connection: Connection, empresaId: Int): Funcionario {
            println("\nCadastro do funcionário responsável")
            print("Nome do funcionário: ")
            val nome = scanner.nextLine()

            // Validação do Email
            var email: String
            do {
                print("Email: ")
                email = scanner.nextLine()
                if (!email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
                    println("Email inválido. Certifique-se de inserir um email válido.")
                }
            } while (!email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))

            // Validação da Senha
            var senha: String
            do {
                print("Senha (mínimo 8 caracteres, com pelo menos uma letra, um número e um caractere especial): ")
                senha = scanner.nextLine()
                if (senha.length < 8 || !senha.matches(Regex(".*[a-zA-Z].*")) ||
                    !senha.matches(Regex(".*\\d.*")) ||
                    !senha.matches(Regex(".*[!@#\$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*"))
                ) {
                    println("Senha inválida. Certifique-se de seguir os critérios de senha.")
                }
            } while (senha.length < 8 || !senha.matches(Regex(".*[a-zA-Z].*")) ||
                !senha.matches(Regex(".*\\d.*")) ||
                !senha.matches(Regex(".*[!@#\$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*"))
            )

            print("Confirmar Senha: ")
            val confirmarSenha = scanner.nextLine()

            // Validação da confirmação de senha
            if (senha != confirmarSenha) {
                println("Senha inválida. Certifique-se de que a confirmação está correta.")
                return cadastrarFuncionario(scanner, connection, empresaId)
            }

            // Insere os dados do Funcionário no banco de dados
            val insertStatement = connection.prepareStatement(
                """
                INSERT INTO funcionario (nome, email, senha, empresa_id)
                VALUES (?, ?, ?, ?)
            """
            )

            insertStatement.setString(1, nome)
            insertStatement.setString(2, email)
            insertStatement.setString(3, senha)
            insertStatement.setInt(4, empresaId)

            insertStatement.executeUpdate()

            insertStatement.close()

            return Funcionario(nome, email, senha)
        }

        // Método para realizar o login
        fun fazerLogin(scanner: Scanner, connection: Connection) {
            println("Login")

            // Limpar o buffer do scanner antes de ler o email
            scanner.nextLine()

            print("Email: ")
            val email = scanner.nextLine()

            print("Senha: ")
            val senha = scanner.nextLine()

            val funcionario = autenticarUsuario(email, senha, connection)

            if (funcionario != null) {
                println("Olá, ${funcionario.nome}!")
            } else {
                println("Login falhou. Verifique suas credenciais.")
            }
        }


        // Método para autenticar o usuário (funcionário)
        private fun autenticarUsuario(email: String, senha: String, connection: Connection): Funcionario? {
            val query = "SELECT * FROM funcionario WHERE email = ? AND senha = ?"
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, email)
            preparedStatement.setString(2, senha)
            val resultSet = preparedStatement.executeQuery()

            return if (resultSet.next()) {
                Funcionario(
                    resultSet.getString("nome"),
                    resultSet.getString("email"),
                    resultSet.getString("senha")
                )
            } else {
                null
            }
        }
    }
}