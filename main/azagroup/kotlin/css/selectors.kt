@file:Suppress("unused")

package azagroup.kotlin.css

// CSS Selector Reference
// http://www.w3schools.com/cssref/css_selectors.asp


//
// AT-RULES
//
fun Stylesheet.at(rule: Any, body: (Stylesheet.()->Unit)? = null): Stylesheet {
	val stylesheet = Stylesheet(body)
	stylesheet.selector = Selector.createEmpty(stylesheet)
	stylesheet.atRule = "@$rule"
	children.add(stylesheet)
	return stylesheet
}
fun Stylesheet.mediaRaw(query: Any, body: (Stylesheet.()->Unit)? = null)
		= at("media $query", body)
fun Stylesheet.media(vararg conditions: Any, body: (Stylesheet.()->Unit)? = null)
		= mediaRaw("(${conditions.joinToString(") and (")})", body)


//
// CLASSES AND IDs
//
fun ASelector.c(selector: Any, body: (Stylesheet.()->Unit)? = null) = custom(".$selector", false, true, body)
fun ASelector.id(selector: Any, body: (Stylesheet.()->Unit)? = null) = custom("#$selector", false, true, body)


//
// TRAVERSING
//
val ASelector.child: Selector get() = custom(">", false, false)
val ASelector.next: Selector get() = custom("+", false, false)
val ASelector.nextAll: Selector get() = custom("~", false, false)


//
// TAGS
//
val ASelector.any: Selector get() = custom("*")

