--date: 2023-04-11
--author: vstebletsov

create table if not exists genres (
    id bigserial,
    name varchar(255),
    primary key (id)
)