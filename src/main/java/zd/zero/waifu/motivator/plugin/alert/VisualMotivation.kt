package zd.zero.waifu.motivator.plugin.alert

import zd.zero.waifu.motivator.plugin.alert.notification.AlertConfiguration
import zd.zero.waifu.motivator.plugin.player.WaifuSoundPlayer

class VisualMotivation(
    waifuNotification: WaifuNotification,
    waifuSoundPlayer: WaifuSoundPlayer,
    config: AlertConfiguration
) : BaseMotivation(waifuNotification, waifuSoundPlayer, config)
