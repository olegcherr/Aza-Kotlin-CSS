package azagroup.kotlin.css

import java.util.ArrayList


class Selector(
		var stylesheet: Stylesheet
) : ASelector
{
	var rows = ArrayList<Row>(1)


	operator fun invoke(body: Stylesheet.()->Unit): Stylesheet {
		stylesheet.body()
		return stylesheet
	}

	override fun custom(selector: String, _spaceBefore: Boolean, _spaceAfter: Boolean, body: (Stylesheet.() -> Unit)?): Selector {
		append(selector.toString(), _spaceBefore, _spaceAfter)
		body?.invoke(stylesheet)
		return this
	}


	fun append(str: CharSequence, _spaceBefore: Boolean, _spaceAfter: Boolean): Selector {
		if (rows.isEmpty())
			rows.add(Row(str, _spaceBefore, _spaceAfter))

		else for (row in rows)
			row.append(str, _spaceBefore, _spaceAfter)

		return this
	}

	fun append(selector: Selector): Selector {
		val newRows = ArrayList<Row>(rows.size * selector.rows.size)
		for (r1 in rows)
			for (r2 in selector.rows) {
				val r = Row(r1.sb, r1.spaceBefore, r1.spaceAfter)
				r.append(r2.sb, r2.spaceBefore, r2.spaceAfter)
				newRows.add(r)
			}

		rows = newRows
		return this
	}


	infix fun and(obj: ASelector): Selector {
		when (obj) {
			is Selector -> {
				for (row in obj.rows)
					rows.add(row)
				return this
			}
			is Stylesheet -> {
				val selector = obj.selector!!
				selector.rows.addAll(0, rows)
				return selector
			}
			else -> throw RuntimeException("Unknown kind of corresponding object: ${obj.javaClass.simpleName}")
		}
	}


	operator infix fun div(obj: ASelector): Selector {
		child
		return mergeWith(obj)
	}
	operator infix fun mod(obj: ASelector): Selector {
		next
		return mergeWith(obj)
	}
	operator infix fun minus(obj: ASelector): Selector {
		nextAll
		return mergeWith(obj)
	}

	private fun mergeWith(obj: ASelector): Selector {
		when (obj) {
			is Selector -> append(obj)
			is Stylesheet -> {
				append(obj.selector!!)
				obj.moveDataTo(stylesheet)
			}
		}
		return this
	}


	/**
	 * TODO: Escape and add braces ?
	 * https://mathiasbynens.be/notes/css-escapes
	 * http://stackoverflow.com/questions/13987979/how-to-properly-escape-attribute-values-in-css-js-selector-attr-value
	 * https://developer.mozilla.org/en-US/docs/Web/API/CSS/escape
	 */
	operator fun get(attrName: Any) = append("[$attrName]", false, true)
	operator fun get(attrName: Any, attrValue: Any) = append("[$attrName=$attrValue]", false, true)
	operator fun get(attrName: Any, attrFiler: AttrFilter, attrValue: Any) = append("[$attrName$attrFiler=$attrValue]", false, true)


	fun toList(selectorPrefix: CharSequence, _spaceBefore: Boolean) = rows.map { it.toString(selectorPrefix, _spaceBefore) }

	fun toString(selectorPrefix: CharSequence, _spaceBefore: Boolean) = toList(selectorPrefix, _spaceBefore).joinToString(",")
	override fun toString() = toString("", true)


	class Row(
			str: CharSequence,
			var spaceBefore: Boolean = true,
			var spaceAfter: Boolean = true
	) {
		val sb = StringBuilder(str)


		fun append(str: CharSequence, _spaceBefore: Boolean, _spaceAfter: Boolean) {
			if (sb.isEmpty())
				spaceBefore = _spaceBefore
			else if (_spaceBefore && spaceAfter)
				sb.append(' ')

			sb.append(str)

			spaceAfter = _spaceAfter
		}


		fun toString(selectorPrefix: CharSequence, _spaceBefore: Boolean): String {
			return buildString {
				if (selectorPrefix.isNotEmpty()) {
					append(selectorPrefix)
					if (_spaceBefore && spaceBefore) append(' ')
				}
				append(sb)
			}
		}

		override fun toString() = sb.toString()
	}


	companion object
	{
		fun createEmpty(stylesheet: Stylesheet) = Selector(stylesheet).apply { rows.add(Row("", false, true)) }
	}
}