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
package uk.ac.osswatch.simal.spike.elmo;

import java.io.PrintStream;

import javax.xml.namespace.QName;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConceptsAndBehaviours {
  private static final Logger logger = LoggerFactory.getLogger(ConceptsAndBehaviours.class);
  private static final String NS = "http://example.org/0.1/elmoExample#";
  static SesameManagerFactory factory;
  static SesameManager manager;

  /**
   * @param args
   */
  public static void main(String[] args) {
    // See http://www.openrdf.org/doc/elmo/1.0-beta2/xref/index.html

    try {

      ElmoModule module = new ElmoModule();
      // Add concepts (Interfaces)
      module.recordRole(IEngine.class);
      module.recordRole(ICar.class);

      // Add behaviours (classes)
      module.recordRole(DrivingBehaviour.class);
      module.recordRole(DriverBehaviour.class);
      
      // Add behaviour factories
      
      SailRepository repository = new SailRepository(new MemoryStore());
      repository.initialize();

      factory = new SesameManagerFactory(module, repository);
      manager = factory.createElmoManager();

      conceptsAndBehaviours();
    } catch (RepositoryException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (RDFHandlerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /**
   * Demonstrates the use of concepts and behaviours in Elmo.
   * 
   * We have the following class structure.
   * 
   * <pre>
   * public interface Interface1...
   * 
   * public interface Interface2...
   * 
   * public classA implements Interface1
   * 
   * public classA implements Interface1, Interface2
   * </pre>
   * 
   * @throws RDFHandlerException
   * @throws RepositoryException
   * 
   */
  private static void conceptsAndBehaviours() throws RepositoryException,
      RDFHandlerException {
    QName carID = new QName(NS, "car");
    ICar car = manager.designate(ICar.class, carID);
    QName engineID = new QName(NS, "engine");
    IEngine engine = manager.designate(IEngine.class, engineID);
    car.setEngine(engine);
    
    logger.info("Cars current speed is " + car.getSpeed());
    // the accelerate method is provided by the DrivingBehaviour
    car.accelerate();
    logger.info("Cars current speed is " + car.getSpeed() + " (Should be zero since the engine is not started)");

    logger.info("Starting the engine");
    car.getEngine().setIsStarted(true);    
    car.accelerate();
    logger.info("Cars current speed is " + car.getSpeed());

    // FIXME: the changes are not persisted
    dumpXML(carID);
  }

  private static void dumpXML(QName id) throws RepositoryException,
      RDFHandlerException {

    PrintStream out = System.out;

    out.println();
    out.println("=========================================");
    out.println("Dumping XML for: " + id);
    out.println("=========================================");
    out.println();

    org.openrdf.model.Resource resource = new URIImpl(id.toString());
    RDFXMLPrettyWriter XMLWriter = new RDFXMLPrettyWriter(out);

    factory.getRepository().getConnection().exportStatements(resource,
        (URI) null, (Value) null, true, XMLWriter);

    out.println();
  }
}
