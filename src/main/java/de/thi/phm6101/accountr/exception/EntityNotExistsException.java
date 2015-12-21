package de.thi.phm6101.accountr.exception;

/**
 * Created by philipp on 21/12/15.
 */
public class EntityNotExistsException extends RuntimeException{

    public EntityNotExistsException(String message) {
        super(message);
    }
}
