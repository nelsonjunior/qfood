create table location
(
	id bigint generated by default as identity
		constraint location_pkey
			primary key,
	latitude double precision,
	longitude double precision
);

alter table location owner to qfood;

create table restaurant
(
	id bigint generated by default as identity
		constraint restaurant_pkey
			primary key,
	dataupdatedat timestamp,
	datecreatedat timestamp,
	documenti1 varchar(255),
	name varchar(255),
	owner varchar(255),
	location_id bigint
		constraint fk8xt7uuc7ola9jk93wl3i43ule
			references location
);

alter table restaurant owner to qfood;

create table menu
(
	id bigint generated by default as identity
		constraint menu_pkey
			primary key,
	description varchar(255),
	name varchar(255),
	price numeric(19,2),
	restaurant_id bigint
		constraint fkblwdtxevpl4mrds8a12q0ohu6
			references restaurant
);

alter table menu owner to qfood;


CREATE TABLE menu_item (
	menuID int,
	userID varchar(200)
);

INSERT INTO location (id, latitude, longitude) VALUES(1000, -15.817759, -47.836959);

INSERT INTO restaurant (id, location_id, name) VALUES(999, 1000, 'Manguai');

INSERT INTO menu
(id, name, description, restaurant_id, price)
VALUES(9998, 'Cuscuz com Ovo', 'Bom demais no café da manhã', 999, 3.99);

INSERT INTO menu
(id, name, description, restaurant_id, price)
VALUES(9997, 'Peixe frito', 'Agulhinha frita, excelente com Cerveja', 999, 99.99);