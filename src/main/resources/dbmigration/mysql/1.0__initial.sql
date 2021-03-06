-- apply changes
create table sstats_player_sessions (
  id                            varchar(40) not null,
  player_id                     varchar(40),
  player_name                   varchar(255),
  joined                        datetime(6),
  quit                          datetime(6),
  world                         varchar(255),
  world_id                      varchar(40),
  reason                        varchar(13),
  constraint pk_sstats_player_sessions primary key (id)
);

create table sstats_player_statistics (
  id                            varchar(40) not null,
  player_id                     varchar(40),
  player_name                   varchar(255),
  data                          json,
  statistic_id                  varchar(255),
  version                       bigint not null,
  when_created                  datetime(6) not null,
  when_modified                 datetime(6) not null,
  constraint pk_sstats_player_statistics primary key (id)
);

create table sstats_log (
  id                            varchar(40) not null,
  statistic_entry_id            varchar(40),
  diff                          json,
  version                       bigint not null,
  when_created                  datetime(6) not null,
  when_modified                 datetime(6) not null,
  constraint pk_sstats_log primary key (id)
);

create table sstats_statistics (
  id                            varchar(255) not null,
  name                          varchar(255),
  description                   varchar(255),
  source                        varchar(255),
  enabled                       tinyint(1) default 0 not null,
  constraint pk_sstats_statistics primary key (id)
);

create index ix_sstats_player_statistics_statistic_id on sstats_player_statistics (statistic_id);
alter table sstats_player_statistics add constraint fk_sstats_player_statistics_statistic_id foreign key (statistic_id) references sstats_statistics (id) on delete restrict on update restrict;

create index ix_sstats_log_statistic_entry_id on sstats_log (statistic_entry_id);
alter table sstats_log add constraint fk_sstats_log_statistic_entry_id foreign key (statistic_entry_id) references sstats_player_statistics (id) on delete restrict on update restrict;

