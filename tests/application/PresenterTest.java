package application;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
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
		viewListener.itemClicked(3, 3);

		verify(biome).toggleCell(3, 3);
	}

	@Test
	public void testListenerSetOnBiome() {
		new Presenter(biome, biomeView);
		verify(biome).setListener(isA(IBiomeListener.class));
	}

	@Test
	public void testWhenBiomeUpdatedViewIsUpdated() {
		new Presenter(biome, biomeView);
		verify(biome).setListener(biomeListenerCaptor.capture());
		IBiomeListener biomeListener = biomeListenerCaptor.getValue();

		biomeListener.biomeUpdated();
	}
}
