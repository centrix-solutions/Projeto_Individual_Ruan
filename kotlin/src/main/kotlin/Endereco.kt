// Representa os dados do Endereço
data class Endereco(
    val id: Int,
    val empresaId: Int,
    val cep: String,
    val estado: String,
    val cidade: String,
    val bairro: String,
    val rua: String,
    val numero: Int,
    val complemento: String?
)