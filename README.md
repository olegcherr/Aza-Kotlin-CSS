# AzaKotlinCSS

AzaKotlinCSS is a DSL (Domain-specific language) designed for writing CSS using Kotlin – the greatest programming language in the World! :boom::fire::+1:

### Installation

```gradle
repositories {
	jcenter()
}

dependencies {
	compile 'azagroup.kotlin:aza-kotlin-css:1.0'
}
```

### Usage

```kotlin
val css = Stylesheet {
	a {
		width = 10.px
		color = 0xffffff
		opacity = .8

		hover {
			color = 0xf2cacf
		}
	}
}

css.render()
// This will produce the following CSS:
// a{width:10px;color:#fff;opacity:.8}a:hover{color:#f2cacf}
```

In addition, there are 2 other rendering methods: `renderTo(StringBuilder)` and `renderToFile(File|String)`:

```kotlin
css.renderTo(builder)
// Will append CSS to an exsisting StringBuilder

css.renderToFile("style.css")
css.renderToFile(File("style.css"))
// Will render CSS to the file
```

## Selectors

AzaKotlinCSS is able to construct very complex selectors with a huge portion of syntax sugar.

```kotlin
Stylesheet {
	div { top = 0 }
	// div{top:0}

	a.hover { top = 0 }
	// a:hover{top:0}

	div and span { top = 0 }
	// div,span{top:0}

	li.nthChild(2) { top = 0 }
	// li:nth-child(2){top:0}

	input["disabled"] { top = 0 }
	// input[disabled]{top:0}
}
```

Below I'll show you more detailed examples of building CSS selectors using the DSL.

### Tags

```kotlin
Stylesheet {
	a { top = 0 }
	// a{top:0}

	div.span.a { top = 0 }
	// div span a{top:0}

	div and span and ul.li { top = 0 }
	// div,span,ul li{top:0}
}
```

### Classes and Ids

You can define class-selectors in several ways:

```kotlin
Stylesheet {
	".logo" { top = 0 }
	c("logo") { top = 0 }
}
// .logo{top:0}
```

Id-properties can be declared similarly:

```kotlin
Stylesheet {
	"#logo" { top = 0 }
	id("logo") { top = 0 }
}
// #logo{top:0}
```

Of cource you can combine them as you need:

```kotlin
Stylesheet {
	".class1.class2" { top = 0 }
	// .class1.class2{top:0}

	"#logo.class1" { top = 0 }
	// #logo.class1{top:0}

	"#logo"..".class1"..span { top = 0 }
	// #logo .class1 span{top:0}
}
```

### Pseudo classes and elements

```kotlin
Stylesheet {
	a.hover { top = 0 }
	// a:hover{top:0}

	"#logo".firstLetter { top = 0 }
	// #logo:first-letter{top:0}
}
```

By now AzaKotlinCSS is using single `:` for all the pseudo elements to be friendly with IE8. But in the future it will be replaced with `::`.

Some more examples:

```kotlin
Stylesheet {
	div.nthChild(2) { top = 0 }
	// div:nth-child(2){top:0}

	div.nthChild(EVEN) { top = 0 }
	// div:nth-child(even){top:0}

	any.not(lastChild) { top = 0 }
	// *:not(:last-child){top:0}

	"items".not(li) { top = 0 }
	// .items:not(li){top:0}
}
```

### Traversing

```kotlin
Stylesheet {
	div.span { top = 0 }
	div..span { top = 0 }
	div.children.span { top = 0 }
	// div span{top:0}

	div / span { top = 0 }
	div.child.span { top = 0 }
	// div>span{top:0}

	div % span { top = 0 }
	div.next.span { top = 0 }
	// div+span{top:0}

	div - span { top = 0 }
	div.nextAll.span { top = 0 }
	// div~span{top:0}
}
```

### Attributes

```kotlin
Stylesheet {
	input["disabled"] { top = 0 }
	// input[disabled]{top:0}

	input["type", "hidden"] { top = 0 }
	// input[type=hidden]{top:0}

	input["type", "hidden"]["disabled"] { top = 0 }
	// input[type=hidden][disabled]{top:0}

	a["href", startsWith, "http://"] { top = 0 }
	// a[href^="http://"]{top:0}

	"#logo"["type", "main"] { top = 0 }
	// #logo[type=main]{top:0}

	attr("disabled") { top = 0 }
	// [disabled]{top:0}
}
```

### Nesting

AzaKotlinCSS supports any nesting you can even imagine.

```kotlin
Stylesheet {
	div {
		width = AUTO

		a {
			color = 0xffffff

			hover {
				color = 0xff0000
			}
		}
	}
}
// div{width:auto}div a{color:#fff}div a:hover{color:#f00}
```

```kotlin
Stylesheet {
	div {
		color = 0xffffff

		b and strong {
			color = 0xff0000
		}

		child.span {
			color = 0x00ff00
		}
	}
}
// div{color:#fff}div b,div strong{color:#f00}div>span{color:#0f0}
```

## Dimensions

This DSL provides all the major dimension units: `px`, `em`, `percent`, `ex`, `inch`, `cm`, `mm`, `pt`, `pc`.

