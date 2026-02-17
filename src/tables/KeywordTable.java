package tables;

import java.util.HashSet;
import java.util.Set;

/**
 * Stores reserved words of the language.
 */
public class KeywordTable {
    private final Set<String> keywords = new HashSet<>();

    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    public boolean isKeyword(String word) {
        return keywords.contains(word);
    }
}
