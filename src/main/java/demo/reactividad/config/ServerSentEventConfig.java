package demo.reactividad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.reactividad.dto.response.MenuResponseDTO;
import reactor.core.publisher.Sinks;

@Configuration 
public class ServerSentEventConfig {
    
    @Bean 
    public Sinks.Many<MenuResponseDTO> sink() {
        return Sinks.many().replay().limit(1);
    }
}
