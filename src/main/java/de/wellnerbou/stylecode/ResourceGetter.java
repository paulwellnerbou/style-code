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
	private Iterable<String> excludePatterns;
	private Iterable<String> includeAdditionalResources;

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
		if (includeAdditionalResources != null) {
			populateAdditionalResources(resources, url);
		}
		return resources;
	}

	private void populateAdditionalResources(final Resources resources, final URL url) {
		for (final String resourceStr : includeAdditionalResources) {
			final String resolvedResourceStr = processSrcUrl(url, resourceStr);
			if(resolvedResourceStr.endsWith("js")) {
				resources.headresources.add("<script src=\"" + resolvedResourceStr + "\"></script>\n");
			} else if(resolvedResourceStr.endsWith("css")) {
				resources.headresources.add("<link rel=\"stylesheet\" href=\"" + resolvedResourceStr + "\">\n");
			}
		}
	}

	private void populateList(Document doc, String parentSelector, List<String> resourcesList, final URL url) {
		List<Element> headResources = doc.select(parentSelector + " style, " + parentSelector + " link[rel=stylesheet], " + parentSelector + " script");
		for (Element element : headResources) {
			if (element.hasAttr("src") || element.hasAttr("href")) {
				processElementWithUrl(url, element, "src");
				processElementWithUrl(url, element, "href");
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

	private void processElementWithUrl(URL url, Element element, String attributeKey) {
		if (element.hasAttr(attributeKey)) {
			final String src = element.attr(attributeKey);
			element.attr(attributeKey, processSrcUrl(url, src));
		}
	}

	private String processSrcUrl(final URL url, final String src) {
		if (!src.startsWith("http://") && !src.startsWith("//")) {
			if (src.startsWith("/")) {
				return getBaseUrl(url) + src;
			} else {
				return url.toString() + "/" + src;
			}
		}
		return src;
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
