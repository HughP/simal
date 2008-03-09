package uk.ac.osswatch.simal.spike.elmo;

import org.openrdf.elmo.annotations.rdf;

@rdf("http://example.org/0.1/elmoExample#Engine")
public interface IEngine extends IDriverBehaviour {
  
  
  /**
   * Get current status of the engine.
   * 
   * @return
   */
  @rdf("http://example.org/0.1/elmoExample#Started")
  public boolean getIsStarted();
  /**
   * Start and stop the engine.
   * 
   * @return true if started
   */
  public void setIsStarted(boolean start);
}
