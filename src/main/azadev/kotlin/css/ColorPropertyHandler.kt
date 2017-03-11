package azadev.kotlin.css

import azadev.kotlin.css.colors.hex
import kotlin.reflect.KProperty


class ColorPropertyHandler(
		val name: String
) {
	@Suppress("USELESS_CAST")
	operator fun getValue(stylesheet: Stylesheet, property: KProperty<*>)
			= stylesheet.getProperty(name) as Any?

	operator fun setValue(stylesheet: Stylesheet, property: KProperty<*>, value: Any?) {
		stylesheet.setProperty(name, when (value) {
			is Int -> hex(value)
			else -> value
		})
	}
}
