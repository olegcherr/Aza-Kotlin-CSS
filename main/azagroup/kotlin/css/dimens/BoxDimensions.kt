package azagroup.kotlin.css.dimens


class BoxDimensions(
		var top: LinearDimension,
		var right: LinearDimension,
		var bottom: LinearDimension,
		var left: LinearDimension
) {
	override fun toString() = "$top $right $bottom $left"
}
