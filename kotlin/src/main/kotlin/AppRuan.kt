fun main() {
    // Configuração do scanner para entrada de dados
    val scanner = java.util.Scanner(System.`in`)
    var opcao: Int

    // Loop principal do programa
    do {
        // Exibição do menu
        println("Bem-vindo! O que você deseja fazer?")
        println("1 -> Fazer Login")
        println("2 -> Sair")

        try {
            // Leitura da opção do usuário
            opcao = scanner.nextInt()

            when (opcao) {
                1 -> {
                    // Chama a função fazerLogin() e encerra o programa se o login for bem-sucedido
                    if (fazerLogin(scanner)) {
                        return
                    }
                }

                2 -> {
                    // Mensagem de despedida e encerra o programa
                    println("Saindo da aplicação. Até mais!")
                    return
                }

                else -> println("Opção inválida. Tente novamente.")
            }
        } catch (e: Exception) {
            // Tratamento de exceção para entrada inválida
            println("Por favor, insira uma opção válida.")
            scanner.nextLine()
            opcao = 0
        }
    } while (opcao != 2)
}

// Função para realizar o login
fun fazerLogin(scanner: java.util.Scanner): Boolean {
    var email: String
    var senha: String

    // Loop para solicitar email e senha ao usuário
    do {
        println("Digite seu email: ")
        email = scanner.next()

        println("Digite sua senha: ")
        senha = scanner.next()

        // Instanciação da classe Login para realizar o login
        val login = Login()
        val funcionario = login.realizarLogin(email, senha)

        // Verifica se o login foi bem-sucedido
        if (funcionario != null) {
            println("Login bem-sucedido. Bem-vindo, ${funcionario.nome}!")
            return true
        } else {
            println("Falha no login. Verifique suas credenciais e tente novamente.")
        }

        // Pergunta se o usuário deseja tentar novamente
        println("Deseja tentar novamente? (S/N): ")
    } while (scanner.next().equals("S", ignoreCase = true))

    // Retorna false se o usuário optar por não tentar novamente
    return false
}