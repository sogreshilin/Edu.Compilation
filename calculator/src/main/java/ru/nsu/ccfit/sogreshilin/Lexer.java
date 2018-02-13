package main.ru.nsu.ccfit.sogreshilin.calculator;

import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;

public class Lexer {
    private Reader reader;
    private int current;
    private boolean reachedEOF = false;

    public Lexer(Reader reader) throws LexicalError {
        this.reader = reader;
        try {
            current = reader.read();
        } catch (IOException e) {
            throw new LexicalError(e);
        }
    }

    public Lexeme getLexeme() throws LexicalError {
        try {
            while (Character.isWhitespace(current)) {
                current = reader.read();
            }
            StringBuilder currentToken = new StringBuilder();

            Lexeme lexeme;
            switch (current) {
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                    while (Character.isDigit(current)) {
                        currentToken.appendCodePoint(current);
                        current = reader.read();
                    }
                    String number = currentToken.toString();
                    if (!isInRange(number)) {
                        throw new LexicalError("Number is out of range");
                    }
                    return new Lexeme(LexemeType.NUM, number);

                case  -1:
                    if (reachedEOF) {
                        throw new NoSuchElementException();
                    }
                    reachedEOF = true;
                    lexeme = new Lexeme(LexemeType.EOF, "");
                    break;

                case '+': lexeme = new Lexeme(LexemeType.ADD, (char) current); break;
                case '-': lexeme = new Lexeme(LexemeType.SUB, (char) current); break;
                case '*': lexeme = new Lexeme(LexemeType.MUL, (char) current); break;
                case '/': lexeme = new Lexeme(LexemeType.DIV, (char) current); break;
                case '^': lexeme = new Lexeme(LexemeType.POW, (char) current); break;
                case '(': lexeme = new Lexeme(LexemeType.OBR, (char) current); break;
                case ')': lexeme = new Lexeme(LexemeType.CBR, (char) current); break;
                default : throw new LexicalError("Unknown symbol: " + (char) current);
            }
            current = reader.read();
            return lexeme;

        } catch (IOException e) {
            throw new LexicalError(e);
        }
    }

    // -INT_MIN
    private boolean isInRange(String number) {
        return Long.parseLong(number) <= Integer.MAX_VALUE;
    }

    public static void main() {

    }
}
