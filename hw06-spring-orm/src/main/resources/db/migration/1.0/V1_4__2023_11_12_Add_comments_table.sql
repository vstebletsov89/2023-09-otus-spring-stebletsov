--date: 2023-12-11
--author: vstebletsov

create table if not exists comments (
   id bigserial,
   text varchar(500),
   book_id bigint references books (id) on delete cascade,
   primary key (id)
)