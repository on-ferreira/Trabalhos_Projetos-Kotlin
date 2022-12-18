open class Cliente(
    val nome: String,
    val sobrenome:String,
    val cpf: String,
    val senha: String,
    val plano: String
)

class ClienteNormal(
        nome: String,
        sobrenome:String,
        cpf: String,
        senha: String,
        plano: String) : Cliente(nome,sobrenome,cpf,senha,plano) {
    val carteira = CarteiraFisica(senha, "Fisica",0.0)
}

class ClienteDigital(
        nome: String,
        sobrenome:String,
        cpf: String,
        senha: String,
        plano: String) : Cliente(nome,sobrenome,cpf,senha,plano) {
    val carteira = CarteiraDigital(senha,"Digital",0.0)
}

class ClientePremium(
        nome: String,
        sobrenome:String,
        cpf: String,
        senha: String,
        plano: String) : Cliente(nome,sobrenome,cpf,senha,plano){
    val carteiraFisica = CarteiraFisica(senha,"Fisica",0.0)
    val carteiraDigital = CarteiraDigital(senha,"Digital",0.0)
}

abstract class Carteira(
        private val senha: String,
        val tipo: String,
        var saldo: Double,
        val extrato: MutableList<String> = mutableListOf()
){
    fun pagarBoleto(valorBoleto:Double, codBarras:String, senhaDigitada: String) {
        if(this.verificarSenha(senhaDigitada)){
            if(valorBoleto>=saldo) {
                saldo -= valorBoleto
                extrato.add("Boleto $codBarras pago no valor de R$ $valorBoleto")
                // Implementar um função que faça o pagamento do boleto pelo cod barras
            } else TODO() // ValorInsuficiente Exception
        } else TODO() // Senha errada exception
    }

    fun verificarSenha(senhaDigitada:String):Boolean{
        return senhaDigitada == senha
    }
}

class CarteiraFisica(
        senha:String,
        tipo:String,
        saldo:Double,
        extrato:MutableList<String> = mutableListOf()
    ): Carteira(senha,tipo,saldo,extrato){
    fun deposito(valorDeposito:Double){
        if(valorDeposito>0){
            saldo += valorDeposito
            extrato.add("Depósito no valor $valorDeposito")
        } else TODO()
    }

    fun saque(valorSaque:Double, senhaDigitada: String){
        if(this.verificarSenha(senhaDigitada)) {
            if (valorSaque <= saldo){
                saldo -= valorSaque
                extrato.add("Saque no valor de $valorSaque")
            } else TODO(return println("Valor Insuficiente"))
        }else TODO(return println("Senha errada")) //Senha errada exception
    }
}

class CarteiraDigital(
        senha:String,
        tipo:String,
        saldo:Double,
        extrato:MutableList<String> = mutableListOf()
    ):Carteira(senha,tipo,saldo,extrato){
    fun transferenciaPIX(valorTransferencia:Double, chavePix:String,senhaDigitada:String){
        if(this.verificarSenha(senhaDigitada)) {
            if (valorTransferencia <= saldo){
                saldo -= valorTransferencia
                extrato.add("Pix no valor $valorTransferencia enviado para $chavePix")
                // Ideia para o futuro, implementar uma função que envie o valor para outra carteira digital a partir da chavePix
            } else TODO()
        } else TODO() // Senha errada exception
    }

    fun investir(valorInvestimento:Double, senhaDigitada: String){
        if(this.verificarSenha(senhaDigitada)) {
            if (valorInvestimento <= saldo){
                saldo -= valorInvestimento
                extrato.add("$valorInvestimento foram investidos")
                // Adicionar uma funcionalidade que controle a passagem de tempo e faça o juros ser adicionado no saldo
            } else TODO()
        }else TODO() // Senha errada exception
    }

    fun guardar(valorGuardar:Double){
        //Assumo que isso seja semelhante o deposito em uma conta fisica
        if(valorGuardar>0){
            saldo += valorGuardar
            extrato.add("$valorGuardar foram guardados!")
        } else TODO()
    }

}

// Fazer exception de deposito negativo
// Fazer exception de valor no saldo insuficiente para a operação

fun main(){
    val joseFisica = ClienteNormal("Jose","Alvarez","12345678910","123456#","Normal")
    println(joseFisica.carteira.saldo)
    joseFisica.carteira.deposito(10.0)
    println(joseFisica.carteira.saldo)
    joseFisica.carteira.saque(5.00,"123456#")
    println(joseFisica.carteira.saldo)
    joseFisica.carteira.saque(15.00,"123456#")
    println(joseFisica.carteira.saldo)
    joseFisica.carteira.saque(5.00,"123456")
    println(joseFisica.carteira.saldo)
    println(joseFisica.carteira.extrato.joinToString(separator = "\n"))

}
