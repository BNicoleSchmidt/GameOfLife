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
			public void tick5Clicked() throws InterruptedException {
				for(int i=0; i<5; ++i) {
					biome.tick();
					biomeUpdated();
					Thread.sleep(1000);
				}
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
