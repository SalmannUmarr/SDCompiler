package tables;

import scanner.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores identifiers and literals (as tokens) encountered during lexical analysis.
 */
public class SymbolTable {
    private final Map<String, Token> entries = new HashMap<>();

    public void put(String key, Token token) {
        entries.put(key, token);
    }

    public Token get(String key) {
        return entries.get(key);
    }

    public boolean contains(String key) {
        return entries.containsKey(key);
    }
}
