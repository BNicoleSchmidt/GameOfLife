package application;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Biome;
import view.BiomeView;

public class Game extends Application {
	private static int height = 30;
	private static int width = 30;

	@Override
	public void start(Stage primaryStage) {
		BiomeView view = new BiomeView(primaryStage, width, height);
		Biome biome = new Biome(width, height);
		new Presenter(biome, view);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
