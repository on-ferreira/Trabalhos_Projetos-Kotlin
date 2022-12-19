import DepositoNegativoException as DepositoNegativoException

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
    protected var saldo: Double,
    val extrato: MutableList<String> = mutableListOf()
){
    fun pagarBoleto(valorBoleto:Double, codBarras:String, senhaDigitada: String) {
        try {
            if (this.verificarSenha(senhaDigitada)) {
                if (valorBoleto >= saldo) {
                    saldo -= valorBoleto
                    extrato.add("Boleto $codBarras pago no valor de R$ $valorBoleto")
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
                return saldo
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
    saldo:Double,
    extrato:MutableList<String> = mutableListOf()
): Carteira(senha,tipo,saldo,extrato){

    fun deposito(valorDeposito:Double){
        try {
            if (valorDeposito > 0) {
                saldo += valorDeposito
                extrato.add("Depósito no valor $valorDeposito")
            } else throw DepositoNegativoException()
        } catch(e: DepositoNegativoException){
            println(e.localizedMessage)
        }
    }

    fun saque(valorSaque:Double, senhaDigitada: String){
        try{
            if(this.verificarSenha(senhaDigitada)) {
                if (valorSaque <= saldo){
                    saldo -= valorSaque
                    extrato.add("Saque no valor de $valorSaque")
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
    saldo:Double,
    extrato:MutableList<String> = mutableListOf()
    ):Carteira(senha,tipo,saldo,extrato){

    fun transferenciaPIX(valorTransferencia:Double, chavePix:String,senhaDigitada:String){
        try{
            if(this.verificarSenha(senhaDigitada)) {
                if (valorTransferencia <= saldo){
                    saldo -= valorTransferencia
                    extrato.add("Pix no valor $valorTransferencia enviado para $chavePix")
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
                if (valorInvestimento <= saldo){
                    saldo -= valorInvestimento
                    extrato.add("$valorInvestimento foram investidos")
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
                saldo += valorGuardar
                extrato.add("$valorGuardar foram guardados!")
            } else throw DepositoNegativoException()
        }catch(e: DepositoNegativoException){
            println(e.localizedMessage)
        }
    }

}

fun main(){
    val joseFisica = ClientePremium("Jose","Alvarez",
        "12345678910","123456#","Premium")
    println(joseFisica.carteiraFisica.consultarSaldo("123456#"))
    joseFisica.carteiraFisica.deposito(10.0)
    println(joseFisica.carteiraFisica.consultarSaldo("123456#"))
    joseFisica.carteiraFisica.saque(5.00,"123456#")
    println(joseFisica.carteiraFisica.consultarSaldo("123456#"))
    joseFisica.carteiraFisica.saque(15.00,"123456#")
    println(joseFisica.carteiraFisica.consultarSaldo("123456#"))
    joseFisica.carteiraFisica.saque(5.00,"123456")
    println(joseFisica.carteiraFisica.consultarSaldo("123456#"))
    println(joseFisica.carteiraFisica.extrato.joinToString(separator = "\n"))

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
