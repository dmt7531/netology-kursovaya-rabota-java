package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {
    public static final char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        char convert = 0;
        return symbols[color / 32];
    }
}