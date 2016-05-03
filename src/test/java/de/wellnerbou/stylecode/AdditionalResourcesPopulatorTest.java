package de.wellnerbou.stylecode;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class AdditionalResourcesPopulatorTest {

    @Test
    public void populateAdditionalResources_allEmpty() {
        AdditionalResourcesPopulator additionalResourcesPopulator = new AdditionalResourcesPopulator();
        Resources resources = additionalResourcesPopulator.populateAdditionalResources();

        Assertions.assertThat(resources).isNotNull();
        Assertions.assertThat(resources.bodyresources).isEmpty();
        Assertions.assertThat(resources.headresources).isEmpty();
    }

    @Test
    public void populateAdditionalResources_withAdditionalSourcesButNoUrl() {
        AdditionalResourcesPopulator additionalResourcesPopulator = new AdditionalResourcesPopulator();
        additionalResourcesPopulator.addResources("resource.css", "resource.js");
        Resources resources = additionalResourcesPopulator.populateAdditionalResources();

        Assertions.assertThat(resources).isNotNull();
        Assertions.assertThat(resources.headresources).containsExactly("<link rel=\"stylesheet\" href=\"resource.css\">\n", "<script src=\"resource.js\"></script>\n");
        Assertions.assertThat(resources.bodyresources).isEmpty();
    }

    @Test
    public void populateAdditionalResources_withAdditionalSourcesWithHttpsUrlWithPort() throws MalformedURLException {
        AdditionalResourcesPopulator additionalResourcesPopulator = new AdditionalResourcesPopulator(new SourceUrlProcessor(new URL("http://www.example.com")));
        additionalResourcesPopulator.addResources("https://www.example.com:80/other.css");
        Resources resources = additionalResourcesPopulator.populateAdditionalResources();

        Assertions.assertThat(resources).isNotNull();
        Assertions.assertThat(resources.headresources).containsExactly(
                "<link rel=\"stylesheet\" href=\"https://www.example.com:80/other.css\">\n"
        );
    }

    @Test
    public void populateAdditionalResources_withAdditionalSourcesAndUrl() throws MalformedURLException {
        AdditionalResourcesPopulator additionalResourcesPopulator = new AdditionalResourcesPopulator(new SourceUrlProcessor(new URL("http://www.example.com")));
        additionalResourcesPopulator.addResources("resource.css", "resource.js",
                "http://www.example.com/other.css",
                "https://www.example.com:80/other.css",
                "//example.com/another-one.js");
        Resources resources = additionalResourcesPopulator.populateAdditionalResources();

        Assertions.assertThat(resources).isNotNull();
        Assertions.assertThat(resources.headresources).containsExactly("<link rel=\"stylesheet\" href=\"http://www.example.com/resource.css\">\n",
                "<script src=\"http://www.example.com/resource.js\"></script>\n",
                "<link rel=\"stylesheet\" href=\"http://www.example.com/other.css\">\n",
                "<link rel=\"stylesheet\" href=\"https://www.example.com:80/other.css\">\n",
                "<script src=\"//example.com/another-one.js\"></script>\n"
        );
        Assertions.assertThat(resources.bodyresources).isEmpty();
    }
}
