package de.wellnerbou.stylecode;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class SourceUrlProcessorTest {

    @Test
    public void processSrcUrl_absoluteUrlWithHttps() throws MalformedURLException {
        String src = "https://www.example.com/file.css";
        String res = new SourceUrlProcessor(new URL("https://www.example.com/some/where.html")).processSrcUrl(src);
        Assertions.assertThat(res).isEqualTo(src);
    }

    @Test
    public void processSrcUrl_absoluteUrlWithProtocol() throws MalformedURLException {
        String src = "http://www.example.com/file.css";
        String res = new SourceUrlProcessor(new URL("http://www.example.com/some/where.html")).processSrcUrl(src);
        Assertions.assertThat(res).isEqualTo(src);
    }

    @Test
    public void processSrcUrl_absoluteUrlWithoutDomain() throws MalformedURLException {
        String src = "//www.example.com/file.css";
        String res = new SourceUrlProcessor(new URL("http://www.example.com/some/where.html")).processSrcUrl(src);
        Assertions.assertThat(res).isEqualTo(src);
    }

    @Test
    public void processSrcUrl_absoluteUrlWithDomain() throws MalformedURLException {
        String src = "/file.css";
        String res = new SourceUrlProcessor(new URL("http://www.example.com/some/where.html")).processSrcUrl(src);
        Assertions.assertThat(res).isEqualTo("http://www.example.com/file.css");
    }

    @Test
    public void processSrcUrl_relativeUrl() throws MalformedURLException {
        String src = "file.css";
        String res = new SourceUrlProcessor(new URL("http://www.example.com/some/where.html")).processSrcUrl(src);
        Assertions.assertThat(res).isEqualTo("http://www.example.com/some/file.css");
    }

    @Test
    public void processSrcUrl_relativeUrlMoreDirectories() throws MalformedURLException {
        String src = "path/to/some/file.css";
        String res = new SourceUrlProcessor(new URL("http://www.example.com/some/where.html")).processSrcUrl(src);
        Assertions.assertThat(res).isEqualTo("http://www.example.com/some/path/to/some/file.css");
    }

    @Test
    public void test_getPath_fullUrl() throws MalformedURLException {
        String res = new SourceUrlProcessor(new URL("http://www.example.com/some/where/file.html")).getBasePath();
        Assertions.assertThat(res).isEqualTo("/some/where/");
    }

    @Test
    public void test_getPath_onlyDomain() throws MalformedURLException {
        String res = new SourceUrlProcessor(new URL("http://www.example.com")).getBasePath();
        Assertions.assertThat(res).isEqualTo("/");
    }

    @Test(expected = NullPointerException.class)
    public void getBaseUrl_null() throws Exception {
        new SourceUrlProcessor(null).getBaseUrl();
    }

    @Test
    public void getBaseUrl_onlySlash() throws MalformedURLException {
        String res = new SourceUrlProcessor(new URL("http://www.example.com/")).getBaseUrl();
        Assertions.assertThat(res).isEqualTo("http://www.example.com");
    }

    @Test
    public void getBaseUrl_withPort() throws MalformedURLException {
        String res = new SourceUrlProcessor(new URL("http://www.example.com:8080/")).getBaseUrl();
        Assertions.assertThat(res).isEqualTo("http://www.example.com:8080");
    }

    @Test
    public void getBaseUrl_withPathNoPort() throws MalformedURLException {
        String res = new SourceUrlProcessor(new URL("http://www.example.com/path/to/somewhere.extension")).getBaseUrl();
        Assertions.assertThat(res).isEqualTo("http://www.example.com");
    }

    @Test
    public void getBaseUrl_withHttpsNoPort() throws MalformedURLException {
        String res = new SourceUrlProcessor(new URL("https://www.example.com/path/to/somewhere.extension")).getBaseUrl();
        Assertions.assertThat(res).isEqualTo("https://www.example.com");
    }

    @Test
    public void getBaseUrl_withPathWithPort() throws MalformedURLException {
        String res = new SourceUrlProcessor(new URL("http://www.example.com:9191/path/to/somewhere.extension")).getBaseUrl();
        Assertions.assertThat(res).isEqualTo("http://www.example.com:9191");
    }

    @Test(expected = MalformedURLException.class)
    public void getBaseUrl_noProtocol() throws MalformedURLException {
        new SourceUrlProcessor(new URL("//www.example.com/path/to/somewhere.extension")).getBaseUrl();
    }

}