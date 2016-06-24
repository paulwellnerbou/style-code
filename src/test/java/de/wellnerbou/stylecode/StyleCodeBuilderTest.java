package de.wellnerbou.stylecode;

import org.junit.Test;

public class StyleCodeBuilderTest {

    @Test
    public void buildWithoutException() throws Exception {
        new StyleCodeBuilder("src/test/resources/markdown/paulwellnerbou.md")
                .useStylesAndScriptsFrom("http://paul.wellnerbou.de")
                .build()
                .generate("out/paulwellnerbou/");
    }

}