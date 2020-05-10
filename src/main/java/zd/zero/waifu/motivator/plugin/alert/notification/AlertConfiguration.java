package zd.zero.waifu.motivator.plugin.alert.notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlertConfiguration {

    private final boolean isAlertEnabled;

    private final boolean isDisplayNotificationEnabled;

    private final boolean isSoundAlertEnabled;

}
