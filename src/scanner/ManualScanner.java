package scanner;

import java.util.*;
import utils.*;
import tables.*;

public class ManualScanner {
    private final String input;
    private int pos = 0;
    private int line = 1;
    private int col = 1;
    private final Statistics stats;
    private final ErrorHandler errorHandler;

    private final Set<String> keywords = new HashSet<>(Arrays.asList(
            "while", "if", "else", "return", "break", "continue",
            "int", "float", "void", "string", "char", "boolean"
    ));

    public ManualScanner(String input, Statistics stats, ErrorHandler errorHandler) {
        this.input = input;
        this.stats = stats;
        this.errorHandler = errorHandler;
    }

    private char peek() { return pos < input.length() ? input.charAt(pos) : '\0'; }
    private char peekNext() { return (pos + 1) < input.length() ? input.charAt(pos + 1) : '\0'; }

    private char advance() {
        char current = peek();
        pos++;
        if (current == '\n') { line++; col = 1; }
        else { col++; }
        return current;
    }

    public Token nextToken() {
        skipIgnorable();
        if (peek() == '\0') return null;

        int startCol = col;
        char c = peek();

        if (Character.isLetter(c)) return scanWord();
        if (Character.isDigit(c) || (isSign(c) && Character.isDigit(peekNext()))) return scanNumber();
        if (c == '"') return scanString();
        if (c == '\'') return scanChar();

        return scanSymbols();
    }

    private void skipIgnorable() {
        while (true) {
            char c = peek();
            if (Character.isWhitespace(c)) advance();
            else if (c == '#' && peekNext() == '#') {
                while (peek() != '\n' && peek() != '\0') advance();
                stats.addComment();
            } else if (c == '#' && peekNext() == '*') {
                advance(); advance();
                int depth = 1;
                while (depth > 0 && peek() != '\0') {
                    if (peek() == '#' && peekNext() == '*') {
                        advance(); advance();
                        depth++;
                    } else if (peek() == '*' && peekNext() == '#') {
                        advance(); advance();
                        depth--;
                    } else {
                        advance();
                    }
                }
                stats.addComment();
            } else break;
        }
    }

    private Token scanWord() {
        StringBuilder sb = new StringBuilder();
        int startCol = col;
        char first = peek();

        while (Character.isLetterOrDigit(peek()) || peek() == '_') sb.append(advance());
        String lexeme = sb.toString();

        if (lexeme.equals("true") || lexeme.equals("false")) {
            stats.count(Token.Type.BOOLEAN_LITERAL);
            return new Token(Token.Type.BOOLEAN_LITERAL, lexeme, line, startCol);
        }

        if (Character.isLowerCase(first)) {
            if (keywords.contains(lexeme)) {
                stats.count(Token.Type.KEYWORD);
                return new Token(Token.Type.KEYWORD, lexeme, line, startCol);
            }
        }

        if (Character.isUpperCase(first) || Character.isLowerCase(first)) {
            if (lexeme.length() <= 31) {
                stats.count(Token.Type.IDENTIFIER);
                return new Token(Token.Type.IDENTIFIER, lexeme, line, startCol);
            } else {
                errorHandler.report(line, startCol, "Identifier length > 31: " + lexeme);
                return new Token(Token.Type.ERROR, lexeme, line, startCol);
            }
        }
        return new Token(Token.Type.ERROR, lexeme, line, startCol);
    }

    private Token scanNumber() {
        StringBuilder sb = new StringBuilder();
        int startCol = col;

        if (isSign(peek())) sb.append(advance());
        while (Character.isDigit(peek())) sb.append(advance());

        if (peek() == '.') {
            sb.append(advance());
            int count = 0;
            boolean errorReported = false;
            while (Character.isDigit(peek())) {
                if (count < 6) {
                    sb.append(advance());
                } else {
                    if (!errorReported) {
                        errorHandler.report(line, col, "Float decimal limit (6) exceeded");
                        errorReported = true;
                    }
                    advance();
                }
                count++;
            }
            stats.count(Token.Type.FLOAT_LITERAL);
            return new Token(Token.Type.FLOAT_LITERAL, sb.toString(), line, startCol);
        }

        stats.count(Token.Type.INTEGER_LITERAL);
        return new Token(Token.Type.INTEGER_LITERAL, sb.toString(), line, startCol);
    }

