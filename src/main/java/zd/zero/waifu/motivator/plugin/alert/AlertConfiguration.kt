package zd.zero.waifu.motivator.plugin.alert

data class AlertConfiguration(
    val isAlertEnabled: Boolean = false,
    val isDisplayNotificationEnabled: Boolean = false,
    val isSoundAlertEnabled: Boolean = false
) {
    companion object {
        fun allEnabled(): AlertConfiguration =
            AlertConfiguration(
                isAlertEnabled = true,
                isDisplayNotificationEnabled = true,
                isSoundAlertEnabled = true
            )
    }
}
