package com.leesh.quiz.global.xss;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

@Component
public class HtmlCharacterEscapes extends CharacterEscapes {

    private final int[] asciiEscapes;

    private HtmlCharacterEscapes() {
        // XSS 방지 처리할 특수 문자 지정
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {

        SerializedString serializedString;
        char charAt = (char) ch;

        // EMOJI 지원
        if (Character.isHighSurrogate(charAt) || Character.isLowSurrogate(charAt)) {
            String str = "\\u" + String.format("%04x", ch);
            serializedString = new SerializedString(str);
        } else {
            serializedString = new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString(charAt)));
        }

        return serializedString;
    }

}
