/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.daos;

import com.sg.supersightings.models.Location;
import com.sg.supersightings.models.Organization;
import com.sg.supersightings.models.Power;
import com.sg.supersightings.models.Sighting;
import com.sg.supersightings.models.Superperson;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Thomas
 */
public interface SuperSightingsDao {
    Location getLocationById(int id);
    List<Location> getAllLocations();
    Location addLocation(Location loc);
    void updateLocation(Location loc) throws SuperSightingsDaoException;
    void deleteLocationById(int locId) throws SuperSightingsDaoException;
    
    Power getPowerById(int id);
    List<Power> getAllPowers();
    Power addPower(Power pow);
    void updatePower(Power pow) throws SuperSightingsDaoException;
    void deletePowerById(int powId) throws SuperSightingsDaoException;
    
    Superperson getSuperpersonById(int id);
    List<Superperson> getAllSuperpeople();
    Superperson addSuperperson(Superperson person) throws SuperSightingsDaoException;
    void updateSuperperson(Superperson person) throws SuperSightingsDaoException;
    void deleteSuperpersonById(int id) throws SuperSightingsDaoException;

    Organization getOrganizationById(int id);
    List<Organization> getAllOrganizations();
    Organization addOrganization(Organization org) throws SuperSightingsDaoException;
    void updateOrganization(Organization org) throws SuperSightingsDaoException;
    void deleteOrganizationById(int id) throws SuperSightingsDaoException;
    
    List<Organization> getOrganizationsForSuperperson(int personId);
    List<Superperson> getSuperpeopleForOrganization(int orgId);

    Sighting getSightingById(int id);
    List<Sighting> getAllSightings();
    Sighting addSighting(Sighting sight) throws SuperSightingsDaoException;
    void updateSighting(Sighting sight) throws SuperSightingsDaoException;
    void deleteSightingById(int id) throws SuperSightingsDaoException;
    
    List<Sighting> getSightingsForDate(LocalDate date);
    List<Location> getLocationsForSuperperson(int personId);
    List<Superperson> getSuperpeopleForLocation(int locId);

    void clearTables();
}
