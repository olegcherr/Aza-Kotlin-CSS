@file:Suppress("unused")

package azadev.kotlin.css

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


fun stylesheet(body: Stylesheet.()->Unit) = body

fun Stylesheet.url(str: String) = "url($str)"


private val symbols = DecimalFormatSymbols(Locale.ROOT).apply { decimalSeparator = '.' }
val cssDecimalFormat = DecimalFormat("#", symbols).apply { maximumFractionDigits = 5 }
