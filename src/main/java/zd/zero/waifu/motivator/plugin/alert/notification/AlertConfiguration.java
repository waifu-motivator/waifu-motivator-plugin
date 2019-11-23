package zd.zero.waifu.motivator.plugin.alert.notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlertConfiguration {

    private boolean isAlertEnabled;

    private boolean isDisplayNotificationEnabled;

    private boolean isSoundAlertEnabled;

}
