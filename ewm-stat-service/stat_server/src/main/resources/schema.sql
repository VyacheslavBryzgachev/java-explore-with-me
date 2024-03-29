drop table if exists Statistic;

create table if not exists Statistic
(
    id          int generated by default as identity not null,
    app         varchar(100)                         not null,
    uri         varchar(100)                         not null,
    ip          varchar(100)                         not null,
    create_time timestamp without time zone          not null,
    constraint pk_stat PRIMARY KEY (id)
);