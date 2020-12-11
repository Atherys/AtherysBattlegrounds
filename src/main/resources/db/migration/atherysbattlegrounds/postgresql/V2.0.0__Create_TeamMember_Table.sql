create schema if not exists atherysbattlegrounds;

create table atherysbattlegrounds.TeamMember (
    id uuid not null,
    cachedName varchar(255),
    milestone int4 not null,
    milestonesAwarded int4 not null,
    team varchar(255),
    primary key (id)
);