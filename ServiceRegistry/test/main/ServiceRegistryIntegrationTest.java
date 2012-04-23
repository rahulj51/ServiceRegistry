package main;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.EventListener;

import org.junit.Before;
import org.junit.Test;

import sampletypes.AnotherArbitraryClass;
import sampletypes.AnotherRandomSubInterface;
import sampletypes.AnotherRandomSubSubInterface;
import sampletypes.ArbitraryClass;
import sampletypes.ArbitrarySubClass;
import sampletypes.ArbitrarySubSubClass;
import sampletypes.ArbitraryType;

public class ServiceRegistryIntegrationTest {
	
	ServiceRegistry<String> registry;
	
	@Before
	public void setUp() {
		registry = new ServiceRegistry<String>();
	}

	@Test
	public void testExactTypeMatchForInterface() {

	  //register some type and then look it up
		Class<?> clazz = ArbitraryType.class;
		registerType(clazz);
		assertEquals(clazz.toString(), registry.get(clazz));
	
	}
	
  @Test
  public void testExactTypeMatchForClass() {

    //register some class and then look it up
    Class<?> clazz = ArbitraryClass.class;
    registerType(clazz);
    assertEquals(clazz.toString(), registry.get(clazz));
  
  }

	@Test
	public void testClassExtensionMatch() {
	 
	  //register a class and then lookup its subclass
	 
		Class<?> type = ArbitraryClass.class;
		Class<?> subType = ArbitrarySubClass.class;
		registerType(type);
		assertEquals(type.toString(), registry.get(subType));
	}
	
	@Test
	public void classExtensionIsPreferredOverInterfaces() {
		
	  //register a class and an interface
	  //lookup the subclass (that extends the super class and implements the interface). 
	  //The registered superclass should be matched.
	  
		registerType(Serializable.class);
		
		Class<?> type = ArbitraryClass.class;
		Class<?> subType = ArbitrarySubClass.class;  //also implements serializable
		registerType(type);
		
		assertEquals(type.toString(), registry.get(subType));		
	}
	
	
	@Test
	public void matchesInterfaceIfSuperClassNotRegistered() {
		
    //register an interface. do not register the super class.
    //lookup the subclass (that extends a super class and implements the interface). 
    //The registered superclass should be matched.
	  
		Class<?> registeredType = Serializable.class;
    registerType(registeredType);
    
		Class<?> lookupType = ArbitrarySubClass.class;  //also implements serializable
		Object o = registry.get(lookupType);
		assertEquals(registeredType.toString(), o);		
	}
	

	@Test
	public void nearestInterfaceIsPreferred() {
	  //register 2 interfaces implemented by a class
	  //lookup the class. The nearer interface should be returned
	  
	  registerType(EventListener.class);
	  registerType(AnotherRandomSubInterface.class);
	  Class<?> lookupType = AnotherRandomSubSubInterface.class;
    Object o = registry.get(lookupType);
    assertEquals(AnotherRandomSubInterface.class.toString(), o); 
	}
	

  @Test
  public void testClassExtensionMatchForNonImmediateChildren() {  
    
    //Class C extends B extends A
    //register A
    //lookup C -> A should be matched
    
    Class<?> type = ArbitraryClass.class;
    Class<?> subType = ArbitrarySubSubClass.class;
    registerType(type);
    assertEquals(type.toString(), registry.get(subType));
  }
	
	
	@Test
  public void moreSpecificInterfaceIsPreferred() {
	  
	  //search through the hierarchy and find out the nearest match
	  
    Class<?> type = AnotherRandomSubInterface.class;
    Class<?> anotherType = ArbitrarySubClass.class;
    registerType(type);
    registerType(anotherType);
    assertEquals(anotherType.toString(), registry.get(AnotherArbitraryClass.class));    
  }	
	
	
	
	private void registerType(Class<?> type) {
		String randomString = type.toString();
		registry.register(type,randomString);
	}	
}
