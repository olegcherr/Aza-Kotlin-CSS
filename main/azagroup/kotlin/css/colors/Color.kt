@file:Suppress("unused")

package azagroup.kotlin.css.colors

import azagroup.kotlin.css.cssDecimalFormat


class Color(
		var red: Float,
		var green: Float,
		var blue: Float,
		var alpha: Float = 1f
) {
	var redInt: Int
		get() = (red * 255f).toInt()
		set(value) { red = value.toFloat() / 255f }

	var greenInt: Int
		get() = (green * 255f).toInt()
		set(value) { green = value.toFloat() / 255f }

	var blueInt: Int
		get() = (blue * 255f).toInt()
		set(value) { blue = value.toFloat() / 255f }

	var alphaInt: Int
		get() = (alpha * 255f).toInt()
		set(value) { alpha = value.toFloat() / 255f }


	fun copy() = Color(red, green, blue)


	fun toHexString(): String {
		val res = "#${redInt.twoDigitHex()}${greenInt.twoDigitHex()}${blueInt.twoDigitHex()}"
		var i = 1

		while (i <= 5) {
			if (res[i] != res[i+1]) return res
			i += 2
		}

		return "#${res[1]}${res[3]}${res[5]}"
	}

	override fun toString() = when {
		alpha < 1f -> "rgba($redInt,$greenInt,$blueInt,${cssDecimalFormat.format(alpha)})"
		else -> toHexString()
	}


	private fun Int.twoDigitHex()
			= (if (this < 16) "0" else "") + Integer.toHexString(this)


	fun toHSL(): HSLValues {
		val max = Math.max(Math.max(red, green), blue)
		val min = Math.min(Math.min(red, green), blue)
		val avg = (max + min) / 2
		val hsl = HSLValues(avg, avg, avg)

		if (max == min) {
			// achromatic
			hsl.hue = 0f
			hsl.saturation = 0f
		}
		else {
			val d = max - min
			if (hsl.lightness > .5f)
				hsl.saturation = d / (2 - max - min)
			else
				hsl.saturation = d / (max + min)

			when (max) {
				red -> {
					hsl.hue = (green - blue) / d
					if (green < blue)
						hsl.hue += 6f
				}
				green -> hsl.hue = (blue - red) / d + 2
				blue -> hsl.hue = (red - green) / d + 4
			}

			hsl.hue /= 6f
		}

		return hsl
	}

	fun setHsl(hsl: HSLValues): Color {
		if (hsl.saturation == 0f) {
			// achromatic
			red = hsl.lightness
			green = hsl.lightness
			blue = hsl.lightness
		}
		else {
			val q = if (hsl.lightness < .5f)
				hsl.lightness * (1f + hsl.saturation)
			else
				hsl.lightness + hsl.saturation - hsl.lightness * hsl.saturation

			val p = 2f * hsl.lightness - q
			red = hue2rgb(p, q, hsl.hue + 1f / 3f)
			green = hue2rgb(p, q, hsl.hue)
			blue = hue2rgb(p, q, hsl.hue - 1f / 3f)
		}

		return this
	}

	private fun hue2rgb(p: Float, q: Float, _t: Float): Float {
		var t = _t
		if (t < 0f)
			t += 1f
		if (t > 1f)
			t -= 1f
		if (t < 1f / 6f)
			return p + (q - p) * 6f * t
		if (t < .5f)
			return q
		if (t < 2f / 3f)
			return p + (q - p) * (2f / 3f - t) * 6f
		return p
	}


	// Color adjustment (values should be between 0 and 1)

	fun lighten(dl: Float): Color {
		val hsl = toHSL()
		hsl.setLightnessSafe(hsl.lightness + dl)
		return setHsl(hsl)
	}

	fun darken(dl: Float): Color {
		val hsl = toHSL()
		hsl.setLightnessSafe(hsl.lightness - dl)
		return setHsl(hsl)
	}

	fun saturate(dl: Float): Color {
		val hsl = toHSL()
		hsl.setSaturationSafe(hsl.lightness + dl)
		return setHsl(hsl)
	}

	fun desaturate(dl: Float): Color {
		val hsl = toHSL()
		hsl.setSaturationSafe(hsl.lightness - dl)
		return setHsl(hsl)
	}


	companion object
	{
		fun fromRgb(red: Int, green: Int, blue: Int, alpha: Float = 1f)
				= Color(red.toFloat() / 255f, green.toFloat() / 255f, blue.toFloat() / 255f, alpha)

		fun fromHex(_s: String): Color? {
			val s = if (_s[0] == '#') _s.drop(1) else _s

			// In CSS4 the alpha channel comes last:
			// https://www.w3.org/TR/css-color-4/#hex-notation
			return when (s.length) {
				1 -> { // 0x00f
					val b = Integer.parseInt(s[0].toString(), 16)
					return fromRgb(0, 0, b * 16 + b)
				}
				2 -> { // 0x0f0
					val g = Integer.parseInt(s[0].toString(), 16)
					val b = Integer.parseInt(s[1].toString(), 16)
					return fromRgb(0, g * 16 + g, b * 16 + b)
				}
				3 -> {
					val r = Integer.parseInt(s[0].toString(), 16)
					val g = Integer.parseInt(s[1].toString(), 16)
					val b = Integer.parseInt(s[2].toString(), 16)
					return fromRgb(r * 16 + r, g * 16 + g, b * 16 + b)
				}
				4 -> {
					val r = Integer.parseInt(s[0].toString(), 16)
					val g = Integer.parseInt(s[1].toString(), 16)
					val b = Integer.parseInt(s[2].toString(), 16)
					val a = Integer.parseInt(s[3].toString(), 16)
					return fromRgb(r * 16 + r, g * 16 + g, b * 16 + b, 255f / a * 16 + a)
				}
				5 -> { // 0x0faabb
					val r = Integer.parseInt(s.substring(0, 1), 16)
					val g = Integer.parseInt(s.substring(1, 3), 16)
					val b = Integer.parseInt(s.substring(3, 5), 16)
					return fromRgb(r, g, b)
				}
				6 -> {
					val r = Integer.parseInt(s.substring(0, 2), 16)
					val g = Integer.parseInt(s.substring(2, 4), 16)
					val b = Integer.parseInt(s.substring(4, 6), 16)
					return fromRgb(r, g, b)
				}
				7 -> { // 0x0faabbcc
					val r = Integer.parseInt(s.substring(0, 1), 16)
					val g = Integer.parseInt(s.substring(1, 3), 16)
					val b = Integer.parseInt(s.substring(3, 5), 16)
					val a = Integer.parseInt(s.substring(5, 7), 16)
					return fromRgb(r, g, b, a / 255f)
				}
				8 -> {
					val r = Integer.parseInt(s.substring(0, 2), 16)
					val g = Integer.parseInt(s.substring(2, 4), 16)
					val b = Integer.parseInt(s.substring(4, 6), 16)
					val a = Integer.parseInt(s.substring(6, 8), 16)
					return fromRgb(r, g, b, a / 255f)
				}
				else -> null
			}
		}
		fun fromHex(value: Int) = fromHex(Integer.toHexString(value))

		fun fromHSL(hsl: HSLValues): Color {
			val color = Color(0f, 0f, 0f)
			color.setHsl(hsl)
			return color
		}
	}
}




