val zero = "0"
val categoriasMenu = """
    Digite a opção desejada no Menu:
    1.................Adicionar Item
    2....................Editar item
    3........Exibir itens em estoque
    4..........Exibir todos os itens
    0.................Fechar Sistema
""".trimIndent()


class Produtos(val ID:Int, var name:String, var qtd:Int){

    fun editar(newname:String, newqtd:Int){
        name = newname
        qtd = newqtd
    }

    fun calcularZeros():Int {
        when (ID) {
            in 1..9 -> return 3
            in 10..99 -> return 2
            in 100..999 -> return 1
            else -> return 0
        }
    }

    override fun toString(): String = "#${zero.repeat(calcularZeros())}$ID | $name | $qtd"
}

class Estoque(){
    val estoque: MutableList<Produtos> = mutableListOf()
    var quantidade: Int = 0

    fun adicionarProduto() {
        println("Digite o nome do produto")
        val nome = readln()
        println("Digite a quantidade")
        val qtd = readln().toIntOrNull() ?: 0
        try {
            when(qtd){
                in Int.MIN_VALUE..0 -> throw QuantidadeNegativaException()
                in 999..Int.MAX_VALUE ->  throw LimiteEstoqueMaxExcpetion()
            }
        }catch (e: QuantidadeNegativaException){
            println("Erro ao adicionar produto")
            println(e.localizedMessage)

        }catch (e: LimiteEstoqueMaxExcpetion) {
            println("Erro ao adicionar produto")
            println(e.localizedMessage)
            return
        }
        quantidade += 1
        estoque.add(Produtos(ID=quantidade,name=nome,qtd=qtd))
    }

    fun listarTodos(){
        if(quantidade==0){
            println("Nenhum Produto cadastrado no Sistema")
        }
        else {
            println(" ID   |  Peça  | Quantidade")
            estoque.forEach {
                println(it.toString())
            }
        }
    }

    fun listarValidos(){
        println(" ID |  Peça | Quantidade")
        estoque.forEach{
            if (it.qtd > 0) {
                println(it.toString())
            }
        }
    }

    fun validarID(id:Int):Boolean{
        if (id > quantidade) return false
        else return true
    }

    fun editar(){
        println("Digite o ID do item que deseja editar [ou 0 para sair]")
        var id = readln().toInt()

        while(validarID(id)==false){
            println("Digite o ID do item que deseja editar [ou 0 para sair]")
            id = readln().toInt()
        }
        if(id == 0){
            println("Edição cancelada!")
            return
        }
        println("""
            1 - Editar apenas o nome
            2 - Editar apenas a quantidade
            3 - Editar ambos
            0 - Cancelar edição
        """.trimIndent())
        val editarCategoria = readln().toInt()
        when(editarCategoria){
            1 -> {
                println("Digite o novo nome")
                val nome = readln()
                estoque[id-1].editar(newname = nome, newqtd = estoque[id-1].qtd)
            }
            2 -> {
                println("Digite a nova quantidade")
                val qtd = readln().toIntOrNull() ?:0
                try {
                    when(qtd){
                        in Int.MIN_VALUE..0 -> throw QuantidadeNegativaException()
                        in 999..Int.MAX_VALUE ->  throw LimiteEstoqueMaxExcpetion()
                    }
                }catch (e: QuantidadeNegativaException){
                    println("Erro ao editar produto")
                    println(e.localizedMessage)

                }catch (e: LimiteEstoqueMaxExcpetion) {
                    println("Erro ao editar produto")
                    println(e.localizedMessage)
                    return
                }
                estoque[id-1].editar(newname = estoque[id-1].name, newqtd = qtd)
            }
            3 -> {
                println("Digite o novo nome")
                val nome = readln()
                println("Digite a nova quantidade")
                val qtd = readln().toIntOrNull() ?:0
                try {
                    when(qtd){
                        in Int.MIN_VALUE..0 -> throw QuantidadeNegativaException()
                        in 999..Int.MAX_VALUE ->  throw LimiteEstoqueMaxExcpetion()
                    }
                }catch (e: QuantidadeNegativaException){
                    println("Erro ao editar produto")
                    println(e.localizedMessage)

                }catch (e: LimiteEstoqueMaxExcpetion) {
                    println("Erro ao editar produto")
                    println(e.localizedMessage)
                    return
                }
                estoque[id-1].editar(nome,qtd)
            }
            0 -> println("Edição cancelada")
        }
    }
}


fun main(){
    ControledeEstoque()
}

fun ControledeEstoque(){
    val Estoque = Estoque()
    println("Bem Vindo ao sistema de controle de estoque!")
    do {
        println(categoriasMenu)

        val categoria = readln().toInt()

        when (categoria) {
            1 -> Estoque.adicionarProduto()
            2 -> Estoque.editar()
            3 -> Estoque.listarValidos()
            4 -> Estoque.listarTodos()
            else -> Unit
        }
    } while (categoria != 0)


}

class LimiteEstoqueMaxExcpetion: Exception() {
    override fun getLocalizedMessage(): String {
        return "Erro: Quantidade adicionada excede o limite de 999"
    }
}

class QuantidadeNegativaException: Exception() {
    override fun getLocalizedMessage(): String {
        return "Erro: Quantidade digitada é negativa"
    }
}