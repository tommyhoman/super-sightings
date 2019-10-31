/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.services;

import com.sg.supersightings.daos.SuperSightingsDao;
import com.sg.supersightings.daos.SuperSightingsDaoException;
import com.sg.supersightings.models.Location;
import com.sg.supersightings.models.Organization;
import com.sg.supersightings.models.Power;
import com.sg.supersightings.models.Sighting;
import com.sg.supersightings.models.Superperson;
import com.sg.supersightings.viewmodels.OrganizationVM;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Thomas
 */
@Service
public class SuperSightingsService {

    @Autowired
    SuperSightingsDao supeDao;

    /**
     * *************************************************************************
     * Location stuff
     *
     **************************************************************************
     */
    public List<Location> getAllLocations() {
        return supeDao.getAllLocations();
    }

    public Location addLocation(Location loc) {
        return supeDao.addLocation(loc);
    }

    public void deleteLocationById(int id) throws SuperSightingsServiceException {
        try {
            supeDao.deleteLocationById(id);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage());
        }
    }

    public void updateLocation(Location loc) throws SuperSightingsServiceException {
        try {
            supeDao.updateLocation(loc);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    public Location getLocationById(int id) {
        return supeDao.getLocationById(id);
    }

    /**
     * *************************************************************************
     * Power stuff
     *
     **************************************************************************
     */
    public List<Power> getAllPowers() {
        return supeDao.getAllPowers();
    }

    public Power addPower(Power pow) {
        return supeDao.addPower(pow);
    }

    public void deletePowerById(Integer id) throws SuperSightingsServiceException {
        try {
            supeDao.deletePowerById(id);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    public Power getPowerById(int id) {
        return supeDao.getPowerById(id);
    }

    public void updatePower(Power pow) throws SuperSightingsServiceException {
        try {
            supeDao.updatePower(pow);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    /**
     * *************************************************************************
     * Superpeople stuff
     *
     **************************************************************************
     */
    public List<Superperson> getAllSuperpeople() {
        return supeDao.getAllSuperpeople();
    }

    public Superperson addSuperperson(Superperson person) throws SuperSightingsServiceException {
        try {
            return supeDao.addSuperperson(person);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    public void deleteSuperpersonById(int id) throws SuperSightingsServiceException {
        try {
            supeDao.deleteSuperpersonById(id);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    public Superperson getSuperpersonById(int id) {
        return supeDao.getSuperpersonById(id);
    }

    public void updateSuperperson(Superperson superperson) throws SuperSightingsServiceException {
        try {
            supeDao.updateSuperperson(superperson);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    /**
     * *************************************************************************
     * Sighting stuff
     *
     **************************************************************************
     */
    public List<Sighting> getAllSightings() {
        return supeDao.getAllSightings();
    }

    public Sighting addSighting(Sighting sight) throws SuperSightingsServiceException {
        try {
            if (sight.getDate() == null) {
                throw new SuperSightingsServiceException("Date cannot be null/empty");
            }
            return supeDao.addSighting(sight);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    public Sighting getSightingById(int id) {
        return supeDao.getSightingById(id);
    }

    public void deleteSightingById(int id) throws SuperSightingsServiceException {
        try {
            supeDao.deleteSightingById(id);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    public void updateSighting(Sighting sight) throws SuperSightingsServiceException {
        try {
            if (sight.getDate() == null) {
                throw new SuperSightingsServiceException("Date cannot be null/empty");
            }
            supeDao.updateSighting(sight);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    public List<Sighting> getRecentSightings() {
        List<Sighting> sights = supeDao.getAllSightings();
        sights.sort((s1,s2)->s2.getDate().compareTo(s1.getDate()));
        if (sights.size() < 10) {
            return sights;
        }
        return sights.subList(0,10);
    }

    /**
     * *************************************************************************
     * Org stuff
     *
     **************************************************************************
     */
    public List<Organization> getAllOrganizations() {
        return supeDao.getAllOrganizations();
    }

    public Organization addOrganization(OrganizationVM orgVM) throws SuperSightingsServiceException {
        try {
            Organization toAdd = orgVM.getOrg();
            List<Superperson> members = new ArrayList();
            orgVM.getMemberIds().stream().map((id) -> {
                Superperson member = new Superperson();
                member.setId(id);
                return member;
            }).forEachOrdered((member) -> {
                members.add(member);
            });
            toAdd.setMembers(members);
            return supeDao.addOrganization(toAdd);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    public Organization getOrganizationById(int id) {
        return supeDao.getOrganizationById(id);
    }

    public void deleteOrganizationById(int id) throws SuperSightingsServiceException {
        try {
            supeDao.deleteOrganizationById(id);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

    public void updateOrganization(OrganizationVM orgVM) throws SuperSightingsServiceException {
        try {
            Organization toEdit = orgVM.getOrg();
            List<Superperson> members = new ArrayList();
            orgVM.getMemberIds().stream().map((id) -> {
                Superperson member = new Superperson();
                member.setId(id);
                return member;
            }).forEachOrdered((member) -> {
                members.add(member);
            });
            toEdit.setMembers(members);
            supeDao.updateOrganization(toEdit);
        } catch (SuperSightingsDaoException ex) {
            throw new SuperSightingsServiceException(ex.getMessage(), ex);
        }
    }

}
