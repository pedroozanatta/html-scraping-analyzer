import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HtmlAnalyzer {

    public static String ERROR_MESSAGE = "URL connection error";

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

            BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8));
            Scanner scanner = new Scanner(buffer);

        } catch(IOException ioe){
            System.out.println(ERROR_MESSAGE);
        }
    }
}