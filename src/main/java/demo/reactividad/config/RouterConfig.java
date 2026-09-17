package demo.reactividad.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import demo.reactividad.exception.menu.MenuNotFoundException;
import demo.reactividad.exception.menu.MenuUnavailableException;
import demo.reactividad.handler.GloblalExceptionHandler;
import demo.reactividad.handler.MenuRequestHanlder;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor

public class RouterConfig {

    private final MenuRequestHanlder menuRequestHanlder;
    private final GloblalExceptionHandler globalExceptionHandler;

    @Bean
    public RouterFunction<ServerResponse> menuRoute() {
        return RouterFunctions.route()
            .path("/api/v1/menu", this::menuRoutes)
            .build();
    }

    public RouterFunction<ServerResponse> menuRoutes() {
        return RouterFunctions.route()
            .GET("/stream", 
                  accept(MediaType.TEXT_EVENT_STREAM), 
                  this.menuRequestHanlder::getMenuStream)
            .GET("/{menuId}", this.menuRequestHanlder::getMenu)
            .POST("/million", 
                  contentType(MediaType.APPLICATION_NDJSON) //consumes
                  .and(accept(MediaType.APPLICATION_NDJSON)), // produces
                  this.menuRequestHanlder::create1MillionMenu)

            .POST("/", this.menuRequestHanlder::createMenu)
            .DELETE("/", this.menuRequestHanlder::deleteMenu)
            .onError(MenuNotFoundException.class, this.globalExceptionHandler::handleMenuNotFoundException)
            .onError(MenuUnavailableException.class, this.globalExceptionHandler::hanldeMenuUnavailableException)
            .build();
    }
    
}
