package morsinator.text;

import java.io.Reader;
import java.io.IOException;

public class ReaderTextPos {
    private Reader reader;
    private TextPosition textPos;

    public ReaderTextPos(Reader reader) {
        this.reader = reader;
        textPos = new TextPosition();
    }

    public int read() throws IOException {
        int c = reader.read();

        if(c == '\n') {
            textPos.lineFeed();
        } else {
            textPos.newChar();
        }

        return c;
    }

    public TextPosition getTextPos() {
        return textPos;
    }
}