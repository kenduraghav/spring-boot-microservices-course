create sequence order_id_seq start with 1 increment by 50;
create sequence order_item_id_seq start with 1 increment by 50;


create table orders
(
	id 							bigint not null default nextval('order_id_seq') primary key,
	order_number 				text not null,
	username 					text not null,
	customer_name 				text not null,
	customer_email 				text not null,
	customer_phone 				text not null,
	delivery_address_line1		text not null,
	delivery_address_line2 		text,
	delivery_address_city 		text not null,
	delivery_address_state 		text not null,	
	delivery_address_zipcode	text not null,
	delivery_address_country 	text not null,
	status 						text not null,
	comments 					text,
	create_at 					timestamp,
	updated_at 					timestamp
);


create table order_items 
(
	id                	bigint not null default nextval('order_item_id_seq') primary key,
	code 			    text not null,
	name 			    text not null,
	price				numeric not null,
	quantity            integer not null,
	order_id            bigint not null references orders(id)
);