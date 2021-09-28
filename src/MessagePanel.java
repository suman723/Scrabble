
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.util.ArrayList;

/**
 * This is a MessagePanel Class which will display the message in JPanel when calling up
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class MessagePanel extends JPanel{

    // Where messages are wrote to
    private final JEditorPane message;

    private final String header;               // html header
    private ArrayList<String> mainContent;     // all messages printed bar the current line
    private String currentLine;                // any message on current line
    private final String footer;               // html footer
    private final int MAX_LINES = 42;          // maximum number of lines in main content

    Color inputTextColor;           // default colour for input text
    Color errorTextColor;           // default colour for error text

    /**
     * Constructor
     *
     * @param width width of panel
     * @param height height of panel
     */
    public MessagePanel(int width, int height)
    {
        // set preferred size
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        // setup editor pane
        message = new JEditorPane();
        message.setEditable(false);
        message.setBorder(javax.swing.BorderFactory.createEmptyBorder());       // remove default border
        message.setPreferredSize(new Dimension(width - 10, height));

        message.setContentType("text/html");                                    // switches to html
        HTMLEditorKit kit = new HTMLEditorKit();
        message.setEditorKit(kit);

        // default styling
        StyleSheet styleSheet = kit.getStyleSheet();
        Color defaultColour = Constants.matrixMidGreen;
        Color background = Constants.matrixBlack;
        inputTextColor = Constants.matrixLightGreen;
        errorTextColor = Constants.errorRed;
        int defaultFontSize = 12;
        String defaultFont = "Verdana";
        //String defaultFont = "Monospaced";

        // Sets defaults on body
        styleSheet.addRule("body {font-family: " + defaultFont + "; font-size: " + defaultFontSize + "; color: " + colorToRGBString(defaultColour) +
                "; background: " + colorToRGBString(background) + ";}");
        // Removes padding from paragraphs
        styleSheet.addRule("p {padding: 0; margin: 0;");

        // Setup header
        header = "<html><body><p>";
        // Setup footer (padding div at the bottom stops message panel from pushing up against command)
        footer = "</p><div style=\"padding-top: 15px\"></div></body></html>";
        // Setup content
        mainContent = new ArrayList<>();
        currentLine = "";

        // add scrollbar
        JScrollPane scrollV = new JScrollPane(message);
        scrollV.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollV.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        // add components to panel
        //add(message, BorderLayout.CENTER);
        add(scrollV, BorderLayout.CENTER);

        // draws the text to the panel
        drawMessagePanel();

    }

    /**
     * Adds lines to main content and removes the oldest line if there are more than max
     *
     * @param line to add to main content
     */
    private void addToMainContent(String line)
    {
        mainContent.add(line + "</p><p>");
        // check for over max size if so remove oldest
        if (mainContent.size() > MAX_LINES)
            mainContent.remove(0);
    }

    /**
     * Prints text passed in and moves to a new line
     * @param text text to print
     */
    public void println(String text)
    {
        // Update line;
        currentLine = currentLine + text;

        // Move to new line
        addToMainContent(currentLine);
        currentLine = "";

        // redraws the new html
        drawMessagePanel();
    }

    /**
     * Prints text passed in and moves to new line. Prints the text in the colour passed in
     *
     * @param text text to print
     * @param color java Color object representing the colour the text should appear in
     */
    public void println(String text, Color color)
    {
        // Convert color to a string
        String rgbText = colorToRGBString(color);

        // create a span with the chosen colour
        String front = "<span style=\"color: " + rgbText + ";\">";
        String back = "</span>";

        // Update line
        currentLine = currentLine + front + text + back;

        // Move to new line
        addToMainContent(currentLine);
        currentLine = "";

        drawMessagePanel();
    }

    /**
     * Prints a line with a leading ">" and uses inputTextColor set in the constructor
     * @param text text to print
     */
    public void printInputLine(String text)
    {
        println("> " + text, inputTextColor);
        // add blank line
        println("");
    }

    /**
     * Prints text but does not move to a new line
     * @param text text to print
     */
    public void print(String text)
    {
        // Update line;
        currentLine = currentLine + text;

        drawMessagePanel();
    }

    /**
     * Prints text but does not move to a new line
     * Changes the colour of the text (not the entire line) to match color object passed in
     *
     * @param text text to print
     * @param color color object that matches the desired colour of text
     */
    public void print(String text, Color color)
    {
        // Convert color to a string
        String rgbText = colorToRGBString(color);

        // create a span with the chosen colour
        String front = "<span style=\"color: " + rgbText + ";\">";
        String back = "</span>";

        // Update line
        currentLine = currentLine + front + text + back;
    }

    /**
     * Updates the editorPane's text current content
     */
    private void drawMessagePanel()
    {
        String contents = header;
        for (String line : mainContent)
        {
            contents += line;
        }
        contents += currentLine + footer;
        message.setText(contents);

        // Scrolls text
        message.setCaretPosition(message.getDocument().getLength());
    }

    /**
     * Converts a java Color object to a string in the format "rgb(XXX,XXX,XXX)" which can be used in CSS
     *
     * @param color to convert
     * @return rgb string
     */
    private static String colorToRGBString(Color color)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("rgb(");
        sb.append(color.getRed());
        sb.append(",");
        sb.append(color.getGreen());
        sb.append(",");
        sb.append(color.getBlue());
        sb.append(")");
        return sb.toString();
    }

    // main method for debugging the code
    public static void main(String[] args) {

        /* JFrame setting up */
        JFrame frame = new JFrame();
        frame.setTitle("MESSAGE PANEL");   // title for Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //function to close
        frame.setSize(500,500);   // setting up Frame size
        frame.setLayout(new FlowLayout()); // layout function
    }
}
