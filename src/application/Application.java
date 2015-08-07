package application;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import model.Biome;
import view.BiomeView;

public class Application {
	private static int HEIGHT = 25;
	private static int WIDTH = 25;
	public static void main(String[] args) {
		final Biome biome = new Biome(WIDTH, HEIGHT);
		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		shell.setText("Conway's Game of Life");

		BiomeView biomeView = new BiomeView(shell, WIDTH, HEIGHT);

		Label label = new Label(shell, SWT.NONE);
		label.setText("Click cells to toggle life! Click Tick to progress!");
		
		new Presenter(biome, biomeView);

		shell.pack();
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	
	}
}
