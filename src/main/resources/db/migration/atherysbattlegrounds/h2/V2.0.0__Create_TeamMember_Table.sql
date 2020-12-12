create schema if not exists atherysbattlegrounds;

create table atherysbattlegrounds.TeamMember (
    id binary not null,
    cachedName varchar(255),
    milestone integer not null,
    milestonesAwarded integer not null,
    team varchar(255),
    primary key (id)
);