# --- Created by Slick DDL

# --- !Ups

create table "UrlShortener" (
	"hash" VARCHAR NOT NULL PRIMARY KEY, 
	"url" VARCHAR(255) NOT NULL
);

# --- !Downs

drop table "UrlShortener";

