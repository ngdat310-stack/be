-- Dishes a guest ordered by scanning the table QR, so the floor staff can be told about them
alter table order_items add column ordered_by_guest boolean not null default false;
