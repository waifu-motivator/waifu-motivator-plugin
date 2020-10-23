package zd.zero.waifu.motivator.plugin.personality

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.messages.MessageBusConnection
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvent
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEventListener
import zd.zero.waifu.motivator.plugin.motivation.event.MotivationEvents
import zd.zero.waifu.motivator.plugin.personality.core.IdlePersonalityCore
import zd.zero.waifu.motivator.plugin.personality.core.ResetCore
import zd.zero.waifu.motivator.plugin.personality.core.TaskPersonalityCore
import zd.zero.waifu.motivator.plugin.personality.core.emotions.*
import zd.zero.waifu.motivator.plugin.settings.PluginSettingsListener
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorPluginState
import zd.zero.waifu.motivator.plugin.settings.WaifuMotivatorState
import zd.zero.waifu.motivator.plugin.tools.AlarmDebouncer
import zd.zero.waifu.motivator.plugin.tools.toOptional
import java.util.Optional

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
object Wendi : Disposable, EmotionalMutationActionListener {

    private lateinit var messageBusConnection: MessageBusConnection
    private lateinit var emotionCore: EmotionCore
    private val taskPersonalityCore = TaskPersonalityCore()
    private val idlePersonalityCore = IdlePersonalityCore()
    private val resetCore = ResetCore()
    private const val DEBOUNCE_INTERVAL = 80
    private val singleEventDebouncer = AlarmDebouncer<MotivationEvent>(DEBOUNCE_INTERVAL)
    private val idleEventDebouncer = AlarmDebouncer<MotivationEvent>(DEBOUNCE_INTERVAL)
    fun initialize() {
        if (this::messageBusConnection.isInitialized.not()) {
            messageBusConnection = ApplicationManager.getApplication().messageBus.connect()

            emotionCore = EmotionCore(WaifuMotivatorPluginState.getPluginState())

            messageBusConnection.subscribe(PluginSettingsListener.PLUGIN_SETTINGS_TOPIC, object : PluginSettingsListener {
                override fun settingsUpdated(newPluginState: WaifuMotivatorState) {
                    this@Wendi.emotionCore = emotionCore.updateConfig(newPluginState)
                }
            })

            messageBusConnection.subscribe(EMOTIONAL_MUTATION_TOPIC, this)

            messageBusConnection.subscribe(MotivationEventListener.TOPIC, object : MotivationEventListener {
                override fun onEventTrigger(motivationEvent: MotivationEvent) {
                    when (motivationEvent.type) {
                        MotivationEvents.IDLE ->
                            idleEventDebouncer.debounceAndBuffer(motivationEvent) {
                                consumeEvents(it)
                            }
                        else -> singleEventDebouncer.debounce {
                            consumeEvent(motivationEvent)
                        }
                    }
                }
            })
        }
    }

    val currentMood: Optional<Mood>
        get() = if (this::emotionCore.isInitialized) emotionCore.currentMood.toOptional()
        else Optional.empty()

    private fun consumeEvents(bufferedMotivationEvents: List<MotivationEvent>) {
        val emotionalState = emotionCore.deriveMood(bufferedMotivationEvents.first())
        bufferedMotivationEvents.forEach { motivationEvent -> reactToEvent(motivationEvent, emotionalState) }
    }

    private fun consumeEvent(motivationEvent: MotivationEvent) {
        val currentMood = emotionCore.deriveMood(motivationEvent)
        reactToEvent(motivationEvent, currentMood)
        publishMood(currentMood)
    }

    override fun onAction(emotionalMutationAction: EmotionalMutationAction) {
        val mutatedMood = emotionCore.mutateMood(emotionalMutationAction)
        reactToMutation(emotionalMutationAction)
        publishMood(mutatedMood)
    }

    private fun reactToMutation(
        emotionalMutationAction: EmotionalMutationAction
    ) {
        if (emotionalMutationAction.type == EmotionalMutationType.RESET)
            resetCore.processMutationEvent(emotionalMutationAction)
    }

    private fun publishMood(currentMood: Mood) {
        ApplicationManager.getApplication().messageBus
            .syncPublisher(EMOTION_TOPIC)
            .onDerivedMood(currentMood)
    }

    private fun reactToEvent(motivationEvent: MotivationEvent, emotionalState: Mood) {
        when (motivationEvent.type) {
            MotivationEvents.TEST,
            MotivationEvents.TASK -> taskPersonalityCore.processMotivationEvent(motivationEvent, emotionalState)
            MotivationEvents.IDLE -> idlePersonalityCore.processMotivationEvent(motivationEvent, emotionalState)
            else -> {
            }
        }
    }

    override fun dispose() {
        if (this::messageBusConnection.isInitialized) {
            messageBusConnection.dispose()
            singleEventDebouncer.dispose()
            idleEventDebouncer.dispose()
        }
    }
}
