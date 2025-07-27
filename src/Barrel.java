import bagel.Image;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents barrels in the game which can fall subject to gravity. They can be destroyed by mario, and can also
 * cause a game loss if mario touches a barrel without a hammer.
 * The class implements the Fallable interface which handles the falling physics.
 */
public class Barrel extends GameObject implements Fallable {
    private final double GRAVITY = 0.4;

    /**
     * Constructs a new barrel at a specified position. The image for a barrel is also read in and assigned to the
     * it's Img variable
     * @param x The x co-ordinate of the barrel
     * @param y The y co-ordinate of the barrel
     */
    public Barrel(double x, double y) {
        setX(x);
        setY(y);
        setVelocity(0);
        setImg(new Image("res/barrel.png"));
        setDestroyed(false);
    }

    /**
     * Method to read in the barrel positions from the app properties file and create an arraylist of these barrels for
     * a specified level
     * @param level The specified game level these barrels are being loaded for
     * @param gameProps The game properties file containing the barrels information to be read
     * @return A new array list containing all barrels for a specified level
     */
    //
    public static ArrayList<Barrel> readBarrels(int level, Properties gameProps) {
        ArrayList<Barrel> barrels = new ArrayList<Barrel>();

        // Read number of barrels
        int n = Integer.parseInt(gameProps.getProperty("barrel.level" + level + ".count"));

        // Read each individual barrel from the properties file, with string manipulation
        String points;
        for (int i = 1; i <= n; i++) {
            points = gameProps.getProperty("barrel.level" + level + "." + i);
            barrels.add(new Barrel(
                    Double.parseDouble(points.substring(0, points.indexOf(','))),
                    Double.parseDouble(points.substring(points.indexOf(',') + 1))
            ));
        }

        return barrels;
    }

    /**
     * Corrects the position of a barrel if it is unintentionally intersecting with a platform. It positions this
     * barrel directly above the platform if so
     * @param platforms All platforms in the game (on the current level)
     */
    public void adjustPosition(ArrayList<Platform> platforms) {
        for (Platform p : platforms) {

            // check if current barrel intersects with a platform
            if (p.intersects(this)) {
                this.setY(p.getY() - p.getImg().getHeight() / 2.0 - this.getImg().getHeight() / 2.0);
                return;
            }
        }
    }

    /**
     * Resets the barrel state. Used when a level needs to be reset
     */
    @Override
    public void reset() {
        this.setDestroyed(false);
    }

    /**
     * Get the gravity constant affecting this barrel
     * @return The gravity constant
     */
    public double getGRAVITY() {
        return GRAVITY;
    }

    /**
     * Get the current vertical velocity of this barrel
     * @return Vertical velocity
     */
    @Override
    public double getVelocity() {
        return super.getVelocity();
    }
}
