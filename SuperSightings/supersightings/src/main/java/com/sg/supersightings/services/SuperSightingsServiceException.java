/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.services;

/**
 *
 * @author Thomas
 */
public class SuperSightingsServiceException extends Exception {

    public SuperSightingsServiceException(String message) {
        super(message);
    }

    public SuperSightingsServiceException(String message, Throwable ex) {
        super(message, ex);
    }

}
