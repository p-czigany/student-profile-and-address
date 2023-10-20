--liquibase formatted sql
--changeset peterczigany:1

CREATE table IF NOT EXISTS student (

id UUID DEFAULT UUID(),
name VARCHAR2,
email VARCHAR2
);

--rollback DROP TABLE student
