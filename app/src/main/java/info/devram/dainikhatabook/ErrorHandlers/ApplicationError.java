package info.devram.dainikhatabook.ErrorHandlers;

public class ApplicationError extends Exception
{
    public ApplicationError(String message) {
        super(message);
    }
}
