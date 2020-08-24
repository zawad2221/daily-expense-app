package info.devram.dainikhatabook.Interfaces;



public interface GenerateReportListener {
    public enum STATUS_CODE {OK,ERROR};
    public void onReportGenerated(String message,STATUS_CODE code);
}
