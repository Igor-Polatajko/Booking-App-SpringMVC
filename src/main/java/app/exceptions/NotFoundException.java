package app.exceptions;

public class NotFoundException extends RuntimeException {

    private String name;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}