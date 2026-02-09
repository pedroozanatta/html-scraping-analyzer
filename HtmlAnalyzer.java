import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Stack;

public class HtmlAnalyzer {

    public static String ERROR_MESSAGE = "URL connection error";
    public static String MALFORMED_HTML = "malformed HTML";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(ERROR_MESSAGE);
            return;
        }

        String urlString = args[0];

        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            int responseCode = urlConn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8));

                Stack<String> stack = new Stack<>();
                String line;
                String deeptestText = "";
                int depth = 0;
                int maxDepth = -1;

                while ((line = buffer.readLine()) != null) {
                    line = line.trim();

                    if (line.isEmpty()) continue;

                    if (line.startsWith("</")) {
                        String closeTag = extractTagName(line);

                        if(stack.isEmpty()){
                            System.out.println(MALFORMED_HTML);
                            return;
                        }

                        String openTag = stack.pop();

                        if(!openTag.equals(closeTag)){
                            System.out.println(MALFORMED_HTML);
                            return;
                        }

                        depth--;
                        if(depth < 0){
                            System.out.println(MALFORMED_HTML);
                            return;
                        }

                    } else if (line.startsWith("<")) {
                        String tagContent = extractTagName(line);
                        stack.push(tagContent);
                        depth++;

                    } else {
                        if (depth > maxDepth) {
                            maxDepth = depth;
                            deeptestText = line;
                        }
                    }
                }

                if (depth != 0 || !stack.isEmpty()) {
                    System.out.println(MALFORMED_HTML);
                    return;
                }

                System.out.println(deeptestText);

            } else
                System.out.println(ERROR_MESSAGE);

        } catch(MalformedURLException err){
            System.out.println(ERROR_MESSAGE);
            return;
        } catch(IOException err){
            System.out.println(ERROR_MESSAGE);
            return;
        }
    }

    private static String extractTagName(String line){
        if(line.startsWith("</"))
            return line.substring(2, line.length() - 1);
        else if (line.startsWith("<"))
            return line.substring(1, line.length() - 1);

        return null;
    }
}