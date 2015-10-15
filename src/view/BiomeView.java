package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	private VBox controlPanel;
	private CheckBox tickForever;

	public BiomeView(Stage primaryStage, int sizeX, int sizeY) {

		HBox tickButtons;
		SplitPane splitPane = new SplitPane();
		this.grid = new VBox();
		grid.setStyle("-fx-background-color: #000000;");
		tickButtons = new HBox();
		tickButtons.setAlignment(Pos.CENTER);
		this.controlPanel = new VBox();
		Text instructions = new Text("Click cells to toggle life!\nClick Tick buttons to progress!\n");
		instructions.setTextAlignment(TextAlignment.CENTER);
		controlPanel.setAlignment(Pos.CENTER);
		controlPanel.getChildren().add(instructions);

		this.sizeX = sizeX;
		this.sizeY = sizeY;

		createGrid();
		splitPane.getItems().add(grid);
		Button tickButton = createTickButton();
		Button tick5Button = createTick5Button();
		tickButtons.getChildren().add(tickButton);
		tickButtons.getChildren().add(tick5Button);
		controlPanel.getChildren().add(tickButtons);
		Button randomize = createRandomButton();
		Button clear = createClearButton();
		HBox tickForeverBox = new HBox();
		tickForeverBox.setAlignment(Pos.CENTER);
		Text forever = new Text("\nTick Forever: \n");
		this.tickForever = createTickForever();
		tickForeverBox.getChildren().add(forever);
		tickForeverBox.getChildren().add(tickForever);
		controlPanel.getChildren().add(tickForeverBox);
		controlPanel.getChildren().add(randomize);
		controlPanel.getChildren().add(clear);
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
				button.setStyle("-fx-base: #984f27;");
				button.setId(x + "," + y);
				button.setOnAction(mouseListener(x, y));
				hbox.getChildren().add(button);
			}
			grid.getChildren().add(hbox);
		}
	}

	private EventHandler<ActionEvent> mouseListener(int x, int y) {
		return event -> listener.itemClicked(x, y);
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

	private CheckBox createTickForever() {
		CheckBox tickForever = new CheckBox();
		tickForever.setOnAction(event -> {
			if (tickForever.isSelected()) {
				listener.tickForever();
			} else {
				listener.stopTicking();
			}
		});
		return tickForever;
	}

	private Button createRandomButton() {
		Button random = new Button("Randomize Life");
		random.setOnAction(event -> listener.randomize());
		return random;
	}

	private Button createClearButton() {
		Button clear = new Button("Clear");
		clear.setOnAction(event -> listener.clear());
		return clear;
	}

	public void setCellStatus(int x, int y, boolean alive) {
		String id = "#" + x + "," + y;
		Button item = (Button) scene.lookup(id);
		if (alive) {
			item.setStyle("-fx-base: #57b757;");
		} else {
			item.setStyle("-fx-base: #984f27;");
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

	public void setTickForever(boolean b) {
		this.tickForever.setSelected(b);
	}

}
