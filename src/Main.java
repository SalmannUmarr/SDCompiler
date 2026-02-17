import scanner.ManualScanner;
import scanner.Token;

import java.io.File;

/**
 * Entry point to run the scanner on a source file.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <path-to-source-file>");
            return;
        }
        File sourceFile = new File(args[0]);
        ManualScanner scanner = new ManualScanner(sourceFile);
        Token token;
        while ((token = scanner.getNextToken()) != null) {
            System.out.println(token);
        }
    }
}
