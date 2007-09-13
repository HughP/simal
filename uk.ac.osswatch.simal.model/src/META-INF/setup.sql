connect 'jdbc:derby://localhost:1527/simal;create=true;user=simal;password=simal';

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

CREATE TABLE Contributor (
		id BIGINT NOT NULL,
		name VARCHAR(255) NOT NULL,
		email VARCHAR(255) NOT NULL,
		url VARCHAR(255) NOT NULL,
		created DATE NOT NULL
	);

CREATE TABLE Event (
		id BIGINT NOT NULL,
		name VARCHAR(255) NOT NULL,
		shortDesc VARCHAR(255) NOT NULL,
		created DATE NOT NULL,
		startDate DATE NOT NULL,
		endDate DATE NOT NULL
	);

CREATE TABLE Event_Project (
		contributors_id BIGINT,
		projects_id BIGINT
	);

CREATE TABLE Project_Language (
		Project_id BIGINT,
		languages_id BIGINT
	);

CREATE TABLE Contributor_Project (
		contributors_id BIGINT,
		projects_id BIGINT
	);

CREATE TABLE Language (
		id BIGINT NOT NULL,
		name VARCHAR(255) NOT NULL,
		created DATE NOT NULL
	);

ALTER TABLE Project ADD CONSTRAINT Project_PK PRIMARY KEY (id);

ALTER TABLE Contributor ADD CONSTRAINT Contributor_PK PRIMARY KEY (id);

ALTER TABLE Event ADD CONSTRAINT Event_PK PRIMARY KEY (id);

ALTER TABLE Language ADD CONSTRAINT Language_PK PRIMARY KEY (id);


disconnect;
exit;

