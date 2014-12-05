package view;

import java.util.Arrays;

import model.Biome;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
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

	public BiomeView(Display display) {
		this.red = display.getSystemColor(SWT.COLOR_RED);
		this.green = display.getSystemColor(SWT.COLOR_GREEN);
	}

	public Table buildTable(final int sizeX, final int sizeY, Shell shell) {
		table = new Table(shell, SWT.BORDER | SWT.NO_SCROLL
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setLinesVisible(true);
		for (int x = 0; x < sizeX; x++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
		}
		for (int y = 0; y < sizeY; y++) {
			TableItem item = new TableItem(table, SWT.NONE);
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
							System.out.println("item clicked.");
							System.out.println("x is " + i);
							x = i;
						}
					}
					int y = Arrays.asList(table.getItems())
							.indexOf(clickedItem);
					System.out.println("y is " + y);
					listener.itemClicked(x, y);
				}
			}
		});

		return table;
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

	public void update(Biome biome, Table table, Display display) {
		for (int x = 0; x < biome.getSizeX(); x++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(Integer.toString(x));
		}
		for (int y = 0; y < biome.getSizeY(); y++) {
			TableItem item = table.getItem(y);
			for (int x = 0; x < biome.getSizeX(); x++) {
				if (biome.getStatus(x, y)) {
					item.setText(x, "1");
					item.setBackground(x, green);
				} else {
					item.setText(x, "0");
					item.setBackground(x, red);
				}
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
