package ir.amirrezaanari.barandehplanning.planning.components

fun String.toPersianDigits(): String {
    val englishDigits = '0'..'9'
    val persianDigits = listOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    return this.map { char ->
        if (char in englishDigits) {
            persianDigits[char - '0']
        } else {
            char
        }
    }.joinToString("")
}