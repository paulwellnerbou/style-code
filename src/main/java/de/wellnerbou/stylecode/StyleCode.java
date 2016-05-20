package de.wellnerbou.stylecode;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyleCode {

    private String indexHtmlTemplate;
    private String iframeHtmlTemplate;
    private String sourceMarkdownFile;
    private ResourceGetter resourceGetter;

    public StyleCode(final String sourceMarkdownFile) {
        this.sourceMarkdownFile = sourceMarkdownFile;
    }

    public void generate(final String outputDirectory) throws IOException {
        File d = new File(outputDirectory);
        if (!d.isDirectory()) {
            d.mkdirs();
        }
        File md = new File(sourceMarkdownFile);
        writeHtmlFilesToOutDirectory(d, getHtmlFromMarkdown(new FileInputStream(md)));
        copyAdditionalResources(d);
    }

    public String getHtmlFromMarkdown(final InputStream markdownInputStream) throws IOException {
        PegDownProcessor pegDownProcessor = new PegDownProcessor(Extensions.ALL);
        try (final InputStream in = markdownInputStream) {
            final String markdownString = new String(ByteStreams.toByteArray(in));
            return pegDownProcessor.markdownToHtml(markdownString);
        }
    }

    public void writeHtmlFilesToOutDirectory(final File outDirectory, final String contentHtml) throws IOException {
        HashMap<String, Object> scopes = new HashMap<>();
        scopes.put("title", "StyleCode");
        scopes.put("content", contentHtml);
        parseTemplateToOutDirectory(outDirectory, scopes, indexHtmlTemplate);
        parseTemplateToOutDirectory(outDirectory, resourceGetter.fetchAllResources(), iframeHtmlTemplate);
    }

    void parseTemplateToOutDirectory(File outDirectory, Object scopes, String resourceStr) throws IOException {
        String targetFilename = guessTargetFilename(resourceStr);
        try (final Writer writer = new FileWriter(outDirectory + targetFilename); final Reader reader = getTemplateReader(resourceStr)) {
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(reader, resourceStr);
            mustache.execute(writer, scopes);
            writer.flush();
        }
    }

    String guessTargetFilename(String resourceStr) {
        String targetFilename = resourceStr.replace(".mustache", "").replace(".hbs", "");
        if(targetFilename.contains("/")) {
            targetFilename = targetFilename.substring(targetFilename.lastIndexOf("/") + 1);
        }
        if (!targetFilename.endsWith(".html") && !targetFilename.endsWith(".htm")) {
            targetFilename += ".html";
            targetFilename.replace("..", ".");
        }
        return targetFilename;
    }

    public Reader getTemplateReader(final String resourceStr) throws IOException {
        InputStream inputStream;
        if (resourceStr.equals(DefaultTemplateConstants.DEFAULT_INDEX_HTML_TEMPLATE) || resourceStr.equals(DefaultTemplateConstants.DEFAULT_IFRAME_HTML_TEMPLATE)) {
            inputStream = this.getClass().getResourceAsStream(resourceStr);
            if (inputStream == null) {
                throw new IOException("Classpath resource " + resourceStr + " not found: ");
            }
        } else {
            if(new File(resourceStr).exists()) {
                inputStream = new FileInputStream(resourceStr);
            } else {
                inputStream = new URL(resourceStr).openStream();
            }
        }
        return new InputStreamReader(inputStream);
    }

    public void copyAdditionalResources(final File outDirectory) throws IOException {
        final Map<String, ? extends List<String>> resources = ImmutableMap.of(
                "css", Lists.newArrayList("simple-sidebar.css", "toc.css"),
                "js", Lists.newArrayList("style-code.js", "toc.min.js")
        );

        for (Map.Entry<String, ? extends List<String>> entry : resources.entrySet()) {
            File d = new File(outDirectory.getAbsolutePath() + "/" + entry.getKey());
            if (!d.exists()) {
                d.mkdir();
            }
            for (String filename : entry.getValue()) {
                try (InputStream inputStream = this.getClass().getResourceAsStream("/" + entry.getKey() + "/" + filename);
                     OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(d.getAbsolutePath() + "/" + filename))) {
                    ByteStreams.copy(inputStream, outputStream);
                }
            }
        }
    }

    public void setResourceGetter(ResourceGetter resourceGetter) {
        this.resourceGetter = resourceGetter;
    }

    public void setIframeHtmlTemplate(String iframeHtmlTemplate) {
        this.iframeHtmlTemplate = iframeHtmlTemplate;
    }

    public void setIndexHtmlTemplate(String indexHtmlTemplate) {
        this.indexHtmlTemplate = indexHtmlTemplate;
    }
}
