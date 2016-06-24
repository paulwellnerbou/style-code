package de.wellnerbou.stylecode;

import java.net.URL;

public class SourceUrlProcessor {

    private URL url;

    public SourceUrlProcessor(URL url) {
        this.url = url;
    }

    public String processSrcUrl(final String src) {
        if (!src.startsWith("http://") && !src.startsWith("https://") && !src.startsWith("//")) {
            if (src.startsWith("/")) {
                return getBaseUrl() + src;
            } else {
                return getBaseUrl() + getBasePath() + src;
            }
        }
        return src;
    }

    public String getBaseUrl() {
        return url.getProtocol() + "://" + url.getAuthority();
    }

    public String getBasePath() {
        String path = this.url.getPath();
        if(path.contains("/")) {
            return path.substring(0, path.lastIndexOf('/') + 1);
        } else {
            return path + "/";
        }
    }
}
