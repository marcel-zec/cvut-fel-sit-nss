package cz.cvut.fel.ear.rsp.travelandwork.exception;

public class AlreadyLoginException extends Exception {
    public AlreadyLoginException() {
        super("You are already login.");
    }
}
