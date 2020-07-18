package zd.zero.waifu.motivator.plugin.alert

abstract class BaseMotivation: WaifuMotivation {

    override fun motivate() {
        if (isAlertEnabled && isDistractionAllowed) {
            if (isDisplayNotificationEnabled) displayNotification()
            if (isSoundAlertEnabled) soundAlert()
        }
    }
}
