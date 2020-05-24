package cz.cvut.fel.nss.parttimejobportal.exception;

public class BadDateException extends Exception {

    public BadDateException() {
        super("Incorrect date.");
    }

    public BadDateException(String message) {
        super(message);
    }


}
