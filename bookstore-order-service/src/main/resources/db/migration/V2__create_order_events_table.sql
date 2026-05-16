create sequence order_event_id_seq start with 1 increment by 50;

create table order_events 
(
	
	id 				bigint not null default nextVal('order_event_id_seq') primary key,
	order_number 	text not null,
	event_id 		text not null unique,
	event_type 		text not null,
	payload 		text not null,
	created_at 		timestamp,
	updated_at 		timestamp
)