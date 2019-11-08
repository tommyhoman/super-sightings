/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.controllers;

import com.sg.supersightings.models.Organization;
import com.sg.supersightings.models.Superperson;
import com.sg.supersightings.services.SuperSightingsService;
import com.sg.supersightings.services.SuperSightingsServiceException;
import com.sg.supersightings.viewmodels.OrganizationVM;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
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
public class OrganizationController {

    @Autowired
    SuperSightingsService supeService;

    @GetMapping("organizations")
    public String displayOrganizations(Model model) {
        List<Superperson> people = supeService.getAllSuperpeople();
        List<Organization> orgs = supeService.getAllOrganizations();
        Organization org = new Organization();
        model.addAttribute("organizationvm", org);
        model.addAttribute("people", people);
        model.addAttribute("orgs", orgs);
        return "organizations";
    }

    @PostMapping("addOrganization")
    public String addOrganization(@Valid OrganizationVM organizationVM,
            BindingResult result,
            Model model) throws SuperSightingsServiceException {
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Organization>> violations = validate.validate(organizationVM.getOrg());
        if (result.hasErrors() || !violations.isEmpty()) {
            List<Superperson> people = supeService.getAllSuperpeople();
            List<Organization> orgs = supeService.getAllOrganizations();
            model.addAttribute("organizationvm", organizationVM);
            model.addAttribute("errors", violations);
            model.addAttribute("people", people);
            model.addAttribute("orgs", orgs);
            return "organizations";
        }
        supeService.addOrganization(organizationVM);

        return "redirect:/organizations";
    }

    @GetMapping("viewOrganization")
    public String organizationDetail(Integer id, Model model) {
        Organization org = supeService.getOrganizationById(id);
        model.addAttribute("organization", org);
        return "organizationDetail";
    }

    @GetMapping("deleteOrganization")
    public String deleteOrganization(Integer id) throws SuperSightingsServiceException {
        supeService.deleteOrganizationById(id);

        return "redirect:/organizations";
    }

    @GetMapping("editOrganization")
    public String editOrganization(Integer id, Model model) {
        Organization org = supeService.getOrganizationById(id);
        List<Superperson> people = supeService.getAllSuperpeople();
        model.addAttribute("organizationvm", org);
        model.addAttribute("people", people);
        return "editOrganization";
    }

    @PostMapping("editOrganization")
    public String performEditOrganization(@Valid OrganizationVM orgVM, BindingResult result) throws SuperSightingsServiceException {
        if (result.hasErrors()) {
            return "editOrganization";
        }

        supeService.updateOrganization(orgVM);

        return "redirect:/organizations";
    }
}
