package de.thi.phm6101.accountr.exception;

/**
 * Created by philipp on 21/12/15.
 */
public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
