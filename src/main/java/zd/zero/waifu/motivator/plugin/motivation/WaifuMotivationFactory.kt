package zd.zero.waifu.motivator.plugin.motivation

import com.intellij.openapi.project.Project
import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.assets.MotivationAsset

interface WaifuMotivationFactory {
    fun constructMotivation(project: Project, motivation: MotivationAsset, config: AlertConfiguration): WaifuMotivation
}
