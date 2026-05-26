package gr.aueb.cf.eduapp.core.exceptions;

import lombok.Getter;

@Getter
public class AppGenericException extends Exception {

    private final String code;

    public AppGenericException(String message,String code)
    {
        super(message);
        this.code = code;
    }

    public AppGenericException(String code) {
        this.code = code;
    }
}
