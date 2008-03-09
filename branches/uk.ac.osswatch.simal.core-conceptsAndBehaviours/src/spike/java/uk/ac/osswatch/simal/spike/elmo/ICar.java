package uk.ac.osswatch.simal.spike.elmo;

import org.openrdf.elmo.annotations.rdf;

@rdf("http://example.org/0.1/elmoExample#Vehicle")
public interface ICar extends IDrivingBehaviour {
  
  /**
   * Get the current speed of the car.
   */
  @rdf("http://example.org/0.1/elmoExample#speed")
  public int getSpeed();
  
  /**
   * Set the current speed of the car.
   */
  public void setSpeed(int speed);

}
