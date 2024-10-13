select *
from accounts
         join profiles on accounts.id = profiles.id;

UPDATE accounts
SET role = 'ROLE_ADMIN';



delete
from profiles e
where e.id = (select a.id from accounts a order by id desc limit 1);