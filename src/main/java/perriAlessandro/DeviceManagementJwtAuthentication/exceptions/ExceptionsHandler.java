package perriAlessandro.DeviceManagementJwtAuthentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.ErrorsResponseDTO;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
//Per centralizzare la gestione delle eccezioni
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsResponseDTO handleBadRequest(BadRequestException ex) {
        if (ex.getErrorsList() != null) {
            String message = ex.getErrorsList().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            return new ErrorsResponseDTO(message, LocalDateTime.now());

        } else {

            return new ErrorsResponseDTO(ex.getMessage(), LocalDateTime.now());
        }
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    // Eccezioni di tipo NotFoundException
    public ErrorsResponseDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsResponseDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsResponseDTO handleForbidden(AccessDeniedException ex) {
        return new ErrorsResponseDTO("Non hai accesso a questa funzionalità!", LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorsResponseDTO handleNotFound(NotFoundException ex) {
        return new ErrorsResponseDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ErrorsResponseDTO handleGenericErrors(Exception ex) {
        ex.printStackTrace();
        return new ErrorsResponseDTO("Problema lato server! Giuro che lo risolveremo presto!", LocalDateTime.now());
    }
}
