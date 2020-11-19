-- apply changes
alter table sstats_player_sessions drop constraint if exists ck_sstats_player_sessions_reason;
alter table sstats_player_sessions alter column reason varchar(13);
alter table sstats_player_sessions add constraint ck_sstats_player_sessions_reason check ( reason in ('KICK','BAN','QUIT','SHUTDOWN','UNKNOWN','CHANGED_WORLD'));
alter table sstats_player_sessions add column world varchar(255);
alter table sstats_player_sessions add column world_id varchar(40);

