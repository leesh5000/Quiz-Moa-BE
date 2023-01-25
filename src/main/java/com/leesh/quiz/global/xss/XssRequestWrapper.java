package com.leesh.quiz.global.xss;

import com.fasterxml.jackson.core.io.SerializedString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.text.StringEscapeUtils;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    protected XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String[] getParameterValues(String parameter) {

        String[] values = super.getParameterValues(parameter);
        if (values == null)  {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
         return new SerializedString(StringEscapeUtils.escapeHtml4(value)).getValue();
    }

}
