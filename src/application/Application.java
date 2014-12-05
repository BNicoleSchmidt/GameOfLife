package application;

import model.Biome;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import view.BiomeView;

public class Application {

	private static final int HEIGHT = 10;
	private static final int WIDTH = 10;

	public static void main(String[] args) {

		final Biome biome = new Biome(WIDTH, HEIGHT);
		final Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		shell.setText("Conway's Game of Life");

		final BiomeView biomeView = new BiomeView(shell, WIDTH, HEIGHT);

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
