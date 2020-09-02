package zd.zero.waifu.motivator.plugin.personality

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.messages.MessageBusConnection
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents

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
//        Waifu
//        Emotion
//        Notification
//        Determination
//        Interface
object Wendi : Disposable {

    private lateinit var messageBusConnection: MessageBusConnection
    private val log = Logger.getInstance(this::class.java)
    private val emotionCore = EmotionCore()
    private val taskPersonalityCore = TaskPersonalityCore()
    private val idlePersonalityCore = IdlePersonalityCore()

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
