package main.rules;

import java.util.Set;

/**
 * Stubbed matcher for testing
 * 
 */
public class DoNothingMatcher extends Matcher {

  /* (non-Javadoc)
   * @see main.rules.Matcher#match(java.lang.Class, java.util.Set)
   */
  @Override
  public Class<?> match(Class<?> lookupType, Set<Class<?>> registryKeys) {
    // TODO Auto-generated method stub
    return this.getClass();
  }

}
