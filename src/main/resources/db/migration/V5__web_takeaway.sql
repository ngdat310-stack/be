-- Guests can order takeaway from the website. Such an order waits for its money before the kitchen sees it:
-- cash at the counter, or a bank transfer to the chain's QR that the cashier confirms.
alter table orders drop constraint if exists orders_status_check;
alter table orders add constraint orders_status_check
    check (status in ('AWAITING_PAYMENT', 'OPEN', 'COMPLETED', 'CANCELLED'));

alter table orders add column guest_name varchar(100);
alter table orders add column guest_phone varchar(20);
-- How the guest said they will pay (CASH or BANK_TRANSFER); null for orders taken by staff
alter table orders add column pay_method varchar(20) check (pay_method in ('CASH', 'BANK_TRANSFER'));
-- Unguessable key in the guest's tracking link
alter table orders add column public_token varchar(64) unique;
