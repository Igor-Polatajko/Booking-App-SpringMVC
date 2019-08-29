package app.exceptions;

public class DbException extends RuntimeException {

    private String name;

    public DbException(String message) {
        super(message);
    }

    public DbException(String message, String name) {
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
