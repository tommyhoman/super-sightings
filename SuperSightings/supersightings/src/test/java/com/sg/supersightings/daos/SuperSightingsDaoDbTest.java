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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Thomas
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SuperSightingsDaoDbTest {

    @Autowired
    SuperSightingsDao toTest;

    public SuperSightingsDaoDbTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws SuperSightingsDaoException {
        // delete everything from db
        // should be able to just delete all powers, orgs, and locs
//        for(Power p : toTest.getAllPowers()){
//            toTest.deletePowerById(p.getId());
//        }
//        for(Location loc : toTest.getAllLocations()) {
//            toTest.deleteLocationById(loc.getId());
//        }
//        for(Organization o : toTest.getAllOrganizations()) {
//            toTest.deleteOrganizationById(o.getId());
//        }
        // could wrap this up into bigger queries to improve performance
        toTest.clearTables();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddAndGetLocationGoldenPath() {
        Location loc = new Location();
        loc.setName("Test loc name");
        loc.setAddress("Test address");
        loc.setDescription("Test description");
        loc.setLatitude(new BigDecimal("123.456789"));
        loc.setLongitude(new BigDecimal("987.654321"));

        loc = toTest.addLocation(loc);

        Location fromDao = toTest.getLocationById(loc.getId());
        assertEquals(loc, fromDao);
    }

    @Test
    public void testGetLocationInvalidId() {
        Location fromDao = toTest.getLocationById(-1);
        assertNull(fromDao);
    }

    @Test
    public void testGetAllLocationsGoldenPath() {
        Location loc = new Location();
        loc.setName("Test loc name 1");
        loc.setAddress("Test address 1");
        loc.setDescription("Test description 1");
        loc.setLatitude(new BigDecimal("123.456789"));
        loc.setLongitude(new BigDecimal("987.654321"));
        loc = toTest.addLocation(loc);

        Location loc2 = new Location();
        loc2.setName("Test loc name 2");
        loc2.setAddress("Test address 2");
        loc2.setDescription("Test description 2");
        loc2.setLatitude(new BigDecimal("111.111111"));
        loc2.setLongitude(new BigDecimal("999.999999"));
        loc2 = toTest.addLocation(loc2);

        List<Location> locs = toTest.getAllLocations();
        assertEquals(2, locs.size());
        assertTrue(locs.contains(loc));
        assertTrue(locs.contains(loc2));
    }

    @Test
    public void testAddLocationNull() {
        try {
            Location loc = toTest.addLocation(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testUpdateLocationGoldenPath() {
        try {
            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));

            loc = toTest.addLocation(loc);

            Location fromDao = toTest.getLocationById(loc.getId());
            assertEquals(loc, fromDao);

            loc.setName("edited test loc name");
            loc.setLatitude(new BigDecimal("112.358132"));

            toTest.updateLocation(loc);
            fromDao = toTest.getLocationById(loc.getId());

            assertEquals(loc, fromDao);

        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testUpdateLocationNull() {
        try {
            toTest.updateLocation(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (SuperSightingsDaoException ex) {
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testUpdateLocationInvalidId() {
        try {
            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc.setId(-1);

            toTest.updateLocation(loc);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testDeleteLocationGoldenPath() {
        try {
            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));

            loc = toTest.addLocation(loc);

            Location fromDao = toTest.getLocationById(loc.getId());
            assertEquals(loc, fromDao);

            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));
            sight.setPlace(loc);
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);
            person.setOrgs(new ArrayList());
            person = toTest.addSuperperson(person);
            sight.setPerson(person);
            sight = toTest.addSighting(sight);

            toTest.deleteLocationById(loc.getId());
            // verify that both the sighting and location are deleted
            fromDao = toTest.getLocationById(loc.getId());
            assertNull(fromDao);
            // check that all sightings for location are deleted???

        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    public void testDeleteLocationInvalidId() {
        try {
            toTest.deleteLocationById(-1);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
            // check that no sightings were deleted???
        }

    }

    @Test
    public void testAddAndGetPowerGoldenPath() {
        Power pow = new Power();
        pow.setName("Test Pow 1");
        pow = toTest.addPower(pow);

        Power fromDao = toTest.getPowerById(pow.getId());
        assertEquals(pow, fromDao);
    }

    @Test
    public void testGetPowerInvalidId() {
        Power fromDao = toTest.getPowerById(-1);
        assertNull(fromDao);
    }

    @Test
    public void testGetAllPowersGoldenPath() {
        Power pow = new Power();
        pow.setName("Test Pow 1");
        pow = toTest.addPower(pow);

        Power pow2 = new Power();
        pow2.setName("Test Pow 2");
        pow2 = toTest.addPower(pow2);

        List<Power> pows = toTest.getAllPowers();

        assertEquals(2, pows.size());
        assertTrue(pows.contains(pow));
        assertTrue(pows.contains(pow2));
    }

    @Test
    public void testAddPowerNull() {
        try {
            toTest.addPower(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testUpdatePowerGoldenPath() {
        try {
            Power pow = new Power();
            pow.setName("Test Pow 1");
            pow = toTest.addPower(pow);

            Power fromDao = toTest.getPowerById(pow.getId());
            assertEquals(pow, fromDao);

            pow.setName("New Pow 1");
            toTest.updatePower(pow);

            assertNotEquals(pow, fromDao);

            fromDao = toTest.getPowerById(pow.getId());

            assertEquals(pow, fromDao);
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testUpdatePowerNull() {
        try {
            toTest.updatePower(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {

        } catch (SuperSightingsDaoException ex) {
            fail("Expected IllegalArgumentException was not thrown, " + ex.getMessage());
        }
    }

    @Test
    public void testUpdatePowerInvalidId() {
        try {
            Power pow = new Power();
            pow.setName("Test Pow 1");
            pow = toTest.addPower(pow);

            Power fromDao = toTest.getPowerById(pow.getId());
            assertEquals(pow, fromDao);

            pow.setName("New Pow 1");
            pow.setId(-1);
            toTest.updatePower(pow);

            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testDeletePowerByIdGoldenPath() {
        // need to build a power, super, org, and sighting (therefore loc)
        // in case they all need to be deleted
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super decription");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);
            sight = toTest.addSighting(sight);

            Organization org = new Organization();
            org.setName("Test org name");
            org.setDescription("Test org desc");
            org.setAddress("Test org addr");
            List<Superperson> members = new ArrayList();
            members.add(person);
            org.setMembers(members);
            //Adding org does add any members to bridge
            org = toTest.addOrganization(org);

            Power fromDao = toTest.getPowerById(pow.getId());
            assertEquals(pow, fromDao);

            toTest.deletePowerById(pow.getId());
            fromDao = toTest.getPowerById(pow.getId());
            assertNull(fromDao);
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testDeletePowerInvalidId() {
        try {
            toTest.deletePowerById(-1);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testAddAndGetSuperpersonGoldenPath() {
        try {
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            //Add super does not add any orgs for that obj
            person.setOrgs(new ArrayList());

            person = toTest.addSuperperson(person);

            Superperson fromDao = toTest.getSuperpersonById(person.getId());
            assertEquals(person, fromDao);
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetSuperpersonInvalidId() {
        Superperson fromDao = toTest.getSuperpersonById(-1);
        assertNull(fromDao);
    }

    @Test
    public void testGetAllSuperpeopleGoldenPath() {
        try {
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);
            //Add super does not add any orgs for that obj
            person = toTest.addSuperperson(person);

            Superperson person2 = new Superperson();
            person2.setName("Test super name 2");
            person2.setDescription("Test super description 2");
            person2.setPow(pow);
            person2 = toTest.addSuperperson(person2);

            List<Superperson> people = toTest.getAllSuperpeople();

            assertEquals(2, people.size());
            assertTrue(people.contains(person));
            assertTrue(people.contains(person2));
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testAddSuperpersonNull() {
        try {
            Superperson person = toTest.addSuperperson(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {

        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testAddSuperpersonInvalidPowerId() {
        try {
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");

            Power pow = new Power();
            pow.setName("power not in db");
            pow.setId(-1);
            person.setPow(pow);

            //Add super does not add any orgs for that obj
            person.setOrgs(new ArrayList());

            person = toTest.addSuperperson(person);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
            // Check that nothing was added to db???
        }
    }

    @Test
    public void testUpdateSuperpersonGoldenPath() {
        try {
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            //Add super does not add any orgs for that obj
            person.setOrgs(new ArrayList());
            person = toTest.addSuperperson(person);

            Superperson fromDao = toTest.getSuperpersonById(person.getId());
            assertEquals(person, fromDao);

            person.setName("Edited test name");
            pow = new Power();
            pow.setName("Test power 2");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            toTest.updateSuperperson(person);

            fromDao = toTest.getSuperpersonById(person.getId());
            assertEquals(person, fromDao);

        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testUpdateSuperpersonNull() {
        try {
            toTest.updateSuperperson(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (SuperSightingsDaoException ex) {
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testUpdateSuperpersonInvalidId() {
        try {
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            //Add super does not add any orgs for that obj
            person.setOrgs(new ArrayList());
            person.setId(-1);
            toTest.updateSuperperson(person);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testUpdateSuperpersonInvalidPowId() {
        try {
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            //Add super does not add any orgs for that obj
            person.setOrgs(new ArrayList());
            person = toTest.addSuperperson(person);

            Superperson fromDao = toTest.getSuperpersonById(person.getId());
            assertEquals(person, fromDao);

            person.setName("Edited test name");
            pow = new Power();
            pow.setName("Test power 2");
            pow.setId(-1);
            person.setPow(pow);

            toTest.updateSuperperson(person);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testDeleteSuperpersonGoldenPath() {
        try {
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            //Add super does not add any orgs for that obj
            person.setOrgs(new ArrayList());

            person = toTest.addSuperperson(person);

            Superperson fromDao = toTest.getSuperpersonById(person.getId());
            assertEquals(person, fromDao);

            toTest.deleteSuperpersonById(person.getId());

            fromDao = toTest.getSuperpersonById(person.getId());
            assertNull(fromDao);
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testDeleteSuperpersonInvalidId() {
        try {
            toTest.deleteSuperpersonById(-1);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testAddAndGetOrganizationGoldenPath() {
        try {
            Organization org = new Organization();
            org.setName("Test org name");
            org.setDescription("Test org desc");
            org.setAddress("Test org addr");

            List<Superperson> members = new ArrayList();
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super decription");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            person = toTest.addSuperperson(person);
            members.add(person);
            org.setMembers(members);

            //Adding org does add any members to bridge
            org = toTest.addOrganization(org);

            //Get should pull all the members as well
            Organization fromDao = toTest.getOrganizationById(org.getId());
            assertEquals(org, fromDao);
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetOrganizationInvalidId() {
        Organization fromDao = toTest.getOrganizationById(-1);
        assertNull(fromDao);
    }

    @Test
    public void testGetAllOrganizationsGoldenPath() {
        try {
            Organization org = new Organization();
            org.setName("Test org name");
            org.setDescription("Test org desc");
            org.setAddress("Test org addr");
            org = toTest.addOrganization(org);

            Organization org2 = new Organization();
            org2.setName("Test org name");
            org2.setDescription("Test org desc");
            org2.setAddress("Test org addr");
            org2 = toTest.addOrganization(org2);

            List<Organization> orgs = toTest.getAllOrganizations();
            assertEquals(2, orgs.size());
            assertTrue(orgs.contains(org));
            assertTrue(orgs.contains(org2));

        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testAddOrganizationNull() {
        try {
            toTest.addOrganization(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {

        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    public void testAddOrganizationInvalidSuperId() {
        try {
            Organization org = new Organization();
            org.setName("Test org name");
            org.setDescription("Test org desc");
            org.setAddress("Test org addr");

            List<Superperson> members = new ArrayList();
            Superperson person = new Superperson();
            person.setName("person not in db");
            person.setDescription("nonexistant");
            person.setId(-1);

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            members.add(person);
            org.setMembers(members);

            //Adding org does add any members to bridge
            org = toTest.addOrganization(org);
            fail("Expected SuperSightingsException was not thrown");
        } catch (SuperSightingsDaoException ex) {
            // TODO Verify that no part of transaction went through
        }
    }

    @Test
    public void testUpdateOrganizationGoldenPath() {
        try {
            Organization org = new Organization();
            org.setName("Test org name");
            org.setDescription("Test org desc");
            org.setAddress("Test org addr");

            List<Superperson> members = new ArrayList();
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super decription");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            person = toTest.addSuperperson(person);
            members.add(person);
            org.setMembers(members);

            //Adding org does add any members to bridge
            org = toTest.addOrganization(org);

            Organization fromDao = toTest.getOrganizationById(org.getId());
            assertEquals(org, fromDao);

            Superperson person2 = new Superperson();
            person2.setName("Test name 2");
            person2.setPow(pow);
            person2 = toTest.addSuperperson(person2);
            members.add(person2);
            org.setMembers(members);
            toTest.updateOrganization(org);

            fromDao = toTest.getOrganizationById(org.getId());
            assertEquals(org, fromDao);
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testUpdateOrganizationNull() {
        try {
            toTest.updateOrganization(null);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testUpdateOrganizationInvalidId() {
        try {
            Organization org = new Organization();
            org.setName("Test org name");
            org.setDescription("Test org desc");
            org.setAddress("Test org addr");

            List<Superperson> members = new ArrayList();
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super decription");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            person = toTest.addSuperperson(person);
            members.add(person);
            org.setMembers(members);

            //Adding org does add any members to bridge
            org.setId(-1);
            toTest.updateOrganization(org);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testDeleteOrganizationGoldenPath() {
        try {
            Organization org = new Organization();
            org.setName("Test org name");
            org.setDescription("Test org desc");
            org.setAddress("Test org addr");

            List<Superperson> members = new ArrayList();
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super decription");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            person = toTest.addSuperperson(person);
            members.add(person);
            org.setMembers(members);

            //Adding org does add any members to bridge
            org = toTest.addOrganization(org);

            //Get should pull all the members as well
            Organization fromDao = toTest.getOrganizationById(org.getId());
            assertEquals(org, fromDao);

            toTest.deleteOrganizationById(org.getId());

            fromDao = toTest.getOrganizationById(org.getId());
            assertNull(fromDao);
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testDeleteOrganizationInvalidId() {
        try {
            toTest.deleteOrganizationById(-1);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }

    }

    @Test
    public void testGetSuperpeopleForOrganizationGoldenPath() {
        try {
            // need to add two supers to org
            Organization org = new Organization();
            org.setName("Test org name");
            org.setDescription("Test org desc");
            org.setAddress("Test org addr");

            List<Superperson> members = new ArrayList();
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super decription");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            person = toTest.addSuperperson(person);
            members.add(person);

            Superperson person2 = new Superperson();
            person2.setName("Test super 2");
            person2.setDescription("Test desc 2");
            person2.setPow(pow);
            person2 = toTest.addSuperperson(person2);
            members.add(person2);

            org.setMembers(members);

            //Adding org does add any members to bridge
            org = toTest.addOrganization(org);

            List<Superperson> fromDao = toTest.getSuperpeopleForOrganization(org.getId());
            assertEquals(2, fromDao.size());
            assertTrue(fromDao.contains(person));
            assertTrue(fromDao.contains(person2));
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetSuperpeopleForOrganizationInvalidId() {
        List<Superperson> fromDao = toTest.getSuperpeopleForOrganization(-1);
        assertTrue(fromDao.isEmpty());
    }

    @Test
    public void testGetOrganizationsForSuperpersonGoldenPath() {
        try {
            // add a person part of two orgs
            Organization org = new Organization();
            org.setName("Test org name");
            org.setDescription("Test org desc");
            org.setAddress("Test org addr");

            List<Superperson> members = new ArrayList();
            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super decription");

            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            person.setPow(pow);

            person = toTest.addSuperperson(person);
            members.add(person);

            Organization org2 = new Organization();
            org2.setName("Test name 2");
            org2.setDescription("Test descr 2");
            org2.setAddress("Test addr 2");

            org.setMembers(members);
            org2.setMembers(members);

            org = toTest.addOrganization(org);
            org2 = toTest.addOrganization(org2);
            // dao wont have set the members
            org.setMembers(null);
            org2.setMembers(null);

            List<Organization> fromDao = toTest.getOrganizationsForSuperperson(person.getId());

            assertEquals(2, fromDao.size());
            assertTrue(fromDao.contains(org));
            assertTrue(fromDao.contains(org2));

        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetOrganizationsForSuperpersonInvalidId() {
        List<Organization> fromDao = toTest.getOrganizationsForSuperperson(-1);
        assertTrue(fromDao.isEmpty());
    }

    @Test
    public void testAddAndGetSightingGoldenPath() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            // add the full power to the db
            // but only expect the sighting to populate to super name
            // so set name to check to null
            pow.setName(null);
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);

            sight = toTest.addSighting(sight);

            Sighting fromDao = toTest.getSightingById(sight.getId());
            assertEquals(sight, fromDao);
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetSightingInvalidId() {
        Sighting fromDao = toTest.getSightingById(-1);
        assertNull(fromDao);
    }

    @Test
    public void testGetAllSightings() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            // add the full power to the db
            // but only expect the sighting to populate to super name
            // so set name to check to null
            pow.setName(null);
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);
            sight = toTest.addSighting(sight);

            Sighting sight2 = new Sighting();
            sight2.setDate(LocalDate.of(1996, 12, 31));
            sight2.setPlace(loc);
            sight2.setPerson(person);
            sight2 = toTest.addSighting(sight2);

            List<Sighting> sights = toTest.getAllSightings();
            assertEquals(2, sights.size());
            assertTrue(sights.contains(sight));
            assertTrue(sights.contains(sight2));
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testAddSightingNull() {
        try {
            toTest.addSighting(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {

        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testAddSightingInvalidSuperId() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("not in db super name");
            person.setDescription("not in db decription");
            person.setId(-1);
            sight.setPerson(person);

            sight = toTest.addSighting(sight);
            fail("Expected SuperSightingsDaoException was not thrown");

        } catch (SuperSightingsDaoException ex) {

        }
    }

    @Test
    public void testUpdateSightingGoldenPath() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            // add the full power to the db
            // but only expect the sighting to populate to super name
            // so set name to check to null
            pow.setName(null);
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);

            sight = toTest.addSighting(sight);

            Sighting fromDao = toTest.getSightingById(sight.getId());
            assertEquals(sight, fromDao);

            Location loc2 = new Location();
            loc2.setName("Test loc name 2");
            loc2.setAddress("Test address 2");
            loc2.setDescription("Test description 2");
            loc2.setLatitude(new BigDecimal("123.456789"));
            loc2.setLongitude(new BigDecimal("987.654321"));
            loc2 = toTest.addLocation(loc2);
            sight.setPlace(loc2);
            sight.setDate(LocalDate.of(1996, 5, 5));

            toTest.updateSighting(sight);

            fromDao = toTest.getSightingById(sight.getId());
            assertEquals(sight, fromDao);

        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testUpdateSightingNull() {
        try {
            toTest.updateSighting(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (SuperSightingsDaoException ex) {
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testUpdateSightingInvalidId() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            // add the full power to the db
            // but only expect the sighting to populate to super name
            // so set name to check to null
            pow.setName(null);
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);

            sight.setId(-1);

            toTest.updateSighting(sight);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testUpdateSightingInvalidFK() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            // add the full power to the db
            // but only expect the sighting to populate to super name
            // so set name to check to null
            pow.setName(null);
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);

            sight = toTest.addSighting(sight);

            Location loc2 = new Location();
            loc2.setName("Test loc name 2");
            loc2.setAddress("Test address 2");
            loc2.setDescription("Test description 2");
            loc2.setLatitude(new BigDecimal("123.456789"));
            loc2.setLongitude(new BigDecimal("987.654321"));
            loc2.setId(-1);
            sight.setPlace(loc2);

            toTest.updateSighting(sight);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testDeleteSightingGoldenPath() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super decription");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            // add the full power to the db
            // but only expect the sighting to populate to super name
            // so set name to check to null
            pow.setName(null);
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);

            sight = toTest.addSighting(sight);

            Sighting fromDao = toTest.getSightingById(sight.getId());
            assertEquals(sight, fromDao);

            toTest.deleteSightingById(sight.getId());

            fromDao = toTest.getSightingById(sight.getId());
            assertNull(fromDao);
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testDeleteSightingInvalidId() {
        try {
            toTest.deleteSightingById(-1);
            fail("Expected SuperSightingsDaoException was not thrown");
        } catch (SuperSightingsDaoException ex) {
        }
    }

    @Test
    public void testGetSightingsForDateGoldenPath() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            // add the full power to the db
            // but only expect the sighting to populate to super name
            // so set name to check to null
            pow.setName(null);
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);
            sight = toTest.addSighting(sight);

            Sighting sight2 = new Sighting();
            sight2.setDate(LocalDate.of(2000, 1, 1));
            sight2.setPlace(loc);
            sight2.setPerson(person);
            sight2 = toTest.addSighting(sight2);

            List<Sighting> sights = toTest.getSightingsForDate(LocalDate.of(2000, 1, 1));
            assertEquals(2, sights.size());
            assertTrue(sights.contains(sight));
            assertTrue(sights.contains(sight2));
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetSightingsForDateNull() {
        try {
            toTest.getSightingsForDate(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testGetLocationsForSuperperson() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            // add the full power to the db
            // but only expect the sighting to populate to super name
            // so set name to check to null
            pow.setName(null);
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);
            sight = toTest.addSighting(sight);

            Sighting sight2 = new Sighting();
            sight2.setDate(LocalDate.of(2000, 1, 1));

            Location loc2 = new Location();
            loc2.setName("test loc 2");
            loc2.setDescription("test desc 2");
            loc2.setAddress("test addr 2");
            loc2.setLatitude(new BigDecimal("111.111111"));
            loc2.setLongitude(new BigDecimal("999.999999"));
            loc2 = toTest.addLocation(loc2);

            sight2.setPlace(loc2);
            sight2.setPerson(person);
            sight2 = toTest.addSighting(sight2);

            List<Location> locs = toTest.getLocationsForSuperperson(person.getId());
            assertEquals(2, locs.size());
            assertTrue(locs.contains(loc));
            assertTrue(locs.contains(loc2));
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetLocationsForSuperpersonInvalidId() {
        List<Location> locs = toTest.getLocationsForSuperperson(-1);
        assertTrue(locs.isEmpty());
    }

    @Test
    public void testGetSuperpeopleForLocationGoldenPath() {
        try {
            Sighting sight = new Sighting();
            sight.setDate(LocalDate.of(2000, 1, 1));

            Location loc = new Location();
            loc.setName("Test loc name");
            loc.setAddress("Test address");
            loc.setDescription("Test description");
            loc.setLatitude(new BigDecimal("123.456789"));
            loc.setLongitude(new BigDecimal("987.654321"));
            loc = toTest.addLocation(loc);
            sight.setPlace(loc);

            Superperson person = new Superperson();
            person.setName("Test super name");
            person.setDescription("Test super description");
            Power pow = new Power();
            pow.setName("Test power name");
            pow = toTest.addPower(pow);
            // add the full power to the db
            // but only expect the sighting to populate to super name
            // so set name to check to null
            person.setPow(pow);
            person = toTest.addSuperperson(person);
            sight.setPerson(person);
            sight = toTest.addSighting(sight);

            Sighting sight2 = new Sighting();
            sight2.setDate(LocalDate.of(2000, 1, 1));
            sight2.setPlace(loc);
            Superperson person2 = new Superperson();
            person2.setName("test super name 2");
            person2.setDescription("test desc 2");
            person2.setPow(pow);
            person2 = toTest.addSuperperson(person2);
            sight2.setPerson(person2);
            sight2 = toTest.addSighting(sight2);

            List<Superperson> people = toTest.getSuperpeopleForLocation(loc.getId());
            assertEquals(2, people.size());
            assertTrue(people.contains(person));
            assertTrue(people.contains(person2));
        } catch (SuperSightingsDaoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetSuperpeopleForLocationInvalidId() {
        List<Superperson> people = toTest.getSuperpeopleForLocation(-1);
        assertTrue(people.isEmpty());

    }

}
