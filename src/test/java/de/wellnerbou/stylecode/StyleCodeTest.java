package de.wellnerbou.stylecode;

import de.wellnerbou.stylecode.StyleCode;
import de.wellnerbou.stylecode.StyleCodeBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class StyleCodeTest {

    @Test
    public void testGuessFilename() {
        StyleCode styleCode = new StyleCode("");
        Assertions.assertThat(styleCode.guessTargetFilename("src/main/resources/iframe.html.mustache")).isEqualTo("iframe.html");
    }
}
