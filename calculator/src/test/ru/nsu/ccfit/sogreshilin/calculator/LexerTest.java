package test.ru.nsu.ccfit.sogreshilin.calculator;

import main.ru.nsu.ccfit.sogreshilin.calculator.Lexeme;
import main.ru.nsu.ccfit.sogreshilin.calculator.LexemeType;
import main.ru.nsu.ccfit.sogreshilin.calculator.Lexer;
import main.ru.nsu.ccfit.sogreshilin.calculator.LexicalError;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class LexerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getLexemeShortNumber() throws LexicalError {
        Reader reader = new StringReader("42");
        Lexeme[] lexemes = new Lexeme[]{
                new Lexeme(LexemeType.NUM, "42"),
                new Lexeme(LexemeType.EOF, "")
        };
        Lexer lexer = new Lexer(reader);

        for (Lexeme lexeme : lexemes) {
            assertEquals(lexeme, lexer.getLexeme());
        }
    }

    // todo: how to check -INT_MIN value
    @Test
    public void getLexemeVeryLongNumber() throws LexicalError {
        Reader reader = new StringReader(String.valueOf(Long.MAX_VALUE));

        thrown.expect(LexicalError.class);
        thrown.expectMessage("Number is out of range");

        new Lexer(reader).getLexeme();
    }

    @Test
    public void getLexemeInvalidSymbol() throws LexicalError {
        Reader reader = new StringReader("hello world");

        thrown.expect(LexicalError.class);
        thrown.expectMessage("Unknown symbol");

        new Lexer(reader).getLexeme();
    }

    @Test
    public void getLexemeCallTwiceAfterEOF() throws LexicalError {
        Reader reader = new StringReader("");

        thrown.expect(NoSuchElementException.class);

        Lexer lexer = new Lexer(reader);
        assertEquals(new Lexeme(LexemeType.EOF, ""), lexer.getLexeme());
        lexer.getLexeme();
    }

    @Test
    public void getLexemeNumberInBraces() throws LexicalError {
        assertExpressionEquals("(42)",
            new Lexeme(LexemeType.OBR, "("),
            new Lexeme(LexemeType.NUM, "42"),
            new Lexeme(LexemeType.CBR, ")"),
            new Lexeme(LexemeType.EOF, "")
        );
    }

    @Test
    public void getLexemeNegativeAtom() throws LexicalError {
        assertExpressionEquals("(-(42))",
                new Lexeme(LexemeType.OBR, "("),
                new Lexeme(LexemeType.SUB, "-"),
                new Lexeme(LexemeType.OBR, "("),
                new Lexeme(LexemeType.NUM, "42"),
                new Lexeme(LexemeType.CBR, ")"),
                new Lexeme(LexemeType.CBR, ")"),
                new Lexeme(LexemeType.EOF, "")
        );
    }

    @Test
    public void getLexemeFactor() throws LexicalError {
        assertExpressionEquals("(-(42)^0)^0",
            new Lexeme(LexemeType.OBR, "("),
            new Lexeme(LexemeType.SUB, "-"),
            new Lexeme(LexemeType.OBR, "("),
            new Lexeme(LexemeType.NUM, "42"),
            new Lexeme(LexemeType.CBR, ")"),
            new Lexeme(LexemeType.POW, "^"),
            new Lexeme(LexemeType.NUM, "0"),
            new Lexeme(LexemeType.CBR, ")"),
            new Lexeme(LexemeType.POW, "^"),
            new Lexeme(LexemeType.NUM, "0"),
            new Lexeme(LexemeType.EOF, "")
        );
    }

    @Test
    public void getLexemeTerm() throws LexicalError {
        assertExpressionEquals("-42 / (1 * 9) ^ (3 / 0 *)",
            new Lexeme(LexemeType.SUB, "-"),
            new Lexeme(LexemeType.NUM, "42"),
            new Lexeme(LexemeType.DIV, "/"),
            new Lexeme(LexemeType.OBR, "("),
            new Lexeme(LexemeType.NUM, "1"),
            new Lexeme(LexemeType.MUL, "*"),
            new Lexeme(LexemeType.NUM, "9"),
            new Lexeme(LexemeType.CBR, ")"),
            new Lexeme(LexemeType.POW, "^"),
            new Lexeme(LexemeType.OBR, "("),
            new Lexeme(LexemeType.NUM, "3"),
            new Lexeme(LexemeType.DIV, "/"),
            new Lexeme(LexemeType.NUM, "0"),
            new Lexeme(LexemeType.MUL, "*"),
            new Lexeme(LexemeType.CBR, ")"),
            new Lexeme(LexemeType.EOF, "")
        );
    }

    @Test
    public void getLexemeExpression() throws LexicalError {
        assertExpressionEquals("-42 - (-42) + 42",
            new Lexeme(LexemeType.SUB, "-"),
            new Lexeme(LexemeType.NUM, "42"),
            new Lexeme(LexemeType.SUB, "-"),
            new Lexeme(LexemeType.OBR, "("),
            new Lexeme(LexemeType.SUB, "-"),
            new Lexeme(LexemeType.NUM, "42"),
            new Lexeme(LexemeType.CBR, ")"),
            new Lexeme(LexemeType.ADD, "+"),
            new Lexeme(LexemeType.NUM, "42"),
            new Lexeme(LexemeType.EOF, "")
        );
    }

    private void assertExpressionEquals(String expression, Lexeme... lexemes) throws LexicalError {
        Lexer lexer = new Lexer(new StringReader(expression));
        for (Lexeme lexeme : lexemes) {
            assertEquals(lexeme, lexer.getLexeme());
        }
    }


}