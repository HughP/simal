connect 'jdbc:derby://localhost:1527/simal;create=true;user=simal;password=simal';

ALTER TABLE Project DROP CONSTRAINT Project_PK;

ALTER TABLE Event DROP CONSTRAINT Event_PK;

ALTER TABLE Contributor DROP CONSTRAINT Contributor_PK;

ALTER TABLE Language DROP CONSTRAINT Language_PK;

DROP TABLE Contributor_Project;

DROP TABLE Project;

DROP TABLE Event;

DROP TABLE Contributor;

DROP TABLE Event_Project;

DROP TABLE Project_Language;

DROP TABLE Language;

CREATE TABLE Contributor_Project (
		contributors_id BIGINT,
		projects_id BIGINT
	);

CREATE TABLE Project (
		id BIGINT NOT NULL,
		shortName VARCHAR(255) NOT NULL,
		name VARCHAR(255) NOT NULL,
		shortDesc VARCHAR(255) NOT NULL,
		description VARCHAR(255) NOT NULL,
		url VARCHAR(255),
		created DATE NOT NULL,
		mailingListURL VARCHAR(255),
		wikiURL VARCHAR(255),
		downloadURL VARCHAR(255),
		issueTrackerURL VARCHAR(255),
		doapURL VARCHAR(255)
	);

CREATE TABLE Event (
		id BIGINT NOT NULL,
		name VARCHAR(255) NOT NULL,
		shortDesc VARCHAR(255) NOT NULL,
		created DATE NOT NULL,
		startDate DATE NOT NULL,
		endDate DATE
	);

CREATE TABLE Contributor (
		id BIGINT NOT NULL,
		name VARCHAR(255) NOT NULL,
		email VARCHAR(255),
		url VARCHAR(255),
		created DATE NOT NULL
	);

CREATE TABLE Event_Project (
		events_id BIGINT,
		projects_id BIGINT
	);

CREATE TABLE Project_Language (
		Project_id BIGINT,
		languages_id BIGINT
	);

CREATE TABLE Language (
		id BIGINT NOT NULL,
		name VARCHAR(255) NOT NULL,
		created DATE NOT NULL
	);

ALTER TABLE Project ADD CONSTRAINT Project_PK PRIMARY KEY (id);

ALTER TABLE Event ADD CONSTRAINT Event_PK PRIMARY KEY (id);

ALTER TABLE Contributor ADD CONSTRAINT Contributor_PK PRIMARY KEY (id);

ALTER TABLE Language ADD CONSTRAINT Language_PK PRIMARY KEY (id);



CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50), SEQ_COUNT DECIMAL(15));
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0);

disconnect;
exit;

