@file:Suppress("unused")

package azagroup.kotlin.css

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


fun stylesheet(body: Stylesheet.()->Unit) = body

fun Stylesheet.url(str: String) = "url($str)"


private val symbols = DecimalFormatSymbols(Locale.ROOT).apply { decimalSeparator = '.' }
val cssDecimalFormat = DecimalFormat("#", symbols).apply { maximumFractionDigits = 5 }
