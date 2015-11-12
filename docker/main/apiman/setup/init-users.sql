
create table users (
	name varchar(20),
	password varchar (50)
);

create table roles (
	name varchar(20),
	role varchar (20)
);

insert into users values ('admin', 'admin');
insert into users values ('franta', 'franta');

insert into roles values ('admin', 'manager');
insert into roles values ('admin', 'employee'); 
insert into roles values ('franta', 'employee'); 
