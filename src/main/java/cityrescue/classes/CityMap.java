package cityrescue.classes;

/**
 * Represents the grid of the simulation.
 * Has an width and height.
 * Specifies which locations are blocked and unblocked.
 */
public class CityMap {
    private int width;
    private int height;
    private boolean[][] blocked;

    // Constructor to initialise the board 
    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocked = new boolean[width][height];
    }

    public boolean isMoveLegal(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height && !blocked[x][y]) {
            return true; 
        }
        return false;
    }

    public boolean isBlocked(int x, int y) {
        if (blocked[x][y] == true) {
            return true;
        }
        return false;
    }

    public void setBlocked(int x, int y) {
        blocked[x][y] = true;
    }

    public void setUnblocked(int x, int y) {
        blocked[x][y] = false;
    }

    public int countObstacles() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (blocked[x][y] == true) {
                    count++;
                }
            }
        }
        return count;
    }
}