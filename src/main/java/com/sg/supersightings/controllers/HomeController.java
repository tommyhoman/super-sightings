/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.controllers;

import com.sg.supersightings.models.Sighting;
import com.sg.supersightings.services.SuperSightingsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Thomas
 */
@Controller
public class HomeController {
    
    @Autowired
    SuperSightingsService supeService;

    @GetMapping({"/", "/home"})
    public String displayHome(Model model) {
        List<Sighting> recentSights = supeService.getRecentSightings();
        model.addAttribute("sights", recentSights);
        return "index";
    }
}
