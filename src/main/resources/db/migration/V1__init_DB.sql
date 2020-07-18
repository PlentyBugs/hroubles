create sequence hibernate_sequence start 1 increment 1;

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table usr (
    id int8 not null,
    activation_code varchar(255),
    active boolean not null,
    email varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
);

create table product_tag (
    product_id int8 not null,
    tags varchar(255)
);

create table product (
    id int8 not null,
    name varchar(255) not null,
    description varchar(2048) not null,
    count int8 not null,
    price int8 not null,
    user_id int8,
    filename varchar(255),
    primary key (id)
);

alter table if exists user_role add constraint user_role_user_fk foreign key (user_id) references usr;

alter table if exists product_tag add constraint product_tag_product_fk foreign key (product_id) references product;