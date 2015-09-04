package view;

public interface IViewListener {
	void itemClicked(int x, int y);

	void tickClicked();

	void tick5Clicked();

	void tickForever();

	void stopTicking();

	void randomize();

	void clear();
}
