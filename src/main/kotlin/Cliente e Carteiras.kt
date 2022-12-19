data class SaldoObj(var s : Double = 0.0)

open class Cliente(
    val nome: String,
    val sobrenome:String,
    val cpf: String,
    private val senha: String,
    val plano: String
)

class ClienteNormal(
    nome: String,
    sobrenome:String,
    cpf: String,
    senha: String,
    plano: String) : Cliente(nome,sobrenome,cpf,senha,plano) {
    private var saldo = SaldoObj()
    val carteira = CarteiraFisica(senha, "Fisica",saldo)
}

class ClienteDigital(
    nome: String,
    sobrenome:String,
    cpf: String,
    senha: String,
    plano: String) : Cliente(nome,sobrenome,cpf,senha,plano) {
    private var saldo = SaldoObj()
    val carteira = CarteiraDigital(senha,"Digital",saldo)
}

class ClientePremium(
    nome: String,
    sobrenome:String,
    cpf: String,
    senha: String,
    plano: String) : Cliente(nome,sobrenome,cpf,senha,plano){
    private var saldo = SaldoObj()
    val carteiraFisica = CarteiraFisica(senha,"Fisica",saldo)
    val carteiraDigital = CarteiraDigital(senha,"Digital",saldo)
}

abstract class Carteira(
    private val senha: String,
    val tipo: String,
    protected var saldo: SaldoObj,
    val extrato: MutableList<String> = mutableListOf()
){
    fun pagarBoleto(valorBoleto:Double, codBarras:String, senhaDigitada: String) {
        try {
            if (this.verificarSenha(senhaDigitada)) {
                if (valorBoleto <= saldo.s) {
                    saldo.s -= valorBoleto
                    extrato.add("Boleto $codBarras pago no valor de R$ $valorBoleto - Conta $tipo")
                    // Implementar um função que faça o pagamento do boleto pelo cod barras
                } else throw SaldoInsuficienteException()
            } else throw SenhaIncorretaException()
        }catch (e: SaldoInsuficienteException){
            println(e.localizedMessage)
        }catch(e: SenhaIncorretaException){
            println(e.localizedMessage)
        }
    }

    fun consultarSaldo(senhaDigitada: String):Double?{
        try{
            if(this.verificarSenha(senhaDigitada))
                return saldo.s
            else throw SenhaIncorretaException()
        }catch (e: SenhaIncorretaException){
            println(e.localizedMessage)
        }
        return null
    }

    fun verificarSenha(senhaDigitada:String):Boolean{
        return senhaDigitada == senha
    }
}

class CarteiraFisica(
    senha:String,
    tipo:String,
    saldo:SaldoObj,
    extrato:MutableList<String> = mutableListOf()
): Carteira(senha,tipo,saldo,extrato){

    fun deposito(valorDeposito:Double){
        try {
            if (valorDeposito > 0) {
                saldo.s += valorDeposito
                extrato.add("Depósito no valor $valorDeposito - Conta $tipo")
            } else throw DepositoNegativoException()
        } catch(e: DepositoNegativoException){
            println(e.localizedMessage)
        }
    }

    fun saque(valorSaque:Double, senhaDigitada: String){
        try{
            if(this.verificarSenha(senhaDigitada)) {
                if (valorSaque <= saldo.s){
                    saldo.s -= valorSaque
                    extrato.add("Saque no valor de $valorSaque - Conta $tipo")
                } else throw SaldoInsuficienteException()
            }else throw SenhaIncorretaException()
        } catch (e: SaldoInsuficienteException){
            println(e.localizedMessage)
        }catch (e: SenhaIncorretaException){
            println(e.localizedMessage)
        }
    }
}

class CarteiraDigital(
    senha:String,
    tipo:String,
    saldo:SaldoObj,
    extrato:MutableList<String> = mutableListOf()
    ):Carteira(senha,tipo,saldo,extrato){

    fun transferenciaPIX(valorTransferencia:Double, chavePix:String,senhaDigitada:String){
        try{
            if(this.verificarSenha(senhaDigitada)) {
                if (valorTransferencia <= saldo.s){
                    saldo.s -= valorTransferencia
                    extrato.add("Pix no valor $valorTransferencia enviado para $chavePix - Conta $tipo")
                    // Ideia para o futuro, implementar uma função que envie o valor para outra carteira digital a partir da chavePix
                } else throw SaldoInsuficienteException()
            } else throw SenhaIncorretaException()
        } catch (e: SaldoInsuficienteException){
            println(e.localizedMessage)
        } catch (e: SenhaIncorretaException){
            println(e.localizedMessage)
        }
    }

    fun investir(valorInvestimento:Double, senhaDigitada: String){
        try{
            if(this.verificarSenha(senhaDigitada)) {
                if (valorInvestimento <= saldo.s){
                    saldo.s -= valorInvestimento
                    extrato.add("$valorInvestimento foram investidos - Conta $tipo")
                    // Adicionar uma funcionalidade que controle a passagem de tempo e faça o juros ser adicionado no saldo
                } else throw SaldoInsuficienteException()
            }else throw SenhaIncorretaException()
        }catch (e: SaldoInsuficienteException){
            println(e.localizedMessage)
        }catch (e: SenhaIncorretaException){
            println(e.localizedMessage)
        }
    }

    fun guardar(valorGuardar:Double){
        //Assumo que isso seja semelhante o deposito em uma conta fisica
        try{
            if(valorGuardar>0){
                saldo.s += valorGuardar
                extrato.add("$valorGuardar foram guardados! - Conta $tipo")
            } else throw DepositoNegativoException()
        }catch(e: DepositoNegativoException){
            println(e.localizedMessage)
        }
    }

}

fun main(){
    val josePremium = ClientePremium("Jose","Alvarez",
        "12345678910","123456#","Premium")
    println(josePremium.carteiraFisica.consultarSaldo("123456#"))
    josePremium.carteiraFisica.deposito(10.0)
    println(josePremium.carteiraFisica.consultarSaldo("123456#"))
    josePremium.carteiraFisica.saque(5.00,"123456#")
    println(josePremium.carteiraFisica.consultarSaldo("123456#"))
    josePremium.carteiraFisica.saque(15.00,"123456#")
    println(josePremium.carteiraFisica.consultarSaldo("123456#"))
    josePremium.carteiraFisica.saque(5.00,"123456")
    println(josePremium.carteiraFisica.consultarSaldo("123456#"))
    josePremium.carteiraDigital.guardar(100.0)
    println("Saldo Fisica: "+ josePremium.carteiraFisica.consultarSaldo("123456#")
            + " Saldo Digital " + josePremium.carteiraDigital.consultarSaldo("123456#"))
    println(josePremium.carteiraDigital.pagarBoleto(50.5,"ContaLuz","123456#"))
    println(josePremium.carteiraFisica.consultarSaldo("123456#"))
    println(josePremium.carteiraFisica.extrato.joinToString(separator = "\n"))
    println(josePremium.carteiraDigital.extrato.joinToString(separator = "\n"))


}

class DepositoNegativoException: Exception() {
    override fun getLocalizedMessage(): String {
        return "Erro: Tentativa de depositar um valor negativo na conta."
    }
}

class SaldoInsuficienteException: Exception() {
    override fun getLocalizedMessage(): String {
        return "Erro: Saldo Insuficiente para essa operação."
    }
}

class SenhaIncorretaException: Exception() {
    override fun getLocalizedMessage(): String {
        return "Erro: Senha incorreta. Acesso Negado."
    }
}
