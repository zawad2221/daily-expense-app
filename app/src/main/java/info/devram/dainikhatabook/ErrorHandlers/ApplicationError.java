package info.devram.dainikhatabook.ErrorHandlers;

import androidx.annotation.Nullable;

public class ApplicationError extends Exception
{
    private String message;
    private String trace;

    public ApplicationError(String errorMessage, String stackTrace)
    {
        this.message = errorMessage;
        this.trace = stackTrace;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }

    public String getTrace() {
        return trace;
    }
}
