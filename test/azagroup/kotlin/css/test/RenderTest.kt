package azagroup.kotlin.css.test

import azagroup.kotlin.css.*
import azagroup.kotlin.css.colors.clr
import azagroup.kotlin.css.dimens.*
import org.junit.*
import org.junit.Assert.*


// CSS Selectors:
// http://www.w3schools.com/cssref/css_selectors.asp

// LESS Compiler:
// http://winless.org/online-less-compiler

class RenderTest
{
	@Test fun selectors() {
		testRender("", {
			div {}
		})
		testRender("div{width:1}a{width:1}", {
			div { width = 1 }
			a { width = 1 }
		})
		testRender("div a{width:1}a:hover{width:1}", {
			div.a { width = 1 }
			a.hover { width = 1 }
		})
		testRender("div span{width:1}div span a{width:1}", {
			div.span {
				width = 1
				a { width = 1 }
			}
		})
		testRender("div:hover{width:1}div *:hover{width:1}", {
			div {
				hover { width = 1 }
				any.hover { width = 1 }
			}
		})
		testRender("div{width:1}div:hover{width:1}div:hover div{width:1}", {
			div {
				width = 1
				hover {
					width = 1
					div { width = 1 }
				}
			}
		})
	}

	@Test fun selectors_multiple() {
		testRender("a,div:hover,span{width:1}a,div:hover,span{width:1}", {
			a and div.hover and span { width = 1 }
			(a and div.hover and span) { width = 1 }
		})
		testRender("a:hover,div{width:1}a+span,div{width:1}a+span+a,div{width:1}", {
			a.hover and div { width = 1 }
			a % span and div { width = 1 }
			a % span % a and div { width = 1 }
		})
		testRender("div>a,div>span{width:1}div>a:hover,div>span:hover{width:1}div>a:hover,div>span:hover{width:1}", {
			div / (a and span) { width = 1 }
			div / (a and span).hover { width = 1 }
			(div / (a and span)).hover { width = 1 }
		})
		testRender("a,div,span{width:1}a:hover{width:1}a:hover a,a:hover div{width:1}div:hover{width:1}div:hover a,div:hover div{width:1}span:hover{width:1}span:hover a,span:hover div{width:1}", {
			a and div and span {
				width = 1
				hover {
					width = 1
					(a and div) { width = 1 }
				}
			}
		})

		testRender("a:hover{width:1}div:hover{width:1}span:hover{width:1}", {
			a and div and span {
				hover { width = 1 }
			}
		})
		testRender("a:hover,div:hover,span:hover{width:1}", {
			(a and div and span).hover { width = 1 }
		})
	}

	@Test fun selectors_withoutSpaces() {
		testRender("div>a{width:1}span>a{width:1}div>a{width:1}span>a{width:1}", {
			div.child.a { width = 1 }
			span / a { width = 1 }
			div and span {
				child.a { width = 1 }
			}
		})

		testRender("div+a{width:1}span+a{width:1}div+a{width:1}span+a{width:1}", {
			div.next.a { width = 1 }
			span % a { width = 1 }
			(div and span) {
				next.a { width = 1 }
			}
		})

		testRender("div span a,div>a{width:1}", {
			div {
				(span and child).a { width = 1 }
			}
		})

		testRender("div>a:hover,div>span:hover{width:1}", {
			div.child {
				(a and span).hover { width = 1 }
			}
		})
		testRender("div>a+span:hover{width:1}", {
			div.child {
				a % span.hover { width = 1 }
			}
		})
	}

	@Test fun selectors_classesAndIds() {
		testRender(".class1{width:1}#id1{width:2}") {
			c("class1") { width = 1 }
			id("id1") { width = 2 }
		}
		testRender("div#id1>a.class1{width:1}") {
			div.id("id1") {
				child.a.c("class1") { width = 1 }
			}
		}

		testRender(".class1{width:1}") {
			"class1" { width = 1 }
		}
		testRender("#id1{width:1}") {
			"#id1" { width = 1 }
		}
		testRender("div,.class1{width:1}div a{width:2}.class1 a{width:2}") {
			div and "class1" {
				width = 1
				a { width = 2 }
			}
		}

		testRender(".class1>a{width:1}") {
			".class1" / a { width = 1 }
		}
		testRender(".class1+a{width:1}") {
			".class1" % a { width = 1 }
		}
		testRender(".class1~a{width:1}") {
			".class1" - a { width = 1 }
		}
	}

	@Test fun selectors_fn() {
		testRender("div:not(span){width:1}div:not(a:hover) span{width:1}", {
			div.not(span) { width = 1 }
			div.not(a.hover).span { width = 1 }
		})
		testRender("div:nth-child(even){width:1}div a:nth-child(2),span{width:1}", {
			div.nthChild("even") { width = 1 }
			div.a.nthChild(2) and span { width = 1 }
		})
	}

