package azagroup.kotlin.css.dimens

import azagroup.kotlin.css.dimens.LinearDimension.Companion.from as dimen


class BoxDimensions(
		var top: LinearDimension = 0.px,
		var right: LinearDimension = top,
		var bottom: LinearDimension = top,
		var left: LinearDimension = right
) {
	override fun toString(): String {
		return when {
			top == right && top == bottom && top == left -> top.toString()
			top == bottom && left == right -> "$top $right"
			left == right -> "$top $right $bottom"
			else -> "$top $right $bottom $left"
		}
	}


	companion object
	{
		fun from(vararg args: Any): BoxDimensions {
			return when (args.size) {
				1 -> BoxDimensions(dimen(args[0]))
				2 -> BoxDimensions(dimen(args[0]), dimen(args[1]))
				3 -> BoxDimensions(dimen(args[0]), dimen(args[1]), dimen(args[2]))
				else -> BoxDimensions(dimen(args[0]), dimen(args[1]), dimen(args[2]), dimen(args[3]))
			}
		}
	}
}
