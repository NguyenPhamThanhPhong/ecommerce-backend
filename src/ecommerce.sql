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
create table products_images
(
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


COMMIT;

BEGIN;
insert into accounts (id, email, password, role)
values ('00000000-0000-0000-0000-000000000000', 'admin@gmail.com', 'string', 'ROLE_ADMIN'),
       ('00000000-0000-0000-0000-000000000001', 'customer@gmail.com', 'string', 'ROLE_CUSTOMER'),
       ('00000000-0000-0000-0000-000000000002', 'staff@gmail.com', 'string', 'ROLE_STAFF');

insert into profiles (id, date_of_birth, full_name, phone)
values ('00000000-0000-0000-0000-000000000000', now() - INTERVAL '18 years', 'Phong', '0123456789'),
       ('00000000-0000-0000-0000-000000000001', now() - INTERVAL '17 years', 'Phong', '098765432'),
       ('00000000-0000-0000-0000-000000000002', now() - INTERVAL '18 years', 'Phong', '0995555555');

update profiles set primary_address='Ha noi',addresses='{
  "Ha noi": "street 99, district Thanh Oai, Ha Tay, Ha noi",
  "Workplace": "street 88, district 9, Ho Chi Minh"
}' where id in ('00000000-0000-0000-0000-000000000001',
                '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003');


insert into brands (id, name, description, image_url)
values ('00000000-0000-0000-0000-000000000000', 'Apple', 'Apple Inc.',
        'https://www.apple.com/ac/structured-data/images/knowledge_graph_logo.png?202103230739'),
       ('00000000-0000-0000-0000-000000000001', 'Samsung', 'Samsung Inc.',
        'https://www.samsung.com/etc/designs/smg/global/imgs/logo-square-letter.png'),
       ('00000000-0000-0000-0000-000000000002', 'Xiaomi', 'Xiaomi Inc.',
        'https://www.xiaomi.com/static/images/logo.png');

insert into categories (id, name, description, image_url)
values ('00000000-0000-0000-0000-000000000000', 'Smartphone', 'Smartphone',
        'https://www.apple.com/ac/structured-data/images/knowledge_graph_logo.png?202103230739'),
       ('00000000-0000-0000-0000-000000000001', 'Laptop', 'Laptop',
        'https://www.samsung.com/etc/designs/smg/global/imgs/logo-square-letter.png'),
       ('00000000-0000-0000-0000-000000000002', 'Tablet', 'Tablet', 'https://www.xiaomi.com/static/images/logo.png');

insert into products (id, brand_id, category_id, description, discount_percent, thumbnail_url, name, price,
                      quantity, rating, status, stock)
values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000',
        '00000000-0000-0000-0000-000000000000', 'Latest Apple iPhone', 10.00, 'https://example.com/thumbnail.jpg',
        'iPhone 13', 999.99, 100, 4.5, 'DRAFT', 150),
       ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001',
        '00000000-0000-0000-0000-000000000001', 'Latest Samsung Galaxy', 15.00, 'https://example.com/thumbnail2.jpg',
        'Samsung Galaxy S21', 799.99, 200, 4.7, 'ON_SALE', 250),
       ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002',
        '00000000-0000-0000-0000-000000000002', 'Latest Xiaomi Phone', 20.00, 'https://example.com/thumbnail3.jpg',
        'Xiaomi Mi 11', 699.99, 150, 4.6, 'ON_SALE', 200);



insert into products(id, brand_id, category_id, description, discount_percent, thumbnail_url, name, price, quantity,
                     rating, status, stock)
values ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000000',
        '00000000-0000-0000-0000-000000000000', 'Xiao mi', 10.50, 'https://example.com/thumbnail.jpg',
        'iPhone 16', 999.99, 100, 4.5, 'DRAFT', 150),
       ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001',
        '00000000-0000-0000-0000-000000000001', 'Latest Samsung Galaxy', 15.12, 'https://example.com/thumbnail2.jpg',
        'Samsung Galaxy S25', 799.99, 200, 4.7, 'ON_SALE', 250),
       ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000002',
        '00000000-0000-0000-0000-000000000002', 'Latest Xiaomi Phone', 30.55, 'https://example.com/thumbnail3.jpg',
        'Xiaomi Mi 15', 699.99, 150, 4.6, 'ON_SALE', 200);

insert into products_images (seq_no, product_id, name, colour, image_url)
values (1, '00000000-0000-0000-0000-000000000000', 'iPhone 13', 'Black', 'https://example.com/image.jpg'),
       (1, '00000000-0000-0000-0000-000000000001', 'Samsung Galaxy S21', 'White', 'https://example.com/image2.jpg'),
       (1, '00000000-0000-0000-0000-000000000002', 'Xiaomi Mi 11', 'Blue', 'https://example.com/image3.jpg');

insert into blog_posts (id, author_id, content, image_url, is_html, subtitle, title)
values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'This is a blog post',
        'https://example.com/image.jpg', false, 'This is a subtitle', 'This is a title'),
       ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'This is a blog post',
        'https://example.com/image.jpg', false, 'This is a subtitle', 'This is a title'),
       ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'This is a blog post',
        'https://example.com/image.jpg', false, 'This is a subtitle', 'This is a title');

insert into coupons (id, code, coupon_type, description, end_date, start_date, usage_limit, value)
values ('00000000-0000-0000-0000-000000000000', 'PERCENT10', 'PERCENT', '10% off on all items', '2024-12-31 23:59:59',
        '2024-01-01 00:00:00', 3, 10.00),
       ('00000000-0000-0000-0000-000000000001', 'PERCENT20', 'PERCENT', '20% off on selected items',
        '2024-12-31 23:59:59', '2024-01-01 00:00:00', 3, 20.00),
       ('00000000-0000-0000-0000-000000000002', 'PERCENT30', 'PERCENT', '30% off on electronics', '2024-12-31 23:59:59',
        '2024-01-01 00:00:00', 3, 30.00);
COMMIT;
abort;