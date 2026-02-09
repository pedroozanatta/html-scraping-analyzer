import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HtmlAnalyzer {

    public static String ERROR_MESSAGE = "URL connection error";
    public static String MALFORMED_URL = "malformed url";

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

            if(responseCode != 200) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8));
                Scanner scanner = new Scanner(buffer);

                String deeptestText = "";
                int depth = 0;
                int maxDepth = -1;

                while (scanner.hasNextLine()) {

                    String line = scanner.nextLine().trim();

                    if (line.isEmpty()) continue;

                    if (line.startsWith("</")) {
                        depth--;
                        if (depth < 0) {
                            System.out.println(MALFORMED_URL);
                            return;
                        }

                    } else if (line.startsWith("<")) {
                        depth++;

                    } else {
                        if (depth > maxDepth) {
                            maxDepth = depth;
                            deeptestText = line;
                        }
                    }

                    scanner.close();
                }

                if (depth != 0) {
                    System.out.println(MALFORMED_URL);
                    return;
                }

                System.out.println(deeptestText);

            } else
                System.out.println(ERROR_MESSAGE);

        } catch (MalformedURLException err){
            System.out.println(MALFORMED_URL);
            System.exit(1);
        } catch(IOException err){
            System.out.println(ERROR_MESSAGE);
            System.exit(1);
        }
    }
}