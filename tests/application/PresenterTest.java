package application;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import model.Biome;
import model.IBiomeListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import view.BiomeView;
import view.IViewListener;

public class PresenterTest {
	@Mock
	private Biome biome;
	@Mock
	private BiomeView biomeView;
	@Captor
	private ArgumentCaptor<IViewListener> viewListenerCaptor;
	@Captor
	private ArgumentCaptor<IBiomeListener> biomeListenerCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testListenerSetOnView() throws Exception {
		new Presenter(biome, biomeView);
		verify(biomeView).setListener(isA(IViewListener.class));
	}

	@Test
	public void testWhenViewClickedModelIsNotified() throws Exception {
		new Presenter(biome, biomeView);
		verify(biomeView).setListener(viewListenerCaptor.capture());

		IViewListener viewListener = viewListenerCaptor.getValue();
		viewListener.itemClicked(3, 6);

		verify(biome).toggleCell(3, 6);
	}

	@Test
	public void testListenerSetOnBiome() {
		new Presenter(biome, biomeView);
		verify(biome).setListener(isA(IBiomeListener.class));
	}

	@Test
	public void testTableIsUpdatedOnCreation() {
		boolean[][] biomeArray = new boolean[2][4];
		when(biome.getBiome()).thenReturn(biomeArray);

		new Presenter(biome, biomeView);
		verify(biomeView).update(biomeArray);
	}

	@Test
	public void testWhenBiomeUpdatedViewIsUpdated() {
		new Presenter(biome, biomeView);
		verify(biome).setListener(biomeListenerCaptor.capture());
		IBiomeListener biomeListener = biomeListenerCaptor.getValue();

		boolean[][] biomeArray = new boolean[5][5];
		when(biome.getBiome()).thenReturn(biomeArray);

		biomeListener.biomeUpdated();

		verify(biomeView).update(biomeArray);
	}

	@Test
	public void testWhenTickClickedBiomeTicks() {
		new Presenter(biome, biomeView);
		verify(biomeView).setListener(viewListenerCaptor.capture());
		IViewListener viewListener = viewListenerCaptor.getValue();
		viewListener.tickClicked();
		verify(biome).tick();
	}

	@Test
	public void testBiomeUpdatedUsesCorrectArray() {
		new Presenter(biome, biomeView);
		boolean[][] value = new boolean[3][3];
		when(biome.getBiome()).thenReturn(value);
		verify(biome).setListener(biomeListenerCaptor.capture());
		IBiomeListener biomeListener = biomeListenerCaptor.getValue();

		biomeListener.biomeUpdated();

		verify(biomeView).update(value);
	}

	@Test
	public void testWhenTick5ClickedBiomeTicks5Times() throws InterruptedException {
		new Presenter(biome, biomeView);
		verify(biomeView).setListener(viewListenerCaptor.capture());
		IViewListener viewListener = viewListenerCaptor.getValue();

		viewListener.tick5Clicked();

		verify(biome).tick(5);
	}

	@Test
	public void testWhenTickForeverSelectedTickForeverIsSet() {
		new Presenter(biome, biomeView);
		verify(biomeView).setListener(viewListenerCaptor.capture());
		IViewListener viewListener = viewListenerCaptor.getValue();

		viewListener.tickForever();

		verify(biome).setTickForever(true);
	}

	@Test
	public void testWhenTickForeverUnselectedTickForeverIsUnset() {
		new Presenter(biome, biomeView);
		verify(biomeView).setListener(viewListenerCaptor.capture());
		IViewListener viewListener = viewListenerCaptor.getValue();

		viewListener.stopTicking();
		verify(biome).setTickForever(false);
	}
}
