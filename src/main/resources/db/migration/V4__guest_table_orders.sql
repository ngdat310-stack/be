-- Guests order from their phone by scanning the QR on the table: such an order has no staff who opened it.
alter table orders alter column opened_by drop not null;

-- Every table carries an unguessable token printed in its QR code
update dining_tables set qr_token = md5(random()::text || id::text) where qr_token is null;
alter table dining_tables alter column qr_token set not null;
