BEGIN;
create table accounts
(
    id           uuid primary key,
    created_at   timestamp not null default now(),
    deleted_at   timestamp,
    enable_date  timestamp,
    disable_date timestamp,
    email        varchar unique,
    password     varchar,
    login_id     varchar unique,
    is_verified  boolean,
    otp          varchar(6),
    otp_expiry   timestamp,
    role         varchar            default 'ANONYMOUS'
);
create index accounts_email_hash_index
    on accounts (email) where email is not null;
create index accounts_login_id_hash_index
    on accounts (login_id) where login_id is not null;

create table tokens
(
    id             uuid primary key,
    account_id     uuid      not null,
    created_at     timestamp not null default now(),
    deleted_at     timestamp,
    access_token   varchar   not null,
    refresh_token  varchar   not null,
    access_expiry  timestamp not null,
    refresh_expiry timestamp not null,
    constraint FK_tokens_accountId FOREIGN KEY (account_id) references accounts (id)
        ON DELETE CASCADE
);


create table profiles
(
    id            uuid primary key,
    full_name     varchar(40),
    age           smallint,
    avatar_url    varchar(2048),
    phone         varchar(11),
    date_of_birth varchar(10),
    constraint FK_account_admin_id FOREIGN KEY (id) references accounts (id)
        ON DELETE cascade
);

create table blog_posts
(
    id         uuid primary key,
    created_at timestamp not null default now(),
    deleted_at timestamp,
    title      varchar            default '',
    author_id  uuid,
    subtitle   varchar            default now(),
    content    varchar            default '',
    is_html    boolean,
    --MUST BE PROFILES -> IF NOT, THERE'S NO NAME TO DISPLAY
    constraint FK_blogPosts_authorId_profiles_id FOREIGN KEY (author_id) references profiles (id)
        ON DELETE set NULL
);

create table brands
(
    id          uuid primary key,
    created_at  timestamp not null default now(),
    deleted_at  timestamp,
    description varchar,
    image_url   varchar(2048),
    name        varchar(40),
    sold        int,
    stock       int
);

create table categories
(
    id          uuid primary key,
    created_at  timestamp not null default now(),
    deleted_at  timestamp,
    description varchar,
    image_url   varchar(2048),
    name        varchar(40),
    sold        int,
    stock       int
);
create table brands_categories
(
    brand_id    uuid,
    category_id uuid,
    constraint PK_brands_categories_brandId_categoryId primary key (brand_id, category_id),
    constraint FK_brands_categories_brandId FOREIGN KEY (brand_id) references brands (id),
    constraint FK_brands_categories_categoryId FOREIGN KEY (brand_id) references categories (id)
);

create table discounts
(
    id            serial primary key,
    created_at    timestamp not null default now(),
    deleted_at    timestamp,
    title         varchar,
    description   varchar,
    enable_date   timestamp,
    disable_date  timestamp,
    discount_type varchar check ( discount_type in ('PERCENT', 'FIXED', 'PROMOTION'))
);

create table products
(
    id          serial primary key,
    sku         varchar(10), -- Stock Keeping Unit
    created_at  timestamp not null default now(),
    deleted_at  timestamp,
    name        varchar(255),
    description varchar,
    price       numeric(10, 2),
    brand_id    uuid,
    category_id uuid,
    image_url   varchar(2048),
    quantity    int,
    stock       int,
    sold        int,         -- TRIGGER
    attributes  jsonb,
    constraint FK_products_categoryId FOREIGN KEY (category_id) references categories (id),
    constraint FK_products_brandId FOREIGN KEY (brand_id) references brands (id)
);

create table promotion_discounts
(
    id                int primary key,
    required_quantity smallint,
    reward_quantity   smallint,
    reward_product_id int,
    constraint FK_promotions_discountId FOREIGN KEY (id) references discounts (id),
    constraint FK_promotions_rewardProductId FOREIGN KEY (reward_product_id) references products (id)
);


create table percent_discounts
(
    id               int primary key,
    min_condition    numeric(10, 2),
    discount_percent numeric(3, 2) check (discount_percent >= 0 and discount_percent <= 100),
    max_discount     numeric(10, 2),
    constraint FK_promotions_discountId FOREIGN KEY (id) references discounts (id)
);

create table fixed_discounts
(
    id             int primary key,
    min_condition  numeric(10, 2),
    fixed_discount numeric(10, 2),
    constraint FK_promotions_discountId FOREIGN KEY (id) references discounts (id)
);



create table products_discounts
(
    product_id  int,
    discount_id int,
    constraint PK_products_discounts_productId_discountId primary key (product_id, discount_id),
    constraint FK_products_discounts_productId FOREIGN KEY (product_id) references products (id),
    constraint FK_products_discounts_discountId FOREIGN KEY (discount_id) references discounts (id)
);


create table payments
(
    id             serial primary key,
    created_at     timestamp   not null default now(),
    deleted_at     timestamp,
    account_id     uuid unique not null,
    status         varchar,
    payment_method varchar
);

create table vnpay_payments
(
    id          int primary key,
    amount      numeric(10, 2),
    bank_code   varchar,
    order_info  varchar,
    card_method varchar,
    constraint FK_vnpay_payment_paymentId FOREIGN KEY (id) references payments (id)
);

create table cash_payments
(
    id          int primary key,
    amount      numeric(10, 2),
    paid        numeric(10, 2),
    exchange    numeric(10, 2),
    cash_method varchar,
    constraint FK_cash_payment_paymentId FOREIGN KEY (id) references payments (id)
);

create table orders
(
    id          serial primary key,
    created_at  timestamp not null default now(),
    deleted_at  timestamp,
    discount_id int,
    description varchar,
    address     varchar,
    status      varchar,
    --the one who responsible for the order creation
    issuer_id   uuid,
    payment_id  int,
    constraint FK_orders_accountId FOREIGN KEY (issuer_id) references profiles (id),
    constraint FK_orders_paymentId FOREIGN KEY (payment_id) references payments (id),
    constraint FK_orders_discountId FOREIGN KEY (discount_id) references discounts (id)
);

create table order_details
(
    order_id    int not null,
    product_id  int not null,
    quantity    int,
    gross_total numeric(10, 2),
    net_total   numeric(10, 2),
    constraint FK_order_detail_orderId FOREIGN KEY (order_id) references orders (id),
    constraint FK_order_detail_productId FOREIGN KEY (product_id) references products (id)
);
COMMIT;



abort;


