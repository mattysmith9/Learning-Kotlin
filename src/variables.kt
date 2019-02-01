fun main(args: Array<String>) {

    immutableValue()
    mutableValue()
    nullableValue()
}

private fun immutableValue() {
    val name = "Matty"
    println(name)
}

private fun mutableValue() {
    /* mutable prototype */
    val lastName: String
    lastName = "Matty Smith"
    println(lastName)
}

private fun nullableValue() {
    /*
    var lastNameNullable: String   Error!
    lastNameNullable = null        Error!
    */

    var lastNameNullable: String?
    lastNameNullable = null
    println(message = lastNameNullable)
    lastNameNullable = "Matty Clayton Smith"
    println(lastNameNullable)
}


