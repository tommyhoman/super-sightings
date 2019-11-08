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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Thomas
 */
@Repository
public class SuperSightingsDaoDb implements SuperSightingsDao {

    @Autowired
    JdbcTemplate jdbc;

    private static final String BASE_SELECT_LOCATION
            = "SELECT l.id locationId, l.name locName, l.description locDescription, l.address locAddress, "
            + "l.latitude locLat, l.longitude locLong FROM locations l ";
    private static final String BASE_SELECT_POWER
            = "SELECT p.* FROM powers p ";
    private final static String BASE_SELECT_SUPERPEOPLE
            = "SELECT s.Id superId,"
            + " s.Name superName, s.description superDescription,"
            + " s.PowerId powerId, p.Name powerName FROM superpeople s "
            + "JOIN powers p ON p.id = s.powerId ";
    private final static String BASE_SELECT_ORGANIZATION
            = "SELECT o.* FROM organizations o ";
    private static final String BASE_SELECT_SIGHTING = "SELECT "
            + "s.Id sightingId, s.Date sightingDate, s.SuperpersonId superId, s.LocationId locationId, "
            + "p.Name superName, p.description superDescription, p.powerid superPowerId, "
            + "l.name locName, l.description locDescription, l.address locAddress, "
            + "l.latitude locLat, l.longitude locLong FROM sightings s "
            + "JOIN superpeople p on s.superpersonid = p.id "
            + "JOIN locations l on s.locationId = l.id ";

    /* *************************************************************************
        Begin the location dao methods and mapper
    ************************************************************************* */
    @Override
    public Location getLocationById(int id) {
        try {
            final String SELECT_LOCATION_BY_ID = BASE_SELECT_LOCATION + " WHERE id = ?";
            return jdbc.queryForObject(SELECT_LOCATION_BY_ID, new LocationMapper(), id);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Location> getAllLocations() {
        return jdbc.query(BASE_SELECT_LOCATION, new LocationMapper());
    }

    @Override
    public Location addLocation(Location loc) {
        if (loc == null
                || loc.getName() == null
                || loc.getAddress() == null
                || loc.getLatitude() == null
                || loc.getLongitude() == null) {
            throw new IllegalArgumentException("Cannot add null values to database");
        }
        final String INSERT_LOCATION = "INSERT INTO locations(`Name`,`Description`,Address,Latitude,Longitude) VALUES "
                + "(?,?,?,?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    INSERT_LOCATION,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, loc.getName());
            statement.setString(2, loc.getDescription());
            statement.setString(3, loc.getAddress());
            statement.setBigDecimal(4, loc.getLatitude());
            statement.setBigDecimal(5, loc.getLongitude());
            return statement;

        }, keyHolder);

        loc.setId(keyHolder.getKey().intValue());

        return loc;
    }

    @Override
    public void updateLocation(Location loc) throws SuperSightingsDaoException {
        if (loc == null
                || loc.getName() == null
                || loc.getAddress() == null
                || loc.getLatitude() == null
                || loc.getLongitude() == null) {
            throw new IllegalArgumentException("Cannot edit loc to have null value");
        }
        final String UPDATE_LOCATION = "UPDATE locations "
                + "SET name=?, description=?, address=?, "
                + "latitude=?, longitude=? "
                + "WHERE id=?";
        int rowsAffected = jdbc.update(UPDATE_LOCATION,
                loc.getName(),
                loc.getDescription(),
                loc.getAddress(),
                loc.getLatitude(),
                loc.getLongitude(),
                loc.getId());
        if (rowsAffected < 1) {
            throw new SuperSightingsDaoException("Cannot find location with id "
                    + loc.getId() + " to edit");
        }
        if (rowsAffected > 1) {
            throw new IncorrectResultSizeDataAccessException(
                    "ERROR!!! changed multiple values in database when trying "
                    + "to edit a location", 1, rowsAffected);
        }
    }

