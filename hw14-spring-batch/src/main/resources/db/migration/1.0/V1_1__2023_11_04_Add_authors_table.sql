--date: 2023-04-11
--author: vstebletsov

create table if not exists authors (
     id bigserial,
     full_name varchar(255),
     primary key (id)
)