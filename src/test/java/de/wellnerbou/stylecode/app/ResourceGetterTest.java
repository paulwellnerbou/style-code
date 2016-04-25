package de.wellnerbou.stylecode.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceGetterTest {

    @Test
    public void getResourcesFrom() throws Exception {
        String url = "http://dflint1/";

        ResourceGetter resourceGetter = new ResourceGetter();
        ResourceGetter.Resources res = resourceGetter.getResourcesFrom(url);

        for (String resourse : res.headresources) {
            System.out.println(resourse);
        }
    }

}