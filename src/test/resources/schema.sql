create table users
(
    id       serial primary key,
    password varchar(255),
    username varchar(255)
);

create table urls
(
    url_key     varchar(128)  not null primary key,
    destination varchar(2048) not null
        constraint uk_1css5ju7d2o83pmn0446yvof2 unique,
    last_use    date,
    views       integer,
    user_id     integer       not null
        constraint fk31nbxw9e1inas1lmdkwxqv10 references users
);

INSERT INTO users (id, password, username)
VALUES (1, '$2a$10$2/DsWXZmXrncBpAUc6xc..zrdKWkhL2Xy1dTJhHIdXUXNTBWRcnB6', 'testUser');

INSERT INTO users (id, password, username)
VALUES (2, '$2a$10$2/DsWXZmXrncBpAUc6xc..zrdKWkhL2Xy1dTJhHIdXUXNTBWRcnB6', 'anothrUser');

INSERT INTO urls (url_key, destination, last_use, views, user_id)
VALUES ('ThinBC', 'https://test/?sort=Newest', '2023-12-07', 15, 2);
