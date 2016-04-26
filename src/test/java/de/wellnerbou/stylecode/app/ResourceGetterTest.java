package de.wellnerbou.stylecode.app;

import org.junit.Test;

public class ResourceGetterTest {

    @Test
    public void getResourcesFrom() throws Exception {
        String url = "http://dflint1/";

        ResourceGetter resourceGetter = new ResourceGetter(url);
        ResourceGetter.Resources res = resourceGetter.fetch();

        for (String resourse : res.headresources) {
            System.out.println(resourse);
        }
    }

}