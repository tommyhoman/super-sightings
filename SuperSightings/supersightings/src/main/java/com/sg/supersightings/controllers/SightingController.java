/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.controllers;

import com.sg.supersightings.models.Location;
import com.sg.supersightings.models.Sighting;
import com.sg.supersightings.models.Superperson;
import com.sg.supersightings.services.SuperSightingsService;
import com.sg.supersightings.services.SuperSightingsServiceException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Thomas
 */
@Controller
public class SightingController {

    @Autowired
    SuperSightingsService supeService;

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Superperson> people = supeService.getAllSuperpeople();
        List<Location> locs = supeService.getAllLocations();
        List<Sighting> sights = supeService.getAllSightings();
        Sighting sight = new Sighting();
        model.addAttribute("sighting", sight);
        model.addAttribute("sights", sights);
        model.addAttribute("people", people);
        model.addAttribute("locs", locs);
        return "sightings";
    }

    @PostMapping("addSighting")
    public String addSuperperson(@Valid Sighting sight,
            BindingResult result,
            Model model) throws SuperSightingsServiceException {
        if (result.hasErrors()) {
            List<Superperson> people = supeService.getAllSuperpeople();
            List<Location> locs = supeService.getAllLocations();
            List<Sighting> sights = supeService.getAllSightings();
            model.addAttribute("sights", sights);
            model.addAttribute("people", people);
            model.addAttribute("locs", locs);
            return "sightings";
        }
        supeService.addSighting(sight);

        return "redirect:/sightings";
    }

    @GetMapping("viewSighting")
    public String sightingDetail(Integer id, Model model) {
        Sighting sight = supeService.getSightingById(id);
        model.addAttribute("sighting", sight);
        return "sightingDetail";
    }

    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id) throws SuperSightingsServiceException {
        supeService.deleteSightingById(id);

        return "redirect:/sightings";
    }

    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model) {
        Sighting sight = supeService.getSightingById(id);
        List<Superperson> people = supeService.getAllSuperpeople();
        List<Location> locs = supeService.getAllLocations();
        model.addAttribute("sighting", sight);
        model.addAttribute("people", people);
        model.addAttribute("locs", locs);
        return "editSighting";
    }

    @PostMapping("editSighting")
    public String performEditSighting(@Valid Sighting sight, BindingResult result) throws SuperSightingsServiceException {
        if (result.hasErrors()) {
            return "editSighting";
        }

        supeService.updateSighting(sight);

        return "redirect:/sightings";
    }

}
