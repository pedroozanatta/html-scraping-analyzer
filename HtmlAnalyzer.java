import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

public class HtmlAnalyzer {

    public static final String ERROR_MESSAGE = "URL connection error";
    public static final String MALFORMED_HTML = "malformed HTML";

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

    private static HttpURLConnection openConnection(String urlString) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setRequestMethod("GET");

        if(urlConn.getResponseCode() != HttpURLConnection.HTTP_OK){
            throw new IOException(ERROR_MESSAGE);
        }

        return urlConn;
    }

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

    private static int openingTag(String line, Stack<String> stack, int depth) throws IllegalStateException {
        String tagContent = extractTagContent(line);

        if (tagContent == null || tagContent.isEmpty()) {
            throw new IllegalStateException(MALFORMED_HTML);
        }

        stack.push(tagContent);
        return ++depth;
    }

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

    private static String extractTagContent(String line){
        int lastIndex = line.length() - 1;

        if(line.startsWith("</"))
            return line.substring(2, lastIndex);
        else if (line.startsWith("<"))
            return line.substring(1, lastIndex);

        return null;
    }
}