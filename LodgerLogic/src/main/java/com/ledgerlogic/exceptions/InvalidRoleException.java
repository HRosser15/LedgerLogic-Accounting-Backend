package com.ledgerlogic.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidRoleException extends RuntimeException{

    public InvalidRoleException(String message){ super(message);}

    public InvalidRoleException(String message, Throwable cause){
        super(message, cause);
    }

    public InvalidRoleException(Throwable cause){
        super(cause);
    }

    public InvalidRoleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
