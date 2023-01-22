import java.io.File

fun readCsv(fileName: String): List<List<String>> {
    val reader = File(fileName).bufferedReader()
    val header = reader.readLine() // caso queira evitar o header descomentar
    return reader.lineSequence()
        .filter { it.isNotBlank() }
        .map {
            it.split(',', ignoreCase = false)
        }.toList()
}

data class operacao(
    val Ag : Int,
    val account : Int,
    val banco : String,
    val name: String,
    val typeOP : String,
    val time : String,
    val value: Double
    ){
    override fun toString(): String {
        return "Ag: $Ag | Conta: $account | Banco: $banco | Nome: $name | Tipo: $typeOP | Tempo : $time | Valor: R$ $value \n"
    }
}

fun toOperation(lista : List<String>): operacao{
    return operacao(
        lista[0].toInt(),
        lista[1].toInt(),
        lista[2],
        lista[3],
        lista[4],
        lista[5],
        lista[6].toDouble()
    )
}

fun operacaoDuplicada(op: operacao, lista: MutableList<operacao>) : Boolean{
    lista.forEach {
        if (it == op) return true
    }
    return false
}

class Account(val Ag: Int, val account: Int, val banco:String,val name:String, var saldo: Double){
    val Log = mutableListOf<operacao>()

    fun update(op : operacao){
        if(!operacaoDuplicada(op, Log)) {
            when (op.typeOP) {
                "SAQUE" -> {
                            saldo -= op.value
                            updateLog(op)
                            }

                "DEPOSITO" -> {
                                saldo += op.value
                                updateLog(op)
                              }
                else -> return
            }
        }
        else{
            println("Operação duplicada")
        }
    }

    fun updateLog(op : operacao){
        Log.add(op)
    }


    override fun toString(): String {
        return "Ag: $Ag | Conta: $account | Banco: $banco | Nome: $name | Saldo: $saldo \n Log \n" + Log.toString()
    }
}

fun List<Account>.contains2(op : operacao):Int{
    this.forEach{
        if (it.Ag == op.Ag && it.account == op.account && it.banco == op.banco)
            return indexOf(it)
    }
    return -1
}


fun main() {
    val accountList = mutableListOf<Account>()
    val csv = readCsv("src/main/kotlin/operacoes.csv")
    val opList = mutableListOf<operacao>()
    csv.forEach{opList.add(toOperation(it))}
    opList.forEach {
        val index = accountList.contains2(it)
        if( index == -1){
            accountList.add(Account(it.Ag,it.account,it.banco,it.name,0.0))
            accountList[accountList.contains2(it)].update(it)
            println("Conta criada ${it.name}")
        }
        else{
            accountList[index].update(it)
            println("Conta atualizada ${it.name}")

        }
    }
    println(accountList.toString())


}
