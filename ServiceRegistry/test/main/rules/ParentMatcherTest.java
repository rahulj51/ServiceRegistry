package main.rules;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import sampletypes.AnotherArbitraryClass;
import sampletypes.AnotherRandomSubInterface;
import sampletypes.ArbitraryClass;

public class ParentMatcherTest {

  private Set<Class<?>> registryKeys;
  private Matcher parentMatcher;
  
  @Before
  public void setup() {
    
    Matcher nextMatcher = new DoNothingMatcher();
    parentMatcher = new ParentMatcher();
    parentMatcher.setNext(nextMatcher);
    
    registryKeys = new HashSet<Class<?>>();
    registryKeys.add(ArbitraryClass.class); 
    registryKeys.add(AnotherRandomSubInterface.class);
    registryKeys.add(Object.class);
  }

  @Test
  public void testClosestParentIsPreferred() {
    
    //TODO check if rule-1 (class-extension preferred) should override this rule
    Class<?> matchedClass = parentMatcher.match(AnotherArbitraryClass.class, registryKeys);
    assertEquals(AnotherRandomSubInterface.class, matchedClass);    
  }
  
  @Test 
  public void testObjectClassReturnedIfNothingElseMatched() {
    
    Class<?> matchedClass = parentMatcher.match(Thread.class, registryKeys);
    assertEquals(Object.class, matchedClass);
  }
  
}
