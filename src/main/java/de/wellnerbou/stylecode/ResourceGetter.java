package de.wellnerbou.stylecode;

import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ResourceGetter {

    private final String urlStr;

    private boolean includeInlineScripts = false;
    private String[] excludePatterns;

    public ResourceGetter(final String urlStr) {
        this.urlStr = urlStr;
    }

    public Resources fetch() throws IOException {
        if (urlStr != null) {
            Document doc = Jsoup.connect(urlStr).userAgent("style-code").timeout(0).get();
            return fetch(doc);
        }
        return new Resources();
    }

    private Resources fetch(Document doc) throws MalformedURLException {
        Resources resources;
        resources = new Resources();
        URL url = new URL(urlStr);
        populateList(doc, "head", resources.headresources, url);
        populateList(doc, "body", resources.bodyresources, url);
        return resources;
    }

    private void populateList(Document doc, String parentSelector, List<String> resourcesList, final URL url) {
        List<Element> headResources = doc.select(parentSelector + " style, " + parentSelector + " link[rel=stylesheet], " + parentSelector + " script");
        for (Element element : headResources) {
            if(element.hasAttr("src") || element.hasAttr("href")) {
                processElementWithUrl(url, element, "src");
                processElementWithUrl(url, element, "href");
                resourcesList.add(element.toString());
            } else {
                if(!element.nodeName().equals("script") || includeInlineScripts) {
                    resourcesList.add(element.toString());
                }
            }
        }
    }

    private void processElementWithUrl(URL url, Element element, String attributeKey) {
        if(element.hasAttr(attributeKey)) {
            if(!element.attr(attributeKey).startsWith("http://") && !element.attr(attributeKey).startsWith("//")) {
                if(element.attr(attributeKey).startsWith("/")) {
                    element.attr(attributeKey, getBaseUrl(url) + element.attr(attributeKey));
                } else {
                    element.attr(attributeKey, url.toString() + "/" + element.attr(attributeKey));
                }
            }
        }
    }

    private String getBaseUrl(URL url) {
        return url.getProtocol() + "://" + url.getAuthority();
    }

    public void setExcludePatterns(Iterable<String> excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    public void setIncludeInlineScripts(boolean includeInlineScripts) {
        this.includeInlineScripts = includeInlineScripts;
    }

    public class Resources {
        List<String> headresources = Lists.newArrayList();
        List<String> bodyresources = Lists.newArrayList();
    }
}
