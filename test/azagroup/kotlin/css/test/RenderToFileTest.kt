package azagroup.kotlin.css.test

import azagroup.kotlin.css.*
import org.junit.*
import org.junit.Assert.*
import java.io.File
import java.util.Date


class RenderToFileTest : ATest
{
	@Test fun test() {
		val file = File("AzaKotlinCSS_TestFile_${Date().time}.css")
		file.delete()

		val css1 = Stylesheet {
			div { color = 0xffffff }
		}
		val css2 = Stylesheet {
			a { top = 0 }
		}

		assertFalse(file.exists())

		css1.renderToFile(file)
		assertTrue(file.exists())
		assertEquals("div{color:#fff}", file.readText())

		css2.renderToFile(file.path)
		assertTrue(file.exists())
		assertEquals("a{top:0}", file.readText())

		file.delete()
	}
}
