import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    // Realiza a conexão com o banco de dados MySQL
    val connection = Conexao.conectar()

    // Cria as tabelas se não existirem
    Conexao.criarTabelas(connection)

    var escolha: Int

    do {
        println("\nMenu:")
        println("1 - Cadastrar empresa")
        println("2 - Fazer login")
        println("3 - Sair")
        print("Escolha uma opção: ")

        escolha = try {
            scanner.nextInt()
        } catch (e: InputMismatchException) {
            scanner.nextLine()
            -1
        }

        when (escolha) {
            1 -> {
                val empresa = Cadastro.cadastrarEmpresa(scanner, connection)
                println("Empresa cadastrada com sucesso!")
                val funcionario = Cadastro.cadastrarFuncionario(scanner, connection, empresa.id)
                println("Funcionário cadastrado com sucesso!")

                exibirInformacoes(empresa, funcionario)
            }

            2 -> Cadastro.fazerLogin(scanner, connection)
            3 -> println("Saindo da aplicação.")
            else -> println("Opção inválida. Tente novamente.")
        }
    } while (escolha != 3)

    connection.close()
}

// Método para exibir as informações cadastradas
fun exibirInformacoes(empresa: Empresa, funcionario: Funcionario) {
    println("\nInformações cadastradas:")
    println("Empresa:")
    println("ID: ${empresa.id}")
    println("Nome: ${empresa.nome}")
    println("CNPJ: ${empresa.cnpj}")

    // Recupera o endereço associado à Empresa
    val endereco = obterEndereco(empresa.id)
    println("\nEndereço:")
    println("CEP: ${endereco.cep}")
    println("Estado: ${endereco.estado}")
    println("Cidade: ${endereco.cidade}")
    println("Bairro: ${endereco.bairro}")
    println("Rua: ${endereco.rua}")
    println("Número: ${endereco.numero}")
    println("Complemento: ${endereco.complemento}")

    println("\nFuncionário:")
    println("Nome: ${funcionario.nome}")
    println("Email: ${funcionario.email}")
}

// Método para obter o Endereço associado à Empresa
fun obterEndereco(empresaId: Int): Endereco {
    val connection = Conexao.conectar()
    val query = "SELECT * FROM endereco WHERE empresa_id = ?"
    val preparedStatement = connection.prepareStatement(query)
    preparedStatement.setInt(1, empresaId)
    val resultSet = preparedStatement.executeQuery()

    val endereco = if (resultSet.next()) {
        Endereco(
            resultSet.getInt("id"),
            resultSet.getInt("empresa_id"),
            resultSet.getString("cep"),
            resultSet.getString("estado"),
            resultSet.getString("cidade"),
            resultSet.getString("bairro"),
            resultSet.getString("rua"),
            resultSet.getInt("numero"),
            resultSet.getString("complemento")
        )
    } else {
        throw RuntimeException("Endereço não encontrado para a empresa com ID $empresaId")
    }

    preparedStatement.close()
    connection.close()

    return endereco
}