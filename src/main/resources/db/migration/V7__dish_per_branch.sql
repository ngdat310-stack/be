-- The owner decides which branches sell a dish. offered = false: the branch does not carry it at all
-- (unlike is_available = false, which is "sold out for now"). No row still means the branch sells it.
alter table branch_menu_items add column offered boolean not null default true;
