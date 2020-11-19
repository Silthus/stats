-- apply changes
create table sstats_player_sessions (
  id                            varchar(40) not null,
  player_id                     varchar(40),
  player_name                   varchar(255),
  joined                        timestamp,
  quit                          timestamp,
  world                         varchar(255),
  world_id                      varchar(40),
  reason                        varchar(13),
  constraint ck_sstats_player_sessions_reason check ( reason in ('KICK','BAN','QUIT','SHUTDOWN','UNKNOWN','CHANGED_WORLD')),
  constraint pk_sstats_player_sessions primary key (id)
);

create table sstats_player_statistics (
  id                            varchar(40) not null,
  player_id                     varchar(40),
  player_name                   varchar(255),
  data                          clob,
  statistic_id                  varchar(255),
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_sstats_player_statistics primary key (id),
  foreign key (statistic_id) references sstats_statistics (id) on delete restrict on update restrict
);

create table sstats_statistics (
  id                            varchar(255) not null,
  name                          varchar(255),
  description                   varchar(255),
  source                        varchar(255),
  enabled                       int default 0 not null,
  constraint pk_sstats_statistics primary key (id)
);

create table sstats_log (
  id                            varchar(40) not null,
  player_statistic_id           varchar(40),
  diff                          clob,
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_sstats_log primary key (id),
  foreign key (player_statistic_id) references sstats_player_statistics (id) on delete restrict on update restrict
);

