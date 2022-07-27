CREATE TABLE issue
(
    id   bigint NOT NULL,
    name varchar(255),
    CONSTRAINT pk_issue PRIMARY KEY (id)
)
GO

CREATE TABLE client
(
    id   bigint NOT NULL,
    name varchar(255),
    CONSTRAINT pk_client PRIMARY KEY (id)
)
GO