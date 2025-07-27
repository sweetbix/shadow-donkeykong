import java.util.Properties;

/**
 * Abstract parent class of all screens in the game. Basic properties used to read from game and message
 * property files
 */
public abstract class Screen {
    private final Properties GAME_PROPS = IOUtils.readPropertiesFile("res/app.properties");
    private final Properties MESSAGE_PROPS = IOUtils.readPropertiesFile("res/message.properties");

    /**
     * Get the game properties file to read
     * @return Game properties file
     */
    public Properties getGAME_PROPS() {
        return GAME_PROPS;
    }

    /**
     * Get the message properties file to read
     * @return Message properties file
     */
    public Properties getMESSAGE_PROPS() {
        return MESSAGE_PROPS;
    }
}
