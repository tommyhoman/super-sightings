/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.controllers;

import com.sg.supersightings.models.Location;
import com.sg.supersightings.services.SuperSightingsService;
import com.sg.supersightings.services.SuperSightingsServiceException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Thomas
 */
@Controller
public class LocationController {

    @Autowired
    SuperSightingsService supeService;

    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locs = supeService.getAllLocations();
        Location loc = new Location();
        model.addAttribute("location", loc);
        model.addAttribute("locs", locs);
        return "locations";
    }

    @PostMapping("addLocation")
    public String addLocation(@Valid Location loc, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Location> locs = supeService.getAllLocations();
            model.addAttribute("locs", locs);
            return "locations";
        }
        supeService.addLocation(loc);

        return "redirect:/locations";
    }

    @GetMapping("deleteLocation")
    public String deleteLocation(Integer id) throws SuperSightingsServiceException {
        supeService.deleteLocationById(id);

        return "redirect:/locations";
    }

    @GetMapping("editLocation")
    public String editLocation(Integer id, Model model) {
        Location loc = supeService.getLocationById(id);
        model.addAttribute("location", loc);
        return "editLocation";
    }

    @PostMapping("editLocation")
    public String performEditLocation(@Valid Location location, BindingResult result) throws SuperSightingsServiceException {
        if (result.hasErrors()) {
            return "editLocation";
        }

        supeService.updateLocation(location);

        return "redirect:/locations";
    }

//    @GetMapping("location/{id}")
//    public String displayLocationDetails(@PathVariable Integer id, Model model) {
//        Location loc = supeService.getLocationById(id);
//        model.addAttribute("loc", loc);
//        return "location";
//    }
}
