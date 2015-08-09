package application;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Biome;
import view.BiomeView;

public class Game extends Application {
	private static int height = 20;
	private static int width = 20;

	@Override
	public void start(Stage primaryStage) {
		BiomeView view = new BiomeView(primaryStage, width, height);
		Biome biome = new Biome(width, height);
		new Presenter(biome, view);
	}

	public static void main(String[] args) {
		launch(args);
	}
	// public static void main(String[] args) {
	// final Biome biome = new Biome(width, height);
	// Display display = new Display();
	// Shell shell = new Shell(display);
	// GridLayout layout = new GridLayout(3, false);
	// shell.setLayout(layout);
	// shell.setText("Conway's Game of Life");
	//
	// BiomeView biomeView = new BiomeView(shell, width, height);
	//
	// Label label = new Label(shell, SWT.NONE);
	// label.setText("Click cells to toggle life! Click Tick to progress!");
	//
	// new Presenter(biome, biomeView);
	//
	// shell.pack();
	// shell.open();
	//
	// while (!shell.isDisposed()) {
	// if (!display.readAndDispatch())
	// display.sleep();
	// }
	// display.dispose();
	//
	// }
}
