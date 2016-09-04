package azagroup.kotlin.css.colors


class HSLValues(
		var hue: Float,
		var saturation: Float,
		var lightness: Float
) {
	fun setLightnessSafe(l: Float): HSLValues {
		lightness = Math.min(1f, Math.max(0f, l))
		return this
	}

	fun setSaturationSafe(l: Float): HSLValues {
		saturation = Math.min(1f, Math.max(0f, l))
		return this
	}
}
