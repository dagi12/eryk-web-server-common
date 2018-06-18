package pl.edu.amu.wmi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

/**
 * Stworzone przez Eryk Mariankowski dnia 11.07.2017.
 */
@Configuration
public class WebApplicationConfig {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        "classpath:/public/", "classpath:/public/static/js/", "classpath:/public/static/css/", "classpath:/public/static/media/"
    };

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/static/js/*")
            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        registry
            .addResourceHandler("/static/css/*")
            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        registry
            .addResourceHandler("/static/media/*")
            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        registry
            .addResourceHandler("/index.html")
            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        registry
            .addResourceHandler("/")
            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

}