    @Override
    @Transactional
    public void deleteLocationById(int locId) throws SuperSightingsDaoException {
        // to delete a location we need to delete all sightings for loc
//        for (Sighting sight : getAllSightings()) {
//            if (sight.getPlace().getId() == locId) {
//                deleteSightingById(sight.getId());
//            }
//        }
        deleteSightingsForLocation(locId);
        final String DELETE_LOCATION = "DELETE FROM locations WHERE id = ? ";
        int rowsAffected = jdbc.update(DELETE_LOCATION, locId);
        if (rowsAffected < 1) {
            throw new SuperSightingsDaoException("Cannot find location with id "
                    + locId + " to delete");
        }
        if (rowsAffected > 1) {
            throw new IncorrectResultSizeDataAccessException(
                    "ERROR!!! changed multiple values in database when trying "
                    + "to delete a location", 1, rowsAffected);
        }
    }

    private void deleteSightingsForLocation(int locId) {
        final String DELETE_SIGHTINGS_FOR_LOCATION = "DELETE FROM sightings WHERE locationId = ?";
        // could be any num of rows
        jdbc.update(DELETE_SIGHTINGS_FOR_LOCATION, locId);
    }

    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int i) throws SQLException {
            Location loc = new Location();
            loc.setId(rs.getInt("locationId"));
            loc.setName(rs.getString("locName"));
            loc.setDescription(rs.getString("locDescription"));
            loc.setAddress(rs.getString("locAddress"));
            loc.setLatitude(rs.getBigDecimal("locLat"));
            loc.setLongitude(rs.getBigDecimal("locLong"));
            return loc;
        }

    }

    /* *************************************************************************
        Begin the power dao methods and mapper
    ************************************************************************* */
    @Override
    public Power getPowerById(int id) {
        try {
            final String SELECT_POWER_BY_ID = BASE_SELECT_POWER
                    + " WHERE id = ?";
            return jdbc.queryForObject(SELECT_POWER_BY_ID, new PowerMapper(), id);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Power> getAllPowers() {
        return jdbc.query(BASE_SELECT_POWER, new PowerMapper());
    }

    @Override
    public Power addPower(Power pow) {
        if (pow == null
                || pow.getName() == null) {
            throw new IllegalArgumentException("Cannot add null value to db");
        }

        final String INSERT_POWER = "INSERT INTO powers(`Name`) VALUES "
                + "(?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    INSERT_POWER,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, pow.getName());
            return statement;

        }, keyHolder);

        pow.setId(keyHolder.getKey().intValue());

        return pow;
    }

    @Override
    public void updatePower(Power pow) throws SuperSightingsDaoException {
        if (pow == null
                || pow.getName() == null) {
            throw new IllegalArgumentException("Cannot add null value to db");
        }
        final String UPDATE_POWER = "UPDATE powers "
                + "SET name = ? "
                + "WHERE id = ?";
        int rowsAffected = jdbc.update(UPDATE_POWER,
                pow.getName(),
                pow.getId());
        if (rowsAffected < 1) {
            throw new SuperSightingsDaoException("Cannot find power with id "
                    + pow.getId() + " to update");
        }
        if (rowsAffected > 1) {
            throw new IncorrectResultSizeDataAccessException(
                    "ERROR!!! changed multiple values in database when trying "
                    + "to update one power", 1, rowsAffected);
        }
    }

    @Override
    @Transactional
    public void deletePowerById(int powId) throws SuperSightingsDaoException {
        // To delete a power need to delete supers
        // which means we need to delete all sightings of said super
        // add remove the super from all orgs they are a part of
        for (Superperson p : getAllSuperpeople()) {
            if (p.getPow().getId() == powId) {
                deleteSuperpersonById(p.getId());
            }
        }
        final String DELETE_POWER = "DELETE FROM powers "
                + "WHERE id = ?";
        int rowsAffected = jdbc.update(DELETE_POWER, powId);
        if (rowsAffected < 1) {
            throw new SuperSightingsDaoException("Cannot find power with id "
                    + powId + " to delete");
        }
        if (rowsAffected > 1) {
            throw new IncorrectResultSizeDataAccessException(
                    "ERROR!!! changed multiple values in database when trying "
                    + "to delete a power", 1, rowsAffected);
        }
    }

    private static final class PowerMapper implements RowMapper<Power> {

        @Override
        public Power mapRow(ResultSet rs, int i) throws SQLException {
            Power pow = new Power();
            pow.setId(rs.getInt("id"));
            pow.setName(rs.getString("name"));
            return pow;
        }

    }

    /* *************************************************************************
        Begin the superperson dao methods and mapper
    ************************************************************************* */
    @Override
    public Superperson getSuperpersonById(int id) {
        try {
            final String GET_SUPERPERSON_BY_ID = BASE_SELECT_SUPERPEOPLE + " WHERE s.Id = ?";
            Superperson person = jdbc.queryForObject(GET_SUPERPERSON_BY_ID, new SuperpersonMapper(), id);
            person.setOrgs(getOrganizationsForSuperperson(person.getId()));
            return person;
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Superperson> getAllSuperpeople() {
        return jdbc.query(BASE_SELECT_SUPERPEOPLE, new SuperpersonMapper());
    }

    @Override
    public Superperson addSuperperson(Superperson person) throws SuperSightingsDaoException {
        try {
            if (person == null
                    || person.getName() == null
                    || person.getPow() == null) {
                throw new IllegalArgumentException("Cannot add null value for person to db");
            }
            final String INSERT_SUPERPERSON = "INSERT INTO superpeople(Name, Description, PowerId) VALUES "
                    + "(?,?,?)";
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update((Connection conn) -> {

                PreparedStatement statement = conn.prepareStatement(
                        INSERT_SUPERPERSON,
                        Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, person.getName());
                statement.setString(2, person.getDescription());
                statement.setInt(3, person.getPow().getId());
                return statement;

            }, keyHolder);

            person.setId(keyHolder.getKey().intValue());

            return person;
        } catch (DataIntegrityViolationException ex) {
            throw new SuperSightingsDaoException("Cannot add person, " + ex.getMessage());
        }

    }

    @Override
    public void updateSuperperson(Superperson person) throws SuperSightingsDaoException {
        try {
            if (person == null
                    || person.getName() == null
                    || person.getPow() == null) {
                throw new IllegalArgumentException("Cannot edit person to have null value");
            }
            final String UPDATE_SUPERPERSON = "UPDATE superpeople "
                    + "SET name=?, description=?, "
                    + "powerid=? "
                    + "WHERE id=?";
            int rowsAffected = jdbc.update(UPDATE_SUPERPERSON,
                    person.getName(),
                    person.getDescription(),
                    person.getPow().getId(),
                    person.getId());
            if (rowsAffected < 1) {
                throw new SuperSightingsDaoException("Cannot find person with id "
                        + person.getId() + " to edit");
            }
            if (rowsAffected > 1) {
                throw new IncorrectResultSizeDataAccessException(
                        "ERROR!!! changed multiple values in database when trying "
                        + "to edit a person", 1, rowsAffected);
            }
        } catch (DataIntegrityViolationException ex) {
            throw new SuperSightingsDaoException("Cannot edit person, " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteSuperpersonById(int id) throws SuperSightingsDaoException {
        // to delete a super need to remove them from orgs
        // and delete sightings they are in
        final String DELETE_SUPERPOEPLEORGANIZATIONS_FOR_SUPER
                = "DELETE FROM superpeopleorganizations "
                + "WHERE superpersonid = ?";
        jdbc.update(DELETE_SUPERPOEPLEORGANIZATIONS_FOR_SUPER, id);
        final String DELETE_SIGHTINGS_FOR_SUPER
                = "DELETE FROM sightings "
                + "WHERE superpersonid = ?";
        jdbc.update(DELETE_SIGHTINGS_FOR_SUPER, id);
        final String DELETE_SUPERPERSON = "DELETE FROM superpeople "
                + "WHERE id = ?";
        int rowsAffected = jdbc.update(DELETE_SUPERPERSON, id);
        if (rowsAffected < 1) {
            throw new SuperSightingsDaoException("Cannot find super with id "
                    + id + " to delete");
        }
        if (rowsAffected > 1) {
            throw new IncorrectResultSizeDataAccessException(
                    "ERROR!!! changed multiple values in database when trying "
                    + "to delete a super", 1, rowsAffected);
        }
    }

    public static final class SuperpersonMapper implements RowMapper<Superperson> {

        @Override
        public Superperson mapRow(ResultSet rs, int i) throws SQLException {
            Superperson person = new Superperson();
            person.setId(rs.getInt("superId"));
            person.setName(rs.getString("superName"));
            person.setDescription(rs.getString("superDescription"));

            Power pow = new Power();
            pow.setId(rs.getInt("powerId"));
            pow.setName(rs.getString("powerName"));
            person.setPow(pow);

            return person;
        }

    }

    /* *************************************************************************
        Begin the organization dao methods and mapper
    ************************************************************************* */
    @Override
    public Organization getOrganizationById(int id) {
        try {
            final String SELECT_ORG_BY_ID = BASE_SELECT_ORGANIZATION + " WHERE id = ?";
            Organization org = jdbc.queryForObject(SELECT_ORG_BY_ID, new OrganizationMapper(), id);
            org.setMembers(getSuperpeopleForOrganization(id));
            return org;
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return jdbc.query(BASE_SELECT_ORGANIZATION, new OrganizationMapper());
    }

    @Override
    @Transactional
    public Organization addOrganization(Organization org) throws SuperSightingsDaoException {
        if (org == null
                || org.getName() == null
                || org.getAddress() == null) {
            throw new IllegalArgumentException("Cannot add org with null value");
        }
        final String INSERT_ORG = "INSERT INTO organizations(name, description, address) "
                + "VALUES(?,?,?) ";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    INSERT_ORG,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, org.getName());
            statement.setString(2, org.getDescription());
            statement.setString(3, org.getAddress());
            return statement;

        }, keyHolder);

        org.setId(keyHolder.getKey().intValue());
        insertSuperpeopleOrganizationsForOrg(org);

        return org;
    }

    @Override
    @Transactional
    public void updateOrganization(Organization org) throws SuperSightingsDaoException {
        if (org == null
                || org.getName() == null
                || org.getAddress() == null) {
            throw new IllegalArgumentException("Cannot edit org to have null value");
        }
        final String UPDATE_ORGANIZATION = "UPDATE organizations "
                + "SET name=?, description=?, "
                + "address=? "
                + "WHERE id=?";
        int rowsAffected = jdbc.update(UPDATE_ORGANIZATION,
                org.getName(),
                org.getDescription(),
                org.getAddress(),
                org.getId());
        if (rowsAffected < 1) {
            throw new SuperSightingsDaoException("Cannot find organization with id "
                    + org.getId() + " to edit");
        }
        if (rowsAffected > 1) {
            throw new IncorrectResultSizeDataAccessException(
                    "ERROR!!! changed multiple values in database when trying "
                    + "to edit a org", 1, rowsAffected);
        }
        deleteSuperpeopleOrganizationsForOrg(org.getId());
        insertSuperpeopleOrganizationsForOrg(org);
    }

    @Override
    @Transactional
    public void deleteOrganizationById(int id) throws SuperSightingsDaoException {
        // todelete orgs need to delete all their entries from bridge
        deleteSuperpeopleOrganizationsForOrg(id);
        final String DELETE_ORGANIZATION = "DELETE FROM organizations "
                + "WHERE id = ?";
        int rowsAffected = jdbc.update(DELETE_ORGANIZATION, id);
        if (rowsAffected < 1) {
            throw new SuperSightingsDaoException("Cannot find location with id "
                    + id + " to delete");
        }
        if (rowsAffected > 1) {
            throw new IncorrectResultSizeDataAccessException(
                    "ERROR!!! changed multiple values in database when trying "
                    + "to delete an organization", 1, rowsAffected);
        }
    }

    private void deleteSuperpeopleOrganizationsForOrg(int id) {
        final String DELETE_SUPERPEOPLEORGANIZATIONS_FOR_ORG
                = "DELETE FROM superpeopleorganizations WHERE organizationId = ?";
        jdbc.update(DELETE_SUPERPEOPLEORGANIZATIONS_FOR_ORG, id);
    }

    public static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int i) throws SQLException {
            Organization org = new Organization();
            org.setId(rs.getInt("id"));
            org.setName(rs.getString("name"));
            org.setDescription(rs.getString("description"));
            org.setAddress(rs.getString("address"));
            return org;
        }

    }

    /* *************************************************************************
        Combination superperson and organization methods
    ************************************************************************* */
    @Override
    public List<Superperson> getSuperpeopleForOrganization(int orgId) {
        final String SELECT_MEMBERS_FOR_ORG = BASE_SELECT_SUPERPEOPLE
                + "JOIN superpeopleorganizations so ON s.id = so.superpersonid "
                + "WHERE so.organizationid = ?";
        return jdbc.query(SELECT_MEMBERS_FOR_ORG, new SuperpersonMapper(), orgId);
    }

    @Override
    public List<Organization> getOrganizationsForSuperperson(int personId) {
        final String SELECT_ORGS_FOR_SUPER = BASE_SELECT_ORGANIZATION
                + "JOIN superpeopleorganizations so ON o.id = so.organizationId "
                + "WHERE so.superpersonId = ?";
        return jdbc.query(SELECT_ORGS_FOR_SUPER, new OrganizationMapper(), personId);
    }

    private void insertSuperpeopleOrganizationsForOrg(Organization org) throws SuperSightingsDaoException {
        if (org.getMembers() == null) {
            return;
        }
        try {
            final String INSERT_SUPERPERSON_ORGANIZATION = "INSERT INTO superpeopleorganizations(organizationid, superpersonid) "
                    + "VALUES(?,?) ";
            org.getMembers().forEach((p) -> {
                jdbc.update(INSERT_SUPERPERSON_ORGANIZATION, org.getId(), p.getId());
            });
        } catch (DataIntegrityViolationException ex) {
            throw new SuperSightingsDaoException(
                    "Cannot add org into db, " + ex.getMessage());
        }
    }

    /* *************************************************************************
        Begin Sightings Dao Methods
    ************************************************************************* */
    @Override
    public Sighting getSightingById(int id) {
        try {
            final String SELECT_SIGHTING_BY_ID = BASE_SELECT_SIGHTING + " WHERE s.Id = ?";
            return jdbc.queryForObject(SELECT_SIGHTING_BY_ID, new SightingMapper(), id);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Sighting> getAllSightings() {
        // Should probably populate the name of location and super
        return jdbc.query(BASE_SELECT_SIGHTING, new SightingMapper());
    }

    @Override
    public Sighting addSighting(Sighting sight) throws SuperSightingsDaoException {
        try {
            if (sight == null
                    || sight.getDate() == null
                    || sight.getPlace() == null
                    || sight.getPerson() == null) {
                throw new IllegalArgumentException("Cannot add sight with null value");
            }

            final String INSERT_SIGHTING = "INSERT INTO sightings(Date,SuperpersonId,LocationId) VALUES "
                    + "(?,?,?)";
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update((Connection conn) -> {

                PreparedStatement statement = conn.prepareStatement(
                        INSERT_SIGHTING,
                        Statement.RETURN_GENERATED_KEYS);

                statement.setDate(1, Date.valueOf(sight.getDate()));
                statement.setInt(2, sight.getPerson().getId());
                statement.setInt(3, sight.getPlace().getId());
                return statement;

            }, keyHolder);

            sight.setId(keyHolder.getKey().intValue());

            return sight;
        } catch (DataIntegrityViolationException ex) {
            throw new SuperSightingsDaoException("Cannot add sighting, " + ex.getMessage());
        }
    }

    @Override
    public void updateSighting(Sighting sight) throws SuperSightingsDaoException {
        try {
            if (sight == null
                    || sight.getDate() == null
                    || sight.getPerson() == null
                    || sight.getPlace() == null) {
                throw new IllegalArgumentException("Cannot edit sighting to have null value");
            }
            final String UPDATE_SIGHTING = "UPDATE sightings "
                    + "SET date=?, superpersonid=?, "
                    + "locationid=? "
                    + "WHERE id=?";
            int rowsAffected = jdbc.update(UPDATE_SIGHTING,
                    Date.valueOf(sight.getDate()),
                    sight.getPerson().getId(),
                    sight.getPlace().getId(),
                    sight.getId());
            if (rowsAffected < 1) {
                throw new SuperSightingsDaoException("Cannot find sighting with id "
                        + sight.getId() + " to edit");
            }
            if (rowsAffected > 1) {
                throw new IncorrectResultSizeDataAccessException(
                        "ERROR!!! changed multiple values in database when trying "
                        + "to edit a sighting", 1, rowsAffected);
            }
        } catch (DataIntegrityViolationException ex) {
            throw new SuperSightingsDaoException("Cannot edit sighting, " + ex.getMessage());
        }
    }

    @Override
    public void deleteSightingById(int id) throws SuperSightingsDaoException {
        // can just delete all sightings normally
        final String DELETE_SIGHTING = "DELETE FROM sightings WHERE id = ? ";
        int rowsAffected = jdbc.update(DELETE_SIGHTING, id);
        if (rowsAffected < 1) {
            throw new SuperSightingsDaoException("Cannot find sighting with id "
                    + id + " to delete");
        }
        if (rowsAffected > 1) {
            throw new IncorrectResultSizeDataAccessException(
                    "ERROR!!! changed multiple values in database when trying "
                    + "to delete a sighting", 1, rowsAffected);
        }
    }

    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int i) throws SQLException {
            Sighting sight = new Sighting();
            sight.setId(rs.getInt("sightingId"));
            sight.setDate(rs.getDate("sightingDate").toLocalDate());

            Superperson person = new Superperson();
            person.setId(rs.getInt("superId"));
            person.setName(rs.getString("superName"));
            person.setDescription(rs.getString("superDescription"));

            Power pow = new Power();
            pow.setId(rs.getInt("superPowerId"));
            person.setPow(pow);

            sight.setPerson(person);

            Location loc = new Location();
            loc.setId(rs.getInt("locationId"));
            loc.setName(rs.getString("locName"));
            loc.setDescription(rs.getString("locDescription"));
            loc.setAddress(rs.getString("locAddress"));
            loc.setLatitude(rs.getBigDecimal("locLat"));
            loc.setLongitude(rs.getBigDecimal("locLong"));
            sight.setPlace(loc);

            return sight;
        }

    }

    /* *************************************************************************
        Final sighting queries
    ************************************************************************* */
    @Override
    public List<Sighting> getSightingsForDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }
        final String SELECT_SIGHTINGS_FOR_DATE = BASE_SELECT_SIGHTING
                + " WHERE s.date = ?";
        return jdbc.query(SELECT_SIGHTINGS_FOR_DATE,
                new SightingMapper(), date);
    }

    @Override
    public List<Location> getLocationsForSuperperson(int personId) {
        final String SELECT_LOCATIONS_FOR_SUPER = BASE_SELECT_SIGHTING
                + " WHERE p.id = ?";
        return jdbc.query(SELECT_LOCATIONS_FOR_SUPER, new LocationMapper(), personId);
    }

    @Override
    public List<Superperson> getSuperpeopleForLocation(int locId) {
        final String SELECT_SUPERS_FOR_LOCATION
                = "SELECT "
                + "s.Id sightingId, s.Date sightingDate, s.SuperpersonId superId, s.LocationId locationId, "
                + "p.Name superName, p.description superDescription, p.powerid powerId, "
                + "l.name locName, l.description locDescription, l.address locAddress, "
                + "l.latitude locLat, l.longitude locLong, "
                + "po.name powerName FROM sightings s "
                + "JOIN superpeople p on s.superpersonid = p.id "
                + "JOIN locations l on s.locationId = l.id "
                + "JOIN powers po on po.id = p.powerid "
                + "WHERE l.id = ?";
        return jdbc.query(SELECT_SUPERS_FOR_LOCATION, new SuperpersonMapper(), locId);
    }

    /* *************************************************************************
        Used just for testing
    ************************************************************************* */
    @Override
    @Transactional
    public void clearTables() {
        // quick and dirty
        jdbc.update("DELETE FROM superpeopleorganizations");
        jdbc.update("DELETE FROM sightings");
        jdbc.update("DELETE FROM superpeople");
        jdbc.update("DELETE FROM organizations");
        jdbc.update("DELETE FROM powers");
        jdbc.update("DELETE FROM locations");
    }
}
