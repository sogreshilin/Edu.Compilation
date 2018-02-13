package main.ru.nsu.ccfit.sogreshilin.calculator;

public class LexicalError extends Exception {
    public LexicalError() {
        super();
    }

    public LexicalError(String message) {
        super(message);
    }

    public LexicalError(String message, Throwable cause) {
        super(message, cause);
    }

    public LexicalError(Throwable cause) {
        super(cause);
    }
}
