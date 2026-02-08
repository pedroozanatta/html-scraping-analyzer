
public class HtmlAnalyzer {

    public static String ERROR_MESSAGE = "URL connection error";

    public static void main(String[] args) {
        if (args.length == 1){
            System.out.println("Funcionou");
        } else {
            System.out.println(ERROR_MESSAGE);
            System.exit(1);
        }
    }
}