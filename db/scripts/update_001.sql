create table accounts
(
    id    serial,
    name  text   not null,
    phone bigint not null,
    PRIMARY KEY (id)
);

create table halls
(
    row        integer not null,
    seat       integer not null,
    pick       boolean default false,
    id_account integer default null,
    PRIMARY KEY (row, seat),
    FOREIGN KEY (id_account) REFERENCES accounts (id) ON DELETE SET NULL
);

insert into halls (row, seat)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
       (2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6),
       (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6),
       (4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 6);