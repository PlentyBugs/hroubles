insert into product (id, name, description, count, price, user_id, filename) values
    (1, 'My opinion', 'Useless', 4, 100, 2, '1.png'),
    (2, 'The Game', 'Maybe cool', 1, 599, 2, '2.png'),
    (3, 'Tesla pants', 'Red', 12, 1800, 2, '3.png'),
    (4, 'Meaning of life', 'We dont have it' , 0, 99, 2, '4.png');

insert into product_tag (product_id, tags) values (1, 'TOY'), (2, 'GAME'), (3, 'CAR'), (3, 'CLOTHES'), (4, 'SCIENTIFIC');

alter sequence hibernate_sequence restart with 5;