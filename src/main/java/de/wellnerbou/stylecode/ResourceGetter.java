package de.wellnerbou.stylecode;

import com.google.common.base.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ResourceGetter {

    private boolean includeInlineScripts = false;
    private Iterable<String> excludePatterns;

    private SourceUrlProcessor sourceUrlProcessor;
    private AdditionalResourcesPopulator additionalResourcesPopulator;

    public Resources fetchFrom(URL url) throws IOException {
        Document doc = Jsoup.connect(url.toString()).userAgent("Jsoup.connect by style-code/v0.1 (+https://github.com/paulwellnerbou/style-code)").timeout(0).get();
        Resources resources = new Resources();
        populateList(doc, "head", resources.headresources);
        populateList(doc, "body", resources.bodyresources);
        return resources;
    }

    public void enrichWithadditionalResources(Resources resources) {
        if (additionalResourcesPopulator != null) {
            resources.join(additionalResourcesPopulator.populateAdditionalResources());
        }
    }

    private void populateList(Document doc, String parentSelector, List<String> resourcesList) {
        List<Element> headResources = doc.select(parentSelector + " style, " + parentSelector + " link[rel=stylesheet], " + parentSelector + " script");
        for (Element element : headResources) {
            if (element.hasAttr("src") || element.hasAttr("href")) {
                processElementWithUrl(element, "src");
                processElementWithUrl(element, "href");
                final String src = getSource(element);
                if (!isExcluded(src)) {
                    resourcesList.add(element.toString());
                }
            } else {
                if (!element.nodeName().equals("script") || includeInlineScripts) {
                    resourcesList.add(element.toString());
                }
            }
        }
    }

    private boolean isExcluded(final String src) {
        if (this.excludePatterns != null) {
            for (String pattern : this.excludePatterns) {
                if (src.contains(pattern)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getSource(final Element element) {
        String src;
        if (element.hasAttr("src")) {
            src = element.attr("src");
        } else {
            src = element.attr("href");
        }
        return src;
    }

    private void processElementWithUrl(Element element, String attributeKey) {
        if (element.hasAttr(attributeKey)) {
            final String src = element.attr(attributeKey);
            element.attr(attributeKey, sourceUrlProcessor.processSrcUrl(src));
        }
    }

    public void setExcludePatterns(Iterable<String> excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    public void setIncludeInlineScripts(boolean includeInlineScripts) {
        this.includeInlineScripts = includeInlineScripts;
    }

    public void setAdditionalResourcesPopulator(AdditionalResourcesPopulator additionalResourcesPopulator) {
        this.additionalResourcesPopulator = additionalResourcesPopulator;
    }

    public void setSourceUrlProcessor(SourceUrlProcessor sourceUrlProcessor) {
        this.sourceUrlProcessor = sourceUrlProcessor;
    }

}
