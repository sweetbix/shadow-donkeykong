import bagel.Font;

import java.util.Properties;

/**
 * Abstract parent class representing a level in the game. Provides common functionality for constructors, and
 * level reset
 */
public abstract class Level {
    private final Properties GAME_PROPS = IOUtils.readPropertiesFile("res/app.properties");
    private final int FRAMES_PER_SEC = 60;
    private int currentFrame;
    private Font scoreFont;
    private double scoreX;
    private double scoreY;

    /**
     * Constructs a new level by reading in information from game properties file
     */
    public Level() {
        currentFrame = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.maxFrames"));
        scoreFont = new Font(GAME_PROPS.getProperty("font"),
                Integer.parseInt(GAME_PROPS.getProperty("gamePlay.score.fontSize")));
        scoreX = Double.parseDouble(GAME_PROPS.getProperty("gamePlay.score.x"));
        scoreY = Double.parseDouble(GAME_PROPS.getProperty("gamePlay.score.y"));
    }

    /**
     * Resets the level by resetting the frames to the original value
     */
    public void reset() {
        currentFrame = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.maxFrames"));
    }

    /**
     * Get the game properties to read information from. Used in concrete subclasses
     * @return The game properties
     */
    public Properties getGAME_PROPS() {
        return GAME_PROPS;
    }

    /**
     * Get the constant representing the number of frames rendered per second
     * @return The number of frames per second
     */
    public int getFPS() {
        return FRAMES_PER_SEC;
    }

    /**
     * Get the current frame number the level is on. Used for time remaining calculation
     * @return The number of frames remaining in the level
     */
    public int getCurrentFrame() {
        return currentFrame;
    }

    /**
     * Get the font used for score display
     * @return The score display font
     */
    public Font getScoreFont() {
        return scoreFont;
    }

    /**
     * Get the x value at which scores are displayed at
     * @return x coordinate at which scores are displayed at
     */
    public double getScoreX() {
        return scoreX;
    }

    /**
     * Get the y value at which scores are displayed at
     * @return y coordinate at which scores are displayed at
     */
    public double getScoreY() {
        return scoreY;
    }

    /**
     * Set the frame number to a new value. Used to update the frames after each render
     * @param currentFrame The new number of frames remaining
     */
    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }
}
