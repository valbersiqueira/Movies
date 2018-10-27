package valber.com.br.movies.utils;

public class ConvertDataException extends Exception {

private String message;

    public ConvertDataException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
