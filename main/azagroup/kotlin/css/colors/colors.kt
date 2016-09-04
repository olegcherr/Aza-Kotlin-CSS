@file:Suppress("unused")

package azagroup.kotlin.css.colors


fun clr(hex: String) = Color.fromHex(hex)!!
fun clr(hex: Int) = Color.fromHex(hex)!!
fun clr(r: Int, g: Int, b: Int, a: Int = 255) = Color.fromRgb(r, g, b, a)
