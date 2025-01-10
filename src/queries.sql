select *
from accounts
         join profiles on accounts.id = profiles.id;

select *
from coupons;
select *
from brands
limit 1;

select *
from brands;
select *
from categories;

select *
from products_images;

select *
from products;

select *
from orders;

select code, name, available_date, created_at
from products;

select * from payments join vnpay_payments on payments.id = vnpay_payments.id;

UPDATE accounts
SET role = 'ROLE_ADMIN';

select *
from categories;

select *
from products_images;

delete
from profiles e
where e.id = (select a.id from accounts a order by id desc limit 1);


select o1_0.id,o1_0.address,o1_0.code,c1_0.id,c1_0.code,c1_0.coupon_type,c1_0.created_at,c1_0.current_usage,c1_0.deleted_at,c1_0.description,c1_0.end_date,c1_0.order_id,c1_0.start_date,c1_0.usage_limit,c1_0.value,o1_0.coupon_id,o1_0.created_at,o1_0.creator_id,o1_0.deleted_at,o1_0.notes,od1_0.order_id,od1_0.id,od1_0.code,od1_0.created_at,od1_0.deleted_at,od1_0.product_id,p1_0.id,p1_0.available_date,p1_0.brand_id,p1_0.category_id,p1_0.code,p1_0.created_at,p1_0.deleted_at,p1_0.description,p1_0.discount_percent,p1_0.highlights,p1_0.name,p1_0.policies,p1_0.price,p1_0.quantity,p1_0.rating,p1_0.status,p1_0.stock,p1_0.thumbnail_url,od1_0.quantity,p2_0.id,p2_0.addresses,p2_0.avatar_url,p2_0.date_of_birth,p2_0.full_name,p2_0.phone,p2_0.primary_address,o1_0.status,o1_0.total_value from orders o1_0 left join coupons c1_0 on c1_0.id=o1_0.coupon_id left join order_details od1_0 on o1_0.id=od1_0.order_id left join products p1_0 on p1_0.id=od1_0.product_id left join profiles p2_0
    on p2_0.id=o1_0.creator_id where 1=1;

] [ERROR: column c1_0.order_id does not exist
