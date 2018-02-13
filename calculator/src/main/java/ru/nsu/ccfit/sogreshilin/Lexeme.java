package main.ru.nsu.ccfit.sogreshilin.calculator;

import java.util.Objects;

public class Lexeme {
    private LexemeType type;
    private String value;

    public Lexeme(LexemeType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Lexeme(LexemeType type, char value) {
        this.type = type;
        this.value = "" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lexeme lexeme = (Lexeme) o;
        return type == lexeme.type &&
                Objects.equals(value, lexeme.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return "Lexeme{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }

    public LexemeType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public static boolean isAdd(Lexeme lexeme) {
        return lexeme.getType() == LexemeType.ADD;
    }

    public static boolean isSub(Lexeme lexeme) {
        return lexeme.getType() == LexemeType.SUB;
    }

    public static boolean isMul(Lexeme lexeme) {
        return lexeme.getType() == LexemeType.MUL;
    }

    public static boolean isDiv(Lexeme lexeme) {
        return lexeme.getType() == LexemeType.DIV;
    }

    public static boolean isPow(Lexeme lexeme) {
        return lexeme.getType() == LexemeType.POW;
    }

    public static boolean isOpenBrace(Lexeme lexeme) {
        return lexeme.getType() == LexemeType.OBR;
    }

    public static boolean isCloseBrace(Lexeme lexeme) {
        return lexeme.getType() == LexemeType.CBR;
    }

    public static boolean isNumber(Lexeme lexeme) {
        return lexeme.getType() == LexemeType.NUM;
    }

    public static boolean isEOF(Lexeme lexeme) {
        return lexeme.getType() == LexemeType.EOF;
    }
}
