package uk.ac.osswatch.simal.service;
/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public interface IBugDatabaseService extends IService {
    
    /**
     * Create a new Bug Database in the repository.
     * 
     * @return
     * @throws SimalRepositoryException
     *           if an error is thrown whilst communicating with the repository
     * @throws DuplicateURIException
     *           if an entity with the given String already exists
     */
    public IDoapBugDatabase create(String uri) throws SimalRepositoryException,
        DuplicateURIException;

    /**
     * Get a Bug Database with a given simal id.
     * 
     * @param id
     * @return
     * @throws SimalRepositoryException
     */
    public IDoapBugDatabase getById(String id) throws SimalRepositoryException;

}
