create table urls
(
    last_use    date,
    user_id     integer       not null,
    views       integer,
    url_key     varchar(128)  not null,
    destination varchar(2048) not null unique,
    primary key (url_key)
);

alter table if exists urls
    add constraint fk_urls_users
        foreign key (user_id)
            references users;