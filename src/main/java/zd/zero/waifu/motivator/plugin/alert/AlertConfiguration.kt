package zd.zero.waifu.motivator.plugin.alert

data class AlertConfiguration(
    val isAlertEnabled: Boolean = false,
    val isDisplayNotificationEnabled: Boolean = false,
    val isSoundAlertEnabled: Boolean = false
)

object AlertConfigurationAllEnabled {
    fun create() = AlertConfiguration(
        isAlertEnabled = true,
        isDisplayNotificationEnabled = true,
        isSoundAlertEnabled = true
    )
}
