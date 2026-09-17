package demo.reactividad.exception;

public record ErrorResponse (
    String timestamp,
    int status,
    String message,
    String errorCode,
    String path
) {
    
}
