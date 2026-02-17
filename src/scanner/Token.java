package scanner;

public class Token {
    public enum Type {
        KEYWORD, IDENTIFIER, INTEGER_LITERAL, FLOAT_LITERAL,
        STRING_LITERAL, CHAR_LITERAL, BOOLEAN_LITERAL,
        ARITHMETIC_OP, RELATIONAL_OP, LOGICAL_OP, ASSIGNMENT_OP,
        PUNCTUATOR, COMMENT, ERROR
    }

    public Type type;
    public String lexeme;
    public int line;
    public int col;

    public Token(Type type, String lexeme, int line, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.col = col;
    }

    @Override
    public String toString() {
        return String.format("Line %-3d | Col %-3d | %-18s | %s", line, col, type, lexeme);
    }
}