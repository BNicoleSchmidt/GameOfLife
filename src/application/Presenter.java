package application;

import model.Biome;
import model.IBiomeListener;
import view.BiomeView;
import view.IViewListener;

public class Presenter implements IBiomeListener {
	Biome biome;
	BiomeView biomeView;

	public Presenter(final Biome biome, BiomeView biomeView) {
		this.biome = biome;
		this.biomeView = biomeView;
		biomeView.setListener(new IViewListener() {
			@Override
			public void itemClicked(int x, int y) {
				biome.toggleCell(x, y);
			}

			@Override
			public void tickClicked() {
				biome.tick();
			}

			@Override
			public void tick5Clicked() {
				biome.tick(5);
			}

			@Override
			public void tickForever() {
				biome.setTickForever(true);
			}

			@Override
			public void stopTicking() {
				biome.setTickForever(false);
			}

			@Override
			public void randomize() {
				biome.randomize();
			}

			@Override
			public void clear() {
				biome.setTickForever(false);
				biomeView.setTickForever(false);
				biome.clear();
			}
		});

		biome.setListener(this);
		biomeUpdated();
	}

	@Override
	public void biomeUpdated() {
		biomeView.update(biome.getBiome());
	}
}
