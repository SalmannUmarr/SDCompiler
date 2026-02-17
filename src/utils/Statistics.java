package utils;

/**
 * Tracks token counts and comments removed during lexical analysis.
 * Contains counters for all 11 token categories.
 */
public class Statistics {
    private int keywordCount;
    private int identifierCount;
    private int integerLiteralCount;
    private int floatLiteralCount;
    private int stringLiteralCount;
    private int operatorCount;
    private int delimiterCount;
    private int commentCount;
    private int category9Count;
    private int category10Count;
    private int category11Count;

    public int getKeywordCount() { return keywordCount; }
    public void incrementKeywordCount() { keywordCount++; }

    public int getIdentifierCount() { return identifierCount; }
    public void incrementIdentifierCount() { identifierCount++; }

    public int getIntegerLiteralCount() { return integerLiteralCount; }
    public void incrementIntegerLiteralCount() { integerLiteralCount++; }

    public int getFloatLiteralCount() { return floatLiteralCount; }
    public void incrementFloatLiteralCount() { floatLiteralCount++; }

    public int getStringLiteralCount() { return stringLiteralCount; }
    public void incrementStringLiteralCount() { stringLiteralCount++; }

    public int getOperatorCount() { return operatorCount; }
    public void incrementOperatorCount() { operatorCount++; }

    public int getDelimiterCount() { return delimiterCount; }
    public void incrementDelimiterCount() { delimiterCount++; }

    public int getCommentCount() { return commentCount; }
    public void incrementCommentCount() { commentCount++; }

    public int getCategory9Count() { return category9Count; }
    public void incrementCategory9Count() { category9Count++; }

    public int getCategory10Count() { return category10Count; }
    public void incrementCategory10Count() { category10Count++; }

    public int getCategory11Count() { return category11Count; }
    public void incrementCategory11Count() { category11Count++; }
}
