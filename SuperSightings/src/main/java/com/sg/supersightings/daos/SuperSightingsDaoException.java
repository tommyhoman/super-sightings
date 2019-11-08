/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.daos;

/**
 *
 * @author Thomas
 */
public class SuperSightingsDaoException extends Exception {

    public SuperSightingsDaoException(String msg) {
        super(msg);
    }

    public SuperSightingsDaoException(String msg, Throwable inner) {
        super(msg, inner);
    }

}
