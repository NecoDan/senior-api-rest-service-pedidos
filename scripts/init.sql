-- create database senior_system;
-- \c senior_system

create schema if not exists senior_services;
set schema 'senior_services';

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

create table if not exists senior_services.ss01_produto
(
    id                 uuid                         not null,
    codigo_produto     bigint                       not null,
    codigo_barras      varchar                      null,
    descricao          varchar(300)                 not null,
    id_tipo_finalidade integer                      not null,
    valor_custo        decimal(19, 6) default 0.0   not null,
    dt_cadastro        timestamp      default now() not null,
    ativo              bool                         null default true,
    primary key (id)
);

create unique index uq_ss01_produto on senior_services.ss01_produto (id);

alter table senior_services.ss01_produto
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

create table if not exists senior_services.ss02_pedido
(
    id                        uuid                         not null,
    id_status_pedido          integer                      not null,
    valor_itens               decimal(19, 6) default 0.0   not null,
    valor_desconto_total      decimal(19, 6) default 0.0   not null,
    valor_total               decimal(19, 6) default 0.0   not null,
    valor_percentual_desconto decimal(19, 6) default 0.0   not null,
    dt_cadastro               timestamp      default now() not null,
    ativo                     bool                         null default true,
    primary key (id)
);

alter table senior_services.ss02_pedido
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

create table if not exists senior_services.ss03_item_pedido
(
    id             uuid                       not null,
    id_pedido      uuid                       null,
    id_produto     uuid                       null,
    item           integer                    null,
    qtde           decimal(24, 5) default 1.0 null,
    valor_item     decimal(19, 6) default 0.0 null,
    valor_total    decimal(19, 6) default 0.0 null,
    valor_desconto decimal(19, 6) default 0.0 null,
    dt_cadastro    timestamp      default now(),
    ativo          bool                       null default true,
    primary key (id)
);

alter table senior_services.ss03_item_pedido
    add constraint ss03_item_pedido_id_pedido_fkey foreign key (id_pedido) references senior_services.ss02_pedido;

alter table senior_services.ss03_item_pedido
    add constraint ss03_item_pedido_id_produto_fkey foreign key (id_produto) references senior_services.ss01_produto;

alter table senior_services.ss03_item_pedido
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
--////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
