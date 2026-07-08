package cl.duoc.Api_clinica.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice 
public class GlobalExceptionHandler { 

    private ResponseEntity<Object> construirRespuesta(HttpStatus estado, String mensaje, WebRequest request) {
        Map<String, Object> cuerpo = new HashMap<>(); 
        cuerpo.put("fecha", LocalDateTime.now()); 
        cuerpo.put("HTTP status", estado.value()); 
        cuerpo.put("error", estado.getReasonPhrase()); 
        cuerpo.put("mensaje", mensaje); 
        cuerpo.put("ruta", request.getDescription(false)); 
        return new ResponseEntity<>(cuerpo, estado);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> manejarArgumentoInvalido(IllegalArgumentException ex, WebRequest request) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> manejarValidacion(MethodArgumentNotValidException ex, WebRequest request) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; ")); 
        return construirRespuesta(HttpStatus.BAD_REQUEST, mensaje, request); 
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> manejarParametroFaltante(MissingServletRequestParameterException ex, WebRequest request) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, "Falta el parámetro obligatorio: " + ex.getParameterName(), request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> manejarMetodoNoPermitido(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return construirRespuesta(HttpStatus.METHOD_NOT_ALLOWED, "Método no permitido: " + ex.getMethod(), request) //[cite: 14]
    }

    // Cualquier otra excepción no mapeada en el sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> manejarTodoLoDemas(Exception ex, WebRequest request) {
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado. Intente de nuevo más tarde.", request); 
    }
}
