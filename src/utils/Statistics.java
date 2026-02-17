package utils;

import scanner.Token;
import java.util.*;


 /* Tracks token counts per type, total tokens, and comments removed. */

public class Statistics {
    private final Map<Token.Type, Integer> counts = new EnumMap<>(Token.Type.class);
    private int commentsRemoved = 0;

    public Statistics() {
        for (Token.Type type : Token.Type.values()) {
            counts.put(type, 0);
        }
    }

    public void count(Token.Type type) {
        counts.put(type, counts.get(type) + 1);
    }

    public void addComment() {
        commentsRemoved++;
    }

    public int getCount(Token.Type type) {
        return counts.getOrDefault(type, 0);
    }

    public int getCommentCount() {
        return commentsRemoved;
    }

    public void display() {
        System.out.println("\n--- SCANNER STATISTICS ---");
        for (Map.Entry<Token.Type, Integer> entry : counts.entrySet()) {
            if (entry.getKey() != Token.Type.ERROR) {
                System.out.printf("%-18s: %d\n", entry.getKey(), entry.getValue());
            }
        }
        System.out.println("Comments Removed  : " + commentsRemoved);
    }
}