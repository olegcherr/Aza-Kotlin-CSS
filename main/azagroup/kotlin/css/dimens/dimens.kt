@file:Suppress("unused")

package azagroup.kotlin.css.dimens


val auto: LinearDimension = LinearDimension(0f, LinearUnits.AUTO)

val Number.px: LinearDimension get() = dimen(this, LinearUnits.PX)
val Number.em: LinearDimension get() = dimen(this, LinearUnits.EM)
val Number.percent: LinearDimension get() = dimen(this, LinearUnits.PERCENT)
val Number.ex: LinearDimension get() = dimen(this, LinearUnits.EX)
val Number.inch: LinearDimension get() = dimen(this, LinearUnits.INCH)
val Number.cm: LinearDimension get() = dimen(this, LinearUnits.CM)
val Number.mm: LinearDimension get() = dimen(this, LinearUnits.MM)
val Number.pt: LinearDimension get() = dimen(this, LinearUnits.PT)
val Number.pc: LinearDimension get() = dimen(this, LinearUnits.PC)

@Suppress("NOTHING_TO_INLINE")
inline private fun dimen(value: Number, units: LinearUnits) = LinearDimension(value.toFloat(), units)


fun box(vararg args: Any) = BoxDimensions.from(*args)
