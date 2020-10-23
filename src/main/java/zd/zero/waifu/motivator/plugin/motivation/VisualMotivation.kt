package zd.zero.waifu.motivator.plugin.motivation

import zd.zero.waifu.motivator.plugin.alert.AlertConfiguration
import zd.zero.waifu.motivator.plugin.alert.WaifuNotification
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayer

class VisualMotivation(
    waifuNotification: WaifuNotification,
    waifuSoundPlayer: WaifuSoundPlayer,
    config: AlertConfiguration
) : BaseMotivation(waifuNotification, waifuSoundPlayer, config)
