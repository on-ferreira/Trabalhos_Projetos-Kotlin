import kotlin.math.roundToInt

private const val PAES = 1
private const val SALGADOS = 2
private const val DOCES = 3
private const val BEBIDAS = 4
private const val FINALIZAR = 0

private const val LINHA = ".........."

val valorPaoFrances = 0.60
val valorPaoDeLeite = 0.40
val valorPaoDeMilho = 0.50
val valorCoxinha = 5.00
val valorEsfiha = 6.00
val valorPaodeQueijo = 3.00
val valorCarolina = 1.50
val valorPudim = 4.00
val valorBrigadeiro = 2.00
val valorCafePuro = 1.00
val valorPingado = 2.00
val valorSuco = 4.00

val produtoPaoFrances = "Pão Francês"
val produtoPaoDeLeite = "Pão de Leite"
val produtoPaoDeMilho = "Pão de Milho"
val produtoCoxinha = "Coxinha"
val produtoEsfiha = "Esfiha"
val produtoPaodeQueijo = "Pão de Queijo"
val produtoCarolina = "Carolina"
val produtoPudim = "Pudim"
val produtoBrigadeiro = "Brigadeiro"
val produtoCafePuro = "CafePuro"
val produtoPingado = "Pingado"
val produtoSuco = "Suco"

val paes: List<Pair<String, Double>> = listOf(
    Pair(produtoPaoFrances, valorPaoFrances), // index 0
    Pair(produtoPaoDeLeite, valorPaoDeLeite), // index 1
    Pair(produtoPaoDeMilho, valorPaoDeMilho), // index 2
)

val salgados: List<Pair<String, Double>> = listOf(
    Pair(produtoCoxinha, valorCoxinha),
    Pair(produtoEsfiha, valorEsfiha),
    Pair(produtoPaodeQueijo, valorPaodeQueijo)
)

val doces: List<Pair<String, Double>> = listOf(
    Pair(produtoCarolina, valorCarolina),
    Pair(produtoPudim,valorPudim),
    Pair(produtoBrigadeiro, valorBrigadeiro)
)

val bebidas: List<Pair<String, Double>> = listOf(
    Pair(produtoCafePuro, valorCafePuro),
    Pair(produtoPingado,valorPingado),
    Pair(produtoSuco,valorSuco)
)

val categorias = """
    Digite a opção desejada no Menu:
    1..................Pães
    2..............Salgados
    3.................Doces
    4...............Bebidas
    0......Finalizar compra
""".trimIndent()

val itensComanda: MutableList<String> = mutableListOf<String>()
var total: Double = 0.0
var desconto: Double = 0.0

fun main() {
    do {
        var finalizarCompra = "S"
        ePadoca()

        if (itensComanda.isEmpty()) {
            println("Deseja mesmo finalizar a compra? (S/N)")
            finalizarCompra = readln().uppercase()
        } else {
            println("Deseja informa um Cupom de desconto? (S/N)")
            val temCupom = readln().uppercase()
            if(temCupom.equals("S")) aplicarCupomDesconto()
            println("=====================Comanda E-padoca========================")
            println("item.......Produto.............Qtd.......Valor...........Total")
            itensComanda.forEach { item -> // funcao
                println(item)
            }
            println("======================== Total  R$ ${(total*100).roundToInt()/100.0} =====================" +
                    "\n=============== Desconto de Cupons R$ ${(desconto*100).roundToInt()/100.0} ====================" +
                    "\n======================= VOLTE SEMPRE ^-^ ======================")
        }
    } while (finalizarCompra != "S")
}

fun ePadoca() {
    println("Bem Vindo à padaria E-Padoca!")
    do {
        println(categorias)

        val categoria = readln().toInt()

        when (categoria) {
            PAES -> selecionaProduto(paes)
            SALGADOS -> selecionaProduto(salgados)
            DOCES -> selecionaProduto(doces)
            BEBIDAS -> selecionaProduto(bebidas)
            else -> Unit
        }
    } while (categoria != FINALIZAR)
}

fun selecionaProduto(produtos: List<Pair<String, Double>>)
 {
    do {
        imprimirMenu(produtos) // Transformei aquelas várias variaveis iguais em uma função
        val produtoSelecionado = readln().toInt() // valor atual -> 1 (corresponde ao pao frances)

        for ((i, produto) in produtos.withIndex()) {
            if (i.inc() == produtoSelecionado) {
                selecionaQuantidadeDoProduto(produto)
                break
            }
        }
    } while (produtoSelecionado != 0)
}

fun selecionaQuantidadeDoProduto(produto: Pair<String, Double>) {
    println("Digite a quantidade:")
    val quantidade = readln().toInt()
    if (quantidade==0){
        return
    }
    val totalItem = quantidade * produto.second
    val item =
        itemComanda(produto = produto.first, quantidade = quantidade, valorUnitario = produto.second, total = totalItem)
    itensComanda.add(item)
    total += totalItem
}

fun itemComanda(
    produto: String,
    quantidade: Int,
    valorUnitario: Double,
    total: Double,
): String = "${itensComanda.size.inc()}$LINHA$produto$LINHA$quantidade$LINHA$valorUnitario${LINHA}R$${(total*100).roundToInt()/100.0}"

fun imprimirMenu(menu: List<Pair<String, Double>>){
    for(i in menu.indices){
        println("${i+1} - ${menu[i].first}$LINHA - R$ ${menu[i].second}")
    }
    println("0 - Voltar")
}

fun aplicarCupomDesconto(){
    println("Digite o seu cupom")
    val Cupom = readln().uppercase()
    when(Cupom){
        "5PADOCA" -> desconto =  0.05*total
        "10PADOCA"-> desconto = 0.10*total
        "5OFF"    -> if(total>5) desconto = 5.0
                    else {
                        println("Valor não ultrapassa o mínimo de R$5.")
                        cupomInvalido()
                    }
        else -> cupomInvalido()
    }
    total = total - desconto
}

fun cupomInvalido(){
    println("Cupom invalido. Deseja digitar novamente? (S/N)")
    val Cupom = readln().uppercase()
    if(Cupom.equals("S")) aplicarCupomDesconto()
}