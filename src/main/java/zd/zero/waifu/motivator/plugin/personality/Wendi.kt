package zd.zero.waifu.motivator.plugin.personality

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.messages.MessageBusConnection
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.personality.core.IdlePersonalityCore
import zd.zero.waifu.motivator.plugin.personality.core.TaskPersonalityCore
import zd.zero.waifu.motivator.plugin.personality.core.emotions.EmotionCore
import zd.zero.waifu.motivator.plugin.settings.PluginSettingsListener
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState

//                                   Waifu
//                                   Emotion
//                                   Notification
//                                   Determination
//                                   Interface
// %%%%%%%%%%%%%%%%%
// %%%%%%%%%%%%%%%   ,     ,                                     #@@@@&%%%%%%%%%%%%
// %%%%%%%%%%@@@@@@@@@@@& /                                            @@@@%%%%%%%%
// %%%%%%%@@&%*    .                                                    /%%%@@%%%%%
// %%%%%@%%%%                                                             %%%%%@%%%
// %%%@%%%%                                                                 %%%%%@%
// %%%%%%%                                                                   %%%%%%
// %%%%%.                                                       .             ,%%%%
// %%%%                                                                         %%%
// %%%           @@@@@@@/&.                                  @@@@@@@@@.          %%
// %%        .@@#         @@                              @@    #     .@@        %%
// %.       @#     %%     %  @                          ,  %,    (%%     @@      %%
// %.     %@    #%%%%%%%%%%%%                            %%%%%%%%%%%%%     @     %%
// %*    #     %%%%%%%%%%%%%%%                          %%%%%%%%%%%%%%%%    &    %%
// %#         %%%%%%%%%%%%%%%%%                        %%%%%%%%%%%%%%%%%%    @   %%
// %%        %%%%%%%%%%%%%%%%%%(                       %%%%%%%%%%%%%%%%%%       *%%
// %%  .     #%%%%%%%%%%%%%%%%%/                       %%%%%%%%%%%%%%%%%%     / #%%
// %%  @      %%%%%%%%%%%%%%%%%                        ,%%%%%%%%%%%%%%%%        %%%
// %%*         %%%%%%%%%%%%%%%                           %%%%%%%%%%%%%%         %%%
// %%%           %%%%%%%%%%.                               .%%%%%%%%/          #%%%
// %%%                                                                         %%%%
// %%%(   ........                                                   ..........%%%%
// %%%%     ..........                                              ......    %%%%%
// %%%%%                                                                     /%%%%%
// %%%%%*                      ,(((*****************(((                      %%%%%%
// %%%%%%.                    (((********************/(                     %%%%%%%
// %%%%%%%/                    (**********************(                   ,%%%%%%%%
// %%%%%%%%%%                  (*****...............**                 /%%%%%%%%%%%
// %%%%%%%%%%%%%%                (..................               *%%%%%%%%%%%%%%%
// %%%%%%%%%%%%%%%%%%.               ,/,........               %%%%%%%%%%%%%%%%%%%%
// %%%%%%%%%%%%%%%%%%%%%%%*                               #%%%%%%%%%%%%%%%%%%%%%%%%
// %%%%%%%%%%%%%%%%%%%%%%%%%%%..                     ....,%%%%%%%%%%%%%%%%%%%%%%%%%
// %%%%%%%%%%%%%%%%%%%%%%%%%%%.......           .........(%%%%%%%%%%%%%%%%%%%%%%%%%
object Wendi : Disposable {

    private lateinit var messageBusConnection: MessageBusConnection
    private val log = Logger.getInstance(this::class.java)
    private lateinit var emotionCore: EmotionCore
    private val taskPersonalityCore = TaskPersonalityCore()
    private val idlePersonalityCore = IdlePersonalityCore()

    fun initialize() {
        if (this::messageBusConnection.isInitialized.not()) {
            messageBusConnection = ApplicationManager.getApplication().messageBus.connect()

            emotionCore = EmotionCore(WaifuMotivatorPluginState.getPluginState())

            messageBusConnection.subscribe(PluginSettingsListener.PLUGIN_SETTINGS_TOPIC, object : PluginSettingsListener {
                override fun settingsUpdated(newPluginState: WaifuMotivatorState) {
                    this@Wendi.emotionCore = emotionCore.updateConfig(newPluginState)
                }
            })

            messageBusConnection.subscribe(MotivationEventListener.TOPIC, object : MotivationEventListener {
                override fun onEventTrigger(motivationEvent: MotivationEvent) {
                    consumeEvent(motivationEvent)
                }
            })
        }
    }

    private fun consumeEvent(motivationEvent: MotivationEvent) {
        val emotionalState = emotionCore.deriveMood(motivationEvent)
        when (motivationEvent.type) {
            MotivationEvents.TEST,
            MotivationEvents.TASK -> taskPersonalityCore.processMotivationEvent(motivationEvent, emotionalState)
            MotivationEvents.IDLE -> idlePersonalityCore.processMotivationEvent(motivationEvent, emotionalState)
        }
    }

    override fun dispose() {
        if (this::messageBusConnection.isInitialized) {
            messageBusConnection.dispose()
        }
    }
}
