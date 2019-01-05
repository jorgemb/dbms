package JSON;

/**
 * Simple message structure
 * @author Jorge
 */
public class Message {
    private boolean Error;
    private String Type;
    private String Content;

    public boolean isError() {
        return Error;
    }

    public String getType() {
        return Type;
    }

    public String getContent() {
        return Content;
    }
}
