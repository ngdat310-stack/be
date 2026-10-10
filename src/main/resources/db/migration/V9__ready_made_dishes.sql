-- Drinks and ready-made dishes do not go through the kitchen: served straight away, sold out by stock, not by the chef.
alter table menu_items add column needs_kitchen boolean not null default true;
-- Copied onto each order line so the kitchen queue never has to look the dish up
alter table order_items add column needs_kitchen boolean not null default true;
