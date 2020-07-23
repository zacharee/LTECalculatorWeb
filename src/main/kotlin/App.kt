import kotlinx.css.*
import kotlinx.css.properties.BoxShadow
import kotlinx.css.properties.BoxShadows
import kotlinx.css.properties.Time
import kotlinx.css.properties.transition
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.button
import styled.css
import styled.styledDiv
import styled.styledSpan
import kotlin.browser.document

external interface AppState : RState {
    val selectedBands: HashSet<BandInfo>
}

@ExperimentalUnsignedTypes
fun RBuilder.app(handler: RProps.() -> Unit): ReactElement {
    return child(App::class) {
        this.attrs(handler)
    }
}

@ExperimentalUnsignedTypes
class App : RComponent<RProps, AppState>() {
    init {
        state = object : AppState {
            override val selectedBands = HashSet<BandInfo>()
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                padding = "1em"
            }

            styledDiv {
                css {
                    height = LinearDimension("90vh")
                    width = LinearDimension("90vw")
                    backgroundColor = Color("#555555")
                    overflow = Overflow.hidden
                    display = Display.flex
                    borderRadius = LinearDimension("1.5em")
                    margin = "auto"
                    boxShadow = BoxShadows().apply {
                        plusAssign(
                            BoxShadow(
                            false,
                            LinearDimension("6px"),
                            LinearDimension("2px"),
                            LinearDimension("20px"),
                            LinearDimension("0px"),
                            Color("rgba(0,0,0,0.5)")
                        )
                        )
                    }
                }
                attrs.id = "content-wrapper"

                styledDiv {
                    attrs.id = "band-list-control-wrapper"

                    css {
                        width = LinearDimension("20em")
                        height = LinearDimension("100%")
                        overflowY = Overflow.auto
                        backgroundColor = Color("#292929")
                        boxSizing = BoxSizing.borderBox
                        padding = "1em"
                        margin = "auto"
                    }

                    styledDiv {
                        css {
                            display = Display.flex
                            flexDirection = FlexDirection.column
                            height = LinearDimension("100%")
                        }

                        styledDiv {
                            attrs.id = "controls"

                            css {
                                display = Display.flex
                                flexDirection = FlexDirection.row
                                justifyContent = JustifyContent.spaceEvenly
                                marginBottom = LinearDimension("0.5em")
                                flex(0.0, 0.0)
                            }

                            button {
                                attrs.onClickFunction = { onSelectAll() }

                                +"Select All"
                            }

                            button {
                                attrs.onClickFunction = { onDeselectAll() }

                                +"Deselect All"
                            }
                        }

                        styledDiv {
                            attrs.id = "band-list"

                            css {
                                this.setCustomProperty("flex-flow", FlexBasis("column wrap"))
                                overflowY = Overflow.auto

                                display = Display.flex
                                flexDirection = FlexDirection.column
                                flex(1.0, 1.0)
                            }

                            availableBands.forEach {
                                styledDiv {
                                    css {
                                        minHeight = LinearDimension("3em")
                                        borderRadius = LinearDimension("0.25em")
                                        display = Display.flex
                                        cursor = Cursor.pointer
                                        paddingLeft = LinearDimension("0.5em")
                                        paddingRight = LinearDimension("0.5em")

                                        flexDirection = FlexDirection.row

                                        this.setCustomProperty("flex-flow", FlexBasis("column wrap"))

                                        transition("all", Time("0.3s"))

                                        hover {
                                            backgroundColor = Color("#555555")
                                        }

                                        active {
                                            backgroundColor = Color("#555555")
                                        }
                                    }

                                    attrs.id = "band-${it.band}"

                                    var ref: Checkbox? = null

                                    attrs.onClickFunction = { _ ->
                                        ref?.apply { onCheckedChanged(it, !props.isChecked) }
                                    }

                                    checkBox {
                                        onCheckedChangedListener = { checked ->
                                            onCheckedChanged(it, checked)
                                        }

                                        isChecked = state.selectedBands.contains(it)

                                        this.ref<Checkbox> { box ->
                                            ref = box
                                        }

                                        id = "band-${it.band}-checkbox"
                                    }

                                    styledSpan {
                                        css {
                                            color = Color.white
                                            textAlign = TextAlign.left
                                            marginTop = LinearDimension.auto
                                            marginBottom = LinearDimension.auto
                                            verticalAlign = VerticalAlign.middle
                                            cursor = Cursor.pointer
                                            width = LinearDimension("100%")
                                        }

                                        +"${it.band}/${it.freq} MHz/${it.desc}"
                                    }
                                }
                            }
                        }
                    }
                }

                styledDiv {
                    attrs.id = "result-wrapper"

                    css {
                        padding = "1em"
                        width = LinearDimension("100%")
                    }

                    styledDiv {
                        attrs.id = "calculated-value"

                        css {
                            classes.add("result-element")
                            marginBottom = LinearDimension("0.25em")
                        }

                        +"0"
                    }

                    styledDiv {
                        attrs.id = "selected-band-list"

                        css {
                            classes.add("result-element")
                        }

                        +"None Selected"
                    }
                }
            }
        }
    }

    fun onCheckedChanged(bandInfo: BandInfo, checked: Boolean) {
        updateState {
            if (checked) {
                if (!selectedBands.contains(bandInfo)) {
                    selectedBands.add(bandInfo)
                }
            } else {
                if (selectedBands.contains(bandInfo)) {
                    selectedBands.remove(bandInfo)
                }
            }
        }

        onSelectionChanged()
    }

    fun onSelectAll() {
        updateState { selectedBands.addAll(availableBands) }
        onSelectionChanged()
    }

    fun onDeselectAll() {
        updateState { selectedBands.clear() }
        onSelectionChanged()
    }

    fun onSelectionChanged() {
        val result = calculateBandNumber(state.selectedBands.map { it.band })

        val resultView = document.getElementById("calculated-value") as HTMLDivElement
        resultView.textContent = result.toString()

        val bandsView = document.getElementById("selected-band-list") as HTMLDivElement
        bandsView.textContent = state.selectedBands.map { it.band }
            .sorted().joinToString(",").ifEmpty { "None Selected" }
    }

    fun updateState(block: AppState.() -> Unit) {
        state.block()
        forceUpdate()
    }
}