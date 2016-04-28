package de.wellnerbou.stylecode.app;

import com.lexicalscope.jewel.cli.Option;
import com.lexicalscope.jewel.cli.Unparsed;
import de.wellnerbou.stylecode.DefaultTemplateConstants;

import java.util.List;

public interface CliOptions {

    @Option(defaultToNull = true, longName = "resources-from", description = "URL to fetch resources (CSS, JS) from.")
    String getUrlToFetchResourcesFrom();

    @Option(defaultValue = "false", longName = "include-inline-scripts", description = "Wether to include inline JavaScripts found in the HTML of the URL given in --resources-from.")
    boolean includeInlineScripts();

    @Option(defaultValue = {}, longName = "exclude", description = "Resources found in the HTML of the URL given in --resources-from which should not be included (String.contains() is used here):")
    List<String> excludePatterns();

	@Option(defaultValue = {}, longName = "additional-resources", description = "Any other resources which should be included additionally. If you don't have an URL to fetch the resources from you can give the resources here.")
	List<String> additionalResources();

    @Option(defaultValue = "out", longName = "out", description = "Directory the output should be written to.")
    String getOutputDirectory();

    @Option(defaultValue = DefaultTemplateConstants.DEFAULT_IFRAME_HTML_TEMPLATE, longName = "iframe-template", description = "Mustache template file to use for embedded iframes with HTML demos. If you still want resources (<script...>, <style>, <link rel=stylesheet...>) to be included, use {{headerresources}} for resources included in HTML header and {{bodyresources}} for resources included in the HTML body.")
    String useIframeTemplate();

    @Option(defaultValue = DefaultTemplateConstants.DEFAULT_INDEX_HTML_TEMPLATE, longName = "index-template", description = "Mustache template file to use for the resulting index.html.")
    String useIndexTemplate();

    @Unparsed(description = "Markdown file containing the documentation and example HTML.")
    String markdownFile();
}
