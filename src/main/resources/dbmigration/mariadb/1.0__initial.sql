-- apply changes
create table sstats_player_statistics (
  id                            bigint auto_increment not null,
  player_id                     varchar(255),
  player_name                   varchar(255),
  count                         bigint not null,
  statistic_id                  bigint,
  constraint pk_sstats_player_statistics primary key (id)
);

create table sstats_statistics (
  id                            bigint auto_increment not null,
  identifier                    varchar(255),
  type                          varchar(11),
  name                          varchar(255),
  description                   varchar(255),
  source                        varchar(255),
  enabled                       tinyint(1) default 0 not null,
  constraint pk_sstats_statistics primary key (id)
);

create index ix_sstats_player_statistics_statistic_id on sstats_player_statistics (statistic_id);
alter table sstats_player_statistics add constraint fk_sstats_player_statistics_statistic_id foreign key (statistic_id) references sstats_statistics (id) on delete restrict on update restrict;

