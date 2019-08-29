package app.configurations;

import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Import({DbConfig.class})
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "app")
public class ApplicationConfig {
    @Bean
    @Scope("prototype")
    @SuppressWarnings("ConstantConditions")
    Logger getLogger(InjectionPoint injectionPoint) {
        BasicConfigurator.configure();
        return LogManager.getLogger(injectionPoint.getMethodParameter().getContainingClass());
    }
}
