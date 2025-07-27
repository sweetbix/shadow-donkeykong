import bagel.Image;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents a ladder in the game. Ladders can be climbed by mario, allowing him to move between platforms.
 * Implements Fallable to handle falling physics if a ladder spawns midair
 */
public class Ladder extends GameObject implements Fallable {
    private final double GRAVITY = 0.25;

    /**
     * Creates a ladder at the specified position. Loads in image as well
     * @param x The x coordinate of the ladder
     * @param y The y coordinate of the ladder
     */
    public Ladder(double x, double y) {
        setX(x);
        setY(y);
        setVelocity(0);
        setImg(new Image("res/ladder.png"));
    }


    /**
     * Method to read in a list of ladders in the specified level  from the game properties file and store
     * them in an array list
     * @param level The specified level (1 or 2)
     * @param gameProps The game properties file to read ladder information from
     * @return The array list for which the ladders will be stored in
     */
    public static ArrayList<Ladder> readLadders(int level, Properties gameProps) {
        ArrayList<Ladder> ladders = new ArrayList<Ladder>();

        // Read number of ladders
        int n = Integer.parseInt(gameProps.getProperty("ladder.level" + level + ".count"));

        // Read and store each ladder
        String points;
        for (int i = 1; i <= n; i++) {
            points = gameProps.getProperty("ladder.level" + level + "." + i);
            ladders.add(new Ladder(
                    Double.parseDouble(points.substring(0, points.indexOf(','))),
                    Double.parseDouble(points.substring(points.indexOf(',') + 1))
            ));
        }

        return ladders;
    }

    /**
     * Corrects the position of a ladder if it is unintentionally intersecting with a platform. It positions this
     * ladder directly above the platform if so
     * @param platforms All platforms in the game (on the current level)
     */
    public void adjustPosition(ArrayList<Platform> platforms) {
        for (Platform p : platforms) {
            if (p.ladderIntersectsPlatform(this)) {
                // current obstacle intersects with a platform
                this.setY(p.getY() - p.getImg().getHeight() / 2.0 - this.getImg().getHeight() / 2.0);
                return;
            }
        }
    }

    /**
     * Method to reset the state of a ladder. No implementation as ladders won't be reset
     */
    @Override
    public void reset() {

    }

    /**
     * Get the gravity constant of the ladder for falling physics
     * @return Gravity constant of the ladder
     */
    public double getGRAVITY() {
        return GRAVITY;
    }
}
