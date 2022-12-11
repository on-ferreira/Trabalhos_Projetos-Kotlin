import kotlin.random.Random

val menuAquarioOpcoes = """
    Digite a opção desejada no Menu:
    1................Adicionar Peixe
    2..................Listar Peixes
    3...............Alimentar Peixes
    4.................Limpar Aquário
    5.......Fazer Upgrade de Aquário
    0.................Fechar Sistema
""".trimIndent()

val menuLojaOpcoes = """
    Digite a opção desejada no Menu:
    1..............Adicionar Aquario
    2................Listar Aquarios
    3.......Definir valor de Limpeza
    4..............Gerenciar Aquario
    0.................Fechar Sistema
""".trimIndent()

class Peixe(val nome:String,val cor:String, val tamanho:String){

    fun statusPeixe():String {
        val status: Int = Random.nextInt(1, 4)
        return when (status) {
            1 -> "nada tranquilamente pelo aquário"
            2 -> "se esconde entre as algas"
            3 -> "está te encarando"
            4 -> "dorme no canto do aquário"
            else -> "está se esfregando nas algas"
        }
    }

    override fun toString(): String = "$nome de cor $cor e tamanho $tamanho ${statusPeixe()}."
}

class Aquario(
        val index:Int,
        var tamanho: Char,
        var capacidade:Int,
        var qtdPeixes: Int,
        var estaSujo: Boolean,
        var numSujeira: Int
){

    init{
        when(tamanho){
            'P' -> capacidade = 5
            'M' -> capacidade = 10
            'G' -> capacidade = 15
            'X' -> capacidade = 20
        }
    }

    val listaPeixe: MutableList<Peixe> = mutableListOf()

    fun adicionarPeixe(){
        if(estaSujo){
            println("O aquário está sujo. Limpe-o antes de adicionar novos peixes.")
        }else{
            println("Digite o nome do peixe:")
            val nome = readln()
            println("Digite a cor do peixe:")
            val cor = readln()
            println("Digite o tamanho do peixe:")
            val tamanho = readln()
            listaPeixe.add(Peixe(nome,cor,tamanho))
            aumentarQuantidade()
        }
    }

    fun aumentarQuantidade(){
        qtdPeixes += 1
        if(qtdPeixes%numSujeira == 0) estaSujo = true
    }

    fun listarPeixes(){
        if(qtdPeixes == 0)
            println("Sem peixes no aquário!")
        else {
            for (i in listaPeixe)
                println(i.toString())
        }
    }

    fun alimentarPeixes(){
        val escolha = Random.nextInt(1, 101)
        when(qtdPeixes){
            0 -> println("Não existem peixes para serem alimentados")
            1 -> when(escolha%2){
                0 -> println("Falha ao alimentar ${listaPeixe[0].nome}")
                1 -> println("${listaPeixe[0].nome} foi alimentado com sucesso")
            }
            else -> when(escolha){
                1 -> println("Você tropeçou e derrubou o saco de comida.")
                in 2..30 -> println("Falha ao alimentar peixes.")
                in 31..60 -> println("${Random.nextInt(1,qtdPeixes)} peixes foram alimentados.")
                in 61..100 -> println("Todos peixes foram alimentados")
            }
        }
    }

    fun limparAquario(){
        if(estaSujo){
            println("O aquário foi limpo!")
            estaSujo = false
        } else{
            println("O aquário não precisa ser limpo")
        }
    }

    fun fazerUpgrade(){
        when(tamanho){
            'P' -> {
                tamanho = 'M'
                capacidade = 10
            }
            'M' -> {
                tamanho = 'G'
                capacidade = 15
            }
            'G' -> {
                tamanho = 'X'
                capacidade = 20
            }
            'X' -> {
                println("Seu aquário já é do maior tipo possível!")
            }
        }
    }

}

class Loja(var constantLimpeza : Int,var qtdAquarios:Int){
    val listaAquarios: MutableList<Aquario> = mutableListOf()

    fun adicionarAquario(){
        var tamanhoAquario:Char
        do{
            println("Qual o tamanho do seu aquário? [P,M,G,X]")
            tamanhoAquario = readln().uppercase().first()
        }while(!validarTamanho(tamanhoAquario))
        qtdAquarios += 1
        listaAquarios.add(Aquario(qtdAquarios,tamanhoAquario,0,0,false,constantLimpeza))
    }

    fun listarTodos(){
        if (qtdAquarios == 0)
            println("Sem aquários para listar. Adicione um aquário ao sistema primeiro!")
        else {
            var indexImpressão = 1
            for (i in listaAquarios) {
                println("Aquário #$indexImpressão")
                indexImpressão += 1
                i.listarPeixes()
            }
        }
    }

    fun gerenciarAquarios(){
        if (qtdAquarios == 0)
            println("Sem aquários para gerenciar. Adicione um aquário ao sistema primeiro!")
        else{
            var index:Int
            do {
                println("Qual aquário deseja gerenciar? Digite um valor entre 1-$qtdAquarios")
                index = readln().toInt()
            }while(!(index in 1..qtdAquarios))
            menuAquario(listaAquarios[index-1])
        }
    }
}

fun main(){
    menuLoja()
}

fun validarTamanho(tamanho: Char):Boolean{
    when(tamanho){
        'P','M','G','X' -> return true
        else -> return false
    }
}

fun menuAquario(aquarioAtual:Aquario){
    println("Bem Vindo ao sistema de controle de Aquários!")
    do {
        println(menuAquarioOpcoes)
        val escolha = readln().toIntOrNull()
        when (escolha) {
            1 -> aquarioAtual.adicionarPeixe()
            2 -> aquarioAtual.listarPeixes()
            3 -> aquarioAtual.alimentarPeixes()
            4 -> aquarioAtual.limparAquario()
            5 -> aquarioAtual.fazerUpgrade()
            else -> Unit
        }
    }while (escolha != 0)

}

fun menuLoja(){
    val loja = Loja(3, 0)
    println("Bem Vindo ao sistema de controle de Aquários!")
    do {
        println(menuLojaOpcoes)
        val escolha = readln().toIntOrNull()
        when (escolha) {
            1 -> loja.adicionarAquario()
            2 -> loja.listarTodos()
            3 -> {
                println("Digite o novo valor de limpeza:")
                loja.constantLimpeza = readln().toInt()
            }
            4 -> loja.gerenciarAquarios()
            else -> Unit
            }
    }while (escolha!=0)
}