val ASelector.a: Selector get() = custom("a") // Defines a hyperlink.
val ASelector.abbr: Selector get() = custom("abbr") // Defines an abbreviation or an acronym.
val ASelector.acronym: Selector get() = custom("acronym") // Defines an acronym. Not supported in HTML5. Use <abbr> instead.
val ASelector.address: Selector get() = custom("address") // Defines contact information for the author/owner of a document.
val ASelector.applet: Selector get() = custom("applet") // Defines an embedded applet. Not supported in HTML5. Use <embed> or <object> instead.
val ASelector.area: Selector get() = custom("area") // Defines an area inside an image-map.
val ASelector.article: Selector get() = custom("article") // Defines an article.
val ASelector.aside: Selector get() = custom("aside") // Defines content aside from the page content.
val ASelector.audio: Selector get() = custom("audio") // Defines sound content.
val ASelector.b: Selector get() = custom("b") // Defines bold text.
val ASelector.base: Selector get() = custom("base") // Specifies the base URL/target for all relative URLs in a document.
val ASelector.basefont: Selector get() = custom("basefont") // Specifies a default color, size, and font for all text in a document. Not supported in HTML5. Use CSS instead.
val ASelector.bdi: Selector get() = custom("bdi") // Isolates a part of text that might be formatted in a different direction from other text outside it.
val ASelector.bdo: Selector get() = custom("bdo") // Overrides the current text direction.
val ASelector.big: Selector get() = custom("big") // Defines big text. Not supported in HTML5. Use CSS instead.
val ASelector.blockquote: Selector get() = custom("blockquote") // Defines a section that is quoted from another source.
val ASelector.body: Selector get() = custom("body") // Defines the document's body.
val ASelector.br: Selector get() = custom("br") // Defines a single line break.
val ASelector.button: Selector get() = custom("button") // Defines a clickable button.
val ASelector.canvas: Selector get() = custom("canvas") // Used to draw graphics, on the fly, via scripting (usually JavaScript).
val ASelector.caption: Selector get() = custom("caption") // Defines a table caption.
val ASelector.center: Selector get() = custom("center") // Defines centered text. Not supported in HTML5. Use CSS instead.
val ASelector.cite: Selector get() = custom("cite") // Defines the title of a work.
val ASelector.code: Selector get() = custom("code") // Defines a piece of computer code.
val ASelector.col: Selector get() = custom("col") // Specifies column properties for each column within a <colgroup> element.
val ASelector.colgroup: Selector get() = custom("colgroup") // Specifies a group of one or more columns in a table for formatting.
val ASelector.datalist: Selector get() = custom("datalist") // Specifies a list of pre-defined options for input controls.
val ASelector.dd: Selector get() = custom("dd") // Defines a description/value of a term in a description list.
val ASelector.del: Selector get() = custom("del") // Defines text that has been deleted from a document.
val ASelector.details: Selector get() = custom("details") // Defines additional details that the user can view or hide.
val ASelector.dfn: Selector get() = custom("dfn") // Represents the defining instance of a term.
val ASelector.dialog: Selector get() = custom("dialog") // Defines a dialog box or window.
val ASelector.dir: Selector get() = custom("dir") // Defines a directory list. Not supported in HTML5. Use <ul> instead.
val ASelector.div: Selector get() = custom("div") // Defines a section in a document.
val ASelector.dl: Selector get() = custom("dl") // Defines a description list.
val ASelector.dt: Selector get() = custom("dt") // Defines a term/name in a description list.
val ASelector.em: Selector get() = custom("em") // Defines emphasized text.
val ASelector.embed: Selector get() = custom("embed") // Defines a container for an external (non-HTML) application.
val ASelector.fieldset: Selector get() = custom("fieldset") // Groups related elements in a form.
val ASelector.figcaption: Selector get() = custom("figcaption") // Defines a caption for a <figure> element.
val ASelector.figure: Selector get() = custom("figure") // Specifies self-contained content.
val ASelector.font: Selector get() = custom("font") // Defines font, color, and size for text. Not supported in HTML5. Use CSS instead.
val ASelector.footer: Selector get() = custom("footer") // Defines a footer for a document or section.
val ASelector.form: Selector get() = custom("form") // Defines an HTML form for user input.
val ASelector.frame: Selector get() = custom("frame") // Defines a window (a frame) in a frameset. Not supported in HTML5.
val ASelector.frameset: Selector get() = custom("frameset") // Defines a set of frames. Not supported in HTML5.
val ASelector.h1: Selector get() = custom("h1") // Defines HTML headings.
val ASelector.h2: Selector get() = custom("h2") // Defines HTML headings.
val ASelector.h3: Selector get() = custom("h3") // Defines HTML headings.
val ASelector.h4: Selector get() = custom("h4") // Defines HTML headings.
val ASelector.h5: Selector get() = custom("h5") // Defines HTML headings.
val ASelector.h6: Selector get() = custom("h6") // Defines HTML headings.
val ASelector.head: Selector get() = custom("head") // Defines information about the document.
val ASelector.header: Selector get() = custom("header") // Defines a header for a document or section.
val ASelector.hr: Selector get() = custom("hr") // Defines a thematic change in the content.
val ASelector.html: Selector get() = custom("html") // Defines the root of an HTML document.
val ASelector.i: Selector get() = custom("i") // Defines a part of text in an alternate voice or mood.
val ASelector.iframe: Selector get() = custom("iframe") // Defines an inline frame.
val ASelector.img: Selector get() = custom("img") // Defines an image.
val ASelector.input: Selector get() = custom("input") // Defines an input control.
val ASelector.ins: Selector get() = custom("ins") // Defines a text that has been inserted into a document.
val ASelector.kbd: Selector get() = custom("kbd") // Defines keyboard input.
val ASelector.keygen: Selector get() = custom("keygen") // Defines a key-pair generator field (for forms).
val ASelector.label: Selector get() = custom("label") // Defines a label for an <input> element.
val ASelector.legend: Selector get() = custom("legend") // Defines a caption for a <fieldset> element.
val ASelector.li: Selector get() = custom("li") // Defines a list item.
val ASelector.link: Selector get() = custom("link") // Defines the relationship between a document and an external resource (most used to link to style sheets).
val ASelector.main: Selector get() = custom("main") // Specifies the main content of a document.
val ASelector.map: Selector get() = custom("map") // Defines a client-side image-map.
val ASelector.mark: Selector get() = custom("mark") // Defines marked/highlighted text.
val ASelector.menu: Selector get() = custom("menu") // Defines a list/menu of commands.
val ASelector.menuitem: Selector get() = custom("menuitem") // Defines a command/menu item that the user can invoke from a popup menu.
val ASelector.meta: Selector get() = custom("meta") // Defines metadata about an HTML document.
val ASelector.meter: Selector get() = custom("meter") // Defines a scalar measurement within a known range (a gauge).
val ASelector.nav: Selector get() = custom("nav") // Defines navigation links.
val ASelector.noframes: Selector get() = custom("noframes") // Defines an alternate content for users that do not support frames. Not supported in HTML5.
val ASelector.noscript: Selector get() = custom("noscript") // Defines an alternate content for users that do not support client-side scripts.
val ASelector.`object`: Selector get() = custom("object") // Defines an embedded object.
val ASelector.ol: Selector get() = custom("ol") // Defines an ordered list.
val ASelector.optgroup: Selector get() = custom("optgroup") // Defines a group of related options in a drop-down list.
val ASelector.option: Selector get() = custom("option") // Defines an option in a drop-down list.
val ASelector.output: Selector get() = custom("output") // Defines the result of a calculation.
val ASelector.p: Selector get() = custom("p") // Defines a paragraph.
val ASelector.param: Selector get() = custom("param") // Defines a parameter for an object.
val ASelector.pre: Selector get() = custom("pre") // Defines preformatted text.
val ASelector.progress: Selector get() = custom("progress") // Represents the progress of a task.
val ASelector.q: Selector get() = custom("q") // Defines a short quotation.
val ASelector.rp: Selector get() = custom("rp") // Defines what to show in browsers that do not support ruby annotations.
val ASelector.rt: Selector get() = custom("rt") // Defines an explanation/pronunciation of characters (for East Asian typography).
val ASelector.ruby: Selector get() = custom("ruby") // Defines a ruby annotation (for East Asian typography).
val ASelector.s: Selector get() = custom("s") // Defines text that is no longer correct.
val ASelector.samp: Selector get() = custom("samp") // Defines sample output from a computer program.
val ASelector.script: Selector get() = custom("script") // Defines a client-side script.
val ASelector.section: Selector get() = custom("section") // Defines a section in a document.
val ASelector.select: Selector get() = custom("select") // Defines a drop-down list.
val ASelector.small: Selector get() = custom("small") // Defines smaller text.
val ASelector.source: Selector get() = custom("source") // Defines multiple media resources for media elements (<video> and <audio>).
val ASelector.span: Selector get() = custom("span") // Defines a section in a document.
val ASelector.strike: Selector get() = custom("strike") // Defines strikethrough text. Not supported in HTML5. Use <del> or <s> instead.
val ASelector.strong: Selector get() = custom("strong") // Defines important text.
val ASelector.style: Selector get() = custom("style") // Defines style information for a document.
val ASelector.sub: Selector get() = custom("sub") // Defines subscripted text.
val ASelector.summary: Selector get() = custom("summary") // Defines a visible heading for a <details> element.
val ASelector.sup: Selector get() = custom("sup") // Defines superscripted text.
val ASelector.table: Selector get() = custom("table") // Defines a table.
val ASelector.tbody: Selector get() = custom("tbody") // Groups the body content in a table.
val ASelector.td: Selector get() = custom("td") // Defines a cell in a table.
val ASelector.textarea: Selector get() = custom("textarea") // Defines a multiline input control (text area).
val ASelector.tfoot: Selector get() = custom("tfoot") // Groups the footer content in a table.
val ASelector.th: Selector get() = custom("th") // Defines a header cell in a table.
val ASelector.thead: Selector get() = custom("thead") // Groups the header content in a table.
val ASelector.time: Selector get() = custom("time") // Defines a date/time.
val ASelector.title: Selector get() = custom("title") // Defines a title for the document.
val ASelector.tr: Selector get() = custom("tr") // Defines a row in a table.
val ASelector.track: Selector get() = custom("track") // Defines text tracks for media elements (<video> and <audio>).
val ASelector.tt: Selector get() = custom("tt") // Defines teletype text. Not supported in HTML5. Use CSS instead.
val ASelector.u: Selector get() = custom("u") // Defines text that should be stylistically different from normal text.
val ASelector.ul: Selector get() = custom("ul") // Defines an unordered list.
val ASelector.`var`: Selector get() = custom("var") // Defines a variable.
val ASelector.video: Selector get() = custom("video") // Defines a video or movie.
val ASelector.wbr: Selector get() = custom("wbr") // Defines a possible line-break.


