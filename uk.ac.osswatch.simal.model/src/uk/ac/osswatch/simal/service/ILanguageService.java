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
