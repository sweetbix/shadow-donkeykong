import bagel.Image;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents an intelligent monkey in the game. They are able to walk paths and shoot bananas at regular intervals
 * Extends the Monkey class which has shooting behaviour and path walking methods
 */
public class IntelMonkey extends Monkey {
    private final double shootPeriod = 5;
    private double shootTime;

    /**
     * Constructs a new intelligent monkey at a specified start position, with a direction and an array list for the
     * path it will follow. Reads and loads images.
     * @param x The starting x coordinate of the monkey
     * @param y The starting y coordinate of the monkey
     * @param right The direction the monkey is facing (true if right, false if left)
     * @param path An array of Integers representing the path the monkey will follow
     */
    public IntelMonkey(double x, double y, boolean right, ArrayList<Integer> path) {
        super(x, y, right, path);
        setLeftImg(new Image("res/intelli_monkey_left.png"));
        setRightImg(new Image("res/intelli_monkey_right.png"));
        shootTime = 0;

        // Determine correct starting image from its direction
        if (right) {
            setImg(getRightImg());
        } else {
            setImg(getLeftImg());
        }
    }

    /**
     * Read a list of intelligent monkey from the game properties file and return them as an array list
     * @param gameProps The game properties file to read information from
     * @return An array list of all intelligent monkeys
     */
    public static ArrayList<IntelMonkey> readIntelMonkeys(Properties gameProps) {
        ArrayList<IntelMonkey> intelMonkeys = new ArrayList<IntelMonkey>();

        // Read number of intelligent monkeys
        int n = Integer.parseInt(gameProps.getProperty("intelligentMonkey.level2.count"));

        // Read each individual monkey
        String points;
        double x, y;
        boolean right;
        ArrayList<Integer> path;
        for (int i = 1; i <= n; i++) {

            // Read x and y coordinates using string manipulation
            points = gameProps.getProperty("intelligentMonkey.level2." + i);
            x = Double.parseDouble(points.substring(0, points.indexOf(",")));
            y = Double.parseDouble(points.substring(points.indexOf(",") + 1, points.indexOf(";")));

            points = points.substring(points.indexOf(";") + 1);

            // Read the initial direction of the monkey
            if (points.substring(0, points.indexOf(";")).equals("right")) {
                right = true;
            } else {
                right = false;
            }

            points = points.substring(points.indexOf(";") + 1);

            // Finally, read the path the monkey will follow
            path = readPath(points);

            intelMonkeys.add(new IntelMonkey(x,y,right,path));
        }

        return intelMonkeys;
    }

    /**
     * Method allowing the intelligent monkey to shoot bananas, first checking if the interval has passed since the
     * last shot, and adding the new banana to the bananas array list for the level
     * @param bananas An array list representing all the bananas currently existing in the level
     * @return An updated array list with the new banana, or the same one if no banana was shot
     */
    public ArrayList<Banana> shoot(ArrayList<Banana> bananas) {

        // Check whether the interval has passed
        if (shootTime < shootPeriod) {
            return bananas;
        }

        // Ready to shoot
        Banana banana = new Banana(getX(), getY(), isRight());
        bananas.add(banana);
        shootTime = 0;

        return bananas;
    }

    /**
     * Update the time to track the shooting interval
     * @param FPS Constant representing the frames per second in our game. Time per frame can be calculated with
     *            1 / FPS
     */
    public void updateTime(int FPS) {
        shootTime += 1.0 / FPS;
    }
}

