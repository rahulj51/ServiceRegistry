package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import main.rules.DoNothingMatcher;

import org.junit.Before;
import org.junit.Test;

import sampletypes.AnotherRandomInterface;
import sampletypes.ArbitraryType;

/**
 * @author rjain9
 */
public class ServiceRegistryUnitTest {

  private ServiceRegistry rawRegistry;
  private ServiceRegistry<Date> typedRegistry;
  
  @Before
  public void setup() {
    
    rawRegistry = new ServiceRegistry(new DoNothingMatcher());
    typedRegistry = new ServiceRegistry<Date>(new DoNothingMatcher());
    
  }
  
  @Test
  public void rawRegistryAllowsObjectsOfAllTypes() {
    
    String obj1 = new String("hello world");
    rawRegistry.register(ArbitraryType.class, obj1);
    Calendar obj2 = new GregorianCalendar();
    rawRegistry.register(AnotherRandomInterface.class, obj2);
    
    assertTrue(rawRegistry.getRegistryStore().containsKey(ArbitraryType.class));
    assertEquals(obj1, rawRegistry.getRegistryStore().get(ArbitraryType.class));
    
    assertTrue(rawRegistry.getRegistryStore().containsKey(AnotherRandomInterface.class));
    assertEquals(obj2, rawRegistry.getRegistryStore().get(AnotherRandomInterface.class));
  
  }
  
  
  @Test
  public void typedRegistryAllowsSpecificTypes() {
    
    Date obj1 = new Date();
    typedRegistry.register(ArbitraryType.class, obj1);
    Date obj2 = new Date();
    typedRegistry.register(AnotherRandomInterface.class, obj1);    
    
    //compilation errors if anything else is added so no extra assertions needed
    
    assertTrue(typedRegistry.getRegistryStore().containsKey(ArbitraryType.class));
    assertEquals(obj1, typedRegistry.getRegistryStore().get(ArbitraryType.class));
    
    assertTrue(typedRegistry.getRegistryStore().containsKey(AnotherRandomInterface.class));
    assertEquals(obj2, typedRegistry.getRegistryStore().get(AnotherRandomInterface.class));
    
  }
  
  @Test
  public void registryReturnsMatchedObjectFromMatchers() {
    
    
    Date obj1 = new Date();
    typedRegistry.register(ArbitraryType.class, obj1);
    Date obj2 = new Date();
    typedRegistry.register(AnotherRandomInterface.class, obj1);  
    Date obj3 = new Date();
    typedRegistry.register(DoNothingMatcher.class, obj1);
    
    Date matchedDate = typedRegistry.get(DoNothingMatcher.class);
    assertEquals(obj3, matchedDate);
  }

}
