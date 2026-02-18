%%
%public
%class JFlexScanner
%unicode
%line
%column
%type scanner.Token
%function nextToken
%state STRING
%state CHAR
%state ML_COMMENT

%{
private utils.Statistics stats;
private utils.ErrorHandler errorHandler;

private final java.util.Set<String> keywords = new java.util.HashSet<>(java.util.Arrays.asList(
        "while", "if", "else", "return", "break", "continue",
        "int", "float", "void", "string", "char", "boolean"
));

private java.lang.StringBuilder literalBuffer = new java.lang.StringBuilder();
private int literalStartLine = 1;
private int literalStartCol = 1;
private int commentDepth = 0;

public JFlexScanner(java.io.Reader in, utils.Statistics stats, utils.ErrorHandler errorHandler) {
    this(in);
    this.stats = stats;
    this.errorHandler = errorHandler;
}

private scanner.Token token(scanner.Token.Type type, java.lang.String lexeme) {
    return new scanner.Token(type, lexeme, yyline + 1, yycolumn + 1);
}

private void startLiteral() {
    literalBuffer.setLength(0);
    literalStartLine = yyline + 1;
    literalStartCol = yycolumn + 1;
}

private void appendLiteral(java.lang.String text) {
    literalBuffer.append(text);
}

private scanner.Token errorToken(java.lang.String message) {
    errorHandler.report(literalStartLine, literalStartCol, message);
    return new scanner.Token(scanner.Token.Type.ERROR, literalBuffer.toString(), literalStartLine, literalStartCol);
}
%}

DIGIT      = [0-9]
LETTER     = [A-Za-z]
ID_START   = {LETTER}
ID_PART    = {LETTER}|{DIGIT}|_
SIGN       = [+-]
INT        = {SIGN}?{DIGIT}+
FLOAT      = {SIGN}?{DIGIT}*\.{DIGIT}+
WS         = [ \t\f\r\n]+

%%

<YYINITIAL>{WS}               { /* skip whitespace */ }
<YYINITIAL>"##"[^\n]*         { stats.addComment(); }
<YYINITIAL>"#*"               { stats.addComment(); commentDepth = 1; yybegin(ML_COMMENT); }

<ML_COMMENT>"#*"              { commentDepth++; }
<ML_COMMENT>"*#"              { commentDepth--; if (commentDepth == 0) yybegin(YYINITIAL); }
<ML_COMMENT>[^]               { /* consume all */ }
<ML_COMMENT><<EOF>>           { yybegin(YYINITIAL); return null; }

<YYINITIAL>"\""               { startLiteral(); appendLiteral("\""); yybegin(STRING); }
<STRING>"\\"[^]               { appendLiteral(yytext()); }
<STRING>"\""                  { appendLiteral("\""); yybegin(YYINITIAL); stats.count(scanner.Token.Type.STRING_LITERAL); return new scanner.Token(scanner.Token.Type.STRING_LITERAL, literalBuffer.toString(), literalStartLine, literalStartCol); }
<STRING>\n                    { yybegin(YYINITIAL); yypushback(1); return errorToken("Unterminated string"); }
<STRING><<EOF>>               { yybegin(YYINITIAL); return errorToken("Unterminated string"); }
<STRING>[^]                   { appendLiteral(yytext()); }

<YYINITIAL>"'"                { startLiteral(); appendLiteral("'"); yybegin(CHAR); }
<CHAR>"\\"[^]                 { appendLiteral(yytext()); }
<CHAR>"'"                     {
                                appendLiteral("'");
                                yybegin(YYINITIAL);
                                java.lang.String lexeme = literalBuffer.toString();
                                boolean isEscape = lexeme.length() == 4 && lexeme.charAt(1) == '\\';
                                boolean isNormal = lexeme.length() == 3;
                                if (isNormal || isEscape) {
                                    stats.count(scanner.Token.Type.CHAR_LITERAL);
                                    return new scanner.Token(scanner.Token.Type.CHAR_LITERAL, lexeme, literalStartLine, literalStartCol);
                                }
                                return errorToken("Invalid char literal length");
                              }
<CHAR>\n                      { yybegin(YYINITIAL); yypushback(1); return errorToken("Invalid char literal"); }
<CHAR><<EOF>>                 { yybegin(YYINITIAL); return errorToken("Invalid char literal"); }
<CHAR>[^]                     { appendLiteral(yytext()); }

<YYINITIAL>{FLOAT}            {
                                java.lang.String lexeme = yytext();
                                int dot = lexeme.indexOf('.');
                                int decimals = lexeme.length() - dot - 1;
                                if (decimals > 6) {
                                    errorHandler.report(yyline + 1, yycolumn + 1, "Float decimal limit (6) exceeded");
                                    lexeme = lexeme.substring(0, dot + 1 + 6);
                                }
                                stats.count(scanner.Token.Type.FLOAT_LITERAL);
                                return token(scanner.Token.Type.FLOAT_LITERAL, lexeme);
                              }
<YYINITIAL>{INT}              { stats.count(scanner.Token.Type.INTEGER_LITERAL); return token(scanner.Token.Type.INTEGER_LITERAL, yytext()); }

<YYINITIAL>{ID_START}{ID_PART}* {
                                  java.lang.String lexeme = yytext();
                                  char first = lexeme.charAt(0);
                                  if ("true".equals(lexeme) || "false".equals(lexeme)) {
                                      stats.count(scanner.Token.Type.BOOLEAN_LITERAL);
                                      return token(scanner.Token.Type.BOOLEAN_LITERAL, lexeme);
                                  }
                                  if (java.lang.Character.isLowerCase(first) && keywords.contains(lexeme)) {
                                      stats.count(scanner.Token.Type.KEYWORD);
                                      return token(scanner.Token.Type.KEYWORD, lexeme);
                                  }
                                  if (lexeme.length() <= 31) {
                                      stats.count(scanner.Token.Type.IDENTIFIER);
                                      return token(scanner.Token.Type.IDENTIFIER, lexeme);
                                  }
                                  errorHandler.report(yyline + 1, yycolumn + 1, "Identifier length > 31: " + lexeme);
                                  return token(scanner.Token.Type.ERROR, lexeme);
                                }

<YYINITIAL>"=="|"!="|"<="|">="|"+="|"-="|"*="|"/=" { stats.count(scanner.Token.Type.ASSIGNMENT_OP); return token(scanner.Token.Type.ASSIGNMENT_OP, yytext()); }
<YYINITIAL>"&&"|"||"          { stats.count(scanner.Token.Type.LOGICAL_OP); return token(scanner.Token.Type.LOGICAL_OP, yytext()); }
<YYINITIAL>"+"|"-"|"*"|"/"|"%" { stats.count(scanner.Token.Type.ARITHMETIC_OP); return token(scanner.Token.Type.ARITHMETIC_OP, yytext()); }
<YYINITIAL>"="                { stats.count(scanner.Token.Type.ASSIGNMENT_OP); return token(scanner.Token.Type.ASSIGNMENT_OP, yytext()); }
<YYINITIAL>"<"|">"|"!"        { stats.count(scanner.Token.Type.RELATIONAL_OP); return token(scanner.Token.Type.RELATIONAL_OP, yytext()); }
<YYINITIAL>"("|")"|"{"|"}"|"["|"]"|","|";"|":"|"." { stats.count(scanner.Token.Type.PUNCTUATOR); return token(scanner.Token.Type.PUNCTUATOR, yytext()); }

<YYINITIAL><<EOF>>            { return null; }
<YYINITIAL>[^]                { return token(scanner.Token.Type.ERROR, yytext()); }
