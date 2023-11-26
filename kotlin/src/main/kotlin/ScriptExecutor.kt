import java.io.File
import java.io.IOException

class ScriptExecutor {
    companion object {
        fun executarScriptPython(funcionarioId: Int) {
            try {
                // Substitua "caminho/para/seu/script.py" pelo caminho real para o seu script Python
                val caminhoScriptPython =
                    "C:/Users/RUANCARDOZOMONTANARI/Documents/SPTech/Projeto_Individual_Ruan/python-r/script-python.py"

                val funcionarioId = obterIdDoFuncionarioAqui()

                // Execute o script Python passando o ID do funcionário como argumento
                val comando = "python3 $caminhoScriptPython $funcionarioId"
                val processo = ProcessBuilder(*comando.split("\\s".toRegex()).toTypedArray())
                    .directory(File(System.getProperty("user.dir")))
                    .start()

                val exitCode = processo.waitFor()
                if (exitCode == 0) {
                    println("Script Python executado com sucesso.")
                } else {
                    println("Erro ao executar o script Python.")
                }

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}
