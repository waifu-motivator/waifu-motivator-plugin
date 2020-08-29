package zd.zero.waifu.motivator.plugin.tools

import org.assertj.core.api.Assertions
import org.junit.Test
import java.util.*

internal class BestGirl(val name: String)
class ToolBoxKtTest {

    @Test
    fun allOfShouldOnlyBePresentIfAllAreThere() {
        Assertions.assertThat(
            allOf(
                BestGirl("Ryuko").toOptional(),
                ("Ayy" to "Lmao").toOptional(),
                { println("Hello World") }.toOptional()
            ).isPresent
        ).isTrue()

        Assertions.assertThat(
            allOf(
                BestGirl("Ryuko").toOptional(),
                ("Ayy" to "Lmao").toOptional(),
                Optional.empty<() -> Unit>()
            ).isPresent
        ).isFalse()

        Assertions.assertThat(
            allOf(
                BestGirl("Trash Chan").toOptional().filter { it.name == "Ryuko" },
                ("Ayy" to "Lmao").toOptional(),
                { println("Hello World") }.toOptional()
            ).isPresent
        ).isFalse()

        Assertions.assertThat(
            allOf(
                BestGirl("Ryuko").toOptional(),
                Optional.empty<Pair<String, String>>(),
                { println("Hello World") }.toOptional()
            ).isPresent
        ).isFalse()
    }
}
