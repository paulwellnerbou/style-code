package de.wellnerbou.stylecode.app;

public class StyleCodeBuilder {

    private String urlToFetchResourcesFrom;
    private boolean includeInlineScripts = false;
    private String[] excludePatterns;
    private String fromMarkdownFile;

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

    public StyleCodeBuilder excludeResourcesMatching(final String... excludePatterns) {
        this.excludePatterns = excludePatterns;
        return this;
    }

    public StyleCode build() {
        StyleCode styleCode = new StyleCode(fromMarkdownFile);

        ResourceGetter resourceGetter = new ResourceGetter(urlToFetchResourcesFrom);
        resourceGetter.setExcludePatterns(excludePatterns);
        resourceGetter.setIncludeInlineScripts(includeInlineScripts);
        styleCode.setResourceGetter(resourceGetter);

        return styleCode;
    }
}
