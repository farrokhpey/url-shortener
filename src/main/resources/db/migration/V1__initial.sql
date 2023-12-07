create table users
(
    id       serial not null,
    password varchar(255),
    username varchar(255),
    primary key (id)
)