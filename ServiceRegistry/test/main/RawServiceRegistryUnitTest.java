package main;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import sampletypes.ArbitraryClass;
import sampletypes.ArbitrarySubClass;
import sampletypes.ArbitrarySubSubClass;
import sampletypes.ArbitraryType;

public class RawServiceRegistryUnitTest {
	
	ServiceRegistry registry;
	
	@Before
	public void setUp() {
		registry = new ServiceRegistry();
	}

	@Test
	public void registryRespondsToRegisterMethod() {
		
		registerType(ArbitraryType.class);
	}


	@Test
	public void testExactTypeMatch() {

		Class<?> clazz = ArbitraryType.class;
		registerType(clazz);
		assertEquals(clazz.toString(), registry.get(clazz));
	
	}
	
	
	@Test
	public void testClassExtensionMatch() {	
		Class<?> type = ArbitraryClass.class;
		Class<?> subType = ArbitrarySubClass.class;
		registerType(type);
		assertEquals(type.toString(), registry.get(subType));
	}
	
	@Test
	public void classExtensionIsPreferredOverInterfaces() {
		
		registerType(Serializable.class);
		
		Class<?> type = ArbitraryClass.class;
		Class<?> subType = ArbitrarySubClass.class;  //also implements serializable
		registerType(type);
		
		assertEquals(type.toString(), registry.get(subType));		
	}
	
	@Test
	public void matchesInterfaceIfSuperClassNotRegistered() {
		
		Class<?> registeredType = Serializable.class;
		Class<?> lookupType = ArbitrarySubClass.class;  //also implements serializable
		registerType(registeredType);
		Object o = registry.get(lookupType);
		assertEquals(registeredType.toString(), o);		
	}	
	
	@Test
	public void testClassExtensionMatchForNonImmediateChildren() {	
		Class<?> type = ArbitraryClass.class;
		Class<?> subType = ArbitrarySubSubClass.class;
		registerType(type);
		assertEquals(type.toString(), registry.get(subType));
	}	
	
	
	private void registerType(Class<?> type) {
		String randomString = type.toString();
		registry.register(type,randomString);
	}	
}
