package se.joelabs.mediaconverter.rest.resources;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * ConfigResource Tester.
 */
public class ConfigResourceTest {

    private ConfigResource configResource;

    @Before
    public void before() throws Exception {
        configResource = new ConfigResource();
        configResource.init();
    }

    @After
    public void after() throws Exception {
        configResource.dispose();
    }

    @Test
    public void test() {
        configResource.setOutputDir("banan");
        assertEquals("banan", configResource.getOutputDir());
    }
}
