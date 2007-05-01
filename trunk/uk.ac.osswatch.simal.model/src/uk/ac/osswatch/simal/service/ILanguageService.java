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
package uk.ac.osswatch.simal.service;

import java.util.Collection;

import uk.ac.osswatch.simal.model.Language;

public interface ILanguageService {
    public Language findLanguage(long id);
    public int save(Language language);
    public boolean delete(Language language);
    public boolean update(Language language);
    /**
     * Find all languages known in the system
     * @return
     */
    public Collection<Language> findAll();

}
