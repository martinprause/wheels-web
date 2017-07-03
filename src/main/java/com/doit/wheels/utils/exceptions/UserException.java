package com.doit.wheels.utils.exceptions;

/**
 * Created by gorbach on 03.07.17.
 */
public class UserException extends Exception {
    public UserException(){};

    public UserException(String message){
        super(message);
    }
}
