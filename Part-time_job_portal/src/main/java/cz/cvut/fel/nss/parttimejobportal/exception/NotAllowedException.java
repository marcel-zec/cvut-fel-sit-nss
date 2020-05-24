package cz.cvut.fel.nss.parttimejobportal.exception;

public class NotAllowedException extends Exception{
    public NotAllowedException() {
        super("Forbidden operation.");
    }
    public NotAllowedException(String message) {
        super(message);
    }
}
