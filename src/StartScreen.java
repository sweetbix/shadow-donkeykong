import bagel.Font;
import bagel.Window;

/**
 * Represents the screen which appears at the start of the game. Displays message prompts.
 * Extends the Screen super class to inherit basic properties
 */
public class StartScreen extends Screen {
    private String titleMsg;
    private String promptMsg;
    private Font titleFont;
    private Font promptFont;
    private double titleY;
    private double promptY;

    /**
     * Constructs a new instance of start screen. Reads all messages from the message properties file and prompt y
     * coordinates and stores these
     */
    public StartScreen() {
        titleMsg = this.getMESSAGE_PROPS().getProperty("home.title");
        promptMsg = this.getMESSAGE_PROPS().getProperty("home.prompt");
        titleFont = new Font(this.getGAME_PROPS().getProperty("font"),
                Integer.parseInt(this.getGAME_PROPS().getProperty("home.title.fontSize")));
        promptFont = new Font(this.getGAME_PROPS().getProperty("font"),
                Integer.parseInt(this.getGAME_PROPS().getProperty("home.prompt.fontSize")));
        titleY = Double.parseDouble(this.getGAME_PROPS().getProperty("home.title.y"));
        promptY = Double.parseDouble(this.getGAME_PROPS().getProperty("home.prompt.y"));
    }

    /**
     * Renders information on the screen. A title and a prompt to start the game
     */
    public void render() {
        titleFont.drawString(titleMsg, (Window.getWidth() - titleFont.getWidth(titleMsg)) / 2, titleY);
        promptFont.drawString(promptMsg, (Window.getWidth() - promptFont.getWidth(promptMsg)) / 2, promptY);
    }
}
