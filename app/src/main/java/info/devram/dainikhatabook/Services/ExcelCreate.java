package info.devram.dainikhatabook.Services;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Interfaces.GenerateReportListener;
import info.devram.dainikhatabook.Models.DashBoardObject;

public class ExcelCreate implements Runnable {

//    private static final String TAG = "ExcelCreate";


    private List<AccountEntity> accounts;
    private GenerateReportListener mListener;
    private OutputStream mStream;

    public ExcelCreate(List<AccountEntity> accountEntities, OutputStream stream,
                       GenerateReportListener listener) {
        this.accounts = accountEntities;
        this.mListener = listener;
        this.mStream = stream;
    }

    private Workbook createExcel() {

        Workbook workbook = new HSSFWorkbook();
        Sheet expenses = workbook.createSheet();
        Sheet incomes = workbook.createSheet();
        workbook.setSheetName(0,"expenses");
        workbook.setSheetName(1,"incomes");
        Row row;
        row = expenses.createRow(1);
        createRowHeading(row);
        row = incomes.createRow(1);
        createRowHeading(row);
        int expRowNumber = 1;
        int incRowNumber = 1;
        for(int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).accountRepoType.getRepoType().equals(Config.EXPENSE_TABLE_NAME)) {
                row = expenses.createRow(expRowNumber);
                Cell cellDataType = row.createCell(0);
                cellDataType.setCellValue(accounts.get(i).accountType.getType());
                Cell cellDataDate = row.createCell(1);
                String cellDate = serializeDate(accounts.get(i).accountCreatedDate.getCreatedAt());
                cellDataDate.setCellValue(cellDate);
                Cell cellDataAmount = row.createCell(2);
                cellDataAmount.setCellValue(accounts.get(i).accountMoney.getAmount());
                Cell cellDataDesc = row.createCell(3);
                cellDataDesc.setCellValue(accounts.get(i).accountDescription.getDesc());
                expRowNumber++;
            }else {
                row = incomes.createRow(incRowNumber);
                Cell cellDataType = row.createCell(0);
                cellDataType.setCellValue(accounts.get(i).accountType.getType());
                Cell cellDataDate = row.createCell(1);
                String cellDate = serializeDate(accounts.get(i).accountCreatedDate.getCreatedAt());
                cellDataDate.setCellValue(cellDate);
                Cell cellDataAmount = row.createCell(2);
                cellDataAmount.setCellValue(accounts.get(i).accountMoney.getAmount());
                Cell cellDataDesc = row.createCell(3);
                cellDataDesc.setCellValue(accounts.get(i).accountDescription.getDesc());
                incRowNumber++;
            }
        }

        return workbook;
    }

    @Override
    public void run() {

        Workbook workbook = createExcel();
        try {
            workbook.write(mStream);
            mListener.onReportGenerated("Excel File Created", GenerateReportListener.STATUS_CODE.OK);
        } catch (IOException e) {
            e.printStackTrace();
            mListener.onReportGenerated("Error Creating File", GenerateReportListener.STATUS_CODE.ERROR);
        }finally {
            try {
                mStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String serializeDate(long date) {
        String format = "dd/MM/YY";
        Date converted = new Date(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CANADA);
        return simpleDateFormat.format(converted);
    }

    private void createRowHeading(Row row) {
        Cell cellHeadingType = row.createCell(0);
        cellHeadingType.setCellValue("Type");
        Cell cellHeadingDate = row.createCell(1);
        cellHeadingDate.setCellValue("Date");
        Cell cellHeadingAmount = row.createCell(2);
        cellHeadingAmount.setCellValue("Amount");
        Cell cellHeadingDesc = row.createCell(3);
        cellHeadingDesc.setCellValue("Description");
    }
}