	@Test fun selectors_attributes() {
		testRender("input[disabled]{width:1}input[disabled=true]:hover{width:1}", {
			input["disabled"] { width = 1 }
			input["disabled", true].hover { width = 1 }
		})
		testRender("input[disabled],textarea[disabled]{width:1}input[disabled][type=hidden],textarea[disabled][type=hidden]{width:1}", {
			(input and textarea)["disabled"] { width = 1 }
			(input and textarea)["disabled"]["type", "hidden"] { width = 1 }
		})
		testRender("input[disabled][type*=dd],textarea[type*=dd]{width:1}", {
			(input["disabled"] and textarea)["type", contains, "dd"] { width = 1 }
		})
	}

	@Test fun selectors_atRules() {
		testRender("@media (min-width: 100px){div{width:1}}@media (min-width: 100px) and (max-width: 200px){div{width:2}}") {
			media("min-width: 100px") {
				div { width = 1 }
			}
			media("min-width: 100px", "max-width: 200px") {
				div { width = 2 }
			}
		}
		testRender("input{width:1}@media (min-width: 100px){div{width:2}div a{width:3}span{width:4}}textarea{width:5}") {
			input { width = 1 }
			media("min-width: 100px") {
				div {
					width = 2
					a { width = 3 }
				}
				span { width = 4 }
			}
			textarea { width = 5 }
		}

		testRender("div{width:1}@media (min-width: 100px){div a{width:2}div:hover{width:3}div *:hover{width:4}div *:hover:not(1){width:5}}") {
			div {
				width = 1
				media("min-width: 100px") {
					a { width = 2 }
					hover { width = 3 }
					any.hover {
						width = 4
						not(1) { width = 5 }
					}
				}
			}
		}
		testRender("@media (min-width: 100px){div{width:1}}@media (min-width: 100px){a{width:1}}") {
			(div and a) {
				media("min-width: 100px") {
					width = 1
				}
			}
		}

		testRender("@media all and (not handheld){div{width:1}}") {
			mediaRaw("all and (not handheld)") {
				div { width = 1 }
			}
		}

		testRender("@-webkit-keyframes anim1{from{top:0px}to{top:100px}}@keyframes anim2{0%{top:0px}100%{top:100px}}") {
			at("-webkit-keyframes anim1") {
				custom("from") { top = 0.px }
				custom("to") { top = 100.px }
			}
			"@keyframes anim2" {
				custom("0%") { top = 0.px }
				custom("100%") { top = 100.px }
			}
		}
	}


	@Test fun properties() {
		testRender("a{width:auto;font-size:14px;opacity:.2}") {
			a {
				width = auto
				color = null
				fontSize = 14.px
				background = null
				opacity = .2
			}
		}
		testRender("div{background:-moz-linear-gradient(top, #000 0%, #fff 100%);background:-webkit-linear-gradient(top, #000 0%, #fff 100%);background:linear-gradient(top bottom, #000 0%, #fff 100%)}") {
			div {
				background = "-moz-linear-gradient(top, #000 0%, #fff 100%)" // FF3.6+
				background = "-webkit-linear-gradient(top, #000 0%, #fff 100%)" // Chrome10+, Safari5.1+
				background = "linear-gradient(top bottom, #000 0%, #fff 100%)" // W3C
			}
		}
	}


	@Test fun dimensions() {
		testRender("a{width:auto}a{width:1px}a{width:.2em}a{width:50%}a{width:17.257ex}a{width:1.55555in}") {
			a { width = auto }
			a { width = 1.px }
			a { width = .2.em }
			a { width = 50f.percent }
			a { width = 17.257.ex }
			a { width = 1.55555f.inch }
		}
	}


	@Test fun colors() {
		assertEquals("#fff",                    clr("#fff").toString())
		assertEquals("#fff",                    clr("#ffffff").toString())
		assertEquals("rgba(0,10,255,0)",        clr(0,10,255,0).toString())
		assertEquals("rgba(255,255,255,0.47)",  clr(255,255,255,120).toString())
		assertEquals("rgba(0,0,0,0.019)",       clr(0,0,0,5).toString())

		testRender("a{color:#fff}a{color:rgba(255,100,0,0.196)}a{color:#fff}a{color:#f2cacf}") {
			a { color = clr("#fff") }
			a { color = clr(255,100,0,50) }
			a { color = 0xfff }
			a { color = 0xf2cacf }
		}
	}


	private fun testRender(expected: String, callback: Stylesheet.()->Unit) {
		val stylesheet = Stylesheet(callback)
		assertEquals(expected, stylesheet.toString())
	}
}
