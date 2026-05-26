package gr.aueb.cf.eduapp.core.exceptions;

public class EntityInvalidArgumentException extends AppGenericException{

    private static final String DEFAULT_CODE = "InvalidArgument";

    public EntityInvalidArgumentException(String code, String massage){
        super(code + DEFAULT_CODE,massage );
    }
}
