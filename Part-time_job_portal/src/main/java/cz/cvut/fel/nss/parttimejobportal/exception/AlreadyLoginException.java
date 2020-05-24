package cz.cvut.fel.nss.parttimejobportal.exception;

public class AlreadyLoginException extends Exception {
    public AlreadyLoginException() {
        super("You are already login.");
    }
}
