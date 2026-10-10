-- Where each branch is, so a guest's phone can show the nearest one for takeaway
alter table branches add column latitude double precision check (latitude between -90 and 90);
alter table branches add column longitude double precision check (longitude between -180 and 180);
