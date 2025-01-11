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

update payments set status = 'PENDING';
select * from payments;

select v1_0.id,v1_1.account_id,v1_1.amount,v1_1.code,v1_1.created_at,v1_1.deleted_at,v1_1.order_id,v1_1.payment_method,v1_1.status,v1_0.bank_code,v1_0.card_method,v1_0.order_info,v1_0.secure_hash,v1_0.trans_no,v1_0.trans_ref
from vnpay_payments v1_0
    join payments v1_1 on v1_0.id=v1_1.id;

select * from vnpay_payments;
select * from payments;
update orders set total_value = 100_000;
update payments set amount = 100_000;

update products set price = 100_000 where code <= 9 ;
update products set price = 200_000 where code > 9 ;

BEGIN;
select o1_0.id,o1_0.address,o1_0.code,o1_0.coupon_id,o1_0.created_at,o1_0.creator_id,o1_0.deleted_at,o1_0.notes,p1_0.id,p1_0.payment_method,p1_0.account_id,p1_0.amount,p1_0.code,p1_0.created_at,p1_0.deleted_at,p1_0.order_id,p1_0.status,p1_1.exchange,p1_1.paid,p1_2.bank_code,p1_2.card_method,p1_2.order_info,p1_2.secure_hash,p1_2.trans_no,p1_2.trans_ref,o1_0.total_value from orders o1_0 left join (payments p1_0 left join cash_payments p1_1 on p1_0.id=p1_1.id left join vnpay_payments p1_2
    on p1_0.id=p1_2.id) on o1_0.id=p1_0.order_id where o1_0.coupon_id=?;

ABORT;
