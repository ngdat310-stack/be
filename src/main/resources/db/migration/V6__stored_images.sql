-- Images the owner uploads (the bank transfer QR), kept in the database so no file storage has to be set up
create table stored_images (
    image_key    varchar(50) primary key,
    content_type varchar(50) not null,
    data         bytea not null,
    updated_by   integer references staffs (id),
    updated_at   timestamp(6) not null
);
