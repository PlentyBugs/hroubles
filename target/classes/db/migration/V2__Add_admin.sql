insert into usr (id, username, password, active) values (2, 'admin', '$2a$08$le4MAoiLpwgrSVr5YCpzsOd0m7ogsd71K9hhdI1VYumql5WYvcqUe', true);

insert into user_role (user_id, roles) values (2, 'USER'), (2, 'ADMIN'), (2, 'SELLER');