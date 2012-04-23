package main.rules;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import sampletypes.AnotherRandomSubInterface;
import sampletypes.AnotherRandomSubSubInterface;
import sampletypes.ArbitraryClass;
import sampletypes.ArbitrarySubClass;
import sampletypes.ArbitrarySubType;
import sampletypes.ArbitraryType;

public class ExactMatcherTest {

  private Set<Class<?>> registryKeys;
  private Matcher exactMatcher;
  
  @Before
  public void setup() {
    
    Matcher nextMatcher = new DoNothingMatcher();
    exactMatcher = new ExactMatcher();
    exactMatcher.setNext(nextMatcher);
    
    registryKeys = new HashSet<Class<?>>();
    registryKeys.add(ArbitraryClass.class);
    registryKeys.add(Serializable.class);
    registryKeys.add(ArbitraryType.class);
    registryKeys.add(EventListener.class);
  }
  
  @Test
  public void testExactClassNameMatch() {
    
    Class<?> matchedClass = exactMatcher.match(ArbitraryClass.class, registryKeys);
    assertEquals(ArbitraryClass.class, matchedClass);
    
  }

  @Test
  public void testExactInterfaceNameMatch() {
    
    Class<?> matchedClass = exactMatcher.match(Serializable.class, registryKeys);
    assertEquals(Serializable.class, matchedClass);
    
  }  

  @Test
  public void testDirectInheritanceMatchForAClass() {
    
    Class<?> matchedClass = exactMatcher.match(ArbitrarySubClass.class, registryKeys);
    assertEquals(ArbitraryClass.class, matchedClass);
    
  }    
  
  @Test
  public void testDirectInheritanceMatchForAnInterface() {
    
    Class<?> matchedClass = exactMatcher.match(ArbitrarySubType.class, registryKeys);
    assertEquals(ArbitraryType.class, matchedClass);
    
  }    
  
  @Test
  public void testDirectInheritanceMatchForMultipleInterfaces() {
    
    Class<?> matchedClass = exactMatcher.match(AnotherRandomSubSubInterface.class, registryKeys);
    assertEquals(EventListener.class, matchedClass);
  }   
  
  @Test
  public void testThatNearestInterfaceIsPreferred() {
    
    registryKeys.remove(EventListener.class);
    registryKeys.add(AnotherRandomSubInterface.class);
    Class<?> matchedClass = exactMatcher.match(AnotherRandomSubSubInterface.class, registryKeys);
    assertEquals(AnotherRandomSubInterface.class, matchedClass);
  }   
  
  @Test
  public void testNextMatcherIsUsedIfNoExactMatchFound() {
    
    Class<?> matchedClass = exactMatcher.match(Thread.class, registryKeys); //Thread.class does not exist in the registry;
    assertEquals(DoNothingMatcher.class, matchedClass);
  }
}
