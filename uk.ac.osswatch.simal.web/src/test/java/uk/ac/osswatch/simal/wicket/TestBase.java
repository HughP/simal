package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.BeforeClass;

public abstract class TestBase {

  protected static final int NUMBER_OF_TEST_PROJECTS = 4;
  protected static WicketTester tester;

  @BeforeClass
  public static void setUpBeforeClass() {
    UserApplication.setIsTest(true);
    tester = new WicketTester();
  }
}