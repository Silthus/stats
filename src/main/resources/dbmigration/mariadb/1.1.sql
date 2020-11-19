-- apply changes
alter table sstats_player_sessions modify reason varchar(13);
alter table sstats_player_sessions add column world varchar(255);
alter table sstats_player_sessions add column world_id varchar(40);

