connect 'jdbc:derby://localhost:1527/simal;create=true;user=simal;password=simal';

DROP TABLE Project;
CREATE TABLE Project (
		id BIGINT NOT NULL,
		shortName VARCHAR(255) NOT NULL,
		name VARCHAR(255) NOT NULL,
		shortDesc VARCHAR(255) NOT NULL,
		description VARCHAR(255) NOT NULL,
		url VARCHAR(255) NOT NULL,
		created DATE NOT NULL,
		mailingListURL VARCHAR(255) NOT NULL,
		wikiURL VARCHAR(255) NOT NULL,
		downloadURL VARCHAR(255) NOT NULL,
		issueTrackerURL VARCHAR(255) NOT NULL,
		doapURL VARCHAR(255) NOT NULL
	);

DROP TABLE Contributor;
CREATE TABLE Contributor (
		id BIGINT NOT NULL,
		name VARCHAR(255) NOT NULL,
		email VARCHAR(255) NOT NULL,
		url VARCHAR(255) NOT NULL,
		created DATE NOT NULL
	);

DROP TABLE Event;
CREATE TABLE Event (
		id BIGINT NOT NULL,
		name VARCHAR(255) NOT NULL,
		shortDesc VARCHAR(255) NOT NULL,
		created DATE NOT NULL,
		startDate DATE NOT NULL,
		endDate DATE NOT NULL
	);

DROP TABLE Event_Project;
CREATE TABLE Event_Project (
		contributors_id BIGINT,
		projects_id BIGINT
	);

DROP TABLE Project_Language;
CREATE TABLE Project_Language (
		Project_id BIGINT,
		languages_id BIGINT
	);

DROP TABLE Contributor_Project;
CREATE TABLE Contributor_Project (
		contributors_id BIGINT,
		projects_id BIGINT
	);

DROP TABLE Language;
CREATE TABLE Language (
		id BIGINT NOT NULL,
		name VARCHAR(255) NOT NULL,
		created DATE NOT NULL
	);

ALTER TABLE Project ADD CONSTRAINT Project_PK PRIMARY KEY (id);

ALTER TABLE Contributor ADD CONSTRAINT Contributor_PK PRIMARY KEY (id);

ALTER TABLE Event ADD CONSTRAINT Event_PK PRIMARY KEY (id);

ALTER TABLE Language ADD CONSTRAINT Language_PK PRIMARY KEY (id);

CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50), SEQ_COUNT DECIMAL(15));
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0);

disconnect;
exit;

