select *
from accounts
         join profiles on accounts.id = profiles.id;
abort;
select *
from coupons;
select *
from brands;
select *
from categories;
select *
from products_images
left join products p on product_id = p.id;
select *
from products;
select *
from orders;


select *
from blog_posts;

select *
from payments
         join vnpay_payments on payments.id = vnpay_payments.id;




select *
from categories;

select *
from products_images
         join products p on product_id = p.id;

delete
from profiles e
where e.id = (select a.id from accounts a order by id desc limit 1);

update payments
set status = 'PENDING';
select *
from payments;

select *
from vnpay_payments;
select *
from payments;

select *
from orders o
where o.deleted_at is null;

select created_at::date, sum(total_value)
from orders o
where created_at is not null
  and created_at::date >= (CURRENT_DATE - INTERVAL '7 days')::date
group by created_at::date

update products p
set policies = '<section id="policies">
    <h2>Our Policies</h2>
    <ul>
        <li>30-day money-back guarantee.</li>
        <li>Free shipping on orders over $50.</li>
        <li>24/7 customer support for all inquiries.</li>
    </ul>
</section>',
    highlights = '<section id="highlights">
    <h2>Product Highlights</h2>
    <ul>
        <li>Innovative design with premium materials.</li>
        <li>Seamless performance with advanced technology.</li>
        <li>Eco-friendly and sustainable packaging.</li>
    </ul>
</section>';



