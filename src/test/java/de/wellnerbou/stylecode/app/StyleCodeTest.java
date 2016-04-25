package de.wellnerbou.stylecode.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class StyleCodeTest {

    private StyleCode styleCode = new StyleCode();;

    @Test
    public void generate() throws Exception {
        styleCode.generate("src/test/resources/markdown/paulwellnerbou.md", "out", "http://paul.wellnerbou.de");
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
