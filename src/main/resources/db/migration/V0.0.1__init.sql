create table test_entity
(
    id    bigserial    not null primary key,
    key   varchar(128) not null,
    value varchar(2048)
);

create index idx__test_entity__name on test_entity (key);
