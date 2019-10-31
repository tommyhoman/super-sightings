drop database if exists SuperSightingsTest;

create database SuperSightingsTest;

use SuperSightingsTest;

create table Organizations (
	Id int primary key auto_increment,
    `Name` varchar(100) not null,
    `Description` varchar(200) null,
    Address varchar(250) not null
);

create table Powers (
	Id int primary key auto_increment,
	`Name` varchar(100) not null
);

create table Superpeople (
	Id int primary key auto_increment,
    `Name` varchar(100) not null,
    `Description` varchar(200) null,
    PowerId int not null,
    foreign key (PowerId) references Powers(Id)
);

create table SuperpeopleOrganizations (
	OrganizationId int not null,
    SuperpersonId int not null,
    primary key(OrganizationId, SuperpersonId),
    foreign key (OrganizationId) references Organizations(Id),
    foreign key (SuperpersonId) references Superpeople(Id)
);

create table Locations (
	Id int primary key auto_increment,
	`Name` varchar(100) not null,
    `Description` varchar(200) null,
    Address varchar(250) not null,
    Latitude decimal(9,6) not null,
    Longitude decimal(9,6) not null
);

create table Sightings (
	Id int primary key auto_increment,
    `Date` Date not null,
    SuperpersonId int not null,
    LocationId int not null,
    foreign key (SuperpersonId) references Superpeople(Id),
    foreign key (LocationId) references Locations(Id)
);