BEGIN;

create table accounts
(
    id           uuid        not null
        constraint accounts_pkey
            primary key,
    created_at   timestamp(6) default now(),
    deleted_at   timestamp(6),
    disable_date timestamp(6),
    email        varchar(40) not null,
    enable_date  timestamp(6),
    is_verified  boolean,
    login_id     varchar(40),
    otp          varchar(6),
    otp_expiry   timestamp(6),
    password     varchar(40) not null,
    role         varchar(40)  default 'ANONYMOUS'::text,
    code         serial
);

create table profiles
(
    id            uuid not null
        constraint profiles_pkey
            primary key,
    avatar_url    varchar(2048),
    date_of_birth timestamp(6),
    full_name     varchar(40),
    phone         varchar(11)
);

create table brands
(
    id          uuid not null
        constraint brands_pkey
            primary key,
    created_at  timestamp(6) default now(),
    deleted_at  timestamp(6),
    description varchar,
    image_url   varchar(2048),
    name        varchar(40)
        constraint uk_brands_tbl_name
            unique,
    code        serial
);

create table categories
(
    id          uuid not null
        constraint categories_pkey
            primary key,
    created_at  timestamp(6) default now(),
    deleted_at  timestamp(6),
    description varchar(255),
    image_url   varchar(2048),
    name        varchar(40)
        constraint uk_categories_tbl_name
            unique,
    parent_id   uuid
        constraint fk_categories_tbl_categories_id
            references categories,
    code        serial
);

create table discounts
(
    id            uuid        not null
        constraint discounts_pkey
            primary key,
    discount_type varchar(31) not null,
    created_at    timestamp(6) default now(),
    deleted_at    timestamp(6),
    description   text,
    disable_date  timestamp(6),
    enable_date   timestamp(6),
    title         text,
    code          serial
);

create table fixed_discounts
(
    fixed_discount real,
    min_condition  real,
    id             uuid not null
        constraint fixed_discounts_pkey
            primary key
        constraint fk_fixed_discounts_tbl_discounts_id
            references discounts
);

create table payments
(
    payment_method varchar(31) not null,
    id             uuid        not null
        constraint payments_pkey
            primary key,
    created_at     timestamp(6) default now(),
    deleted_at     timestamp(6),
    account_id     uuid,
    status         text,
    code           serial
);

create table cash_payments
(
    amount      numeric(10, 2),
    cash_method text,
    exchange    numeric(10, 2),
    paid        numeric(10, 2),
    id          uuid not null
        constraint cash_payments_pkey
            primary key
        constraint fk_cash_payments_tbl_payments_id
            references payments
);

create table orders
(
    id          uuid not null
        constraint orders_pkey
            primary key,
    created_at  timestamp(6) default now(),
    deleted_at  timestamp(6),
    address     varchar(255),
    description text,
    discount_id uuid
        constraint fk_orders_tbl_discounts_id
            references discounts,
    issuer_id   uuid,
    status      varchar(255)
        constraint orders_status_check
            check ((status)::text = ANY
                   ((ARRAY ['PROCESSING'::character varying, 'SHIPPING'::character varying, 'DELIVERED'::character varying, 'CANCELLED'::character varying])::text[])),
    payment_id  uuid not null
        constraint uk_orders_tbl_payments_id
            unique
        constraint fk_orders_tbl_payments_id
            references payments,
    code        serial
);

create table percent_discounts
(
    discount_percent numeric(3, 2),
    max_discount     numeric(38, 2),
    min_condition    numeric(38, 2),
    id               uuid not null
        constraint percent_discounts_pkey
            primary key
        constraint fk_percent_discounts_tbl_discounts_id
            references discounts
);

create table products
(
    id          uuid not null
        constraint products_pkey
            primary key,
    created_at  timestamp(6) default now(),
    deleted_at  timestamp(6),
    attributes  jsonb,
    description text,
    image_url   varchar(2048),
    name        varchar(255),
    price       numeric(10, 2),
    product_no  varchar(255) default 0,
    quantity    integer,
    sku         varchar(10),
    sold        integer,
    stock       integer,
    brand_id    uuid
        constraint fk_products_tbl_brands_id
            references brands,
    category_id uuid
        constraint fk_products_tbl_categories_id
            references categories,
    code        serial
);

create table order_details
(
    order_id    uuid not null
        constraint fk_order_details_tbl_orders_id
            references orders
            on delete cascade,
    product_id  uuid not null
        constraint fk_products_tbl_orders_id
            references products,
    gross_total numeric(10, 2),
    net_total   numeric(10, 2),
    quantity    integer,
    constraint order_details_pkey
        primary key (order_id, product_id)
);

create table products_discounts
(
    product_id  uuid not null
        constraint fk_products_discounts_tbl_products_id
            references products,
    discount_id uuid not null
        constraint fk_products_discounts_tbl_discounts_id
            references discounts,
    constraint products_discounts_pkey
        primary key (product_id, discount_id)
);



create table blog_posts
(
    id         uuid not null
        constraint blog_posts_pkey
            primary key,
    created_at timestamp(6) default now(),
    deleted_at timestamp(6),
    author_id  uuid
        constraint blog_posts_tbl_profiles_id
            references profiles,
    content    text         default ''::text,
    image_url  text         default ''::text,
    is_html    boolean,
    subtitle   text         default now(),
    title      text         default ''::text,
    is_draft   boolean,
    code       serial
);

create table promotion_discounts
(
    required_quantity integer,
    reward_product_id uuid
        constraint products_discounts_tbl_products_id
            references products,
    reward_quantity   integer,
    id                uuid not null
        constraint promotion_discounts_pkey
            primary key
        constraint fk_promotion_discounts_tbl_discounts_id
            references discounts
);

create table tokens
(
    id             uuid         not null
        constraint tokens_pkey
            primary key,
    created_at     timestamp(6) default now(),
    deleted_at     timestamp(6),
    access_expiry  timestamp(6) not null,
    access_token   text         not null,
    account_id     uuid         not null,
    refresh_expiry timestamp(6) not null,
    refresh_token  text         not null,
    code           varchar(6)
);

create table vnpay_payments
(
    amount      numeric(10, 2),
    bank_code   text,
    card_method text,
    order_info  text,
    id          uuid not null
        constraint vnpay_payments_pkey
            primary key
        constraint vnpay_payments_tbl_payments_id
            references payments
);

COMMIT;

BEGIN;
insert into accounts (id, email, password, role)
values ('00000000-0000-0000-0000-000000000000', 'admin@gmail.com', 'string', 'ROLE_ADMIN'),
       ('00000000-0000-0000-0000-000000000001', 'customer@gmail.com','string','ROLE_CUSTOMER'),
       ('00000000-0000-0000-0000-000000000002', 'staff@gmail.com','string','ROLE_STAFF');

insert into profiles (id,date_of_birth, full_name, phone)
values ('00000000-0000-0000-0000-000000000000', now() - INTERVAL '18 years', 'Phong', '0123456789'),
       ('00000000-0000-0000-0000-000000000001', now() - INTERVAL '17 years', 'Phong', '098765432'),
       ('00000000-0000-0000-0000-000000000002', now() - INTERVAL '18 years', 'Phong', '0995555555');

COMMIT ;