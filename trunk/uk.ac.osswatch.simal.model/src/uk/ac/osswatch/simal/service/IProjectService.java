package uk.ac.osswatch.simal.service;

import java.util.Collection;

import uk.ac.osswatch.simal.model.Project;

public interface IProjectService {
    public Project findProject(long id);

    public int save(Project project);

    public boolean delete(Project project);

    public boolean update(Project project);
    
    public Collection<Project> findAll();

}
