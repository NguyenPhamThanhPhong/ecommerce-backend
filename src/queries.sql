select *
from accounts
         join profiles on accounts.id = profiles.id;

select count(1)
from Accounts a;

select *
from brands
limit 1;

select *
from brands;
select *
from categories;

select * from products_images;

select *
from products;

select code,name,available_date,created_at from products;


UPDATE accounts
SET role = 'ROLE_ADMIN';

select *
from categories;


delete
from profiles e
where e.id = (select a.id from accounts a order by id desc limit 1);