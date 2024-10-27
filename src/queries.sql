select *
from accounts
         join profiles on accounts.id = profiles.id;

select count(1) from Accounts a;
abort;







UPDATE accounts
SET role = 'ROLE_ADMIN';

select * from categories;


delete
from profiles e
where e.id = (select a.id from accounts a order by id desc limit 1);