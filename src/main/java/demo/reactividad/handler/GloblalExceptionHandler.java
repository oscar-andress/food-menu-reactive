package demo.reactividad.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import demo.reactividad.exception.ErrorResponse;
import demo.reactividad.exception.MenuException;
import demo.reactividad.exception.menu.MenuNotFoundException;
import demo.reactividad.exception.menu.MenuUnavailableException;
import reactor.core.publisher.Mono;

@Component
public class GloblalExceptionHandler {
    
    public Mono<ServerResponse> hanldeMenuUnavailableException(MenuUnavailableException ex, ServerRequest request) {
        return buildResponse(ex, request, HttpStatus.CONFLICT);
    }

    public Mono<ServerResponse> handleMenuNotFoundException(MenuNotFoundException ex, ServerRequest request) {
        return buildResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    private Mono<ServerResponse> buildResponse(MenuException ex, ServerRequest request, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now().toString(), 
            status.value(), 
            ex.getMessage(), 
            ex.getErrorCode(),
            request.path());
        return ServerResponse.status(status).bodyValue(error);
    }
}
