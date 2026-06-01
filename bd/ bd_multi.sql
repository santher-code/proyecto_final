create database bd_multi;
use bd_multi;

create table personaje (
id_personaje int not null primary key auto increment,
nombre varchar(20) not null,
rol varchar(10) not null,
nivel smallint not null,
vida smallint not null , 
daño smallint not null 
);


