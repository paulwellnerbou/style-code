package de.wellnerbou.stylecode;

import com.google.common.collect.Lists;

import java.util.List;

public class Resources {
    List<String> headresources = Lists.newArrayList();
    List<String> bodyresources = Lists.newArrayList();

    public void join(Resources resources) {
        headresources.addAll(resources.headresources);
        bodyresources.addAll(resources.bodyresources);
    }
}
