package zd.zero.waifu.motivator.plugin.personality

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.messages.MessageBusConnection
import zd.zero.waifu.motivator.plugin.ProjectConstants
import zd.zero.waifu.motivator.plugin.assets.WaifuAssetCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventCategory
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.onboarding.UpdateNotification
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.tools.AssetTools.attemptToShowCategories

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
    private var emotionalState = EmotionalState(Mood.NORMAL)

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

    // todo: don't show motivation for positive task if test is running

    private fun consumeEvent(motivationEvent: MotivationEvent) {
        emotionalState = deriveMood(motivationEvent)
        when (motivationEvent.type) {
            MotivationEvents.TEST -> showTestMotivation(motivationEvent)
            else -> {
            }
        }
    }

    private fun deriveMood(motivationEvent: MotivationEvent): EmotionalState {
        return when (motivationEvent.category) {
            MotivationEventCategory.POSITIVE,
            MotivationEventCategory.NEUTRAL -> shouldICalmDown(motivationEvent)
            MotivationEventCategory.NEGATIVE -> tryToRemainCalm(motivationEvent)
        }
    }

    private fun tryToRemainCalm(motivationEvent: MotivationEvent): EmotionalState {
        val observedFrustrationEvents = emotionalState.observedNegativeEvents + 1
        val newMood = if (observedFrustrationEvents >= WaifuMotivatorPluginState.getPluginState().eventsBeforeFrustration) {
            Mood.FRUSTRATED
        } else {
            emotionalState.mood
        }

        return emotionalState.copy(
            mood = newMood,
            observedNegativeEvents = observedFrustrationEvents
        )
    }

    // todo: frustration cooldown
    private fun shouldICalmDown(motivationEvent: MotivationEvent): EmotionalState {
        return when (motivationEvent.type) {
            MotivationEvents.IDLE ->
                EmotionalState(Mood.NORMAL)
            MotivationEvents.TASK -> {
                emotionalState.copy(observedNegativeEvents = emotionalState.observedPositiveEvents + 1)
            }
            else -> emotionalState
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

data class EmotionalState(
    val mood: Mood,
    val observedPositiveEvents: Int = 0,
    val observedNegativeEvents: Int = 0
)

enum class Mood {
    ENRAGED, FRUSTRATED, AGITATED, HAPPY, NORMAL, BORED
}
