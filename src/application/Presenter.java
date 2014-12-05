package application;

import model.Biome;
import model.IBiomeListener;
import view.BiomeView;
import view.IViewListener;

public class Presenter {
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
		});

		biome.setListener(new IBiomeListener() {

			@Override
			public void biomeUpdated() {
				// TODO Auto-generated method stub

			}

		});
	}
}
