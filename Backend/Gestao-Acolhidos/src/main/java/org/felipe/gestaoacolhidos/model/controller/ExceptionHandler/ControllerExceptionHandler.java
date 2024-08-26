package org.felipe.gestaoacolhidos.model.controller.ExceptionHandler;


import org.felipe.gestaoacolhidos.model.exceptions.HostedAlreadyRegisteredException;
import org.felipe.gestaoacolhidos.model.exceptions.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    //USER CONTROLLER
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    //HOSTED CONTROLLER
    @ExceptionHandler(HostedAlreadyRegisteredException.class)
    public ResponseEntity<Object> handleHostedAlreadyRegisteredException(HostedAlreadyRegisteredException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    //GENERIC CONTROLLER
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body("SOMETHING WENT WRONG");
    }


}
