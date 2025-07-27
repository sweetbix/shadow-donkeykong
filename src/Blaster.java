import bagel.Image;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents a blaster weapon in the game. They are loaded into level 2 only and can be collected by mario to
 * shoot bullets.
 * The class extends GameObject to inherit basic game properties
 */
public class Blaster extends GameObject {

    /**
     * Constructs a new blaster at a specified position. Loads the image for the blaster from res folder as well.
     * @param x x coordinate of the blaster
     * @param y y coordinate of the blaster
     */
    public Blaster(double x, double y) {
        setX(x);
        setY(y);
        setDestroyed(false);
        setImg(new Image("res/blaster.png"));
    }

    /**
     * Method to read in the blaster positions from the app properties file and create an arraylist of these blasters
     * @param level The specified level (should be 2 as blasters are only in level 2)
     * @param gameProps The game properties file to read the blaster information from
     * @return A new array list containing all the blasters in this level
     */
    public static ArrayList<Blaster> readBlasters(int level, Properties gameProps) {
        ArrayList<Blaster> blasters = new ArrayList<Blaster>();

        // Read number of blasters
        int n = Integer.parseInt(gameProps.getProperty("blaster.level" + level + ".count"));

        // Read each blaster, with string manipulation
        String points;
        for (int i = 1; i <= n; i++) {
            points = gameProps.getProperty("blaster.level" + level + "." + i);
            blasters.add(new Blaster(
                    Double.parseDouble(points.substring(0, points.indexOf(','))),
                    Double.parseDouble(points.substring(points.indexOf(',') + 1))
            ));
        }

        return blasters;
    }

    /**
     * Resets the state of a blaster. Used when level 2 is reset
     */
    public void reset() {
        setDestroyed(false);
    }
}
