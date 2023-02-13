fun String.type(patterns: List<Regex>, patternNames: Map<Regex, String>) : String {
    for (pattern in patterns) {
        if (this.matches(pattern))
            return patternNames[pattern] ?: "Padrão sem nome."
    }
    return "Nenhum  padrão encontrado."
}

fun main(){
    val patternCpf = Regex("[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}")
    val patternCnpj = Regex("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")
    val patternCpfOrCnpj = Regex("[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2} | \\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")
    val patternMoedas = Regex("[RU][$][\\s0-9]+[.,][0-9]{2}")
    val patternEmail = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|com.br)$")

    val patternList = listOf(patternCpf,patternCnpj,patternCpfOrCnpj,patternMoedas,patternEmail)

    val patternNames = mapOf(
        patternCpf to "CPF",
        patternCnpj to "CNPJ",
        patternCpfOrCnpj to "CPF ou CNPJ",
        patternMoedas to "Moedas",
        patternEmail to "E-mail"
    )

//    val emailTest = "abc123@provedor.com"
//    val cpfTest = "123.456.789-00"
//    val cnpjTest = "12.345.678/0001-00"
//    val noPatternTest = "arroz"
//    val moedaTest = "U$ 123.58"
//    val listaTests = listOf(emailTest,cpfTest,cnpjTest,noPatternTest,moedaTest)
//
//    listaTests.forEach{
//        println("\"$it\" pertence ao padrão ${it.type(patternList, patternNames)}")
//    }

    var input:String = ""
    while(!input.equals("Sair")){
        println("Digite alguma coisa para ver se corresponde a algum padrão: ")
        input = readln()
        println(input.type(patternList, patternNames))
    }

}
