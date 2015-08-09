package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BiomeView {

	private IViewListener listener;
	private int sizeX;
	private int sizeY;
	private Scene scene;
	private VBox vBox;

	public BiomeView(Stage primaryStage, int sizeX, int sizeY) {

		SplitPane splitPane = new SplitPane();
		this.vBox = new VBox();

		this.sizeX = sizeX;
		this.sizeY = sizeY;

		createGrid();
		Button tickButton = createTickButton();
		Button tick5Button = createTick5Button();
		// hbox.getChildren().addAll(table);
		splitPane.getItems().add(vBox);
		splitPane.getItems().add(tickButton);
		splitPane.getItems().add(tick5Button);
		this.scene = new Scene(splitPane);

		primaryStage.setTitle("Conway's Game of Life");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void createGrid() {
		for (int y = 0; y < sizeY; y++) {
			HBox hbox = new HBox();
			for (int x = 0; x < sizeX; x++) {
				Button button = new Button("");
				button.setStyle("-fx-base: #f3622d;");
				button.setId(x + "," + y);
				button.setOnAction(mouseListener(x, y));
				hbox.getChildren().add(button);
			}
			vBox.getChildren().add(hbox);
		}
	}

	private EventHandler<ActionEvent> mouseListener(int x, int y) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Clicked : " + x + "," + y);
				listener.itemClicked(x, y);
			}
		};
	}

	private Button createTickButton() {
		Button tickButton = new Button("Tick");
		tickButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				listener.tickClicked();
			}
		});
		return tickButton;
	}

	private Button createTick5Button() {
		Button tick5Button = new Button("5 Ticks");
		tick5Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				listener.tick5Clicked();
			}
		});
		return tick5Button;
	}

	public void setCellStatus(int x, int y, boolean alive) {
		System.out.println("In setcellstatus");
		String id = "#" + x + "," + y;
		Button item = (Button) scene.lookup(id);
		if (item == null) {
			System.out.println("null");
		}
		System.out.println(alive);
		if (alive) {
			item.setStyle("-fx-base: #57b757;");
		} else {
			item.setStyle("-fx-base: #f3622d;");
		}
	}

	public void update(boolean[][] biome) {
		System.out.println("in update");
		new JFXThreadRunner().runLater(() -> {
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					setCellStatus(x, y, biome[x][y]);
				}
			}
		});
	}

	public void setListener(IViewListener listener) {
		this.listener = listener;
	}

}
