package de.wellnerbou.stylecode.app;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyleCode {

    private String sourceMarkdownFile;
    private ResourceGetter resourceGetter;

    public StyleCode(final String sourceMarkdownFile) {
        this.sourceMarkdownFile = sourceMarkdownFile;
    }

    public void generate(final String outputDirectory) throws IOException {
        File d = new File(outputDirectory);
        if(!d.isDirectory()) {
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
        scopes.put("title", "StyleDoc");
        scopes.put("content", contentHtml);
        parseTemplateToOutDirectory(outDirectory, scopes, "/index.html.mustache");
        parseTemplateToOutDirectory(outDirectory, resourceGetter.fetch(), "/iframe.html.mustache");
    }

    private void parseTemplateToOutDirectory(File outDirectory, Object scopes, String resourceStr) throws IOException {
        String targetFilename = resourceStr.replace(".mustache", "");
        try (final Writer writer = new FileWriter(outDirectory + targetFilename); final Reader reader = getTemplateReader(resourceStr)) {
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(reader, resourceStr);
            mustache.execute(writer, scopes);
            writer.flush();
        }
    }

    public Reader getTemplateReader(final String resourceStr) throws IOException {
        InputStream res = this.getClass().getResourceAsStream(resourceStr);
        if(res == null) {
            throw new IOException("Resource " +  resourceStr + " not found.");
        }
        return new InputStreamReader(res);
    }

    public void copyAdditionalResources(final File outDirectory) throws IOException {
		final Map<String, ? extends List<String>> resources = ImmutableMap.of(
				"css", Lists.newArrayList("simple-sidebar.css", "toc.css"),
				"js", Lists.newArrayList("style-code.js", "toc.min.js")
		);

        for (Map.Entry<String, ? extends List<String>> entry : resources.entrySet()) {
            File d = new File(outDirectory.getAbsolutePath() + "/" + entry.getKey());
            if(!d.exists()) {
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

    public void setSourceMarkdownFile(String sourceMarkdownFile) {
        this.sourceMarkdownFile = sourceMarkdownFile;
    }

    public void setResourceGetter(ResourceGetter resourceGetter) {
        this.resourceGetter = resourceGetter;
    }
}
