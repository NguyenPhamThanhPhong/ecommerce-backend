package ecommerce.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.Include;
import org.springframework.boot.actuate.web.exchanges.servlet.HttpExchangesFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ActuatorConfig {

    @Bean
    public InMemoryHttpExchangeRepository httpTraceRepository() {
        return new InMemoryHttpExchangeRepository();
    }

    private final Set<Include> includes = Include.defaultIncludes();

    private static class TraceRequestFilter extends HttpExchangesFilter {

        public TraceRequestFilter(HttpExchangeRepository repository, Set<Include> includes) {
            super(repository, includes);
        }

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            return request.getServletPath().contains("actuator") || request.getServletPath().contains("swagger");
        }
    }

    @Bean
    public HttpExchangesFilter httpExchangesFilter() {
        return new TraceRequestFilter(httpTraceRepository(), includes);
    }
}