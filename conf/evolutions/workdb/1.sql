# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table public.sales (
  id_sale                       serial not null,
  head                          varchar(255),
  text                          varchar(255),
  constraint pk_sales primary key (id_sale)
);

create table public.user (
  id                            integer not null,
  login                         varchar(255),
  password                      varchar(255),
  isadmin                       boolean default false not null,
  constraint pk_user primary key (id)
);
create sequence public.user_id_seq;

create table public.basket (
  id_basket                     serial not null,
  id_product                    integer,
  login                         varchar(255),
  constraint pk_basket primary key (id_basket)
);

create table public.rating (
  id_user                       integer not null,
  id_product                    integer,
  rating                        integer,
  constraint pk_rating primary key (id_user)
);
create sequence public.rating_id_user_id_seq;

create table public.wine (
  id_product                    serial not null,
  name                          varchar(255),
  colour                        varchar(255),
  country                       varchar(255),
  brand                         varchar(255),
  shelf_life                    varchar(255),
  sugar                         varchar(255),
  grape_sort                    varchar(255),
  price                         float,
  value                         float,
  degree                        float,
  constraint pk_wine primary key (id_product)
);


# --- !Downs

drop table if exists public.sales cascade;

drop table if exists public.user cascade;
drop sequence if exists public.user_id_seq;

drop table if exists public.basket cascade;

drop table if exists public.rating cascade;
drop sequence if exists public.rating_id_user_id_seq;

drop table if exists public.wine cascade;

