/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.controllers;

import com.sg.supersightings.models.Power;
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
public class SuperpersonController {

    @Autowired
    SuperSightingsService supeService;

    @GetMapping("superpeople")
    public String displaySuperpeople(Model model) {
        List<Superperson> people = supeService.getAllSuperpeople();
        List<Power> pows = supeService.getAllPowers();
        Superperson person = new Superperson();
        model.addAttribute("people", people);
        model.addAttribute("pows", pows);
        model.addAttribute("superperson", person);
        return "superpeople";
    }

    @PostMapping("addSuperperson")
    public String addSuperperson(@Valid Superperson person,
            BindingResult result,
            Model model) throws SuperSightingsServiceException {
        if (result.hasErrors()) {
            List<Superperson> people = supeService.getAllSuperpeople();
            List<Power> pows = supeService.getAllPowers();
            model.addAttribute("people", people);
            model.addAttribute("pows", pows);
            return "superpeople";
        }
        supeService.addSuperperson(person);

        return "redirect:/superpeople";
    }

    @GetMapping("viewSuperperson")
    public String courseDetail(Integer id, Model model) {
        Superperson person = supeService.getSuperpersonById(id);
        model.addAttribute("superperson", person);
        return "superpersonDetail";
    }

    @GetMapping("deleteSuperperson")
    public String deleteSuperperson(Integer id) throws SuperSightingsServiceException {
        supeService.deleteSuperpersonById(id);

        return "redirect:/superpeople";
    }

    @GetMapping("editSuperperson")
    public String editSuperperson(Integer id, Model model) {
        Superperson person = supeService.getSuperpersonById(id);
        List<Power> pows = supeService.getAllPowers();
        model.addAttribute("pows", pows);
        model.addAttribute("superperson", person);
        return "editSuperperson";
    }

    @PostMapping("editSuperperson")
    public String performEditSuperperson(@Valid Superperson person, BindingResult result) throws SuperSightingsServiceException {
        if (result.hasErrors()) {
            return "editSuperperson";
        }

        supeService.updateSuperperson(person);

        return "redirect:/superpeople";
    }
}
