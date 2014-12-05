package model;

public class Biome {

	private boolean[][] biome;
	private int sizeX;
	private int sizeY;
	private IBiomeListener listener;

	public Biome(int x, int y) {
		biome = new boolean[x][y];
		this.sizeX = x;
		this.sizeY = y;
	}

	public void tick() {
		boolean[][] nextBiome = new boolean[sizeX][sizeY];
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				nextBiome[x][y] = getNextState(x, y);
			}
		}
		this.biome = nextBiome;
		// listener
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
		int alive = checkNeighbor(x - 1, y - 1) + checkNeighbor(x, y - 1)
				+ checkNeighbor(x + 1, y - 1) + checkNeighbor(x - 1, y)
				+ checkNeighbor(x + 1, y) + checkNeighbor(x - 1, y + 1)
				+ checkNeighbor(x, y + 1) + checkNeighbor(x + 1, y + 1);
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

	public void setStatus(int x, int y, boolean status) {
		this.biome[x][y] = status;
	}

	public void toggleCell(int x, int y) {
		setStatus(x, y, !getStatus(x, y));
	}

	public void setListener(IBiomeListener listener) {
		this.listener = listener;
	}

}
