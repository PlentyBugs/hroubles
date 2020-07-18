delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '$2a$08$bgSzfgN9UVrXLMzNodznVOerzznIXTMWyD3qBAygUmg507KJ4F5aC', '12'),
(2, true, '$2a$08$bgSzfgN9UVrXLMzNodznVOerzznIXTMWyD3qBAygUmg507KJ4F5aC', 'admin');
insert into user_role(user_id, roles) values (1, 'USER'), (2, 'USER'), (2, 'ADMIN');