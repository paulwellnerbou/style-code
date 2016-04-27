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

    @Test
    public void generateDfl() throws Exception {
        new StyleCodeBuilder("custom-stuff/dfl.md")
                .useStylesAndScriptsFrom("http://w8-wsc09/")
                .build()
                .generate("out/dfl/");
    }
}
