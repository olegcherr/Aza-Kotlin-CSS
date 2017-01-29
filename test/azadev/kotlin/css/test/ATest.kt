package azadev.kotlin.css.test

import azadev.kotlin.css.Stylesheet
import org.junit.Assert.*


interface ATest
{
	fun testRender(expected: String, callback: Stylesheet.()->Unit) {
		val stylesheet = Stylesheet(callback)
		assertEquals(expected, stylesheet.render())
	}

	fun testRender(expected: String, stylesheet: Stylesheet) {
		assertEquals(expected, stylesheet.render())
	}
}
