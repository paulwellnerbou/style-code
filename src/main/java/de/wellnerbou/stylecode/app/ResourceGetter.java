package de.wellnerbou.stylecode.app;

import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ResourceGetter {

    public Resources getResourcesFrom(final String urlStr) throws IOException {
        Resources resources = new Resources();
        if (urlStr != null) {
            Document doc = Jsoup.connect(urlStr).userAgent("Mozilla").timeout(0).get();

            URL url = new URL(urlStr);
            populateList(doc, "head", resources.headresources, url);
            populateList(doc, "body", resources.bodyresources, url);
        }
        return resources;
    }

    private void populateList(Document doc, String parentSelector, List<String> resourcesList, final URL url) {
        List<Element> headResources = doc.select(parentSelector + " style, " + parentSelector + " link[rel=stylesheet], " + parentSelector + " script");
        for (Element element : headResources) {
            processElementWithUrl(url, element, "src");
            processElementWithUrl(url, element, "href");
            resourcesList.add(element.toString());
        }
    }

    private void processElementWithUrl(URL url, Element element, String attributeKey) {
        if(element.hasAttr(attributeKey)) {
            if(!element.attr(attributeKey).startsWith("http://")) {
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

    public class Resources {
        List<String> headresources = Lists.newArrayList();
        List<String> bodyresources = Lists.newArrayList();
    }
}
