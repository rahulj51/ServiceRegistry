package main;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import sampletypes.ArbitraryType;

public class TypedServiceRegistryUnitTest {

	@Test
	public void typeSpecificServiceRegistryCanBeCrated() {

		try {
			//@SuppressWarnings("unused")
			//ServiceRegistry<ArbitraryType> registry = new ServiceRegistry<ArbitraryType>(); 
		} catch(Exception e) {
			fail("An exception has occured " + e.getMessage());
		}
	}

}
