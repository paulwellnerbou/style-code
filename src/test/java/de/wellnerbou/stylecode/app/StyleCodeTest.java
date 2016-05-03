package de.wellnerbou.stylecode.app;

import de.wellnerbou.stylecode.StyleCodeBuilder;
import org.junit.Test;

public class StyleCodeTest {

    @Test
    public void generate() throws Exception {
        new StyleCodeBuilder("src/test/resources/markdown/paulwellnerbou.md")
                .useStylesAndScriptsFrom("http://paul.wellnerbou.de")
                .build()
                .generate("out/paulwellnerbou/");
    }
}
