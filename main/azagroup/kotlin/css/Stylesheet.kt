package azagroup.kotlin.css

import java.util.ArrayList


@Suppress("unused")
class Stylesheet(
		callback: (Stylesheet.()->Unit)? = null
) : ASelector
{
	var selector: Selector? = null
	var atRule: String? = null

	val properties = ArrayList<Property>(2)

	val children = ArrayList<Stylesheet>()


	init { callback?.invoke(this) }


	operator fun String.invoke(body: Stylesheet.()->Unit) = stringToSelector().invoke(body)
	operator fun String.div(obj: ASelector) = stringToSelector().div(obj)
	operator fun String.mod(obj: ASelector) = stringToSelector().mod(obj)
	operator fun String.minus(obj: ASelector) = stringToSelector().minus(obj)

	private fun String.stringToSelector() = when (this[0]) {
		'#' -> id(this.drop(1))
		'@' -> at(this.drop(1)).selector!!
		'.' -> c(this.drop(1))
		else -> c(this)
	}


	fun include(stylesheet: Stylesheet) {
		children.add(stylesheet)
	}

	override fun custom(selector: String, _spaceBefore: Boolean, _spaceAfter: Boolean, body: (Stylesheet.() -> Unit)?): Selector {
		val stylesheet = Stylesheet()
		val sel = Selector(stylesheet)
		sel.append(selector.toString(), _spaceBefore, _spaceAfter)

		stylesheet.selector = sel
		include(stylesheet)

		body?.invoke(stylesheet)

		return sel
	}


	fun getProperty(name: String) = properties.find { it.name == name }

	fun setProperty(name: String, value: Any?) {
		properties.add(Property(name, value))
	}


	fun moveDataTo(stylesheet: Stylesheet) {
		stylesheet.properties.addAll(properties)
		properties.clear()

		stylesheet.children.addAll(children)
		children.clear()
	}


	fun render() = buildString { render(this) }
	fun renderTo(sb: StringBuilder) = render(sb)

	private fun render(sb: StringBuilder, selectorPrefix: CharSequence = "", _spaceBefore: Boolean = true) {
		val selector = selector
		val atRule = atRule


		if (atRule != null)
			sb.append(atRule).append('{')


		if (properties.isNotEmpty()) {
			if (selector != null)
				sb.append(selector.toString(selectorPrefix, _spaceBefore)).append('{')

			val lastIdx = properties.lastIndex
			properties.forEachIndexed { i, property ->
				val value = property.value
				if (value != null)
					sb.run {
						append(property.name)
						append(":")
						append(if (value is Number) cssDecimalFormat.format(value.toFloat()) else value)

						if (i < lastIdx)
							append(";")
					}
				else if (i == lastIdx && sb.last() == ';')
					sb.setLength(sb.length-1)
			}

			if (selector != null)
				sb.append("}")
		}


		for (child in children) {
			if (selector != null)
				for (row in selector.rows)
					child.render(sb, row.toString(selectorPrefix, _spaceBefore), row.spaceAfter)
			else
				child.render(sb, selectorPrefix)
		}


		if (atRule != null)
			sb.append('}')
	}


	override fun toString() = "Stylesheet(sel:$selector; props:${properties.size}; childs:${children.size})"


	class Property(
			val name: String,
			val value: Any?
	)
}
