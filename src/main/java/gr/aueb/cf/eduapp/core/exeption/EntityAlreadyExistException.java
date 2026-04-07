package gr.aueb.cf.eduapp.core.exeption;

public class EntityAlreadyExistException extends AppGenericException {

    private static final String DEFAULT_CODE = "AlreadyExist";

    public EntityAlreadyExistException(String code,String message) {
        super(code + DEFAULT_CODE,message);
    }
}
