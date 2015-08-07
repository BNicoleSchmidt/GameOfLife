package view;

import java.util.Arrays;

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
	private int sizeX;
	private int sizeY;

	public BiomeView(Shell shell, int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.red = shell.getDisplay().getSystemColor(SWT.COLOR_RED);
		this.green = shell.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		

		createTable(shell);
		createTickButton(shell);
		createTick5Button(shell);
	}


	private void createTable(Shell shell) {
		table = new Table(shell, SWT.BORDER | SWT.NO_SCROLL
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setLinesVisible(true);

		for (int x = 0; x < sizeX; x++) {
			new TableColumn(table, SWT.CENTER);
		}
		for (int y = 0; y < sizeY; y++) {
			new TableItem(table, SWT.CENTER);
		}
		for (int x = 0; x < sizeX; x++) {
			table.getColumn(x).pack();
		}
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		table.addListener(SWT.MouseDown, mouseListener());
	}

	private Listener mouseListener() {
		return new Listener() {
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TableItem clickedItem = table.getItem(pt);
				if (clickedItem != null) {
					int x = findClickedColumn(pt, clickedItem);

					int y = Arrays.asList(table.getItems())
							.indexOf(clickedItem);

					if (x != -1) {
						listener.itemClicked(x, y);
					}
				}
			}

		};
	}

	private int findClickedColumn(Point pt, TableItem clickedItem) {
		for (int i = 0; i < sizeX; i++) {
			Rectangle rect = clickedItem.getBounds(i);
			if (rect.contains(pt)) {
				return i;
			}
		}
		return -1;
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

	private void createTick5Button(Shell shell) {
		Button tick5Button = new Button(shell, SWT.PUSH);
		tick5Button.setText("5 Ticks");
		tick5Button.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true, 1, 1));
		tick5Button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					listener.tick5Clicked();
				} catch (InterruptedException e1) {
					System.out.print("Error: " + e1.getMessage());
					e1.printStackTrace();
				}
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

	public void update(boolean[][] biome) {
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				setCellStatus(x, y, biome[x][y]);
			}
		}
		table.deselectAll();
	}

	public void setListener(IViewListener listener) {
		this.listener = listener;
	}

}
