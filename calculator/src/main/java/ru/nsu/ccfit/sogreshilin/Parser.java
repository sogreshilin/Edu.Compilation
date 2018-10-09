package main.ru.nsu.ccfit.sogreshilin.calculator;

/**
 * Parser class parses and calculates values.
 * Throws ParserError if happen during parsing.
 */
public class Parser {
    private Lexer lexer;
    private Lexeme current;

    public Parser(Lexer lexer) throws LexicalError {
        this.lexer = lexer;

        current = lexer.getLexeme();
    }

    private int parseExpression() throws LexicalError, ParseError {
        int temp = parseTerm();
        while (Lexeme.isAdd(current) || Lexeme.isSub(current)) {
            int sign = Lexeme.isAdd(current) ? 1 : -1;
            current = lexer.getLexeme();
            int value = parseTerm();
            temp += sign * value;
        }
        return temp;
    }

    private int parseTerm() throws LexicalError, ParseError {
        int temp = parseFactor();
        while (Lexeme.isMul(current) || Lexeme.isDiv(current)) {
            Lexeme operation = current;
            current = lexer.getLexeme();
            int value = parseFactor();
            if (Lexeme.isMul(operation)) {
                temp *= value;
            } else if (Lexeme.isDiv(operation)) {
                temp /= value;
            }
        }
        return temp;
    }

    private int pow(int a, int b) {
        if (b < 0) {
            return (a == 1) ? 1 : 0;
        }
        if (b == 0) {
            return 1;
        }
        if (b == 1) {
            return a;
        }
        if (b % 2 == 0) {
            return pow(a * a, b / 2);
        } else {
            return a * pow(a * a, b / 2);
        }
    }

    private int parseFactor() throws LexicalError, ParseError {
        int temp = parsePower();
        if (!Lexeme.isPow(current)) {
            return temp;
        }
        current = lexer.getLexeme();
        int value = parseFactor();
        return pow(temp, value);
    }

    private int parsePower() throws LexicalError, ParseError {
        if (Lexeme.isSub(current)) {
            current = lexer.getLexeme();
            return -parseFactor();
        }
        return parseAtom();
    }

    private int parseAtom() throws LexicalError, ParseError {
        if (Lexeme.isNumber(current)) {
            int number = Integer.parseInt(current.getValue());
            current = lexer.getLexeme();
            return number;
        }

        if (Lexeme.isOpenBrace(current)) {
            current = lexer.getLexeme();
            int temp = parseExpression();
            if (Lexeme.isCloseBrace(current)) {
                current = lexer.getLexeme();
                return temp;
            }
        }

        throw new ParseError("Invalid expression at: " + current.getValue());
    }

    public int calculate() throws LexicalError, ParseError {
        int temp = parseExpression();
        if (Lexeme.isEOF(current)) {
            return temp;
        }
        throw new ParseError("Invalid expression. Found extra symbols in the end");
    }
}
