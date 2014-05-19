package nl.pvanassen.steam.store;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MarketPageHandleTest {

	@Test
	public void testEmptyHandle() throws IOException {
		MarketPageHandle handle = new MarketPageHandle();
		handle.handle(getClass().getResourceAsStream("/empty-marketpage.html"));
		assertNotNull("Expected object", handle.getOutstandings());
		assertEquals("Expected 0", 0, handle.getOutstandings().getAmount());
		assertEquals("Expected 0", 0, handle.getOutstandings().getItems());
		assertNotNull(handle.getItems());
        assertNotNull(handle.getOutstandings().getAppIds());
	}


	@Test
	public void testLoadedHandle() throws IOException {
		MarketPageHandle handle = new MarketPageHandle();
		handle.handle(getClass().getResourceAsStream("/loaded-marketpage.html"));
		assertNotNull("Expected object", handle.getOutstandings());
		assertEquals("Expected 6380", 6380, handle.getOutstandings().getAmount());
		assertEquals("Expected 87", 87, handle.getOutstandings().getItems());
        assertNotNull(handle.getItems());
        assertEquals(87, handle.getItems().size());
        assertNotNull(handle.getOutstandings().getAppIds());
	}

}