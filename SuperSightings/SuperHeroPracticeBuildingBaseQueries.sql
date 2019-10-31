use SuperSightings;

SELECT s.Id superId, s.Name superName, s.description superDescription, s.PowerId powerId, p.Name powerName FROM superpeople s
JOIN powers p ON p.id = s.powerId;

SELECT s.Id sightingId, s.Date sightingDate, s.SuperpersonId superId, s.LocationId locationId, p.Name superName, p.description superDescription,
p.powerid superPowerId, l.name locName, l.description locDescription, l.address locAddress, l.latitude locLat, l.longitude locLong FROM sightings s 
JOIN superpeople p on s.superpersonid = p.id
JOIN locations l on s.locationId = l.id;

SELECT s.Id superId, s.Name superName, s.description superDescription, s.PowerId powerId, p.Name powerName FROM superpeople s
JOIN powers p ON p.id = s.powerId
JOIN superpeopleorganizations so ON s.id = so.superpersonid;

SELECT o.* FROM organizations o 
JOIN superpeopleorganizations so ON o.id = so.organizationId;
