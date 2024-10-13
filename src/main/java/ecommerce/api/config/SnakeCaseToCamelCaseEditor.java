package ecommerce.api.config;

import java.beans.PropertyEditorSupport;

public class SnakeCaseToCamelCaseEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        if (text == null) {
            setValue(null);
        } else {
            // Convert snake_case to camelCase
            String camelCaseText = convertSnakeToCamel(text);
            setValue(camelCaseText);
        }
    }

    private String convertSnakeToCamel(String text) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : text.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}