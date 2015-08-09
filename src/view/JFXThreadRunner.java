package view;

import javafx.application.Platform;

public class JFXThreadRunner {

	public void runLater(Runnable runnable) {
		Platform.runLater(runnable);
	}

}
