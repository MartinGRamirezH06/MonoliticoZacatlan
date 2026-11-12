package mx.fes.unam.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigSeguridad implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Todo requiere sesión, menos el login y los archivos estáticos
        registry.addInterceptor(new AuthInterceptor(false))
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/error", "/css/**", "/js/**", "/img/**", "/bootstrap/**");

        // Estas rutas son solo del administrador
        registry.addInterceptor(new AuthInterceptor(true))
                .addPathPatterns("/bloque/**", "/producto/**", "/vendedor/**", "/admin/**");
    }
}
