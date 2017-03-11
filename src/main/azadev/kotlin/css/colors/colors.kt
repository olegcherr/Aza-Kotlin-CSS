@file:Suppress("unused")

package azadev.kotlin.css.colors


fun hex(hex: String) = Color.fromHex(hex)!!
fun hex(hex: Int) = Color.fromHex(hex)!!
fun rgb(r: Int, g: Int, b: Int) = Color.fromRgb(r, g, b)
fun rgba(r: Int, g: Int, b: Int, a: Number = 1f) = Color.fromRgb(r, g, b, a.toFloat())
