package org.felipe.gestaoacolhidos.model.exceptions;

public class HostedAlreadyRegisteredException extends RuntimeException {
    public HostedAlreadyRegisteredException(String message) {
        super(message);
    }
}
