/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.controllers;

import com.sg.supersightings.services.SuperSightingsServiceException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author Thomas
 */
@ControllerAdvice
public class ServiceErrorAdvice {
    
    @ExceptionHandler({SuperSightingsServiceException.class, IllegalArgumentException.class})
    public String handle(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
    
    
    
}
