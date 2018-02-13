package main.ru.nsu.ccfit.sogreshilin.calculator;

public class ParseError extends Exception {
    public ParseError() {
        super();
    }

    public ParseError(String message) {
        super(message);
    }

    public ParseError(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseError(Throwable cause) {
        super(cause);
    }
}
