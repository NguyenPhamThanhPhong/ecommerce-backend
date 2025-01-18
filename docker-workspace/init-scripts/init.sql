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

--- accounts
BEGIN;
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('b90c8cba-e8bd-4f79-9094-44fc367c8237',  '2025-01-16 11:27:53.786139', null, '2045-01-16 07:00:00.000000', 'abc@gmail.com', '2005-01-16 07:00:00.000000', true, null, null, '123456', 'ROLE_CUSTOMER');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('e008aa5d-d4e8-483d-a1a0-238fd05a6f2d',  '2025-01-16 11:32:48.469848', null, '2045-01-16 07:00:00.000000', 'abc@gmail.com', '2005-01-16 07:00:00.000000', true, null, null, '123456', 'ROLE_CUSTOMER');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('afb2afc2-f4b3-4042-9952-a260bce8656b',  '2025-01-16 11:33:48.348222', null, '2045-01-16 07:00:00.000000', 'phong@gmail.com', '2005-01-16 07:00:00.000000', true, null, null, '123456', 'ROLE_CUSTOMER');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('1035e8d0-60c1-49cc-bf70-e5efb2c931bd',  '2025-01-16 12:54:50.451065', null, '2025-01-30 07:00:00.000000', 'phongstaff@gmail.com', '2025-01-01 07:00:00.000000', true, null, null, '123456', 'ROLE_STAFF');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('c4bae77e-c260-4ac5-b721-26455facbece',  '2025-01-16 12:56:21.280529', null, '2025-01-31 07:00:00.000000', 'phongstaff2@gmail.com', '2017-01-03 07:00:00.000000', true, null, null, '123456', 'ROLE_STAFF');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('00000000-0000-0000-0000-000000000000', '2025-01-03 04:10:48.640731', null, '2025-01-31 12:36:07.060464', 'admin@gmail.com', '2005-01-16 07:00:00.000000', true, null, null, 'string', 'ROLE_ADMIN');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('00000000-0000-0000-0000-000000000001', '2025-01-03 04:10:48.640731', null, '2025-01-31 12:36:07.060464', 'customer@gmail.com', '2005-01-16 07:00:00.000000', true, null, null, 'string', 'ROLE_CUSTOMER');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('00000000-0000-0000-0000-000000000002', '2025-01-03 04:10:48.640731', '2025-01-16 12:36:07.060464', '2025-01-31 12:36:07.060464', 'staff@gmail.com', '2005-01-16 07:00:00.000000', true, null, null, 'string', 'ROLE_STAFF');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('ff7bc69b-ab6b-4966-ba03-66fcafe27e1e',  '2025-01-17 22:00:05.244148', null, '2040-01-03 04:10:48.640731', '21522458@gm.uit.edu.vn', '2000-01-03 04:10:48.640731', false, '406134', '2025-01-18 02:13:43.355120', 'phong123', 'ROLE_CUSTOMER');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES ('b9601835-69da-4f9e-adae-522dd64f86bf',  '2025-01-18 02:00:43.806301', null, '2040-01-03 04:10:48.640731', 'phong11@gmail.com', '2000-01-03 04:10:48.640731', false, null, null, '123456', 'ROLE_CUSTOMER');
---- profiles
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('00000000-0000-0000-0000-000000000001', null, '2008-01-03 04:10:48.640731', 'Phong', '098765432', 'Ha noi', '{"Ha noi": "street 99, district Thanh Oai, Ha Tay, Ha noi", "Workplace": "street 88, district 9, Ho Chi Minh"}');
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('00000000-0000-0000-0000-000000000002', null, '2007-01-03 04:10:48.640731', 'Phong', '0995555555', 'Ha noi', '{"Ha noi": "street 99, district Thanh Oai, Ha Tay, Ha noi", "Workplace": "street 88, district 9, Ho Chi Minh"}');
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('b90c8cba-e8bd-4f79-9094-44fc367c8237', null, null, 'Nguyen Phong', '0922555666', null, null);
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('e008aa5d-d4e8-483d-a1a0-238fd05a6f2d', null, null, 'phong@gmail.com', '0011223335', null, null);
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('afb2afc2-f4b3-4042-9952-a260bce8656b', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737002029/ecommerce/account/afb2afc2-f4b3-4042-9952-a260bce8656b.png', null, 'Phong Phong', '0111223657', null, null);
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('1035e8d0-60c1-49cc-bf70-e5efb2c931bd', null, '2017-01-03 07:00:00.000000', 'Phong staffs', '099999', null, null);
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('c4bae77e-c260-4ac5-b721-26455facbece', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737006981/ecommerce/account/c4bae77e-c260-4ac5-b721-26455facbece.png', null, 'Phong Staffs 2', '0123456551', null, null);
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737140195/ecommerce/account/ff7bc69b-ab6b-4966-ba03-66fcafe27e1e.png', null, 'Nguyen Pham Thanh Phong ', '0155556677', null, '{"Workplace": "Duong 1000"}');
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('b9601835-69da-4f9e-adae-522dd64f86bf', null, null, 'Phong', null, null, null);
INSERT INTO public.profiles (id, avatar_url, date_of_birth, full_name, phone, primary_address, addresses) VALUES ('00000000-0000-0000-0000-000000000000', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1736436253/ecommerce/account/00000000-0000-0000-0000-000000000000.png', '2025-01-02 07:00:00.000000', 'Nguyen Pham Thanh Phong Phong', '0125556677', 'Ha noi', '{"Ha noi": "street 99, district Thanh Oai, Ha Tay, Ha noi", "Workplace": "street 88, district 9, Ho Chi Minh"}');
----- brands
INSERT INTO public.brands (id, created_at, deleted_at, description, image_url, name) VALUES ('00000000-0000-0000-0000-000000000000',  '2025-01-03 04:10:48.640731', null, 'Apple Inc. op', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737014263/ecommerce/brand/00000000-0000-0000-0000-000000000000.png', 'Apple with something');
INSERT INTO public.brands (id, created_at, deleted_at, description, image_url, name) VALUES ('00000000-0000-0000-0000-000000000002',  '2025-01-03 04:10:48.640731', null, 'Xiaomi Inc.', 'https://www.xiaomi.com/static/images/logo.png', 'Xiaomi');
INSERT INTO public.brands (id, created_at, deleted_at, description, image_url, name) VALUES ('18cd853c-4699-4e75-83da-2efdec443be6', '2025-01-16 14:58:14.601206', null, 'Phong Brand', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737014294/ecommerce/brand/18cd853c-4699-4e75-83da-2efdec443be6.png', 'My new Brand');
INSERT INTO public.brands (id, created_at, deleted_at, description, image_url, name) VALUES ('7bdf2da1-a50e-4a56-8a95-b8d43888ec71', '2025-01-18 02:09:33.567243', null, 'Hello', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737140974/ecommerce/brand/7bdf2da1-a50e-4a56-8a95-b8d43888ec71.jpg', 'Phong Brand');
INSERT INTO public.brands (id, created_at, deleted_at, description, image_url, name) VALUES ('00000000-0000-0000-0000-000000000001',  '2025-01-03 04:10:48.640731', null, 'Samsung Inc. SamSungzzzz', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737141115/ecommerce/brand/00000000-0000-0000-0000-000000000001.png', 'Samsung SamSungzzzz');
---- categories
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url, name) VALUES ('00000000-0000-0000-0000-000000000001', '2025-01-03 04:10:48.640731', null, 'Laptop', 'https://www.samsung.com/etc/designs/smg/global/imgs/logo-square-letter.png', 'Laptop');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url, name) VALUES ('00000000-0000-0000-0000-000000000002', '2025-01-03 04:10:48.640731', null, 'Tablet', 'https://www.xiaomi.com/static/images/logo.png', 'Tablet');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url, name) VALUES ('00000000-0000-0000-0000-000000000000', '2025-01-03 04:10:48.640731', null, 'Smartphone PHong aaa', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737013014/ecommerce/category/00000000-0000-0000-0000-000000000000.png', 'Smartphone Phong aa');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url, name) VALUES ('a8936af9-2c65-4c2a-ae82-dd9c0eba911b',  '2025-01-18 02:12:47.828157', null, 'Phong hello', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737141168/ecommerce/category/a8936af9-2c65-4c2a-ae82-dd9c0eba911b.jpg', 'Phong Category');
---- products
INSERT INTO public.products (id, created_at, deleted_at, available_date, brand_id, category_id, description, discount_percent, thumbnail_url, name, price, quantity, rating, status, stock, policies, highlights)
VALUES ('00000000-0000-0000-0000-000000000004', '2025-01-03 07:27:58.490858', null, '2025-01-07 22:21:19.000000', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Latest Samsung Galaxy', 15.12, 'https://example.com/thumbnail2.jpg', 'Samsung Galaxy S25', 200000.00, 200, 4.7, 'ON_SALE', 250, e'<section id="policies">
    <h2>Our Policies</h2>
    <ul>
        <li>30-day money-back guarantee.</li>
        <li>Free shipping on orders over $50.</li>
        <li>24/7 customer support for all inquiries.</li>
    </ul>
</section>', e'<section id="highlights">
    <h2>Product Highlights</h2>
    <ul>
        <li>Innovative design with premium materials.</li>
        <li>Seamless performance with advanced technology.</li>
        <li>Eco-friendly and sustainable packaging.</li>
    </ul>
</section>');
INSERT INTO public.products (id, created_at, deleted_at, available_date, brand_id, category_id, description, discount_percent, thumbnail_url, name, price, quantity, rating, status, stock, policies, highlights)
VALUES ('00000000-0000-0000-0000-000000000003', '2025-01-03 07:27:58.490858', null, '2025-01-07 22:21:19.000000', '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'Xiao mi', 10.50, 'https://example.com/thumbnail.jpg', 'iPhone 16', 200000.00, 100, 4.5, 'DRAFT', 150, e'<section id="policies">
    <h2>Our Policies</h2>
    <ul>
        <li>30-day money-back guarantee.</li>
        <li>Free shipping on orders over $50.</li>
        <li>24/7 customer support for all inquiries.</li>
    </ul>
</section>', e'<section id="highlights">
    <h2>Product Highlights</h2>
    <ul>
        <li>Innovative design with premium materials.</li>
        <li>Seamless performance with advanced technology.</li>
        <li>Eco-friendly and sustainable packaging.</li>
    </ul>
</section>');
INSERT INTO public.products (id, created_at, deleted_at, available_date, brand_id, category_id, description, discount_percent, thumbnail_url, name, price, quantity, rating, status, stock, policies, highlights)
VALUES ('fb811c58-239d-4ef2-9e8c-1cd6d9b83dd3', '2025-01-14 12:39:06.228320', null, '2025-01-19 07:00:00.000000', '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'string', 2.00, 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1736834347/ecommerce/product/fb811c58-239d-4ef2-9e8c-1cd6d9b83dd3.png', 'phong new iphone', 20.00, 20, 4.5, 'ON_SALE', 20, e'<section id="policies">
    <h2>Our Policies</h2>
    <ul>
        <li>30-day money-back guarantee.</li>
        <li>Free shipping on orders over $50.</li>
        <li>24/7 customer support for all inquiries.</li>
    </ul>
</section>', e'<section id="highlights">
    <h2>Product Highlights</h2>
    <ul>
        <li>Innovative design with premium materials.</li>
        <li>Seamless performance with advanced technology.</li>
        <li>Eco-friendly and sustainable packaging.</li>
    </ul>
</section>');
INSERT INTO public.products (id,created_at, deleted_at, available_date, brand_id, category_id, description, discount_percent, thumbnail_url, name, price, quantity, rating, status, stock, policies, highlights)
VALUES ('00000000-0000-0000-0000-000000000000',  '2025-01-03 04:10:48.640731', null, '2025-01-19 07:00:00.000000', '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000001', 'Latest Apple iPhone22', 10.00, 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1736956911/ecommerce/product/00000000-0000-0000-0000-000000000000.png', 'iPhone 134', 100000.00, 100, 4.5, 'ON_SALE', 150, e'<section id="policies">
    <h2>Our Policies</h2>
    <ul>
        <li>30-day money-back guarantee.</li>
        <li>Free shipping on orders over $50.</li>
        <li>24/7 customer support for all inquiries.</li>
    </ul>
</section>', e'<section id="highlights">
    <h2>Product Highlights</h2>
    <ul>
        <li>Innovative design with premium materials.</li>
        <li>Seamless performance with advanced technology.</li>
        <li>Eco-friendly and sustainable packaging.</li>
    </ul>
</section>');
INSERT INTO public.products (id,created_at, deleted_at, available_date, brand_id, category_id, description, discount_percent, thumbnail_url, name, price, quantity, rating, status, stock, policies, highlights)
VALUES ('00000000-0000-0000-0000-000000000002', '2025-01-03 04:10:48.640731', null, '2025-01-07 22:21:19.000000', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'Latest Xiaomi Phone', 20.00, 'https://example.com/thumbnail3.jpg', 'Xiaomi Mi 11', 100000.00, 150, 4.6, 'ON_SALE', 200, e'<section id="policies">
    <h2>Our Policies</h2>
    <ul>
        <li>30-day money-back guarantee.</li>
        <li>Free shipping on orders over $50.</li>
        <li>24/7 customer support for all inquiries.</li>
    </ul>
</section>', e'<section id="highlights">
    <h2>Product Highlights</h2>
    <ul>
        <li>Innovative design with premium materials.</li>
        <li>Seamless performance with advanced technology.</li>
        <li>Eco-friendly and sustainable packaging.</li>
    </ul>
</section>');
INSERT INTO public.products (id, created_at, deleted_at, available_date, brand_id, category_id, description, discount_percent, thumbnail_url, name, price, quantity, rating, status, stock, policies, highlights)
VALUES ('00000000-0000-0000-0000-000000000005', '2025-01-03 07:27:58.490858', null, '2025-01-07 00:40:42.000000', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'Latest Xiaomi Phone', 30.55, 'https://example.com/thumbnail3.jpg', 'Xiaomi Mi 15', 200000.00, 150, 4.6, 'ON_SALE', 200, e'<section id="policies">
    <h2>Our Policies</h2>
    <ul>
        <li>30-day money-back guarantee.</li>
        <li>Free shipping on orders over $50.</li>
        <li>24/7 customer support for all inquiries.</li>
    </ul>
</section>', e'<section id="highlights">
    <h2>Product Highlights</h2>
    <ul>
        <li>Innovative design with premium materials.</li>
        <li>Seamless performance with advanced technology.</li>
        <li>Eco-friendly and sustainable packaging.</li>
    </ul>
</section>');
INSERT INTO public.products (id, created_at, deleted_at, available_date, brand_id, category_id, description, discount_percent, thumbnail_url, name, price, quantity, rating, status, stock, policies, highlights)
VALUES ('00000000-0000-0000-0000-000000000001', '2025-01-03 04:10:48.640731', null, '2025-01-19 22:21:03.000000', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Latest Samsung Galaxy', 15.00, 'https://example.com/thumbnail2.jpg', 'Samsung Galaxy S21', 100000.00, 200, 4.7, 'ON_SALE', 250, e'<section id="policies">
    <h2>Our Policies</h2>
    <ul>
        <li>30-day money-back guarantee.</li>
        <li>Free shipping on orders over $50.</li>
        <li>24/7 customer support for all inquiries.</li>
    </ul>
</section>', e'<section id="highlights">
    <h2>Product Highlights</h2>
    <ul>
        <li>Innovative design with premium materials.</li>
        <li>Seamless performance with advanced technology.</li>
        <li>Eco-friendly and sustainable packaging.</li>
    </ul>
</section>');
---- products_images
INSERT INTO public.products_images (seq_no, product_id, name, colour, created_at, deleted_at, image_url) VALUES (1, '00000000-0000-0000-0000-000000000000', 'iPhone 13', 'black', '2025-01-03 06:03:09.068736', null, 'https://example.com/image.jpg');
INSERT INTO public.products_images (seq_no, product_id, name, colour, created_at, deleted_at, image_url) VALUES (1, '00000000-0000-0000-0000-000000000001', 'Samsung Galaxy S21', 'white', '2025-01-03 06:03:09.068736', null, 'https://example.com/image2.jpg');
INSERT INTO public.products_images (seq_no, product_id, name, colour, created_at, deleted_at, image_url) VALUES (1, '00000000-0000-0000-0000-000000000002', 'Xiaomi Mi 11', 'blue', '2025-01-03 06:03:09.068736', null, 'https://example.com/image3.jpg');
INSERT INTO public.products_images (seq_no, product_id, name, colour, created_at, deleted_at, image_url) VALUES (0, 'fb811c58-239d-4ef2-9e8c-1cd6d9b83dd3', 'Screenshot 2024-10-25 220224.png', '#C4E8AC', null, null, 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1736834674/ecommerce/product/fb811c58-239d-4ef2-9e8c-1cd6d9b83dd3_0.png');
INSERT INTO public.products_images (seq_no, product_id, name, colour, created_at, deleted_at, image_url) VALUES (1, 'fb811c58-239d-4ef2-9e8c-1cd6d9b83dd3', 'Screenshot 2024-10-25 220247.png', '#7EDFF1', null, null, 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1736834678/ecommerce/product/fb811c58-239d-4ef2-9e8c-1cd6d9b83dd3_1.png');
INSERT INTO public.products_images (seq_no, product_id, name, colour, created_at, deleted_at, image_url) VALUES (0, '00000000-0000-0000-0000-000000000000', 'Screenshot 2024-10-25 223156.png', '#D28F1D', null, null, 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1736956925/ecommerce/product/00000000-0000-0000-0000-000000000000_0.png');
---- favorite_products
INSERT INTO public.favorite_products (account_id, product_id) VALUES ('ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', '00000000-0000-0000-0000-000000000005');
---- coupons
INSERT INTO public.coupons (id, code, created_at, deleted_at, coupon_type, description, end_date, start_date, usage_limit, value, current_usage) VALUES ('00000000-0000-0000-0000-000000000001', 'PERCENT20', '2025-01-03 04:10:48.640731', null, 'PERCENT', '20% off on selected items', '2024-12-31 23:59:59.000000', '2024-01-01 00:00:00.000000', 3, 20.00, 0);
INSERT INTO public.coupons (id, code, created_at, deleted_at, coupon_type, description, end_date, start_date, usage_limit, value, current_usage) VALUES ('81b9ca68-4f8e-4709-ad5e-d2d35740c8fd', 'AAA', '2025-01-10 17:05:00.039910', null, 'PERCENT', '10 time and percentage', '2025-01-31 17:02:26.000000', '2025-01-05 17:02:26.000000', 10, 10.00, null);
INSERT INTO public.coupons (id, code, created_at, deleted_at, coupon_type, description, end_date, start_date, usage_limit, value, current_usage) VALUES ('00000000-0000-0000-0000-000000000002', 'PERCENT30', '2025-01-03 04:10:48.640731', null, 'PERCENT', '30% off on electronics', '2025-02-10 23:59:59.000000', '2024-01-01 00:00:00.000000', 50, 30.00, 11);
INSERT INTO public.coupons (id, code, created_at, deleted_at, coupon_type, description, end_date, start_date, usage_limit, value, current_usage) VALUES ('00000000-0000-0000-0000-000000000000', 'PERCENT10', '2025-01-03 04:10:48.640731', null, 'PERCENT', '10% off on all items', '2025-02-24 23:59:59.000000', '2024-01-01 00:00:00.000000', 3, 10.00, 3);
---- orders
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('7d9863bb-697f-4d3b-adbe-9354965bcfea', '2025-01-10 22:10:31.652637', null, 'Hanoi', null, null, 'string', 100000.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('5de69141-41e9-40b9-936c-a037e38958e5', '2025-01-10 23:44:41.222655', null, 'abc', '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'ABCCCCCZZZZZ', 100000.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('b4b3f959-0718-447b-94af-3d485f8951e6', '2025-01-10 23:50:29.427730', null, 'abc', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'ABCCCCCZZZZZ', 100000.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('478d6e95-b0dd-48ad-9c1d-ef0d15ba6b29', '2025-01-11 00:14:02.859772', null, 'abc', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'ABCCCCCZZZZZ', 100000.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('3f433c9e-ce9a-494b-8559-8316d563eb5c', '2025-01-11 13:53:03.025774', null, 'Hanoi', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'string', 244832.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('8879fe0d-09f0-4b1b-81e5-d6d8f2e80314', '2025-01-12 00:40:58.897040', null, 'street 88, district 9, Ho Chi Minh', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'this is my fav', 125300.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('e7e7eca9-07b1-4675-aa66-a2ada1e7e649',  '2025-01-12 14:40:06.117259', null, 'street 99, district Thanh Oai, Ha Tay, Ha noi', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'this is my fav', 125300.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('075dc9d2-9447-43d9-b29f-4dc8cb8e7eb0', '2025-01-10 21:53:46.704360', '2025-01-15 22:25:41.438958', 'Hanoi', null, null, 'string', 100000.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('ba3b81b4-db2a-4720-8437-b5ac450dae54',  '2025-01-17 22:10:12.663344', null, null, '00000000-0000-0000-0000-000000000002', 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', null, 178332.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('83468a8d-59ec-4bcf-ba0b-07263549909a',  '2025-01-17 22:10:40.585575', null, null, '00000000-0000-0000-0000-000000000002', 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', null, 178332.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('e585f751-b4b3-4a1b-bd82-5b47519e3968',  '2025-01-17 22:10:45.268242', null, null, '00000000-0000-0000-0000-000000000002', 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', null, 178332.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('cc690ff9-4c9e-4b3d-a415-14112455982b',  '2025-01-17 22:15:05.181827', null, null, '00000000-0000-0000-0000-000000000002', 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', null, 335062.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('a2797254-ee2e-4210-b8d0-a5708013161c',  '2025-01-17 22:21:15.354124', null, '17 duong 160', '00000000-0000-0000-0000-000000000002', 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', 'I love these...', 335062.00);
INSERT INTO public.orders (id, created_at, deleted_at, address, coupon_id, creator_id, notes, total_value) VALUES ('a8f99a42-c88d-43f4-8516-4c18f0341a17',  '2025-01-18 01:51:53.103550', null, '17 Duong 160 Thu Duc, TP. Ho Chi Minh', '00000000-0000-0000-0000-000000000002', 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', 'Toi thich dien thoai APPLE', 234332.00);
----order_details
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('e4d9e81a-1b5a-41c8-a1b0-9294524de9a8', '2025-01-10 21:53:46.704360', null, 2, '075dc9d2-9447-43d9-b29f-4dc8cb8e7eb0', '00000000-0000-0000-0000-000000000000');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('eac35aba-b747-41e6-9097-0add3a6d874b', '2025-01-10 22:10:31.652637', null, 2, '7d9863bb-697f-4d3b-adbe-9354965bcfea', '00000000-0000-0000-0000-000000000000');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('9f65a35e-062c-4958-8421-acc3821a69d1', '2025-01-10 23:44:41.222655', null, 2, '5de69141-41e9-40b9-936c-a037e38958e5', '00000000-0000-0000-0000-000000000000');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('aa1660a7-0303-4ec4-8af2-563ca65da841', '2025-01-10 23:50:29.427730', null, 2, 'b4b3f959-0718-447b-94af-3d485f8951e6', '00000000-0000-0000-0000-000000000000');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('9b29742e-ea1b-42d4-a9fa-6b1a460f0957', '2025-01-11 00:14:02.859772', null, 2, '478d6e95-b0dd-48ad-9c1d-ef0d15ba6b29', '00000000-0000-0000-0000-000000000000');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('175060d5-5e81-45cc-bced-1975fc20c1a2', '2025-01-11 13:53:03.025774', null, 2, '3f433c9e-ce9a-494b-8559-8316d563eb5c', '00000000-0000-0000-0000-000000000000');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('f61147e1-e9de-44e4-9f21-8abd9991e399', '2025-01-11 13:53:03.025774', null, 1, '3f433c9e-ce9a-494b-8559-8316d563eb5c', '00000000-0000-0000-0000-000000000004');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('5009b330-836e-47c1-80bb-bc670e8c90ff', '2025-01-12 00:40:58.897040', null, 1, '8879fe0d-09f0-4b1b-81e5-d6d8f2e80314', '00000000-0000-0000-0000-000000000003');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('084df650-9dc2-4326-b1e8-ce4bfefdc574', '2025-01-12 14:40:06.117259', null, 1, 'e7e7eca9-07b1-4675-aa66-a2ada1e7e649', '00000000-0000-0000-0000-000000000003');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('64e63388-77c5-448c-bef5-29cd30c3d25b',  '2025-01-17 22:10:12.663344', null, 1, 'ba3b81b4-db2a-4720-8437-b5ac450dae54', '00000000-0000-0000-0000-000000000001');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('0b46fc2c-36f7-4067-87a2-3cbc88f9cd6a',  '2025-01-17 22:10:12.663344', null, 1, 'ba3b81b4-db2a-4720-8437-b5ac450dae54', '00000000-0000-0000-0000-000000000004');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('04c1c70d-adc8-49d1-9e65-fb9b7bb80138',  '2025-01-17 22:10:40.585575', null, 1, '83468a8d-59ec-4bcf-ba0b-07263549909a', '00000000-0000-0000-0000-000000000001');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('4cdb9999-5e7d-4ace-8e68-ef3ca924d5a8',  '2025-01-17 22:10:40.585575', null, 1, '83468a8d-59ec-4bcf-ba0b-07263549909a', '00000000-0000-0000-0000-000000000004');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('3808c8a2-e083-40a9-aaa6-ad4b5ea4ad89',  '2025-01-17 22:10:45.268242', null, 1, 'e585f751-b4b3-4a1b-bd82-5b47519e3968', '00000000-0000-0000-0000-000000000001');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('e86ce53e-3fc1-484b-bd7c-d7529bbebd04',  '2025-01-17 22:10:45.268242', null, 1, 'e585f751-b4b3-4a1b-bd82-5b47519e3968', '00000000-0000-0000-0000-000000000004');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('85174f72-f0b9-4cfa-8cfc-3d703293b339',  '2025-01-17 22:15:05.181827', null, 2, 'cc690ff9-4c9e-4b3d-a415-14112455982b', '00000000-0000-0000-0000-000000000001');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('e6eb5ace-dad0-42d4-83c9-619fa8a17281',  '2025-01-17 22:15:05.181827', null, 1, 'cc690ff9-4c9e-4b3d-a415-14112455982b', '00000000-0000-0000-0000-000000000004');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('bfa395e8-d790-4604-910e-e789373f80e9',  '2025-01-17 22:15:05.181827', null, 1, 'cc690ff9-4c9e-4b3d-a415-14112455982b', '00000000-0000-0000-0000-000000000005');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('7eb682ed-8099-408e-a14f-1afd45fef328',  '2025-01-17 22:21:15.354124', null, 2, 'a2797254-ee2e-4210-b8d0-a5708013161c', '00000000-0000-0000-0000-000000000001');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('5f59bd3e-c7fb-4c8f-99f8-28939b77c25f',  '2025-01-17 22:21:15.354124', null, 1, 'a2797254-ee2e-4210-b8d0-a5708013161c', '00000000-0000-0000-0000-000000000004');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('ed7e6a58-a831-4370-bb69-4c8995ac6333',  '2025-01-17 22:21:15.354124', null, 1, 'a2797254-ee2e-4210-b8d0-a5708013161c', '00000000-0000-0000-0000-000000000005');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('90ef126d-024b-4e8f-ad47-4fa88f23ef8c',  '2025-01-18 01:51:53.103550', null, 1, 'a8f99a42-c88d-43f4-8516-4c18f0341a17', '00000000-0000-0000-0000-000000000001');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('6645cb28-1e61-436e-82be-1a0e516d6be6',  '2025-01-18 01:51:53.103550', null, 1, 'a8f99a42-c88d-43f4-8516-4c18f0341a17', '00000000-0000-0000-0000-000000000002');
INSERT INTO public.order_details (id, created_at, deleted_at, quantity, order_id, product_id) VALUES ('c0385ecd-8a5b-4ea4-9cd7-1ce626022581',  '2025-01-18 01:51:53.103550', null, 1, 'a8f99a42-c88d-43f4-8516-4c18f0341a17', '00000000-0000-0000-0000-000000000004');
---- order_payments
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', 'de3949df-22ca-4302-9d0c-77857d0f98b5', '2025-01-10 21:53:46.704360', null, null, 'PENDING', '075dc9d2-9447-43d9-b29f-4dc8cb8e7eb0', 100000.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', '0dbda149-e6c2-472d-a4fb-5f1584ea3f00', '2025-01-10 22:10:31.652637', null, null, 'PENDING', '7d9863bb-697f-4d3b-adbe-9354965bcfea', 100000.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', 'f804649c-f6fb-4a29-a010-35395b1f2328', '2025-01-10 23:44:41.222655', null, '00000000-0000-0000-0000-000000000000', 'PENDING', '5de69141-41e9-40b9-936c-a037e38958e5', 100000.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', 'f8a3d770-4da1-4ccf-b2f6-3a5372131e9e', '2025-01-10 23:50:29.427730', null, '00000000-0000-0000-0000-000000000000', 'PENDING', 'b4b3f959-0718-447b-94af-3d485f8951e6', 100000.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', '3c707855-449d-42f5-90ed-9235e05170bd', '2025-01-11 00:14:02.859772', null, '00000000-0000-0000-0000-000000000000', 'PENDING', '478d6e95-b0dd-48ad-9c1d-ef0d15ba6b29', 100000.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', 'a6b7b4da-be56-46fe-9af9-1844858e8458', '2025-01-11 13:53:03.025774', null, '00000000-0000-0000-0000-000000000000', 'PAID', '3f433c9e-ce9a-494b-8559-8316d563eb5c', 244832.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', '6144ca92-ed68-4c39-8f3a-f67078b24bc7', '2025-01-12 00:40:58.897040', null, '00000000-0000-0000-0000-000000000000', 'PENDING', '8879fe0d-09f0-4b1b-81e5-d6d8f2e80314', 125300.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', 'd16d411c-4a99-494e-b39a-b3c210262f00', '2025-01-12 14:40:06.117259', null, '00000000-0000-0000-0000-000000000000', 'PAID', 'e7e7eca9-07b1-4675-aa66-a2ada1e7e649', 125300.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', '7824c729-c350-4994-818e-cdad8a89d14f', '2025-01-17 22:10:12.663344', null, 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', 'PENDING', 'ba3b81b4-db2a-4720-8437-b5ac450dae54', 178332.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', '1a3310b5-dbc2-4304-801f-e15e0bef0ed9',  '2025-01-17 22:10:40.585575', null, 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', 'PENDING', '83468a8d-59ec-4bcf-ba0b-07263549909a', 178332.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', 'f5e12ab9-a953-49f0-a160-17099c6bf543',  '2025-01-17 22:10:45.268242', null, 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', 'PENDING', 'e585f751-b4b3-4a1b-bd82-5b47519e3968', 178332.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', 'c0235b6c-6519-4275-b33f-504522091eb5',  '2025-01-17 22:15:05.181827', null, 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', 'PENDING', 'cc690ff9-4c9e-4b3d-a415-14112455982b', 335062.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', '1cf521e8-ed6b-4456-80b8-92e3b25ad22b',  '2025-01-17 22:21:15.354124', null, 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', 'PAID', 'a2797254-ee2e-4210-b8d0-a5708013161c', 335062.00);
INSERT INTO public.payments (payment_method, id, created_at, deleted_at, account_id, status, order_id, amount) VALUES ('Payment', 'ee8f136e-6a07-4ec5-88a1-2f3abf694941',  '2025-01-18 01:51:53.103550', null, 'ff7bc69b-ab6b-4966-ba03-66fcafe27e1e', 'PAID', 'a8f99a42-c88d-43f4-8516-4c18f0341a17', 234332.00);
----- order_details_orders
INSERT INTO public.vnpay_payments (bank_code, card_method, order_info, id, trans_ref, trans_no, secure_hash) VALUES (null, null, 'this is my favorite', 'f804649c-f6fb-4a29-a010-35395b1f2328', '5de69141-41e9-40b9-936c-a037e38958e5', null, null);
INSERT INTO public.vnpay_payments (bank_code, card_method, order_info, id, trans_ref, trans_no, secure_hash) VALUES ('NCB', 'ATM', 'this is my favorite', 'a6b7b4da-be56-46fe-9af9-1844858e8458', '3f433c9e-ce9a-494b-8559-8316d563eb5c', '14788917', 'ff7bd0196b3212d9e90637205d219c3c47f7a02ae64dc6832e78906034307c01992977c106005b7c366805a8eb73d41a6891ef38493ecab0bb1dd1ceda18ceec');
INSERT INTO public.vnpay_payments (bank_code, card_method, order_info, id, trans_ref, trans_no, secure_hash) VALUES (null, null, 'this is my fav', '6144ca92-ed68-4c39-8f3a-f67078b24bc7', '8879fe0d-09f0-4b1b-81e5-d6d8f2e80314', null, null);
INSERT INTO public.vnpay_payments (bank_code, card_method, order_info, id, trans_ref, trans_no, secure_hash) VALUES ('NCB', 'ATM', 'this is my fav', 'd16d411c-4a99-494e-b39a-b3c210262f00', 'e7e7eca9-07b1-4675-aa66-a2ada1e7e649', '14789582', 'f788e108deec599763df6a876a00001cd10a052b01faee0bd171c9d8298a3148d2ff62964a1db6637ae9cfc5846de24f381f299c11224bafb22194bf85794cff');
INSERT INTO public.vnpay_payments (bank_code, card_method, order_info, id, trans_ref, trans_no, secure_hash) VALUES ('NCB', 'ATM', 'I love these...', '1cf521e8-ed6b-4456-80b8-92e3b25ad22b', 'a2797254-ee2e-4210-b8d0-a5708013161c', '14797266', '9f55443f13ed329efd9ba08dbd85f400118600e987bec1f541d836f0811c1f088e2120b12fe762903f65c1f9b7198dfb3ab1facb15b8b9e4e3c86116a0679a3d');
INSERT INTO public.vnpay_payments (bank_code, card_method, order_info, id, trans_ref, trans_no, secure_hash) VALUES ('NCB', 'ATM', 'Toi thich dien thoai APPLE', 'ee8f136e-6a07-4ec5-88a1-2f3abf694941', 'a8f99a42-c88d-43f4-8516-4c18f0341a17', '14797416', 'a3dda5b027dc191910c805989b805c22016ab5e31a15dbfece02a3f37726b4083e7f03aac48fb4d57c96cf92cfcea607fb6832d96c3dea00c9abac7e8f50da39');
----- blog_posts:
INSERT INTO public.blog_posts (id, created_at, deleted_at, author_id, content, image_url, is_html, subtitle, title) VALUES ('00000000-0000-0000-0000-000000000001', '2025-01-03 04:10:48.640731', null, '00000000-0000-0000-0000-000000000001', 'This is a blog post', 'https://example.com/image.jpg', false, 'This is a subtitle', 'This is a title');
INSERT INTO public.blog_posts (id, created_at, deleted_at, author_id, content, image_url, is_html, subtitle, title) VALUES ('00000000-0000-0000-0000-000000000002', '2025-01-03 04:10:48.640731', '2025-01-17 13:51:35.073428', '00000000-0000-0000-0000-000000000002', 'This is a blog post', 'https://example.com/image.jpg', false, 'This is a subtitle', 'This is a title');
INSERT INTO public.blog_posts (id, created_at, deleted_at, author_id, content, image_url, is_html, subtitle, title) VALUES ('3e5e8f5f-7ada-4178-9c3a-01cc75d0ea51',  '2025-01-17 13:49:36.072838', null, '00000000-0000-0000-0000-000000000000', '<p><strong>introduce to you....</strong></p>', 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1737096575/ecommerce/blog/3e5e8f5f-7ada-4178-9c3a-01cc75d0ea51.png', true, 'Phong title', 'My New post');
INSERT INTO public.blog_posts (id, created_at, deleted_at, author_id, content, image_url, is_html, subtitle, title) VALUES ('00000000-0000-0000-0000-000000000000', '2025-01-03 04:10:48.640731', null, '00000000-0000-0000-0000-000000000000', '<p>Phong Post</p>', null, true, 'Phong subtitle', 'Phong');
COMMIT ;