```kotlin
Stylesheet {
	width = AUTO
	// width:auto

	width = 10.px
	// width:10px

	width = .2.em
	// width:.2em

	width = 50.percent
	// width:50%

	width = 17.257.ex
	// width:17.257ex

	width = 1.55555.inch
	// width:1.55555in
}
```

AzaKotlinCSS also has the convenient `box` helper:

```kotlin
Stylesheet {
	padding = box(10, 5, 0, 20)
	// padding:10px 5px 0 20px

	padding = box(10.ex, 5.percent)
	// padding:10ex 5%

	padding = box(10, 10, 10, 10)
	// padding:10px

	padding = box(10)
	// padding:10px
}
```

As you can see, values without an explicitly defined dimension will be treated as `px`.

Also don't forget that you can use dimensions directly, without `box`. For example: `padding = 10.px`.

## Colors

To define a color you'll probably will be glad to use a hexademical notation of `Integer`. It's really convinient and looks almost exacly like CSS:

```kotlin
Stylesheet {
	a { color = 0xf2cacf }
	// a{color:#f2cacf}

	a { color = 0xffffff }
	// a{color:#fff}
}
```

Note that 3-digit hex-values will be considered as 6-digit hex with 3 zeros at the beginning.
For example `0xfff` will be treated as `0x000fff`. There is nothing we can do with it since, as you remember, it's a hex representation of `Integer`.

AzaKotlinCSS also provides `rgb(a)` and `hex` color-helpers:

```kotlin
Stylesheet {
	a { color = rgb(0,10,255) }
	// a{color:#000aff}

	a { color = rgba(255, 255, 255, .47) }
	// a{color:rgba(255,255,255,.47)}

	a { color = hex(0xf2cacf) }
	// a{color:#f2cacf}

	a { color = hex("#f00") }
	// a{color:#f00}
}
```

As the example shows, the `hex` helper supports shorthand color notations.

## @ At-rules

AzaKotlinCSS lets you create at-rules using (guess what!) the `at` helper:

```kotlin
Stylesheet {
	at("keyframes animation1") {
		"from" { top = 0 }
		"30%" { top = 50.px }
		"68%,72%" { top = 70.px }
		"to" { top = 100.px }
	}
}
// @keyframes animation1{from{top:0}30%{top:50px}68%,72%{top:70px}to{top:100px}}
```

```kotlin
Stylesheet {
	at("font-face") {
		fontFamily = "Bitstream Vera Serif Bold"
		src = url("VeraSeBd.ttf")
		fontWeight = BOLD
	}
}
// @font-face{font-family:Bitstream Vera Serif Bold;src:url(VeraSeBd.ttf);font-weight:bold}
```

For media queries the DSL provides convenient `media` helper, that joins all the passed arguments using the `and` operator:

```kotlin
Stylesheet {
	media("min-width: 100px", "orientation: landscape") {
		div { top = 0 }
	}
}
// @media (min-width: 100px) and (orientation: landscape){div{top:0}}
```

But of course, you can still use the `at` helper for complex rules:

```kotlin
Stylesheet {
	at("media not screen and (color), print and (color)") {
		div { top = 0 }
	}
}
// @media not screen and (color), print and (color){div{top:0}}
```

Also note that you can easily use `media` as a nested rule. It will be pushed to the top of its hierarchy and will have all the selectors it was called within:

```kotlin
Stylesheet {
	div {
		top = 0

		media("min-width: 100px") {
			top = 1
		}
	}
}
// div{top:0}@media (min-width: 100px){div{top:1}}
```

And one more useful tip. Didn't you forget that all this stuff is written on Kotlin? This means that you can bravely create any extension-methods you need.

For example, let's create a custom media-rule, to use it later in several places and be able to change the rule any time you want:

```kotlin
fun Stylesheet.myMediaQuery(body: Stylesheet.()->Unit)
		= media("min-width: 100px", "orientation: landscape").invoke(body)

Stylesheet {
	myMediaQuery {
		div { top = 0 }
	}
}
// @media (min-width: 100px) and (orientation: landscape){div{top:0}}
```

## Includes and mixins

To combine several `Stylesheet`s together you can use the `include` method:

```kotlin
val css = Stylesheet {
	a { color = 0xffffff }
}
val css_a = Stylesheet {
	a.hover { color = 0xff0000 }
}
val css_b = Stylesheet {
	a.active { color = 0x00ff00 }
}

css.include(css_a).include(css_b).render()
// a{color:#fff}a:hover{color:#f00}a:active{color:#0f0}
```

If you want to add an in-place mixin, then use the lowercased `stylesheet` helper:

```kotlin
val clrfix = stylesheet {
	zoom = 1
	after {
		content = " "
		display = BLOCK
		clear = BOTH
	}
}

Stylesheet {
	div {
		margin = 0
		clrfix()
	}
}

// div{margin:0;zoom:1}div::after{content:" ";display:block;clear:both}
```

## Issues

For now AzaKotlinCSS doesn't optimize the `and` selector, so be aware of possible code redundancies:

```kotlin
Stylesheet {
	b and strong {
		color = 0xffffff

		span {
			color = 0xff0000
		}
	}
}

// This will produce:
// b,strong{color:#fff}b span{color:#f00}strong span{color:#f00}

// But the better way would be:
// b,strong{color:#fff}b span,strong span{color:#f00}
```

## License

This software is released under the MIT License.
See [LICENSE.txt](LICENSE.txt) for details.
