CREATE TABLE ticketing.order_item
(
    id          BIGINT AUTO_INCREMENT  NOT NULL,
    created_at  datetime DEFAULT NOW() NOT NULL,
    modified_at datetime DEFAULT NOW() NOT NULL,
    order_id    VARCHAR(255)           NOT NULL,
    ticket_id   BIGINT                 NOT NULL,
    CONSTRAINT pk_orderitementity PRIMARY KEY (id)
);

CREATE TABLE ticketing.orders
(
    order_id    VARCHAR(255)           NOT NULL,
    created_at  datetime DEFAULT NOW() NOT NULL,
    modified_at datetime DEFAULT NOW() NOT NULL,
    amount      DECIMAL                NOT NULL,
    order_date  datetime               NOT NULL,
    state       VARCHAR(255)           NOT NULL,
    user_id     BIGINT                 NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (order_id)
);

CREATE TABLE ticketing.payment
(
    id             BIGINT AUTO_INCREMENT  NOT NULL,
    created_at     datetime DEFAULT NOW() NOT NULL,
    modified_at    datetime DEFAULT NOW() NOT NULL,
    amount         DECIMAL                NOT NULL,
    payment_method VARCHAR(255)           NOT NULL,
    order_id       VARCHAR(255)           NOT NULL,
    payment_status VARCHAR(255)           NOT NULL,
    payment_date   datetime               NOT NULL,
    idempotent_key VARCHAR(255)           NOT NULL,
    user_id        BIGINT                 NOT NULL,
    CONSTRAINT pk_payment PRIMARY KEY (id)
);

CREATE TABLE ticketing.payment_event_entity
(
    event_id      BIGINT       NOT NULL,
    payment_id    BIGINT       NOT NULL,
    event_type    SMALLINT     NOT NULL,
    event_status  SMALLINT     NOT NULL,
    occurred_at   datetime     NOT NULL,
    processed_at  datetime     NULL,
    error_message VARCHAR(255) NULL,
    CONSTRAINT pk_paymentevententity PRIMARY KEY (event_id)
);

CREATE TABLE ticketing.seat
(
    seat_id      BIGINT AUTO_INCREMENT  NOT NULL,
    created_at   datetime DEFAULT NOW() NOT NULL,
    modified_at  datetime DEFAULT NOW() NOT NULL,
    row_index    INT                    NOT NULL,
    floor        INT                    NOT NULL,
    number       INT                    NOT NULL,
    section      VARCHAR(255)           NOT NULL,
    is_available boolean                 NOT NULL,
    CONSTRAINT pk_seat PRIMARY KEY (seat_id)
);

CREATE TABLE ticketing.ticket
(
    ticket_id      BIGINT AUTO_INCREMENT  NOT NULL,
    created_at     datetime DEFAULT NOW() NOT NULL,
    modified_at    datetime DEFAULT NOW() NOT NULL,
    seat_id        BIGINT                 NOT NULL,
    seller_id   BIGINT                 NOT NULL,
    status         VARCHAR(255)           NOT NULL,
    price          DECIMAL                NOT NULL,
    open_date_time datetime               NOT NULL,
    CONSTRAINT pk_ticket PRIMARY KEY (ticket_id)
);

CREATE TABLE ticketing.banks
(
    id            BIGINT AUTO_INCREMENT  NOT NULL,
    created_at    datetime DEFAULT NOW() NOT NULL,
    modified_at   datetime DEFAULT NOW() NOT NULL,
    name          VARCHAR(255)           NOT NULL,
    `description` VARCHAR(255)           NOT NULL,
    account_note  VARCHAR(255)           NOT NULL,
    depositor     BIT(1)                 NOT NULL,
    seller_id     BIGINT                 NOT NULL,
    CONSTRAINT pk_banks PRIMARY KEY (id)
);

CREATE TABLE ticketing.seller
(
    id                  BIGINT AUTO_INCREMENT  NOT NULL,
    created_at          datetime DEFAULT NOW() NOT NULL,
    modified_at         datetime DEFAULT NOW() NOT NULL,
    representative_name VARCHAR(255)           NOT NULL,
    company_name        VARCHAR(255)           NULL,
    license_number      VARCHAR(255)           NOT NULL,
    email               VARCHAR(255)           NOT NULL,
    phone_number        VARCHAR(255)           NOT NULL,
    is_active           BIT(1)                 NOT NULL,
    CONSTRAINT pk_seller PRIMARY KEY (id)
);

CREATE TABLE ticketing.wallet
(
    id                       BIGINT AUTO_INCREMENT  NOT NULL,
    created_at               datetime DEFAULT NOW() NOT NULL,
    modified_at              datetime DEFAULT NOW() NOT NULL,
    amount                   DECIMAL                NOT NULL,
    fee                      DECIMAL                NOT NULL,
    settlement_status        SMALLINT               NOT NULL,
    settlement_complete_date datetime                   NULL,
    order_id                 VARCHAR(255)           NOT NULL,
    seller_id                BIGINT                 NOT NULL,
    CONSTRAINT pk_wallet PRIMARY KEY (id)
);