//
// PSEUDO-ELEMENTS
//
val ASelector.active: Selector get() = pseudo(":active") // (CSS1) Selects the active link
val ASelector.after: Selector get() = pseudo("::after") // (CSS2) Insert something after the content of each <p> element
val ASelector.before: Selector get() = pseudo("::before") // (CSS2) Insert something before the content of each <p> element
val ASelector.checked: Selector get() = pseudo(":checked") // (CSS3) Selects every checked <input> element
val ASelector.disabled: Selector get() = pseudo(":disabled") // (CSS3) Selects every disabled <input> element
val ASelector.empty: Selector get() = pseudo(":empty") // (CSS3) Selects every <p> element that has no children (including text nodes)
val ASelector.enabled: Selector get() = pseudo(":enabled") // (CSS3) Selects every enabled <input> element
val ASelector.firstChild: Selector get() = pseudo(":first-child") // (CSS2) Selects every <p> element that is the first child of its parent
val ASelector.firstLetter: Selector get() = pseudo("::first-letter") // (CSS1) Selects the first letter of every <p> element
val ASelector.firstLine: Selector get() = pseudo("::first-line") // (CSS1) Selects the first line of every <p> element
val ASelector.firstOfType: Selector get() = pseudo(":first-of-type") // (CSS3) Selects every <p> element that is the first <p> element of its parent
val ASelector.focus: Selector get() = pseudo(":focus") // (CSS2) Selects the input element which has focus
val ASelector.hover: Selector get() = pseudo(":hover") // (CSS1) Selects links on mouse over
val ASelector.inRange: Selector get() = pseudo(":in-range") // (CSS3) Selects input elements with a value within a specified range
val ASelector.invalid: Selector get() = pseudo(":invalid") // (CSS3) Selects all input elements with an invalid value
val ASelector.lastChild: Selector get() = pseudo(":last-child") // (CSS3) Selects every <p> element that is the last child of its parent
val ASelector.lastOfType: Selector get() = pseudo(":last-of-type") // (CSS3) Selects every <p> element that is the last <p> element of its parent
val ASelector.onlyChild: Selector get() = pseudo(":only-child") // (CSS3) Selects every <p> element that is the only child of its parent
val ASelector.onlyOfType: Selector get() = pseudo(":only-of-type") // (CSS3) Selects every <p> element that is the only <p> element of its parent
val ASelector.optional: Selector get() = pseudo(":optional") // (CSS3) Selects input elements with no "required" attribute
val ASelector.outOfRange: Selector get() = pseudo(":out-of-range") // (CSS3) Selects input elements with a value outside a specified range
val ASelector.readOnly: Selector get() = pseudo(":read-only") // (CSS3) Selects input elements with the "readonly" attribute specified
val ASelector.readWrite: Selector get() = pseudo(":read-write") // (CSS3) Selects input elements with the "readonly" attribute NOT specified
val ASelector.required: Selector get() = pseudo(":required") // (CSS3) Selects input elements with the "required" attribute specified
val ASelector.root: Selector get() = pseudo(":root") // (CSS3) Selects the document's root element
val ASelector.selection: Selector get() = pseudo("::selection") // (Was drafted for CSS3, but removed before the Recommendation status) Selects the portion of an element that is selected by a user
val ASelector.target: Selector get() = pseudo(":target") // (CSS3) Selects the current active #news element (clicked on a URL containing that anchor name)
val ASelector.unvisited: Selector get() = pseudo(":link") // (CSS1) Selects all unvisited links
val ASelector.valid: Selector get() = pseudo(":valid") // (CSS3) Selects all input elements with a valid value
val ASelector.visited: Selector get() = pseudo(":visited") // (CSS1) Selects all visited links

fun ASelector.lang(language: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":lang($language)", body) // (CSS2) Selects every <p> element with a lang attribute equal to "it" (Italian)
fun ASelector.not(selector: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":not($selector)", body) // (CSS3) Selects every element that is not a <p> element
fun ASelector.nthChild(n: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":nth-child($n)", body) // (CSS3) Selects every <p> element that is the second child of its parent
fun ASelector.nthLastChild(n: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":nth-last-child($n)", body) // (CSS3) Selects every <p> element that is the second child of its parent, counting from the last child
fun ASelector.nthLastOfType(n: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":nth-last-of-type($n)", body) // (CSS3) Selects every <p> element that is the second <p> element of its parent, counting from the last child
fun ASelector.nthOfType(n: Any, body: (Stylesheet.()->Unit)? = null) = pseudoFn(":nth-of-type($n)", body) // (CSS3) Selects every <p> element that is the second <p> element of its parent
