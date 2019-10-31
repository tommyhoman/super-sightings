/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.controllers;

import com.sg.supersightings.models.Power;
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
public class PowerController {

    @Autowired
    SuperSightingsService supeService;

    @GetMapping("powers")
    public String displayPowers(Model model) {
        List<Power> pows = supeService.getAllPowers();
        Power pow = new Power();
        model.addAttribute("power", pow);
        model.addAttribute("pows", pows);
        return "powers";
    }

    @PostMapping("addPower")
    public String addPower(@Valid Power power, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Power> pows = supeService.getAllPowers();
            model.addAttribute("pows", pows);
            return "powers";
        }
        supeService.addPower(power);
        return "redirect:/powers";
    }

    @GetMapping("deletePower")
    public String deletePower(Integer id) throws SuperSightingsServiceException {
        supeService.deletePowerById(id);

        return "redirect:/powers";
    }

    @GetMapping("editPower")
    public String editPower(Integer id, Model model) {
        Power pow = supeService.getPowerById(id);
        model.addAttribute("power", pow);
        return "editPower";
    }

    @PostMapping("editPower")
    public String performEditPower(@Valid Power power, BindingResult result) throws SuperSightingsServiceException {
        if (result.hasErrors()) {
            return "editPower";
        }

        supeService.updatePower(power);

        return "redirect:/powers";
    }
}
