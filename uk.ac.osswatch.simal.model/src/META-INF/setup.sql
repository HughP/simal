CREATE TABLE Project (
		id BIGINT NOT NULL generated always as identity,
		shortName VARCHAR(255) NOT NULL,
		name VARCHAR(255) NOT NULL,
		shortDesc VARCHAR(255) NOT NULL,
		description VARCHAR(255) NOT NULL,
		url VARCHAR(255) NOT NULL,
		created DATE NOT NULL,
		mailingListURL VARCHAR(255) NOT NULL,
		wikiURL VARCHAR(255) NOT NULL,
		downloadURL VARCHAR(255) NOT NULL,
		issueTrackerURL VARCHAR(255) NOT NULL
	);

ALTER TABLE Project ADD CONSTRAINT Project_PK PRIMARY KEY (id);

