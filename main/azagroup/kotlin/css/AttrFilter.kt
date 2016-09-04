package azagroup.kotlin.css


enum class AttrFilter(
		val symbol: Char?
) {
	EQUALS(null),           // [attribute=value]

	CONTAINS('*'),          // [attribute*=value]
	CONTAINS_WORD('~'),     // [attribute~=value]

	STARTS_WITH('^'),       // [attribute^=value]
	STARTS_WITH_WORD('|'),  // [attribute|=value]

	ENDS_WITH('$');         // [attribute$=value]


	override fun toString() = symbol?.toString() ?: ""
}

val equals          = AttrFilter.EQUALS
val contains        = AttrFilter.CONTAINS
val containsWord    = AttrFilter.CONTAINS_WORD
val startsWith      = AttrFilter.STARTS_WITH
val startsWithWord  = AttrFilter.STARTS_WITH_WORD
val endsWith        = AttrFilter.ENDS_WITH
