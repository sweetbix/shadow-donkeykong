import bagel.Image;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents a hammer in the game that can be collected and used by mario. They are stationary game objects
 * that are destroyed upon collection.
 * Extends game object to inherent basic object properties
 */
public class Hammer extends GameObject {
    /**
     * Constructs a hammer at the specified position. It will not move from this position (hammers can not move).
     * Loads in the image as well.
     * @param x The x coordinate of the hammer
     * @param y The y coordinate of the hammer
     */
    public Hammer(double x, double y) {
        setX(x);
        setY(y);
        setDestroyed(false);
        setImg(new Image("res/hammer.png"));
    }

    /**
     * Method to read in a list of hammers in the specified level  from the game properties file and store
     * them in an array list
     * @param level The specified level (1 or 2)
     * @param gameProps The game properties file to read hammer information from
     * @return The array list for which the hammers will be stored in
     */
    public static ArrayList<Hammer> readHammers(int level, Properties gameProps) {
        ArrayList<Hammer> hammers = new ArrayList<Hammer>();

        int n = Integer.parseInt(gameProps.getProperty("hammer.level" + level + ".count"));

        String points;
        for (int i = 1; i <= n; i++) {
            points = gameProps.getProperty("hammer.level" + level + "." + i);
            hammers.add(new Hammer(
                    Double.parseDouble(points.substring(0, points.indexOf(','))),
                    Double.parseDouble(points.substring(points.indexOf(',') + 1))
            ));
        }

        return hammers;
    }

    /**
     * Used to reset the state of a hammer by setting them as not-destroyed
     */
    @Override
    public void reset() {
        setDestroyed(false);
    }
}
