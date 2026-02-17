import scanner.*;
import utils.*;
import tables.*;
import java.io.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        String[] testFiles = {
                "test1.saltif",
                "test2.saltif",
                "test3.saltif",
                "test4.saltif",
                "test5.saltif"
        };

        StringBuilder finalLog = new StringBuilder();
        finalLog.append("COMPILER TEST RESULTS\n").append("=".repeat(30)).append("\n");

        for (String fileName : testFiles) {
            try {
                Path path = Paths.get(System.getProperty("user.dir"), "test", fileName);

                if (!Files.exists(path)) {
                    continue;
                }

                StringBuilder currentFileLog = new StringBuilder();
                String header = "\n=============== PROCESSING: " + fileName +
                        "===============\n" + "-".repeat(55) + "\n";
                currentFileLog.append(header);
                currentFileLog.append("TOKEN OUTPUT:\n");

                String content = Files.readString(path);

                Statistics stats = new Statistics();
                ErrorHandler err = new ErrorHandler();
                SymbolTable symbols = new SymbolTable();
                ManualScanner scanner = new ManualScanner(content, stats, err);

                int totalTokens = 0;
                Token t;
                while ((t = scanner.nextToken()) != null) {
                    // Requirement C: Specific Output Format
                    String tokenStr = String.format("<%s, \"%s\", Line: %d, Col: %d>",
                            t.type, t.lexeme, t.line, t.col);

                    currentFileLog.append(tokenStr).append("\n");
                    symbols.insert(t);
                    totalTokens++;
                }

                // Requirement D: Statistics
                int lineCount = content.split("\n", -1).length;
                currentFileLog.append("\nSTATISTICS:\n");
                currentFileLog.append(String.format("Lines Processed: %d\nTotal Tokens: %d\n", lineCount, totalTokens));

                currentFileLog.append("\n--- SCANNER STATISTICS ---\n");
                for (Token.Type type : Token.Type.values()) {
                    if (type != Token.Type.ERROR) {
                        currentFileLog.append(String.format("%-18s: %d\n", type, stats.getCount(type)));
                    }
                }
                currentFileLog.append("Comments Removed  : ").append(stats.getCommentCount()).append("\n");

                // Requirement E: Symbol Table
                currentFileLog.append(symbols.getTableAsString());

                currentFileLog.append("\n[Errors Found: ").append(err.getCount()).append("]\n");
                currentFileLog.append("\n");

                // Output to console and log
                String finalOutput = currentFileLog.toString();
                System.out.print(finalOutput);
                finalLog.append(finalOutput);

            } catch (Exception e) {
                System.err.println("Error processing " + fileName + ": " + e.getMessage());
            }
        }

        System.out.println("\nAll tests complete. Results saved to TestResults.txt");

        try {
            Files.writeString(Paths.get("TestResults.txt"), finalLog.toString());
        } catch (IOException e) {
            System.err.println("Could not write results file.");
        }
    }
}