package info.devram.dainikhatabook.Services;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Interfaces.GenerateReportListener;

public class PdfCreate implements Runnable {

//    private static final String TAG = "PdfCreate";

    private List<AccountEntity> accounts;
    private GenerateReportListener mListener;
    private OutputStream mStream;

    public PdfCreate(List<AccountEntity> accountEntities, OutputStream stream,
                     GenerateReportListener mListener) {
        this.accounts = accountEntities;
        this.mListener = mListener;
        this.mStream = stream;
    }

    @Override
    public void run() {
        PdfDocument pdfDocument = createPdfFile();
        try {
            pdfDocument.writeTo(mStream);
            this.mListener.onReportGenerated("PDF File Created",
                    GenerateReportListener.STATUS_CODE.OK);
        } catch (IOException e) {
            e.printStackTrace();
            this.mListener.onReportGenerated("Error Creating file",
                    GenerateReportListener.STATUS_CODE.ERROR);
        } finally {
            pdfDocument.close();
            try {
                mStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private PdfDocument createPdfFile() {
        PdfDocument document = new PdfDocument();
        int width = 576;
        int height = 720;
        PageInfo expPageInfo = new PageInfo.Builder(width, height, 1).create();
        PageInfo incPageInfo = new PageInfo.Builder(width, height, 2).create();
        Page expPage = document.startPage(expPageInfo);
        Canvas expPageCanvas = expPage.getCanvas();
        expPageCanvas.drawText("Expenses", 10, 10, new Paint());
        drawHeading(expPageCanvas);
        int expY = 70;
        int incY = 70;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).accountRepoType.getRepoType().equalsIgnoreCase(Config.EXPENSE_TABLE_NAME)) {
                expPageCanvas.drawText(accounts.get(i).accountType.getType(), 10, expY, new Paint());
                String cellDate = serializeDate(accounts.get(i).accountCreatedDate.getCreatedAt());
                expPageCanvas.drawText(cellDate, 100, expY, new Paint());
                expPageCanvas.drawText(String.valueOf(
                        accounts.get(i).accountMoney.getAmount()), 200, expY, new Paint());
                expPageCanvas.drawText(accounts.get(i).accountDescription.getDesc(), 300, expY, new Paint());
                expY = expY + 30;
            }
        }
        document.finishPage(expPage);
        Page incPage = document.startPage(incPageInfo);
        Canvas incPageCanvas = incPage.getCanvas();
        incPageCanvas.drawText("Incomes", 10, 10, new Paint());
        drawHeading(incPageCanvas);
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).accountRepoType.getRepoType().equalsIgnoreCase(Config.INCOME_TABLE_NAME)) {
                incPageCanvas.drawText(accounts.get(i).accountType.getType(), 10, incY, new Paint());
                String cellDate = serializeDate(accounts.get(i).accountCreatedDate.getCreatedAt());
                incPageCanvas.drawText(cellDate, 100, incY, new Paint());
                incPageCanvas.drawText(String.valueOf(
                        accounts.get(i).accountMoney.getAmount()), 200, incY, new Paint());
                incPageCanvas.drawText(accounts.get(i).accountDescription.getDesc(), 300, incY, new Paint());
                incY = incY + 30;
            }

        }
        document.finishPage(incPage);
        return document;
    }

    private String serializeDate(long date) {
        String format = "dd/MM/YY";
        Date converted = new Date(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CANADA);
        return simpleDateFormat.format(converted);
    }

    private void drawHeading(Canvas canvas) {
        canvas.drawText("Type", 10, 40, new Paint());
        canvas.drawText("Date", 100, 40, new Paint());
        canvas.drawText("Amount", 200, 40, new Paint());
        canvas.drawText("Description", 300, 40, new Paint());
    }
}
