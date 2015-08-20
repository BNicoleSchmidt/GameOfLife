package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BiomeTest {
	private Biome biome;

	@Mock
	private IBiomeListener biomeListener;
	@Mock
	private ScheduledExecutorService executorService;
	@Captor
	private ArgumentCaptor<Runnable> runnableCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		biome = new Biome(10, 10, executorService);
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
		verify(executorService).schedule(runnableCaptor.capture(), eq(0L), eq(TimeUnit.MILLISECONDS));
		assertTrue(biome.getStatus(4, 3));

		runnableCaptor.getValue().run();

		assertFalse(biome.getStatus(4, 3));
	}

	@Test
	public void testCellWithThreeLivingNeighborsComesAliveOnTick() {
		biome.setStatus(4, 4, true);
		biome.setStatus(4, 2, true);
		biome.setStatus(3, 3, true);

		biome.tick();
		verify(executorService).schedule(runnableCaptor.capture(), eq(0L), eq(TimeUnit.MILLISECONDS));
		assertFalse(biome.getStatus(4, 3));

		runnableCaptor.getValue().run();

		assertTrue(biome.getStatus(4, 3));
	}

	@Test
	public void testLivingCellWithTwoLivingNeighborsStaysAliveOnTick() {
		biome.setStatus(5, 5, true);
		biome.setStatus(5, 6, true);
		biome.setStatus(5, 7, true);

		biome.tick();
		verify(executorService).schedule(runnableCaptor.capture(), eq(0L), eq(TimeUnit.MILLISECONDS));
		assertTrue(biome.getStatus(5, 6));

		runnableCaptor.getValue().run();

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
		verify(executorService).schedule(runnableCaptor.capture(), eq(0L), eq(TimeUnit.MILLISECONDS));
		assertTrue(biome.getStatus(9, 8));

		runnableCaptor.getValue().run();

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
		verify(executorService).schedule(runnableCaptor.capture(), eq(0L), eq(TimeUnit.MILLISECONDS));
		verify(biomeListener, never()).biomeUpdated();

		runnableCaptor.getValue().run();

		verify(biomeListener).biomeUpdated();
	}

	@Test
	public void testTick1CallsExecutorService1Time() throws Exception {
		biome.setListener(biomeListener);

		biome.tick(1);
		verify(executorService, times(1)).schedule(runnableCaptor.capture(), eq(0L), eq(TimeUnit.MILLISECONDS));

		runnableCaptor.getValue().run();

		verify(biomeListener).biomeUpdated();
	}

	@Test
	public void testTick2CallsExecutorService2Times() throws Exception {
		biome.setListener(biomeListener);

		biome.tick(2);

		verify(executorService, times(1)).schedule(runnableCaptor.capture(), eq(0L), eq(TimeUnit.MILLISECONDS));
		verify(executorService, never()).schedule(runnableCaptor.capture(), eq(500L), eq(TimeUnit.MILLISECONDS));

		runnableCaptor.getValue().run();

		verify(biomeListener, times(1)).biomeUpdated();
		verify(executorService).schedule(runnableCaptor.capture(), eq(500L), eq(TimeUnit.MILLISECONDS));

		runnableCaptor.getValue().run();

		verify(biomeListener, times(2)).biomeUpdated();
		verify(executorService, times(1)).schedule(runnableCaptor.capture(), eq(500L), eq(TimeUnit.MILLISECONDS));
	}

	@Test
	public void testTick3CallsExecutorService3Times() throws Exception {
		biome.setListener(biomeListener);

		biome.tick(3);

		verify(executorService, times(1)).schedule(runnableCaptor.capture(), eq(0L), eq(TimeUnit.MILLISECONDS));
		verify(executorService, never()).schedule(runnableCaptor.capture(), eq(500L), eq(TimeUnit.MILLISECONDS));

		runnableCaptor.getValue().run();

		verify(biomeListener, times(1)).biomeUpdated();
		verify(executorService).schedule(runnableCaptor.capture(), eq(500L), eq(TimeUnit.MILLISECONDS));

		runnableCaptor.getAllValues().get(0).run();

		verify(biomeListener, times(2)).biomeUpdated();
		verify(executorService, times(2)).schedule(runnableCaptor.capture(), eq(500L), eq(TimeUnit.MILLISECONDS));

		runnableCaptor.getAllValues().get(0).run();

		verify(biomeListener, times(3)).biomeUpdated();
		verify(executorService, times(2)).schedule(runnableCaptor.capture(), eq(500L), eq(TimeUnit.MILLISECONDS));
	}

	@Test
	public void testWhenTickForeverIsSetBiomeContinuesTicking() {
		biome.setListener(biomeListener);
		biome.setTickForever(true);
		verify(executorService, times(1)).schedule(runnableCaptor.capture(), eq(0L), eq(TimeUnit.MILLISECONDS));
		runnableCaptor.getValue().run();

		verify(biomeListener, times(1)).biomeUpdated();
		verify(executorService).schedule(runnableCaptor.capture(), eq(500L), eq(TimeUnit.MILLISECONDS));

		runnableCaptor.getAllValues().get(0).run();

		verify(biomeListener, times(2)).biomeUpdated();
		verify(executorService, times(2)).schedule(runnableCaptor.capture(), eq(500L), eq(TimeUnit.MILLISECONDS));

		runnableCaptor.getAllValues().get(0).run();

		verify(biomeListener, times(3)).biomeUpdated();
	}
}