    private Token scanString() {
        StringBuilder sb = new StringBuilder();
        int startCol = col;
        sb.append(advance());
        while (peek() != '"' && peek() != '\n' && peek() != '\0') {
            if (peek() == '\\') {
                sb.append(advance());
                if (peek() != '\0') sb.append(advance());
            } else {
                sb.append(advance());
            }
        }
        if (peek() == '"') {
            sb.append(advance());
            stats.count(Token.Type.STRING_LITERAL);
            return new Token(Token.Type.STRING_LITERAL, sb.toString(), line, startCol);
        }
        errorHandler.report(line, startCol, "Unterminated string");
        return new Token(Token.Type.ERROR, sb.toString(), line, startCol);
    }

    private Token scanChar() {
        StringBuilder sb = new StringBuilder();
        int startCol = col;
        sb.append(advance());

        while (peek() != '\'' && peek() != '\n' && peek() != '\0') {
            if (peek() == '\\') { sb.append(advance()); sb.append(advance()); }
            else sb.append(advance());
        }

        if (peek() == '\'') {
            sb.append(advance());
            String lexeme = sb.toString();
            // Validate length (must be 'c' or '\c')
            boolean isEscape = lexeme.length() == 4 && lexeme.charAt(1) == '\\';
            boolean isNormal = lexeme.length() == 3;

            if (isNormal || isEscape) {
                stats.count(Token.Type.CHAR_LITERAL);
                return new Token(Token.Type.CHAR_LITERAL, lexeme, line, startCol);
            } else {
                errorHandler.report(line, startCol, "Invalid char literal length");
                return new Token(Token.Type.ERROR, lexeme, line, startCol);
            }
        }
        errorHandler.report(line, startCol, "Invalid char literal");
        return new Token(Token.Type.ERROR, sb.toString(), line, startCol);
    }

    private Token scanSymbols() {
        int startCol = col;
        char c = advance();
        String lexeme = String.valueOf(c);
        char next = peek();

        if ((c == '=' || c == '!' || c == '<' || c == '>' || c == '+' || c == '-' || c == '*' || c == '/') && next == '=') {
            lexeme += advance();
            stats.count(Token.Type.ASSIGNMENT_OP);
            return new Token(Token.Type.ASSIGNMENT_OP, lexeme, line, startCol);
        }

        if (c == '&' && next == '&') { lexeme += advance(); stats.count(Token.Type.LOGICAL_OP); return new Token(Token.Type.LOGICAL_OP, lexeme, line, startCol); }
        if (c == '|' && next == '|') { lexeme += advance(); stats.count(Token.Type.LOGICAL_OP); return new Token(Token.Type.LOGICAL_OP, lexeme, line, startCol); }

        if ("+-*/%".contains(lexeme)) { stats.count(Token.Type.ARITHMETIC_OP); return new Token(Token.Type.ARITHMETIC_OP, lexeme, line, startCol); }
        if ("=><!".contains(lexeme)) {
            if (lexeme.equals("=")) { stats.count(Token.Type.ASSIGNMENT_OP); return new Token(Token.Type.ASSIGNMENT_OP, lexeme, line, startCol); }
            stats.count(Token.Type.RELATIONAL_OP);
            return new Token(Token.Type.RELATIONAL_OP, lexeme, line, startCol);
        }
        if ("(){}[],;:.".contains(lexeme)) { stats.count(Token.Type.PUNCTUATOR); return new Token(Token.Type.PUNCTUATOR, lexeme, line, startCol); }

        return new Token(Token.Type.ERROR, lexeme, line, startCol);
    }

    private boolean isSign(char c) { return c == '+' || c == '-'; }
}