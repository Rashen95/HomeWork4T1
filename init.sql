create schema my_schema

    create table my_schema.users
    (
        id         bigserial,
        user_name  varchar(30)  not null unique,
        password   varchar(100) not null,
        first_name varchar(30)  not null,
        last_name  varchar(30)  not null,
        role       varchar(30)  not null check (role in ('ROLE_USER', 'ROLE_ADMIN')),
        primary key (id)
    );

insert into my_schema.users (user_name, password, first_name, last_name, role)
values ('admin', '$2a$10$XKzwxHC2YVB4duuoighUeO/LhOoZEQ5bZRGazB1GIc59etkGxkMmC', 'АДМИНИСТРАТОР', '', 'ROLE_ADMIN');