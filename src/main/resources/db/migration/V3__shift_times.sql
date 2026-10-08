-- A shift is a time of day that repeats ("Ca sáng 07:00-15:00"), and a staff assignment is for a calendar day.
-- The tables were created with full timestamps; nothing has used them yet, so convert in place.
alter table shifts alter column start_time type time using start_time::time;
alter table shifts alter column end_time type time using end_time::time;
alter table staff_shifts alter column work_date type date using work_date::date;
