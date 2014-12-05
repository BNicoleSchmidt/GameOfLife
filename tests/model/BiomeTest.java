package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BiomeTest {

	@Mock
	private IBiomeListener biomeListener;

	private Biome biome;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		biome = new Biome(10, 10);
	}

	@Test
	public void testBiomeSizeXIsCorrect() {
		assertEquals(10, biome.getSizeX());
	}

	@Test
	public void testBiomeSizeYIsCorrect() {
		assertEquals(10, biome.getSizeY());
	}

	@Test
	public void testBiomeContentsBeginDead() {
		assertFalse(biome.getStatus(5, 5));
	}

	@Test
	public void testGetStatusReturnsCorrectStatus() {
		biome.setStatus(4, 3, true);
		assertTrue(biome.getStatus(4, 3));
	}

	@Test
	public void testGetStatusReturnsFalseWhenOutOfBounds() {
		assertFalse(biome.getStatus(-1, 0));
		assertFalse(biome.getStatus(0, 11));
	}

	@Test
	public void testCountNeighborsReturnsAccurateCount() {
		biome.setStatus(4, 3, true);
		biome.setStatus(4, 2, true);
		biome.setStatus(4, 1, true);
		assertEquals(biome.countNeighbors(4, 2), 2);
	}

	@Test
	public void testCellWithLessThanTwoNeighborsAliveDiesOnTick() {
		biome.setStatus(4, 3, true);
		biome.tick();
		assertFalse(biome.getStatus(4, 3));
	}

	@Test
	public void testCellWithThreeLivingNeighborsComesAliveOnTick() {
		biome.setStatus(4, 4, true);
		biome.setStatus(4, 2, true);
		biome.setStatus(3, 3, true);
		biome.tick();
		assertTrue(biome.getStatus(4, 3));
	}

	@Test
	public void testLivingCellWithTwoLivingNeighborsStaysAliveOnTick() {
		biome.setStatus(5, 5, true);
		biome.setStatus(5, 6, true);
		biome.setStatus(5, 7, true);
		biome.tick();
		assertTrue(biome.getStatus(5, 6));
	}

	@Test
	public void testCellWithFourLivingNeighborsDiesOnTick() {
		biome.setStatus(8, 8, true);
		biome.setStatus(8, 9, true);
		biome.setStatus(8, 7, true);
		biome.setStatus(9, 8, true);
		biome.setStatus(9, 9, true);
		biome.tick();
		assertFalse(biome.getStatus(9, 8));
	}

	@Test
	public void testToggleCellChangesFromDeadToAlive() {
		biome.toggleCell(5, 5);
		assertTrue(biome.getStatus(5, 5));
	}

	@Test
	public void testToggleCellChangesFromAliveToDead() {
		biome.setStatus(5, 5, true);
		biome.toggleCell(5, 5);
		assertFalse(biome.getStatus(5, 5));
	}

	@Test
	public void testToggleCellNotifiesListener() {
		biome.setListener(biomeListener);

		biome.toggleCell(5, 5);

		verify(biomeListener).biomeUpdated();
	}

	@Test
	public void testTickNotifiesListener() {
		biome.setListener(biomeListener);

		biome.tick();

		verify(biomeListener).biomeUpdated();
	}
}