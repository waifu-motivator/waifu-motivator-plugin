package zd.zero.waifu.motivator.plugin.settings

import java.awt.Component
import javax.swing.JCheckBox
import javax.swing.JList
import javax.swing.ListCellRenderer

data class CheckListItem(
    val label: String,
    var isSelected: Boolean = false
)

class CheckboxListCellRenderer : JCheckBox(), ListCellRenderer<CheckListItem> {

    override fun getListCellRendererComponent(
        list: JList<out CheckListItem>,
        value: CheckListItem,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        componentOrientation = list.componentOrientation
        font = list.font
        background = list.background
        foreground = list.foreground
        setSelected(value.isSelected)
        isEnabled = list.isEnabled
        text = value.label
        return this
    }
}
