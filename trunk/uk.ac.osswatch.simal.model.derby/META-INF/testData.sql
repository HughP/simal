connect 'jdbc:derby://localhost:1527/simal;create=true;user=simal;password=simal';

insert into Project values (1, 'test');

disconnect;
exit;
