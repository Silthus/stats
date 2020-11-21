-- apply changes
create table sstats_player_sessions (
  id                            uuid not null,
  player_id                     uuid,
  player_name                   varchar(255),
  joined                        timestamp,
  quit                          timestamp,
  world                         varchar(255),
  world_id                      uuid,
  reason                        varchar(13),
  constraint ck_sstats_player_sessions_reason check ( reason in ('KICK','BAN','QUIT','SHUTDOWN','UNKNOWN','CHANGED_WORLD')),
  constraint pk_sstats_player_sessions primary key (id)
);

create table sstats_player_statistics (
  id                            uuid not null,
  player_id                     uuid,
  player_name                   varchar(255),
  data                          clob,
  statistic_id                  varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_sstats_player_statistics primary key (id)
);

create table sstats_log (
  id                            uuid not null,
  statistic_entry_id            uuid,
  diff                          clob,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_sstats_log primary key (id)
);

create table sstats_statistics (
  id                            varchar(255) not null,
  name                          varchar(255),
  description                   varchar(255),
  source                        varchar(255),
  enabled                       boolean default false not null,
  constraint pk_sstats_statistics primary key (id)
);

create index ix_sstats_player_statistics_statistic_id on sstats_player_statistics (statistic_id);
alter table sstats_player_statistics add constraint fk_sstats_player_statistics_statistic_id foreign key (statistic_id) references sstats_statistics (id) on delete restrict on update restrict;

create index ix_sstats_log_statistic_entry_id on sstats_log (statistic_entry_id);
alter table sstats_log add constraint fk_sstats_log_statistic_entry_id foreign key (statistic_entry_id) references sstats_player_statistics (id) on delete restrict on update restrict;

