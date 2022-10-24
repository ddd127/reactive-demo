create table person
(
    id      bigserial not null primary key,
    name    varchar(256),
    balance bigint
)