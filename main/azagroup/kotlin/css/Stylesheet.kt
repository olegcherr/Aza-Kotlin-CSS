package azagroup.kotlin.css

import java.util.ArrayList


// CSS Selector Reference
// http://www.w3schools.com/cssref/css_selectors.asp

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


	fun include(stylesheet: Stylesheet): Stylesheet {
		children.add(stylesheet)
		return this
	}

	override fun custom(selector: String, _spaceBefore: Boolean, _spaceAfter: Boolean, body: (Stylesheet.() -> Unit)?): Selector {
		val stylesheet = Stylesheet()
		val sel = Selector(stylesheet)
		sel.custom(selector, _spaceBefore, _spaceAfter)

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
			val selectorStr = selector?.toString(selectorPrefix, _spaceBefore)
			val hasSelector = !selectorStr.isNullOrEmpty()

			if (hasSelector)
				sb.append(selectorStr).append('{')

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

			if (hasSelector)
				sb.append("}")
		}


		for (child in children) {
			val rows = selector?.rows
			if (rows != null && rows.isNotEmpty())
				rows.forEach { child.render(sb, it.toString(selectorPrefix, _spaceBefore), it.spaceAfter) }
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
	) {
		override fun toString() = "$name:$value"
	}


	//
	// AT-RULES
	//
	fun at(rule: Any, body: (Stylesheet.()->Unit)? = null): Stylesheet {
		val stylesheet = Stylesheet(body)
		stylesheet.selector = Selector.createEmpty(stylesheet)
		stylesheet.atRule = "@$rule"
		include(stylesheet)
		return stylesheet
	}

	fun media(vararg conditions: Any, body: (Stylesheet.()->Unit)? = null)
			= at("media (${conditions.joinToString(") and (")})", body)


	//
	// MAIN COMMANDS
	//
	fun CharSequence.custom(selector: String, _spaceBefore: Boolean = true, _spaceAfter: Boolean = true, body: (Stylesheet.()->Unit)? = null): Selector {
		return when (this) {
			is ASelector -> custom(selector, _spaceBefore, _spaceAfter, body)
			else -> toSelector().custom(selector, _spaceBefore, _spaceAfter, body)
		}
	}
	fun CharSequence.pseudo(selector: String, body: (Stylesheet.()->Unit)? = null): Selector {
		return when (this) {
			is ASelector -> pseudo(selector, body)
			else -> toSelector().pseudo(selector, body)
		}
	}
	fun CharSequence.pseudoFn(selector: String, body: (Stylesheet.()->Unit)? = null): Selector {
		return when (this) {
			is ASelector -> pseudoFn(selector, body)
			else -> toSelector().pseudoFn(selector, body)
		}
	}

	infix fun CharSequence.and(obj: ASelector): Selector {
		val sel = toSelector()
		when (obj) {
			is Selector -> {
				for (row in obj.rows)
					sel.rows.add(row)
				return sel
			}
			is Stylesheet -> {
				val selector = obj.selector!!
				selector.rows.addAll(0, sel.rows)
				return selector
			}
			else -> throw RuntimeException("Unknown kind of corresponding object: ${obj.javaClass.simpleName}")
		}
	}

	private fun CharSequence.toSelector() = when (this) {
		is Selector -> this
		is Stylesheet -> this.selector!!
		else -> when (this[0]) {
			'.' -> this@Stylesheet.c(this.drop(1))
			'#' -> this@Stylesheet.id(this.drop(1))
			'@' -> this@Stylesheet.at(this.drop(1)).selector!!
			else -> this@Stylesheet.c(this)
		}
	}


	//
	// SUGAR
	//
	operator fun CharSequence.invoke(body: Stylesheet.()->Unit) = toSelector().invoke(body)
	operator fun CharSequence.div(obj: ASelector) = child.append(obj)
	operator fun CharSequence.mod(obj: ASelector) = next.append(obj)
	operator fun CharSequence.minus(obj: ASelector) = nextAll.append(obj)
	operator fun CharSequence.rangeTo(obj: ASelector) = children.append(obj)


	//
	// ATTRIBUTES
	//
	/*
	TODO: Escape and add braces ?
	https://mathiasbynens.be/notes/css-escapes
	http://stackoverflow.com/questions/13987979/how-to-properly-escape-attribute-values-in-css-js-selector-attr-value
	https://developer.mozilla.org/en-US/docs/Web/API/CSS/escape
	 */
	fun CharSequence.attr(attrName: Any, body: (Stylesheet.()->Unit)? = null): Selector {
		return when (this) {
			is ASelector -> custom("[$attrName]", false, true, body)
			else -> toSelector().attr(attrName, body)
		}
	}
	fun CharSequence.attr(attrName: Any, attrValue: Any, body: (Stylesheet.()->Unit)? = null): Selector {
		return when (this) {
			is ASelector -> custom("[$attrName=${escapeAttrValue(attrValue.toString())}]", false, true, body)
			else -> toSelector().attr(attrName, attrValue, body)
		}
	}
	fun CharSequence.attr(attrName: Any, attrValue: Any, attrFiler: AttrFilter, body: (Stylesheet.()->Unit)? = null): Selector {
		return when (this) {
			is ASelector -> custom("[$attrName$attrFiler=${escapeAttrValue(attrValue.toString())}]", false, true, body)
			else -> toSelector().attr(attrName, attrValue, attrFiler, body)
		}
	}

	operator fun CharSequence.get(attrName: Any, body: (Stylesheet.()->Unit)? = null) = attr(attrName, body)
	operator fun CharSequence.get(attrName: Any, attrValue: Any, body: (Stylesheet.()->Unit)? = null) = attr(attrName, attrValue, body)
	operator fun CharSequence.get(attrName: Any, attrFiler: AttrFilter, attrValue: Any, body: (Stylesheet.()->Unit)? = null) = attr(attrName, attrValue, attrFiler, body)

	private fun escapeAttrValue(str: String): String {
		// http://stackoverflow.com/questions/5578845/css-attribute-selectors-the-rules-on-quotes-or-none
		val isIdentifier = str.all { it >= '0' && it <= '9' || it >= 'a' && it <= 'z' || it >= 'A' && it <= 'Z' || it == '-' || it == '_' }
		return if (isIdentifier) str else "\"${str.replace("\"", "\\\"")}\""
	}


	//
	// CLASSES AND IDs
	//
	fun CharSequence.c(selector: Any, body: (Stylesheet.()->Unit)? = null) = custom(".$selector", false, true, body)
	fun CharSequence.id(selector: Any, body: (Stylesheet.()->Unit)? = null) = custom("#$selector", false, true, body)


	//
	// TRAVERSING
	//
	val CharSequence.children: Selector get() = custom(" ", false, false)
	val CharSequence.child: Selector get() = custom(">", false, false)
	val CharSequence.next: Selector get() = custom("+", false, false)
	val CharSequence.nextAll: Selector get() = custom("~", false, false)


	//
	// TAGS
	//
	val CharSequence.any: Selector get() = custom("*")

	val CharSequence.a: Selector get() = custom("a") // Defines a hyperlink.
	val CharSequence.abbr: Selector get() = custom("abbr") // Defines an abbreviation or an acronym.
	val CharSequence.acronym: Selector get() = custom("acronym") // Defines an acronym. Not supported in HTML5. Use <abbr> instead.
	val CharSequence.address: Selector get() = custom("address") // Defines contact information for the author/owner of a document.
	val CharSequence.applet: Selector get() = custom("applet") // Defines an embedded applet. Not supported in HTML5. Use <embed> or <object> instead.
	val CharSequence.area: Selector get() = custom("area") // Defines an area inside an image-map.
	val CharSequence.article: Selector get() = custom("article") // Defines an article.
	val CharSequence.aside: Selector get() = custom("aside") // Defines content aside from the page content.
	val CharSequence.audio: Selector get() = custom("audio") // Defines sound content.
	val CharSequence.b: Selector get() = custom("b") // Defines bold text.
	val CharSequence.base: Selector get() = custom("base") // Specifies the base URL/target for all relative URLs in a document.
	val CharSequence.basefont: Selector get() = custom("basefont") // Specifies a default color, size, and font for all text in a document. Not supported in HTML5. Use CSS instead.
	val CharSequence.bdi: Selector get() = custom("bdi") // Isolates a part of text that might be formatted in a different direction from other text outside it.
	val CharSequence.bdo: Selector get() = custom("bdo") // Overrides the current text direction.
	val CharSequence.big: Selector get() = custom("big") // Defines big text. Not supported in HTML5. Use CSS instead.
	val CharSequence.blockquote: Selector get() = custom("blockquote") // Defines a section that is quoted from another source.
	val CharSequence.body: Selector get() = custom("body") // Defines the document's body.
	val CharSequence.br: Selector get() = custom("br") // Defines a single line break.
	val CharSequence.button: Selector get() = custom("button") // Defines a clickable button.
	val CharSequence.canvas: Selector get() = custom("canvas") // Used to draw graphics, on the fly, via scripting (usually JavaScript).
	val CharSequence.caption: Selector get() = custom("caption") // Defines a table caption.
	val CharSequence.center: Selector get() = custom("center") // Defines centered text. Not supported in HTML5. Use CSS instead.
	val CharSequence.cite: Selector get() = custom("cite") // Defines the title of a work.
	val CharSequence.code: Selector get() = custom("code") // Defines a piece of computer code.
	val CharSequence.col: Selector get() = custom("col") // Specifies column properties for each column within a <colgroup> element.
	val CharSequence.colgroup: Selector get() = custom("colgroup") // Specifies a group of one or more columns in a table for formatting.
	val CharSequence.datalist: Selector get() = custom("datalist") // Specifies a list of pre-defined options for input controls.
	val CharSequence.dd: Selector get() = custom("dd") // Defines a description/value of a term in a description list.
	val CharSequence.del: Selector get() = custom("del") // Defines text that has been deleted from a document.
	val CharSequence.details: Selector get() = custom("details") // Defines additional details that the user can view or hide.
	val CharSequence.dfn: Selector get() = custom("dfn") // Represents the defining instance of a term.
	val CharSequence.dialog: Selector get() = custom("dialog") // Defines a dialog box or window.
	val CharSequence.dir: Selector get() = custom("dir") // Defines a directory list. Not supported in HTML5. Use <ul> instead.
	val CharSequence.div: Selector get() = custom("div") // Defines a section in a document.
	val CharSequence.dl: Selector get() = custom("dl") // Defines a description list.
	val CharSequence.dt: Selector get() = custom("dt") // Defines a term/name in a description list.
	val CharSequence.em: Selector get() = custom("em") // Defines emphasized text.
	val CharSequence.embed: Selector get() = custom("embed") // Defines a container for an external (non-HTML) application.
	val CharSequence.fieldset: Selector get() = custom("fieldset") // Groups related elements in a form.
	val CharSequence.figcaption: Selector get() = custom("figcaption") // Defines a caption for a <figure> element.
	val CharSequence.figure: Selector get() = custom("figure") // Specifies self-contained content.
	val CharSequence.font: Selector get() = custom("font") // Defines font, color, and size for text. Not supported in HTML5. Use CSS instead.
	val CharSequence.footer: Selector get() = custom("footer") // Defines a footer for a document or section.
	val CharSequence.form: Selector get() = custom("form") // Defines an HTML form for user input.
	val CharSequence.frame: Selector get() = custom("frame") // Defines a window (a frame) in a frameset. Not supported in HTML5.
	val CharSequence.frameset: Selector get() = custom("frameset") // Defines a set of frames. Not supported in HTML5.
	val CharSequence.h1: Selector get() = custom("h1") // Defines HTML headings.
	val CharSequence.h2: Selector get() = custom("h2") // Defines HTML headings.
	val CharSequence.h3: Selector get() = custom("h3") // Defines HTML headings.
	val CharSequence.h4: Selector get() = custom("h4") // Defines HTML headings.
	val CharSequence.h5: Selector get() = custom("h5") // Defines HTML headings.
	val CharSequence.h6: Selector get() = custom("h6") // Defines HTML headings.
	val CharSequence.head: Selector get() = custom("head") // Defines information about the document.
	val CharSequence.header: Selector get() = custom("header") // Defines a header for a document or section.
	val CharSequence.hr: Selector get() = custom("hr") // Defines a thematic change in the content.
	val CharSequence.html: Selector get() = custom("html") // Defines the root of an HTML document.
	val CharSequence.i: Selector get() = custom("i") // Defines a part of text in an alternate voice or mood.
	val CharSequence.iframe: Selector get() = custom("iframe") // Defines an inline frame.
	val CharSequence.img: Selector get() = custom("img") // Defines an image.
	val CharSequence.input: Selector get() = custom("input") // Defines an input control.
	val CharSequence.ins: Selector get() = custom("ins") // Defines a text that has been inserted into a document.
	val CharSequence.kbd: Selector get() = custom("kbd") // Defines keyboard input.
	val CharSequence.keygen: Selector get() = custom("keygen") // Defines a key-pair generator field (for forms).
	val CharSequence.label: Selector get() = custom("label") // Defines a label for an <input> element.
	val CharSequence.legend: Selector get() = custom("legend") // Defines a caption for a <fieldset> element.
	val CharSequence.li: Selector get() = custom("li") // Defines a list item.
	val CharSequence.link: Selector get() = custom("link") // Defines the relationship between a document and an external resource (most used to link to style sheets).
	val CharSequence.main: Selector get() = custom("main") // Specifies the main content of a document.
	val CharSequence.map: Selector get() = custom("map") // Defines a client-side image-map.
	val CharSequence.mark: Selector get() = custom("mark") // Defines marked/highlighted text.
	val CharSequence.menu: Selector get() = custom("menu") // Defines a list/menu of commands.
	val CharSequence.menuitem: Selector get() = custom("menuitem") // Defines a command/menu item that the user can invoke from a popup menu.
	val CharSequence.meta: Selector get() = custom("meta") // Defines metadata about an HTML document.
	val CharSequence.meter: Selector get() = custom("meter") // Defines a scalar measurement within a known range (a gauge).
	val CharSequence.nav: Selector get() = custom("nav") // Defines navigation links.
	val CharSequence.noframes: Selector get() = custom("noframes") // Defines an alternate content for users that do not support frames. Not supported in HTML5.
	val CharSequence.noscript: Selector get() = custom("noscript") // Defines an alternate content for users that do not support client-side scripts.
	val CharSequence.`object`: Selector get() = custom("object") // Defines an embedded object.
	val CharSequence.ol: Selector get() = custom("ol") // Defines an ordered list.
	val CharSequence.optgroup: Selector get() = custom("optgroup") // Defines a group of related options in a drop-down list.
	val CharSequence.option: Selector get() = custom("option") // Defines an option in a drop-down list.
	val CharSequence.output: Selector get() = custom("output") // Defines the result of a calculation.
	val CharSequence.p: Selector get() = custom("p") // Defines a paragraph.
	val CharSequence.param: Selector get() = custom("param") // Defines a parameter for an object.
	val CharSequence.pre: Selector get() = custom("pre") // Defines preformatted text.
	val CharSequence.progress: Selector get() = custom("progress") // Represents the progress of a task.
	val CharSequence.q: Selector get() = custom("q") // Defines a short quotation.
	val CharSequence.rp: Selector get() = custom("rp") // Defines what to show in browsers that do not support ruby annotations.
	val CharSequence.rt: Selector get() = custom("rt") // Defines an explanation/pronunciation of characters (for East Asian typography).
	val CharSequence.ruby: Selector get() = custom("ruby") // Defines a ruby annotation (for East Asian typography).
	val CharSequence.s: Selector get() = custom("s") // Defines text that is no longer correct.
	val CharSequence.samp: Selector get() = custom("samp") // Defines sample output from a computer program.
	val CharSequence.script: Selector get() = custom("script") // Defines a client-side script.
	val CharSequence.section: Selector get() = custom("section") // Defines a section in a document.
	val CharSequence.select: Selector get() = custom("select") // Defines a drop-down list.
	val CharSequence.small: Selector get() = custom("small") // Defines smaller text.
	val CharSequence.source: Selector get() = custom("source") // Defines multiple media resources for media elements (<video> and <audio>).
	val CharSequence.span: Selector get() = custom("span") // Defines a section in a document.
	val CharSequence.strike: Selector get() = custom("strike") // Defines strikethrough text. Not supported in HTML5. Use <del> or <s> instead.
	val CharSequence.strong: Selector get() = custom("strong") // Defines important text.
	val CharSequence.style: Selector get() = custom("style") // Defines style information for a document.
	val CharSequence.sub: Selector get() = custom("sub") // Defines subscripted text.
	val CharSequence.summary: Selector get() = custom("summary") // Defines a visible heading for a <details> element.
	val CharSequence.sup: Selector get() = custom("sup") // Defines superscripted text.
	val CharSequence.table: Selector get() = custom("table") // Defines a table.
	val CharSequence.tbody: Selector get() = custom("tbody") // Groups the body content in a table.
	val CharSequence.td: Selector get() = custom("td") // Defines a cell in a table.
	val CharSequence.textarea: Selector get() = custom("textarea") // Defines a multiline input control (text area).
	val CharSequence.tfoot: Selector get() = custom("tfoot") // Groups the footer content in a table.
	val CharSequence.th: Selector get() = custom("th") // Defines a header cell in a table.
	val CharSequence.thead: Selector get() = custom("thead") // Groups the header content in a table.
	val CharSequence.time: Selector get() = custom("time") // Defines a date/time.
	val CharSequence.title: Selector get() = custom("title") // Defines a title for the document.
	val CharSequence.tr: Selector get() = custom("tr") // Defines a row in a table.
	val CharSequence.track: Selector get() = custom("track") // Defines text tracks for media elements (<video> and <audio>).
	val CharSequence.tt: Selector get() = custom("tt") // Defines teletype text. Not supported in HTML5. Use CSS instead.
	val CharSequence.u: Selector get() = custom("u") // Defines text that should be stylistically different from normal text.
	val CharSequence.ul: Selector get() = custom("ul") // Defines an unordered list.
	val CharSequence.`var`: Selector get() = custom("var") // Defines a variable.
	val CharSequence.video: Selector get() = custom("video") // Defines a video or movie.
	val CharSequence.wbr: Selector get() = custom("wbr") // Defines a possible line-break.


	//
	// PSEUDO-ELEMENTS
	//
	val CharSequence.active: Selector get() = pseudo(":active") // (CSS1) Selects the active link
	val CharSequence.after: Selector get() = pseudo("::after") // (CSS2) Insert something after the content of each <p> element
	val CharSequence.before: Selector get() = pseudo("::before") // (CSS2) Insert something before the content of each <p> element
	val CharSequence.checked: Selector get() = pseudo(":checked") // (CSS3) Selects every checked <input> element
	val CharSequence.disabled: Selector get() = pseudo(":disabled") // (CSS3) Selects every disabled <input> element
	val CharSequence.empty: Selector get() = pseudo(":empty") // (CSS3) Selects every <p> element that has no children (including text nodes)
	val CharSequence.enabled: Selector get() = pseudo(":enabled") // (CSS3) Selects every enabled <input> element
	val CharSequence.firstChild: Selector get() = pseudo(":first-child") // (CSS2) Selects every <p> element that is the first child of its parent
	val CharSequence.firstLetter: Selector get() = pseudo("::first-letter") // (CSS1) Selects the first letter of every <p> element
	val CharSequence.firstLine: Selector get() = pseudo("::first-line") // (CSS1) Selects the first line of every <p> element
	val CharSequence.firstOfType: Selector get() = pseudo(":first-of-type") // (CSS3) Selects every <p> element that is the first <p> element of its parent
	val CharSequence.focus: Selector get() = pseudo(":focus") // (CSS2) Selects the input element which has focus
	val CharSequence.hover: Selector get() = pseudo(":hover") // (CSS1) Selects links on mouse over
	val CharSequence.inRange: Selector get() = pseudo(":in-range") // (CSS3) Selects input elements with a value within a specified range
	val CharSequence.invalid: Selector get() = pseudo(":invalid") // (CSS3) Selects all input elements with an invalid value
	val CharSequence.lastChild: Selector get() = pseudo(":last-child") // (CSS3) Selects every <p> element that is the last child of its parent
	val CharSequence.lastOfType: Selector get() = pseudo(":last-of-type") // (CSS3) Selects every <p> element that is the last <p> element of its parent
	val CharSequence.onlyChild: Selector get() = pseudo(":only-child") // (CSS3) Selects every <p> element that is the only child of its parent
	val CharSequence.onlyOfType: Selector get() = pseudo(":only-of-type") // (CSS3) Selects every <p> element that is the only <p> element of its parent
	val CharSequence.optional: Selector get() = pseudo(":optional") // (CSS3) Selects input elements with no "required" attribute
	val CharSequence.outOfRange: Selector get() = pseudo(":out-of-range") // (CSS3) Selects input elements with a value outside a specified range
	val CharSequence.readOnly: Selector get() = pseudo(":read-only") // (CSS3) Selects input elements with the "readonly" attribute specified
	val CharSequence.readWrite: Selector get() = pseudo(":read-write") // (CSS3) Selects input elements with the "readonly" attribute NOT specified
	val CharSequence.required: Selector get() = pseudo(":required") // (CSS3) Selects input elements with the "required" attribute specified
	val CharSequence.root: Selector get() = pseudo(":root") // (CSS3) Selects the document's root element
	val CharSequence.selection: Selector get() = pseudo("::selection") // (Was drafted for CSS3, but removed before the Recommendation status) Selects the portion of an element that is selected by a user
	val CharSequence.target: Selector get() = pseudo(":target") // (CSS3) Selects the current active #news element (clicked on a URL containing that anchor name)
	val CharSequence.unvisited: Selector get() = pseudo(":link") // (CSS1) Selects all unvisited links
	val CharSequence.valid: Selector get() = pseudo(":valid") // (CSS3) Selects all input elements with a valid value
	val CharSequence.visited: Selector get() = pseudo(":visited") // (CSS1) Selects all visited links

	fun CharSequence.lang(language: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":lang($language)", body) // (CSS2) Selects every <p> element with a lang attribute equal to "it" (Italian)
	fun CharSequence.not(selector: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":not($selector)", body) // (CSS3) Selects every element that is not a <p> element
	fun CharSequence.nthChild(n: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":nth-child($n)", body) // (CSS3) Selects every <p> element that is the second child of its parent
	fun CharSequence.nthLastChild(n: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":nth-last-child($n)", body) // (CSS3) Selects every <p> element that is the second child of its parent, counting from the last child
	fun CharSequence.nthLastOfType(n: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":nth-last-of-type($n)", body) // (CSS3) Selects every <p> element that is the second <p> element of its parent, counting from the last child
	fun CharSequence.nthOfType(n: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":nth-of-type($n)", body) // (CSS3) Selects every <p> element that is the second <p> element of its parent

}
