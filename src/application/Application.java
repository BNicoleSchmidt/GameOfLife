package application;

import model.Biome;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import view.BiomeView;

public class Application {

	public static void main(String[] args) {

		final Biome biome = new Biome(10, 10);
		final Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);

		final BiomeView biomeView = new BiomeView(shell, 10, 10);

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
