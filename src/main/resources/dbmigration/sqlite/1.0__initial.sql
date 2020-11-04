-- apply changes
create table sstats_player_statistics (
  id                            integer not null,
  player_id                     varchar(255),
  player_name                   varchar(255),
  count                         integer not null,
  statistic_id                  integer,
  constraint pk_sstats_player_statistics primary key (id),
  foreign key (statistic_id) references sstats_statistics (id) on delete restrict on update restrict
);

create table sstats_statistics (
  id                            integer not null,
  identifier                    varchar(255),
  type                          varchar(11),
  name                          varchar(255),
  description                   varchar(255),
  source                        varchar(255),
  enabled                       int default 0 not null,
  constraint ck_sstats_statistics_type check ( type in ('ONLINE_TIME')),
  constraint pk_sstats_statistics primary key (id)
);

