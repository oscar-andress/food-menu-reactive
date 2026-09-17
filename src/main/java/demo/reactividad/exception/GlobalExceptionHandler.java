// package demo.reactividad.exception;

// import java.time.LocalDateTime;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.server.ServerWebExchange;

// import demo.reactividad.exception.menu.MenuNotFoundException;
// import demo.reactividad.exception.menu.MenuUnavailableException;
// import reactor.core.publisher.Mono;

// @RestControllerAdvice
// public class GlobalExceptionHandler {

//     @ExceptionHandler(MenuNotFoundException.class)
//     Mono<ResponseEntity<ErrorResponse>> handleMenuNotFoundException(MenuNotFoundException ex, ServerWebExchange request) {
//         return buildResponse(ex, request, HttpStatus.NOT_FOUND);
//     }

//     @ExceptionHandler(MenuUnavailableException.class)
//     Mono<ResponseEntity<ErrorResponse>> hanldeMenuUnavailableException(MenuUnavailableException ex, ServerWebExchange request) {
//         return buildResponse(ex, request, HttpStatus.CONFLICT);
//     }

//     Mono<ResponseEntity<ErrorResponse>> buildResponse(MenuException ex, ServerWebExchange request, HttpStatus status) {
//         ErrorResponse error = new ErrorResponse(
//             LocalDateTime.now().toString(), 
//             status.value(), 
//             ex.getMessage(), 
//             ex.getErrorCode(),
//             request.getRequest().getPath().value());
//         return Mono.just(ResponseEntity.status(status).body(error));
//     }

// }
