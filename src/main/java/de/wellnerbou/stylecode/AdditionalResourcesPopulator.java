package de.wellnerbou.stylecode;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;

public class AdditionalResourcesPopulator {

    private Collection<String> additionalResources = Lists.newArrayList();
    private SourceUrlProcessor sourceUrlProcessor;

    public AdditionalResourcesPopulator() {
    }

    public AdditionalResourcesPopulator(SourceUrlProcessor sourceUrlProcessor) {
        this.sourceUrlProcessor = sourceUrlProcessor;
    }

    public Resources populateAdditionalResources() {
        Resources resources = new Resources();
        for (final String resourceStr : additionalResources) {
            String resolvedResourceStr = resourceStr;
            if (sourceUrlProcessor != null) {
                resolvedResourceStr = sourceUrlProcessor.processSrcUrl(resourceStr);
            }
            if (resolvedResourceStr.endsWith("js")) {
                resources.headresources.add("<script src=\"" + resolvedResourceStr + "\"></script>\n");
            } else if (resolvedResourceStr.endsWith("css")) {
                resources.headresources.add("<link rel=\"stylesheet\" href=\"" + resolvedResourceStr + "\">\n");
            }
        }
        return resources;
    }

    public void addResources(String... resources) {
        Collections.addAll(additionalResources, resources);
    }
}
