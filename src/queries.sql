select *
from accounts
         join profiles on accounts.id = profiles.id;

select *
from coupons;
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

select *
from categories;

select *
from products_images;

delete
from profiles e
where e.id = (select a.id from accounts a order by id desc limit 1);

update payments set status = 'PENDING';
select * from payments;

select v1_0.id,v1_1.account_id,v1_1.amount,v1_1.code,v1_1.created_at,v1_1.deleted_at,v1_1.order_id,v1_1.payment_method,v1_1.status,v1_0.bank_code,v1_0.card_method,v1_0.order_info,v1_0.secure_hash,v1_0.trans_no,v1_0.trans_ref
from vnpay_payments v1_0
    join payments v1_1 on v1_0.id=v1_1.id;

select * from vnpay_payments;
select * from payments;

select * from orders o where o.deleted_at is null

