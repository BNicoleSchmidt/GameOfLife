package view;

import java.util.Arrays;

import model.Biome;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class BiomeView {

	private Table table;
	private Color red;
	private Color green;
	private IViewListener listener;

	public BiomeView(Shell shell, final int sizeX, final int sizeY) {
		this.red = shell.getDisplay().getSystemColor(SWT.COLOR_RED);
		this.green = shell.getDisplay().getSystemColor(SWT.COLOR_GREEN);

		createTable(shell, sizeX, sizeY);
		createTickButton(shell);
	}

	private void createTable(Shell shell, final int sizeX, final int sizeY) {
		table = new Table(shell, SWT.BORDER | SWT.NO_SCROLL
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setLinesVisible(true);

		for (int x = 0; x < sizeX; x++) {
			new TableColumn(table, SWT.NONE);
		}
		for (int y = 0; y < sizeY; y++) {
			new TableItem(table, SWT.NONE);
		}
		for (int x = 0; x < sizeX; x++) {
			table.getColumn(x).pack();
		}
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		table.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TableItem clickedItem = table.getItem(pt);
				int x = -1;
				if (clickedItem != null) {
					for (int i = 0; i < sizeX; i++) {
						Rectangle rect = clickedItem.getBounds(i);
						if (rect.contains(pt)) {
							x = i;
						}
					}
					int y = Arrays.asList(table.getItems())
							.indexOf(clickedItem);
					if (x != -1) {
						listener.itemClicked(x, y);
					}
				}
			}
		});
	}

	private void createTickButton(Shell shell) {
		Button tickButton = new Button(shell, SWT.PUSH);
		tickButton.setText("Tick");
		tickButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true,
				1, 1));
		tickButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listener.tickClicked();
			}
		});
	}

	public void setCellStatus(int x, int y, boolean alive) {
		TableItem item = table.getItem(y);
		if (alive) {
			item.setText(x, "1");
			item.setBackground(x, green);
		} else {
			item.setText(x, "0");
			item.setBackground(x, red);
		}
	}

	public void update(Biome biome) {
		for (int x = 0; x < biome.getSizeX(); x++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(Integer.toString(x));
		}
		for (int y = 0; y < biome.getSizeY(); y++) {
			for (int x = 0; x < biome.getSizeX(); x++) {
				setCellStatus(x, y, biome.getStatus(x, y));
			}
		}
		for (int x = 0; x < biome.getSizeX(); x++) {
			table.getColumn(x).pack();
		}
	}

	public void setListener(IViewListener listener) {
		this.listener = listener;
	}

}
