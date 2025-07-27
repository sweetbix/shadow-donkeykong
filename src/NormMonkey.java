import bagel.Image;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents a normalmonkey in the game. They are only able to walk paths.
 * Extends the Monkey class which has shooting behaviour and path walking methods
 */
public class NormMonkey extends Monkey {
    /**
     * Constructs a new normal monkey at a specified start position, with a direction and an array list for the
     * path it will follow. Reads and loads images.
     * @param x The starting x coordinate of the monkey
     * @param y The starting y coordinate of the monkey
     * @param right The direction the monkey is facing (true if right, false if left)
     * @param path An array of Integers representing the path the monkey will follow
     */
    public NormMonkey(double x, double y, boolean right, ArrayList<Integer> path) {
        super(x, y, right, path);
        setLeftImg(new Image("res/normal_monkey_left.png"));
        setRightImg(new Image("res/normal_monkey_right.png"));

        // Determine correct starting image from its direction
        if (right) {
            setImg(getRightImg());
        } else {
            setImg(getLeftImg());
        }
    }

    /**
     * Read a list of normal monkey from the game properties file and return them as an array list
     * @param gameProps The game properties file to read information from
     * @return An array list of all normal monkeys
     */
    public static ArrayList<NormMonkey> readNormMonkeys(ArrayList<NormMonkey> normMonkeys, Properties gameProps) {
        normMonkeys = new ArrayList<NormMonkey>();

        int n = Integer.parseInt(gameProps.getProperty("normalMonkey.level2.count"));

        String points;
        double x, y;
        boolean right;
        ArrayList<Integer> path;
        for (int i = 1; i <= n; i++) {

            // Read x and y coordinates using string manipulation
            points = gameProps.getProperty("normalMonkey.level2." + i);
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

            normMonkeys.add(new NormMonkey(x,y,right,path));
        }

        return normMonkeys;
    }
}
