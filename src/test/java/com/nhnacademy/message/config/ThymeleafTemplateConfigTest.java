package com.nhnacademy.message.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "mail.redirect.uri=http://test-redirect-url.com")
class ThymeleafTemplateConfigTest {

    @Autowired
    private ThymeleafTemplateConfig thymeleafTemplateConfig;

    @Test
    void testTemplateResolver() {
        SpringResourceTemplateResolver resolver = thymeleafTemplateConfig.templateResolver();

        assertNotNull(resolver);
        assertEquals("classpath:/templates/", resolver.getPrefix());
        assertEquals(".html", resolver.getSuffix());
        assertEquals(TemplateMode.HTML, resolver.getTemplateMode());
        assertFalse(resolver.isCacheable());
    }

    @Test
    void testTemplateEngine() {
        SpringTemplateEngine engine = thymeleafTemplateConfig.templateEngine();

        assertNotNull(engine);
        assertTrue(engine.getEnableSpringELCompiler());

        // 템플릿 리졸버가 올바르게 설정되었는지 확인
        assertNotNull(engine.getTemplateResolvers());
        assertEquals(1, engine.getTemplateResolvers().size());
        assertInstanceOf(SpringResourceTemplateResolver.class, engine.getTemplateResolvers().iterator().next());
    }
}