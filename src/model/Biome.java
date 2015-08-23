package model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Biome {

	boolean tickForever = false;
	private boolean[][] biome;
	private int sizeX;
	private int sizeY;
	private IBiomeListener listener;
	private ScheduledExecutorService executorService;

	public Biome(int x, int y) {
		this(x, y, Executors.newSingleThreadScheduledExecutor());
	}

	Biome(int x, int y, ScheduledExecutorService executorService) {
		this.executorService = executorService;
		biome = new boolean[x][y];
		this.sizeX = x;
		this.sizeY = y;
	}

	public void tick() {
		tick(1);
	}

	public void tick(int timesToTick) {
		if ((timesToTick > 0 || tickForever) && !(timesToTick > 0 && tickForever)) {
			Runnable runnable = new Runnable() {
				int remainingTicks = timesToTick;

				@Override
				public void run() {
					boolean[][] nextBiome = new boolean[sizeX][sizeY];
					for (int x = 0; x < sizeX; x++) {
						for (int y = 0; y < sizeY; y++) {
							nextBiome[x][y] = getNextState(x, y);
						}
					}
					biome = nextBiome;
					notifyListener();

					remainingTicks--;
					if ((remainingTicks > 0 || tickForever) && !(remainingTicks > 0 && tickForever)) {
						executorService.schedule(this, 500, TimeUnit.MILLISECONDS);
					}
				}
			};

			executorService.schedule(runnable, 0, TimeUnit.MILLISECONDS);
		}
	}

	private boolean getNextState(int x, int y) {
		if (countNeighbors(x, y) == 3) {
			return true;
		} else if (getStatus(x, y) && countNeighbors(x, y) == 2) {
			return true;
		}
		return false;
	}

	public int countNeighbors(int x, int y) {
		int alive = -checkNeighbor(x, y);
		for (int xOffset = -1; xOffset <= 1; ++xOffset) {
			for (int yOffset = -1; yOffset <= 1; ++yOffset) {
				alive += checkNeighbor(x + xOffset, y + yOffset);
			}
		}
		return alive;
	}

	private int checkNeighbor(int x, int y) {
		if (getStatus(x, y)) {
			return 1;
		}
		return 0;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public boolean getStatus(int x, int y) {
		if (x >= 0 && x < this.sizeX && y >= 0 && y < this.sizeY) {
			return this.biome[x][y];
		}
		return false;
	}

	protected void setStatus(int x, int y, boolean status) {
		this.biome[x][y] = status;
	}

	public void setTickForever(boolean bool) {
		this.tickForever = bool;
		if (bool) {
			tick(0);
		}
	}

	public boolean getTickForever() {
		return tickForever;
	}

	public void toggleCell(int x, int y) {
		setStatus(x, y, !getStatus(x, y));
		notifyListener();
	}

	private void notifyListener() {
		if (listener != null) {
			listener.biomeUpdated();
		}
	}

	public void setListener(IBiomeListener listener) {
		this.listener = listener;
	}

	public boolean[][] getBiome() {
		return this.biome;
	}

}
