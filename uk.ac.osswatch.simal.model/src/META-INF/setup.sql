-- Copyright 2007 University of Oxford
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

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

