package azagroup.kotlin.css


interface ASelector
{
	fun custom(selector: String, _spaceBefore: Boolean = true, _spaceAfter: Boolean = true, body: (Stylesheet.()->Unit)? = null): Selector

	fun pseudo(selector: String) = custom(selector, false)

	fun pseudoFn(selector: String, body: (Stylesheet.()->Unit)? = null) = custom(selector, false, true, body)
}
