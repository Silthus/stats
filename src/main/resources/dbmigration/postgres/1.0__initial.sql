-- apply changes
create table sstats_player_sessions (
  id                            uuid not null,
  player_id                     uuid,
  player_name                   varchar(255),
  joined                        timestamptz,
  quit                          timestamptz,
  reason                        varchar(8),
  constraint ck_sstats_player_sessions_reason check ( reason in ('KICK','BAN','QUIT','SHUTDOWN','UNKNOWN')),
  constraint pk_sstats_player_sessions primary key (id)
);

create table sstats_player_statistics (
  id                            uuid not null,
  player_id                     uuid,
  player_name                   varchar(255),
  data                          json,
  statistic_id                  varchar(11),
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_sstats_player_statistics primary key (id)
);

create table sstats_statistics (
  id                            varchar(11) not null,
  name                          varchar(255),
  description                   varchar(255),
  source                        varchar(255),
  enabled                       boolean default false not null,
  constraint ck_sstats_statistics_id check ( id in ('ONLINE_TIME')),
  constraint pk_sstats_statistics primary key (id)
);

create table sstats_log (
  id                            uuid not null,
  player_statistic_id           uuid,
  diff                          json,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_sstats_log primary key (id)
);

create index ix_sstats_player_statistics_statistic_id on sstats_player_statistics (statistic_id);
alter table sstats_player_statistics add constraint fk_sstats_player_statistics_statistic_id foreign key (statistic_id) references sstats_statistics (id) on delete restrict on update restrict;

create index ix_sstats_log_player_statistic_id on sstats_log (player_statistic_id);
alter table sstats_log add constraint fk_sstats_log_player_statistic_id foreign key (player_statistic_id) references sstats_player_statistics (id) on delete restrict on update restrict;

