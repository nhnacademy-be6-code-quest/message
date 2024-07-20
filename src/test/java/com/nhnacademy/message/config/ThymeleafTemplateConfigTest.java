package com.nhnacademy.message.config;

import org.junit.jupiter.api.Test;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import static org.junit.jupiter.api.Assertions.*;

class ThymeleafTemplateConfigTest {

    private final ThymeleafTemplateConfig config = new ThymeleafTemplateConfig();

    @Test
    void testTemplateResolver() {
        SpringResourceTemplateResolver resolver = config.templateResolver();

        assertNotNull(resolver);
        assertEquals("classpath:/templates/", resolver.getPrefix());
        assertEquals(".html", resolver.getSuffix());
        assertEquals(TemplateMode.HTML, resolver.getTemplateMode());
        assertFalse(resolver.isCacheable());
    }

    @Test
    void testTemplateEngine() {
        SpringTemplateEngine engine = config.templateEngine();

        assertNotNull(engine);
        assertTrue(engine.getTemplateResolvers().iterator().next() instanceof SpringResourceTemplateResolver);
    }
}