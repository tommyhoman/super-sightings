# super-sightings

This project is a java Spring boot application. Users are able to perform all CRUD operations for superheroes, locations, organizations of superheroes, locations and sightings of superheroes at particular locations. Uses thymeleaf to create filled out HTML templates to be displayed through a browser when endpoints are hit. Database queries are run with JDBC template and persisted in a MySQL database.

## To run this project

Being a maven-based project all it takes to run the java application is to import and run as is. The database layer however was manually made so run the SuperHeroSightings.sql script and set up the application.properties to connect to your created SQL database.
