package zd.zero.waifu.motivator.plugin.settings

import com.intellij.ui.components.JBList
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

object SettingsTools {

    @JvmStatic
    fun buildPreferredCharacterListener(): MouseAdapter =
        object : MouseAdapter() {
            override fun mouseClicked(event: MouseEvent) {
                val characterList = event.source
                if (characterList !is JBList<*>) return

                val selectedCharacterIndex = characterList.locationToIndex(event.point)
                val itemsInCharacterList = characterList.model.size

                if (itemsInCharacterList < 0 || selectedCharacterIndex > itemsInCharacterList) return
                val item = characterList.model.getElementAt(selectedCharacterIndex)

                if (item !is CheckListItem) return
                item.isSelected = !item.isSelected
                characterList.repaint(characterList.getCellBounds(selectedCharacterIndex, selectedCharacterIndex))
            }
        }
}
