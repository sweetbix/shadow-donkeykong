/**
 * Represents the first level of the game. Renders score and time information only.
 * Extends the Level parent class which provides common level properties
 */
public class Level1 extends Level {
    /**
     * Creates a new level 1 instance.
     */
    public Level1() {
        super();
    }

    /**
     * Render the score and time left in the game
     * @param score The current player score
     */
    public void render(int score) {
        this.getScoreFont().drawString(String.format("Score %d\n" + "Time Left %d", score, getCurrentFrame() / getFPS()),
                getScoreX(), getScoreY());
        this.setCurrentFrame(getCurrentFrame() - 1);
    }
}
