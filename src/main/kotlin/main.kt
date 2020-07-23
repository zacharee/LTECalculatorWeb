import kotlinx.css.*
import kotlinx.css.properties.Time
import kotlinx.css.properties.transition
import react.RBuilder
import react.dom.render
import styled.*
import kotlin.browser.document

//val selectedBands = HashSet<BandInfo>()
val availableBands = LinkedHashSet<BandInfo>()

fun CSSBuilder.withSelector(selector: String, block: RuleSet) = selector(block)

@ExperimentalUnsignedTypes
fun main() {
    document.bgColor = "#333333"
    loadBandInfo()

    injectGlobal {
        withSelector(".checkbox") {
            filter = "invert(72%) sepia(0%) saturate(335%) hue-rotate(191deg) brightness(88%) contrast(74%);"
            backgroundImage = Image("url(/unchecked.svg)")
            backgroundRepeat = BackgroundRepeat.noRepeat
            width = LinearDimension("1.5em")
            height = width
            border = "none"

            margin = "auto 0"

            transition("all", Time("0.3s"))
        }

        withSelector(".checkbox.checked") {
            filter = "invert(71%) sepia(98%) saturate(490%) hue-rotate(73deg) brightness(100%) contrast(94%)"
            backgroundImage = Image("url(/checked.svg) !important")
        }

        withSelector(".result-element") {
            backgroundColor = Color("#292929")
            color = Color.white

            padding = "2rem"
            margin = "auto 0"

            borderRadius = LinearDimension("1rem")
            wordBreak = WordBreak.breakAll

            textAlign = TextAlign.center
            justifyContent = JustifyContent.center
        }

        withSelector("#calculated-value") {
            fontSize = LinearDimension("20pt")
        }

        withSelector("#selected-band-list") {
            fontSize = LinearDimension("16pt")
        }

        withSelector("#content-wrapper") {
            flexDirection = FlexDirection.row
        }

        withSelector("#result-wrapper") {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.spaceBetween
        }

        media("screen and (max-width: 680px)") {
            withSelector("#content-wrapper") {
                flexDirection = FlexDirection.columnReverse
            }

            withSelector("#band-list-control-wrapper") {
                width = LinearDimension("100%")
                height = LinearDimension.none
                flex(0.0, 1.0)
            }

            withSelector("#result-wrapper") {
                width = LinearDimension.auto
                overflowY = Overflow.auto
                height = LinearDimension.fillAvailable
                flex(1.0, 1.0)
            }
        }

        button {
            borderRadius = LinearDimension("0.25em")
            backgroundColor = Color("#1ef760")
            minWidth = LinearDimension("4.5em")
            height = LinearDimension("3em")
            border = "none"
            padding = "0.5em 1em"
            fontSize = LinearDimension("0.75em")
            cursor = Cursor.pointer

            transition("all", Time("0.3s"))

            hover {
                backgroundColor = Color("#5eff8f")
            }

            active {
                backgroundColor = Color("#1ef760")
            }
        }

        input {
            backgroundColor = Color("#393939")
            border = "none"
            borderRadius = LinearDimension("0.1em")
            minHeight = LinearDimension("3em")
            color = Color.white
        }

        textarea {
            backgroundColor = Color("#393939")
            border = "none"
            borderRadius = LinearDimension("0.1em")
            minHeight = LinearDimension("3em")
            color = Color.white
        }
    }

    val root = document.getElementById("root")
    render(root) {
        app {  }
    }
}

fun loadBandInfo() {
    bands.mapTo(availableBands) {
        BandInfo.getFromString(it)
    }
}