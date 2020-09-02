package zd.zero.waifu.motivator.plugin.personality

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.messages.MessageBusConnection
import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification
import zd.zero.waifu.motivator.plugin.tools.AssetTools.attemptToShowCategories

//%%%%%%%%%%%%%%%   ,     ,                                     #@@@@&%%%%%%%%%%%%
//%%%%%%%%%%@@@@@@@@@@@& /                                            @@@@%%%%%%%%
//%%%%%%%@@&%*    .                                                    /%%%@@%%%%%
//%%%%%@%%%%                                                             %%%%%@%%%
//%%%@%%%%                                                                 %%%%%@%
//%%%%%%%                                                                   %%%%%%
//%%%%%.                                                       .             ,%%%%
//%%%%                                                                         %%%
//%%%           @@@@@@@/&.                                  @@@@@@@@@.          %%
//%%        .@@#         @@                              @@    #     .@@        %%
//%.       @#     %%     %  @                          ,  %,    (%%     @@      %%
//%.     %@    #%%%%%%%%%%%%                            %%%%%%%%%%%%%     @     %%
//%*    #     %%%%%%%%%%%%%%%                          %%%%%%%%%%%%%%%%    &    %%
//%#         %%%%%%%%%%%%%%%%%                        %%%%%%%%%%%%%%%%%%    @   %%
//%%        %%%%%%%%%%%%%%%%%%(                       %%%%%%%%%%%%%%%%%%       *%%
//%%  .     #%%%%%%%%%%%%%%%%%/                       %%%%%%%%%%%%%%%%%%     / #%%
//%%  @      %%%%%%%%%%%%%%%%%                        ,%%%%%%%%%%%%%%%%        %%%
//%%*         %%%%%%%%%%%%%%%                           %%%%%%%%%%%%%%         %%%
//%%%           %%%%%%%%%%.                               .%%%%%%%%/          #%%%
//%%%                                                                         %%%%
//%%%(   ........                                                   ..........%%%%
//%%%%     ..........                                              ......    %%%%%
//%%%%%                                                                     /%%%%%
//%%%%%*                      ,(((*****************(((                      %%%%%%
//%%%%%%.                    (((********************/(                     %%%%%%%
//%%%%%%%/                    (**********************(                   ,%%%%%%%%
//%%%%%%%%%%                  (*****...............**                 /%%%%%%%%%%%
//%%%%%%%%%%%%%%                (..................               *%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%.               ,/,........               %%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%*                               #%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%..                     ....,%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%.......           .........(%%%%%%%%%%%%%%%%%%%%%%%%%
//        Waifu
//        Emotion
//        Notification
//        Determination
//        Interface
object Wendi : Disposable {

    private lateinit var messageBusConnection: MessageBusConnection

    fun initialize() {
        if (this::messageBusConnection.isInitialized.not()) {
            messageBusConnection = ApplicationManager.getApplication().messageBus.connect()

            messageBusConnection.subscribe(MotivationEventListener.TOPIC, object : MotivationEventListener {
                override fun onEventTrigger(motivationEvent: MotivationEvent) {
                    consumeEvent(motivationEvent)
                }
            })
        }
    }

    private fun consumeEvent(motivationEvent: MotivationEvent) {
        when (motivationEvent.type) {
            MotivationEvents.TEST -> showTestMotivation(motivationEvent)
            else -> {
            }
        }
    }

    private fun showTestMotivation(motivationEvent: MotivationEvent) {
        val project = motivationEvent.project
        attemptToShowCategories(
            project,
            motivationEvent.alertConfigurationSupplier,
            {
                UpdateNotification.sendMessage(
                    "'${motivationEvent.title}' Unavailable Offline",
                    ProjectConstants.WAIFU_UNAVAILABLE_MESSAGE,
                    project
                )
            },
            *getRelevantCategories(motivationEvent)
        )
    }

    private fun getRelevantCategories(motivationEvent: MotivationEvent): Array<out WaifuAssetCategory> {
        return when (motivationEvent.category) {
            MotivationEventCategory.POSITIVE -> arrayOf(
                WaifuAssetCategory.CELEBRATION
            )
            MotivationEventCategory.NEGATIVE -> arrayOf(
                WaifuAssetCategory.DISAPPOINTMENT,
                WaifuAssetCategory.SHOCKED
            )
            MotivationEventCategory.NEUTRAL -> arrayOf()
        }

    }

    override fun dispose() {
        if (this::messageBusConnection.isInitialized) {
            messageBusConnection.dispose()
        }
    }
}
