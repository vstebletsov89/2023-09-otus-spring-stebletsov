insert into authors(full_name)
values ('TestAuthor_1'), ('TestAuthor_2'), ('TestAuthor_3');

insert into genres(name)
values ('TestGenre_1'), ('TestGenre_2'), ('TestGenre_3');

insert into books(title, author_id, genre_id)
values ('TestBookTitle_1', 1, 1), ('TestBookTitle_2', 2, 2), ('TestBookTitle_3', 3, 3);
