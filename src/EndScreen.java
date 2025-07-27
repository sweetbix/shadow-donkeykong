import bagel.Font;
import bagel.Window;

/**
 * Represents the screen which appears after the game is lost or won. Displays win / loss messages and prompts,
 * the final score, and a 'continue' prompt
 * Extends the Screen super class to inherit basic properties
 */
public class EndScreen extends Screen {
    private String continueMsg;
    private String scoreMsg;
    private String wonMsg;
    private String lostMsg;
    private Font statusFont;
    private Font scoreFont;
    private double statusY;
    private double scoreY;

    /**
     * Constructs a new instance of end screen. Reads all messages from the message properties file and prompt y
     * coordinates and stores these
     */
    public EndScreen() {
        continueMsg = this.getMESSAGE_PROPS().getProperty("gameEnd.continue");
        scoreMsg = this.getMESSAGE_PROPS().getProperty("gameEnd.score");
        wonMsg = this.getMESSAGE_PROPS().getProperty("gameEnd.won");
        lostMsg = this.getMESSAGE_PROPS().getProperty("gameEnd.lost");
        statusFont = new Font(this.getGAME_PROPS().getProperty("font"),
                Integer.parseInt(this.getGAME_PROPS().getProperty("gameEnd.status.fontSize")));
        scoreFont = new Font(this.getGAME_PROPS().getProperty("font"),
                Integer.parseInt(this.getGAME_PROPS().getProperty("gameEnd.scores.fontSize")));
        statusY = Double.parseDouble(this.getGAME_PROPS().getProperty("gameEnd.status.y"));
        scoreY = Double.parseDouble(this.getGAME_PROPS().getProperty("gameEnd.scores.y"));

    }

    /**
     * Method which renders and displays the game end screen based on outcome
     * Displays either a win or loss message, the final score, and a continue prompt, all of these being
     * centred horizontally
     * @param won True of the game was fun, otherwise the game was lost
     * @param score The final score
     */
    public void render(boolean won, int score) {
        if (won) {
            statusFont.drawString(wonMsg, (Window.getWidth() - statusFont.getWidth(wonMsg)) / 2.0, statusY);
        } else {
            statusFont.drawString(lostMsg, (Window.getWidth() - statusFont.getWidth(lostMsg)) / 2.0, statusY);
        }

        scoreFont.drawString(scoreMsg + " " + score, (Window.getWidth() - scoreFont.getWidth(String.format("%s %d", scoreMsg, score))) / 2, scoreY);

        scoreFont.drawString(continueMsg, (Window.getWidth() - scoreFont.getWidth(continueMsg)) / 2.0, Window.getHeight() - 100);
    }
}
