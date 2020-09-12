package info.devram.dainikhatabook.ErrorHandlers;

public class LogError implements Runnable {

    private String fileName;
    private ApplicationError error;

    public LogError(String fileName, ApplicationError error) {
        this.fileName = fileName;
        this.error = error;
    }

    private void logError()
    {
        this.error.getMessage();
    }


    @Override
    public void run() {

    }
}
