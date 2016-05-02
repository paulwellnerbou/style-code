package de.wellnerbou.stylecode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class StyleCodeBuilder {

    private String urlToFetchResourcesFrom;
    private boolean includeInlineScripts = false;
    private Iterable<String> excludePatterns;
    private String fromMarkdownFile;
    private Iterable<String> additionalResources;
    private String indexHtmlTemplate = DefaultTemplateConstants.DEFAULT_INDEX_HTML_TEMPLATE;
    private String iframeHtmlTemplate = DefaultTemplateConstants.DEFAULT_IFRAME_HTML_TEMPLATE;

    public StyleCodeBuilder(final String fromMarkdownFile) {
        this.fromMarkdownFile = fromMarkdownFile;
    }

    public StyleCodeBuilder useStylesAndScriptsFrom(final String urlToFetchResourcesFrom) {
        this.urlToFetchResourcesFrom = urlToFetchResourcesFrom;
        return this;
    }

    public StyleCodeBuilder includeInlineScripts(final boolean includeInlineScripts) {
        this.includeInlineScripts = includeInlineScripts;
        return this;
    }

    public StyleCodeBuilder withAdditionalResources(final Iterable<String> additionalResources) {
        this.additionalResources = additionalResources;
        return this;
    }

    public StyleCodeBuilder excludeResourcesMatching(final Iterable<String> excludePatterns) {
        this.excludePatterns = excludePatterns;
        return this;
    }

    public StyleCodeBuilder excludeResourcesMatching(final String... excludePatterns) {
        this.excludePatterns = Arrays.asList(excludePatterns);
        return this;
    }

    public StyleCodeBuilder withIframeHtmlTemplate(final String iframeHtmlTemplate) {
        this.iframeHtmlTemplate = iframeHtmlTemplate;
        return this;
    }

    public StyleCodeBuilder withIndexHtmlTemplate(final String indexHtmlTemplate) {
        this.indexHtmlTemplate = indexHtmlTemplate;
        return this;
    }

    public StyleCode build() throws MalformedURLException {
        ResourceGetter resourceGetter = new ResourceGetter();
        resourceGetter.setExcludePatterns(excludePatterns);
        resourceGetter.setIncludeInlineScripts(includeInlineScripts);
        SourceUrlProcessor sourceUrlProcessor = null;
        if (urlToFetchResourcesFrom != null) {
            sourceUrlProcessor = new SourceUrlProcessor(new URL(urlToFetchResourcesFrom));
            resourceGetter.setSourceUrlProcessor(sourceUrlProcessor);
        }
        if (additionalResources != null) {
            AdditionalResourcesPopulator additionalResourcesPopulator;
            if (sourceUrlProcessor != null) {
                additionalResourcesPopulator = new AdditionalResourcesPopulator(sourceUrlProcessor);
            } else {
                additionalResourcesPopulator = new AdditionalResourcesPopulator();
            }
            resourceGetter.setAdditionalResourcesPopulator(additionalResourcesPopulator);
        }

        StyleCode styleCode = new StyleCode(fromMarkdownFile);
        styleCode.setResourceGetter(resourceGetter);
        styleCode.setIframeHtmlTemplate(iframeHtmlTemplate);
        styleCode.setIndexHtmlTemplate(indexHtmlTemplate);

        return styleCode;
    }
}
