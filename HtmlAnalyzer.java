/**
 * HtmlAnalyzer
 * - Program to analyze an HTML document obtained from a URL and return the first text found at the deepest level of the structure.
 * - Also validates if the HTML is formed correctly.
 */
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

public class HtmlAnalyzer {

    public static final String ERROR_MESSAGE = "URL connection error";
    public static final String MALFORMED_HTML = "malformed HTML";

    /**
     * @param args command line arguments (args[0] must be the URL
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(ERROR_MESSAGE);
            return;
        }

        String urlString = args[0];

        try{
            HttpURLConnection connection = openConnection(urlString);

            try(BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String response = deepestText(buffer);
                System.out.println(response);
            }

            connection.disconnect();

        } catch (IllegalStateException err) {
            System.out.println(MALFORMED_HTML);
        } catch (IOException err) {
            System.out.println(ERROR_MESSAGE);
        }
    }

    /**
     * Opens an HTTP connection and validates if response code is 200 OK
     * @param urlString URL to be accessed
     * @return validated HttpUrlConnection
     * @throws IOException if connection fails or response is not 200 OK
     */
    private static HttpURLConnection openConnection(String urlString) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setRequestMethod("GET");

        if(urlConn.getResponseCode() != HttpURLConnection.HTTP_OK){
            throw new IOException(ERROR_MESSAGE);
        }

        return urlConn;
    }

    /**
     * Analyzes the HTML and returns the text at the deepest nesting level.
     * @param buffer BufferedReader with HTML content
     * @return text found at the deepest level
     * @throws IOException if a read error occurs
     * @throws IllegalStateException if HTML is malformed
     */
    private static String deepestText(BufferedReader buffer) throws IOException{
        Stack<String> stack = new Stack<>();
        String line;
        String deepestText = "";
        int depth = 0;
        int maxDepth = -1;

        while ((line = buffer.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) continue;

            if (line.startsWith("</")) {
                depth = closingTag(line, stack, depth);
            } else if (line.startsWith("<")) {
                depth = openingTag(line, stack, depth);
            } else {
                if (depth > maxDepth) {
                    maxDepth = depth;
                    deepestText = line;
                }
            }
        }

        if (depth != 0 || !stack.isEmpty()) {
            throw new IllegalStateException(MALFORMED_HTML);
        }

        return deepestText;
    }

    /**
     * Processes an opening tag and increments depth.
     * @param line line containing the opening tag
     * @param stack stack with opened tags
     * @param depth current depth
     * @return new depth after opening tag
     * @throws IllegalStateException if tag is invalid
     */
    private static int openingTag(String line, Stack<String> stack, int depth) throws IllegalStateException {
        String tagContent = extractTagContent(line);

        if (tagContent == null || tagContent.isEmpty()) {
            throw new IllegalStateException(MALFORMED_HTML);
        }

        stack.push(tagContent);
        return ++depth;
    }

    /**
     * Processes a closing tag and decrements depth.
     * @param line line containing the closing tag
     * @param stack stack with opened tags
     * @param depth current depth
     * @return new depth after closing tag
     * @throws IllegalStateException if structure is invalid
     */
    private static int closingTag(String line, Stack<String> stack, int depth) throws IllegalStateException {
        String closeTag = extractTagContent(line);

        if (stack.isEmpty()) {
            throw new IllegalStateException(MALFORMED_HTML);
        }

        String openTag = stack.pop();

        if (!openTag.equals(closeTag)) {
            throw new IllegalStateException(MALFORMED_HTML);
        }

        depth--;

        if (depth < 0) {
            throw new IllegalStateException(MALFORMED_HTML);
        }

        return depth;
    }

    /**
     * Extracts the tag name from a tag line.
     * @param line line containing the tag
     * @return tag name
     */
    private static String extractTagContent(String line){
        int lastIndex = line.length() - 1;

        if(line.startsWith("</"))
            return line.substring(2, lastIndex);
        else if (line.startsWith("<"))
            return line.substring(1, lastIndex);

        return null;
    }
}