package uk.ac.osswatch.simal.openSocial;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;


public class TestServerThread extends Thread {
  Server server = new Server();

  public void run() {
    try {
      SocketConnector connector = new SocketConnector();
      // Set some timeout options to make debugging easier.
      connector.setMaxIdleTime(1000 * 60 * 60);
      connector.setSoLingerTime(-1);
      connector.setPort(8080);
      server.setConnectors(new Connector[] { connector });

      WebAppContext bb = new WebAppContext();
      bb.setServer(server);
      bb.setContextPath("/");
      bb.setWar("src/main/webapp");

      // START JMX SERVER
      // MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
      // MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
      // server.getContainer().addEventListener(mBeanContainer);
      // mBeanContainer.start();

      server.addHandler(bb);

      try {
        System.out
            .println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
        server.start();
        while (System.in.available() == 0) {
          Thread.sleep(5000);
        }
        server.stop();
        server.join();
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(100);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void destroy() {
    try {
      server.stop();
      server.destroy();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
