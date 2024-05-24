package baitaplon.mv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class ThymeleadfConfig implements WebMvcConfigurer {

	@Autowired
	private ThymeleafRequestInterceptor thymeleafRequestInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(thymeleafRequestInterceptor);
	}
}