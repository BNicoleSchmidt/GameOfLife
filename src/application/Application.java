package application;

import model.Biome;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import view.BiomeView;

public class Application {

	public static void main(String[] args) {

		final Biome biome = new Biome(10, 10);
		final Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);

		final BiomeView biomeView = new BiomeView(display);
		final Table table = biomeView.buildTable(10, 10, shell);
		biomeView.update(biome, table, display);

		Presenter presenter = new Presenter(biome, biomeView);

		Button tickButton = new Button(shell, SWT.PUSH);
		tickButton.setText("Tick");
		tickButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true,
				1, 1));
		tickButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				biome.tick();
				biomeView.update(biome, table, display);
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
