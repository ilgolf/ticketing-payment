create table orders
(
    amount      decimal(38, 2)                     not null,
    created_at  datetime default CURRENT_TIMESTAMP not null,
    modified_at datetime default CURRENT_TIMESTAMP not null,
    order_date  datetime(6)                        not null,
    user_id     bigint                             not null,
    order_id    varchar(255)                       not null
        primary key,
    state       varchar(255)                       not null
);

create table order_item_entity
(
    created_at  datetime default CURRENT_TIMESTAMP not null,
    id          bigint auto_increment
        primary key,
    modified_at datetime default CURRENT_TIMESTAMP not null,
    ticket_id   bigint                             not null,
    order_id    varchar(255)                       not null
);

-- auto-generated definition
create table payment
(
    amount         decimal(38, 2)                     not null,
    created_at     datetime default CURRENT_TIMESTAMP not null,
    id             bigint auto_increment
        primary key,
    modified_at    datetime default CURRENT_TIMESTAMP not null,
    payment_date   datetime(6)                        not null,
    user_id        bigint                             not null,
    idempotent_key varchar(255)                       not null,
    order_id       varchar(255)                       not null,
    payment_method varchar(255)                       not null,
    payment_status varchar(255)                       not null
);

-- auto-generated definition
create table seat
(
    floor        int                                not null,
    is_available bit                                not null,
    number       int                                not null,
    row_index    int                                not null,
    created_at   datetime default CURRENT_TIMESTAMP not null,
    modified_at  datetime default CURRENT_TIMESTAMP not null,
    seat_id      bigint auto_increment
        primary key,
    section      varchar(255)                       not null
);

-- auto-generated definition
create table ticket
(
    price          decimal(38, 2)                     not null,
    created_at     datetime default CURRENT_TIMESTAMP not null,
    modified_at    datetime default CURRENT_TIMESTAMP not null,
    open_date_time datetime(6)                        not null,
    seat_id        bigint                             not null,
    ticket_id      bigint auto_increment
        primary key,
    status         varchar(255)                       not null
);
