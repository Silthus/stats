-- apply changes
create table sstats_player_statistics (
  id                            varchar(40) not null,
  player_id                     varchar(40),
  player_name                   varchar(255),
  data                          json,
  statistic_id                  varchar(11),
  version                       bigint not null,
  when_created                  datetime(6) not null,
  when_modified                 datetime(6) not null,
  constraint pk_sstats_player_statistics primary key (id)
);

create table sstats_statistics (
  id                            varchar(11) not null,
  name                          varchar(255),
  description                   varchar(255),
  source                        varchar(255),
  enabled                       tinyint(1) default 0 not null,
  constraint pk_sstats_statistics primary key (id)
);

create table sstats_log (
  id                            varchar(40) not null,
  player_statistic_id           varchar(40),
  diff                          json,
  version                       bigint not null,
  when_created                  datetime(6) not null,
  when_modified                 datetime(6) not null,
  constraint pk_sstats_log primary key (id)
);

create index ix_sstats_player_statistics_statistic_id on sstats_player_statistics (statistic_id);
alter table sstats_player_statistics add constraint fk_sstats_player_statistics_statistic_id foreign key (statistic_id) references sstats_statistics (id) on delete restrict on update restrict;

create index ix_sstats_log_player_statistic_id on sstats_log (player_statistic_id);
alter table sstats_log add constraint fk_sstats_log_player_statistic_id foreign key (player_statistic_id) references sstats_player_statistics (id) on delete restrict on update restrict;

