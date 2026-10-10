-- Transfers confirmed by the SePay webhook have no cashier behind them
alter table payments alter column received_by drop not null;
