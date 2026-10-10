-- A web takeaway that was cancelled although money was (or may have been) transferred: the counter owes the guest a refund
alter table orders add column refund_due_at timestamp(6);
alter table orders add column refund_settled_at timestamp(6);
alter table orders add column refund_settled_by integer references staffs (id);
