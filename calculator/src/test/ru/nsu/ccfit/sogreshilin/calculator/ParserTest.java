package test.ru.nsu.ccfit.sogreshilin.calculator;

import main.ru.nsu.ccfit.sogreshilin.calculator.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class ParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseExpressionSingleTerm() throws LexicalError, ParseError {
        assertExpressionEquals(42, "42");
    }

    @Test
    public void parseExpressionSimpleTermsWithWhitespaces() throws LexicalError, ParseError {
        String expression = "      42\n\t +        1   -0 +       2";
        int expectedValue = 45;
        assertExpressionEquals(expectedValue, expression);
    }

    @Test
    public void parseExpressionNumberOutOfRange() throws LexicalError, ParseError {
        thrown.expect(LexicalError.class);
        thrown.expectMessage("Number is out of range");

        Reader reader = new StringReader(Long.MAX_VALUE + " + 0");
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        parser.calculate();
    }

    @Test
    public void parseExpressionExtraSymbols() throws LexicalError, ParseError {
        thrown.expect(ParseError.class);
        thrown.expectMessage("Invalid expression. Found extra symbols in the end");

        Reader reader = new StringReader("42 + 1 1");
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        parser.calculate();
    }

    @Test
    public void parseExpressionNoClosingBrace() throws LexicalError, ParseError {
        thrown.expect(ParseError.class);
        thrown.expectMessage("Invalid expression at: ");

        Reader reader = new StringReader("(42 + 1");
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        parser.calculate();
    }

    @Test
    public void parseSimpleTerm() throws LexicalError, ParseError {
        String expression = "42 * (0 / 1)";
        int expectedValue = 0;
        assertExpressionEquals(expectedValue, expression);
    }

    @Test
    public void parseTermDivisionByZero() throws LexicalError, ParseError {
        Reader reader = new StringReader("42 * 0 / 1 / 0");
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        thrown.expect(ArithmeticException.class);
        thrown.expectMessage("/ by zero");
        parser.calculate();
    }

    @Test
    public void parseSimpleFactor() throws LexicalError, ParseError {
        String expression = "42 ^ (1 + 1) + 1";
        int expectedValue = 42 * 42 + 1;
        assertExpressionEquals(expectedValue, expression);
    }

    @Test
    public void parseSimplePower() throws LexicalError, ParseError {
        String expression = "42 ^ -(1 + 1) + 1";
        int expectedValue = 1;
        assertExpressionEquals(expectedValue, expression);
    }

    @Test
    public void calculateExpression() throws LexicalError, ParseError {
        String expression = "(42 - (111 - 100) * 2) ^ ((2 + 1 * (3 + 2)) / (100 / 33))";
        int expectedValue = 400;
        assertExpressionEquals(expectedValue, expression);
    }

    @Test
    public void calculateNeg2Sq() throws LexicalError, ParseError {
        String expression = "-2^2";
        int expectedValue = -4;
        assertExpressionEquals(expectedValue, expression);
    }

    @Test
    public void calculate2MulNeg2() throws LexicalError, ParseError {
        assertExpressionEquals(-4, "2 * - 2");
    }

    private void assertExpressionEquals(int expectedValue, String expression) throws LexicalError, ParseError {
        int calculatedValue = new Parser(new Lexer(new StringReader(expression))).calculate();
        assertEquals(expectedValue, calculatedValue);
    }
}