package azagroup.kotlin.css

import kotlin.reflect.KProperty


class ContentPropertyHandler(
		val name: String
) {
	@Suppress("USELESS_CAST")
	operator fun getValue(stylesheet: Stylesheet, property: KProperty<*>)
			= stylesheet.getProperty(name) as Any?

	operator fun setValue(stylesheet: Stylesheet, property: KProperty<*>, value: Any?) {
		stylesheet.setProperty(name, "\"${value.toString().replace("\"", "\\\"")}\"")
	}
}
