use swiftbill;

insert into payment_methods (name)
values
    ("Cash"),
    ("Online banking");

insert into roles (role_name)
values
    ("ROLE_ADMIN"),
    ("ROLE_STORE_OWNER"),
    ("ROLE_CASHIER"),
    ("ROLE_DATA_ENTRY");

insert into user_status (status_name)
values
    ("Ative"),
    ("Disabled"),
    ("Retired");

insert into users (phone, password, created_date)
values
    ("0989762205", "$2a$10$T3PuW36GVyg2UdvNDQNms.nf0pgvarSrWmbr6UfS3ZZfjR6Zn2E1u", current_timestamp);

insert into rel_user_roles (user_id, role_id)
values
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4);

update users
    set status_id = 1
    where user_id = 1;

