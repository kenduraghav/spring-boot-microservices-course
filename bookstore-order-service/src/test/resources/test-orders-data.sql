truncate table orders cascade;

alter sequence order_id_seq restart with 100;
alter sequence order_item_id_seq restart with 100;

/**
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
**/

insert into orders(id,order_number, username,
customer_name, customer_email, customer_phone, delivery_address_line1, 
delivery_address_line2, delivery_address_city, delivery_address_state, 
delivery_address_zipcode, delivery_address_country, status, comments, create_at, updated_at)
 values 
(100, 'ORD-001', 'testUser', 'John Doe', 'john@gmail.com', '1234567890', '123 Main St', 'Apt 4B', 'Chennai', 'TN', '10001', 'India', 'NEW', 'Please deliver between 9 AM and 5 PM.', now(), now()),
(101, 'ORD-002', 'testUser', 'Jane Smith', 'jane@gmail.com', '0987654321', '456 Elm St', null, 'Chennai', 'TN', '90001', 'India', 'NEW', 'Leave package at the front door.', now(), now());




insert into order_items(id, code, name, price, quantity, order_id) values
(100, 'P001', 'Product 1', 10.00, 2, 100),
(101, 'P002', 'Product 2', 20.00, 1, 100),	
(102, 'P003', 'Product 3', 15.00, 3, 101);
