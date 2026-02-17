package tables;

import java.util.*;
import scanner.Token;

/* Stores identifier name, type, first occurrence, frequency */

public class SymbolTable {
    private static class SymbolEntry {
        Token.Type type;
        int firstLine;
        int frequency;

        SymbolEntry(Token.Type type, int line) {
            this.type = type;
            this.firstLine = line;
            this.frequency = 1;
        }

        void increment() {
            this.frequency++;
        }
    }

    private final Map<String, SymbolEntry> table = new LinkedHashMap<>();

    public void insert(Token token) {
        // Requirement: Store Identifiers and Literals
        if (token.type == Token.Type.IDENTIFIER || token.type.name().contains("LITERAL")) {
            if (table.containsKey(token.lexeme)) {
                table.get(token.lexeme).increment();
            } else {
                table.put(token.lexeme, new SymbolEntry(token.type, token.line));
            }
        }
    }

    public String getTableAsString() {
        if (table.isEmpty()) return "Table is empty.\n";

        StringBuilder sb = new StringBuilder();
        sb.append("\nSYMBOL TABLE\n");
        sb.append("-".repeat(70)).append("\n");
        sb.append(String.format("%-25s | %-18s | %-8s | %-10s\n",
                "Lexeme/Name", "Type", "Line 1st", "Freq"));
        sb.append("-".repeat(70)).append("\n");

        for (Map.Entry<String, SymbolEntry> entry : table.entrySet()) {
            SymbolEntry data = entry.getValue();
            sb.append(String.format("%-25s | %-18s | %-8d | %-10d\n",
                    entry.getKey(), data.type, data.firstLine, data.frequency));
        }
        sb.append("-".repeat(70)).append("\n");
        return sb.toString();
    }

    public void display() {
        System.out.print(getTableAsString());
    }
}