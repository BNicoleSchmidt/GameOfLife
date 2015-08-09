package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class BiomeView {

	private IViewListener listener;
	private int sizeX;
	private int sizeY;
	private Scene scene;
	private VBox grid;
	private HBox buttons;
	private VBox controlPanel;

	public BiomeView(Stage primaryStage, int sizeX, int sizeY) {

		SplitPane splitPane = new SplitPane();
		this.grid = new VBox();
		grid.setStyle("-fx-background-color: #000000;");
		this.buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		this.controlPanel = new VBox();
		Text instructions = new Text("Click cells to toggle life!\nClick Tick buttons to progress!");
		instructions.setTextAlignment(TextAlignment.CENTER);
		controlPanel.setAlignment(Pos.CENTER);
		controlPanel.getChildren().add(instructions);

		this.sizeX = sizeX;
		this.sizeY = sizeY;

		createGrid();
		Button tickButton = createTickButton();
		Button tick5Button = createTick5Button();
		splitPane.getItems().add(grid);
		buttons.getChildren().add(tickButton);
		buttons.getChildren().add(tick5Button);
		controlPanel.getChildren().add(buttons);
		splitPane.getItems().add(controlPanel);
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
				button.setMinSize(25, 25);
				button.setMaxSize(25, 25);
				button.setStyle("-fx-base: #f3622d;");
				button.setId(x + "," + y);
				button.setOnAction(mouseListener(x, y));
				hbox.getChildren().add(button);
			}
			grid.getChildren().add(hbox);
		}
	}

	private EventHandler<ActionEvent> mouseListener(int x, int y) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
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
		String id = "#" + x + "," + y;
		Button item = (Button) scene.lookup(id);
		if (alive) {
			item.setStyle("-fx-base: #57b757;");
		} else {
			item.setStyle("-fx-base: #f3622d;");
		}
	}

	public void update(boolean[][] biome) {
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
