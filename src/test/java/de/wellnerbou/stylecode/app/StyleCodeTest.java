package de.wellnerbou.stylecode.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class StyleCodeTest {

    @Test
    public void generate() throws Exception {
        new StyleCodeBuilder("src/test/resources/markdown/paulwellnerbou.md")
                .useStylesAndScriptsFrom("http://paul.wellnerbou.de")
                .build()
                .generate("out/paulwellnerbou/");
    }

    @Test
    public void getHtmlFromMarkdown() throws Exception {

    }

    @Test
    public void writeHtmlToOutDirectory() throws Exception {

    }

    @Test
    public void getTemplateReader() throws Exception {

    }

    @Test
    public void copyAdditionalResources() throws Exception {

    }
}
