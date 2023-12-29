package zd.zero.waifu.motivator.plugin.service

import com.intellij.openapi.application.ApplicationNamesInfo

object AppService {
    fun getApplicationName(): String = ApplicationNamesInfo.getInstance().fullProductNameWithEdition
}
