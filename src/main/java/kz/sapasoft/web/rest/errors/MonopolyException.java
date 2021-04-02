package kz.sapasoft.web.rest.errors;

public class MonopolyException extends RuntimeException {

    private String errorKey;

    public MonopolyException(String errorKey) {
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return this.errorKey;
    }
}
