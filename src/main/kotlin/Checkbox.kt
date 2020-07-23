import kotlinx.css.*
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import react.*
import styled.css
import styled.styledDiv

external interface CheckboxState : RState {
}

external interface CheckboxProps: RProps {
    var onCheckedChangedListener: ((Boolean) -> Unit)?
    var isChecked: Boolean

    var id: String?
}

fun RBuilder.checkBox(handler: CheckboxProps.() -> Unit): ReactElement {
    return child(Checkbox::class) {
        this.attrs(handler)
    }
}

class Checkbox : RComponent<CheckboxProps, CheckboxState>() {
    override fun RBuilder.render() {
        styledDiv {
            props.id?.let {
                attrs.id = it
            }

            css {
                classes.add("checkbox")
                marginRight = LinearDimension("0.5em")
                backgroundSize = "contain"

                if (props.isChecked) {
                    classes.add("checked")
                } else {
                    classes.remove("checked")
                }
            }

            attrs.onClickFunction = {
                props.onCheckedChangedListener?.invoke(!props.isChecked)
            }
        }
    }
}