BEGIN;

create table accounts
(
    id           uuid not null
        constraint accounts_pkey
            primary key,
    code         serial,
    created_at   timestamp(6) default now(),
    deleted_at   timestamp(6),
    disable_date timestamp(6),
    email        text not null,
    enable_date  timestamp(6),
    is_verified  boolean,
    otp          varchar(6),
    otp_expiry   timestamp(6),
    password     text not null,
    role         text         default 'ANONYMOUS'::text
);

create table profiles
(
    id              uuid not null
        constraint profiles_pkey
            primary key,
    constraint fk_profiles_accounts_id foreign key (id) references accounts (id),
    avatar_url      varchar(2048),
    primary_address varchar(255),
    addresses       jsonb,
    date_of_birth   timestamp(6),
    full_name       varchar(40),
    phone           varchar(11)
);

create table brands
(
    id          uuid not null
        constraint brands_pkey
            primary key,
    code        serial,
    created_at  timestamp(6) default now(),
    deleted_at  timestamp(6),
    description text,
    image_url   varchar(2048),
    name        varchar(40)
        constraint uk_brands_name unique
);

create table categories
(
    id          uuid not null
        constraint categories_pkey
            primary key,
    code        serial,
    created_at  timestamp(6) default now(),
    deleted_at  timestamp(6),
    description text,
    image_url   varchar(255),
    name        varchar(40)
        constraint uk_categories_name unique
);

create table products
(
    id               uuid not null
        constraint products_pkey
            primary key,
    code             serial,
    created_at       timestamp(6) default now(),
    deleted_at       timestamp(6),
    available_date   timestamp(6),
    brand_id         uuid
        constraint fk_products_brands_id
            references brands,
    category_id      uuid
        constraint fk_products_categories_id
            references categories,
    description      text,
    highlights       text,
    policies         text,
    discount_percent numeric(5, 2),
    thumbnail_url    varchar(2048),
    name             varchar(255),
    price            numeric(10, 2),
    quantity         integer,
    rating           numeric(2, 1),
    status           varchar(100),
    stock            integer
);
-- alter table products alter discount_percent type numeric(5, 2) using discount_percent::numeric(5, 2);
-- alter table products alter rating type numeric(2, 1) using rating::numeric(2, 1);
-- alter table products alter price type numeric(10, 2) using price::numeric(10, 2);
create table products_images(
    seq_no     int,
    product_id uuid          not null
        constraint fk_products_images_products_id
            references products,
    constraint products_images_pkey primary key (seq_no, product_id),
    name       varchar(255),
    colour     varchar(255),
    created_at timestamp(6) default now(),
    deleted_at timestamp(6),
    image_url  varchar(2048) not null
);

create table favorite_products
(
    account_id uuid not null,
    product_id uuid not null,
    constraint favorites_products_pkey primary key (account_id, product_id),
    constraint fk_favorites_products_accounts_id foreign key (account_id) references accounts,
    constraint fk_favorites_products_products_id foreign key (product_id) references products
);

create table coupons
(
    id          uuid not null
        constraint coupons_pkey
            primary key,
    code        varchar(100),
    created_at  timestamp(6) default now(),
    deleted_at  timestamp(6),
    coupon_type varchar(100),
    description varchar(255),
    start_date  timestamp(6),
    end_date    timestamp(6),
    usage_limit integer,
    current_usage integer,
    value       numeric(38, 2)
);

create table blog_posts
(
    id         uuid not null
        constraint blog_posts_pkey
            primary key,
    code       serial,
    created_at timestamp(6) default now(),
    deleted_at timestamp(6),
    author_id  uuid
        constraint fk_blog_posts_authors_id references profiles,
    content    text         default ''::text,
    image_url  text         default ''::text,
    is_html    boolean,
    subtitle   text         default now(),
    title      text         default ''::text
);

create table orders
(
    id          uuid not null
        constraint orders_pkey
            primary key,
    code        serial,
    created_at  timestamp(6) default now(),
    deleted_at  timestamp(6),
    address     varchar(255),
    coupon_id   uuid constraint fk_orders_coupons_id references coupons(id),
    creator_id  uuid
        constraint fk_orders_profiles_id
            references profiles (id),
    notes       varchar(200),
    total_value numeric(10, 2)
);

create table payments
(
    id             uuid        not null
        constraint payments_pkey primary key,
    payment_method varchar(31) not null,
    amount      numeric(10, 2),
    order_id       uuid
        constraint uk_payments_order_id
            unique
        constraint fk_payments_orders_id
            references orders(id),
    code           serial,
    created_at     timestamp(6) default now(),
    deleted_at     timestamp(6),
    account_id     uuid constraint fk_payments_accounts_id references accounts,
    status         varchar
);

create table order_details
(
    id         uuid not null
        constraint order_details_pkey
            primary key,
    code       serial,
    created_at timestamp(6) default now(),
    deleted_at timestamp(6),
    quantity   integer,
    order_id   uuid not null
        constraint fk_order_details_orders_id
            references orders,
    product_id uuid not null
        constraint fk_order_details_products_id
            references products
);

create table orders_order_details
(
    order_id         uuid not null
        constraint fk_orders_order_details_orders_id
            references orders,
    order_details_id uuid not null
        constraint uk_orders_order_details_order_details_id
            unique
        constraint fk_orders_order_details_order_details_id
            references order_details,
    constraint orders_order_details_pkey
        primary key (order_id, order_details_id)
);

create table tokens
(
    id             uuid         not null
        constraint tokens_pkey
            primary key,
    code           serial,
    created_at     timestamp(6) default now(),
    deleted_at     timestamp(6),
    access_expiry  timestamp(6) not null,
    access_token   text         not null,
    account_id     uuid         not null,
    refresh_expiry timestamp(6) not null,
    refresh_token  text         not null
);

create table vnpay_payments
(
    bank_code   text,
    card_method text,
    order_info  text,
    trans_ref varchar(100),
    trans_no varchar(100),
    secure_hash varchar(256),
    id          uuid not null
        constraint vnpay_payments_pkey
            primary key
        constraint fk_vnpay_payments_payments_id
            references payments (id)
);

create table cash_payments
(
    amount   numeric(10, 2),
    exchange numeric(10, 2),
    paid     numeric(10, 2),
    id       uuid not null
        constraint cash_payments_pkey
            primary key
        constraint fk_cash_payments_payments_id
            references payments (id)
);

COMMIT;
