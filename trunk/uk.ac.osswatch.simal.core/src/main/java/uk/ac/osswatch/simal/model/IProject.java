package uk.ac.osswatch.simal.model;


/**
 * An interface for wrapping repository specific representations of
 * a Project. Other classes should not access the repository classes
 * directly, instead they should access the data through a class that 
 * implements this interface.
 *
 * @see uk.ac.osswatch.simal.model.elmo.Project
 */
public interface IProject extends IDoapResource {
}
