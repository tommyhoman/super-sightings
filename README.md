# super-sightings

This project is a java Spring web application. Users are able to perform all CRUD operations for superheroes, locations, organizations of superheroes, and sightings of superheroes at particular locations. It uses thymeleaf to create filled out HTML templates to be displayed through a browser when REST endpoints are hit. Database queries are run with JDBC template and persisted in a MySQL database.

## To run this project

Being a maven-based project all it takes to run the java application is to import into your IDE and run as is. The database layer was manually made so run the SuperHeroSightings.sql script and set up the application.properties to connect to the created SQL database.
