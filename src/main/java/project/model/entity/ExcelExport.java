package project.model.entity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class ExcelExport {
    private Workbook workbook;
    private Sheet sheet;
    private List<Object[]> data;

    public ExcelExport() {
        workbook = new XSSFWorkbook();
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }

    public void export(HttpServletResponse response, Object o) throws IOException {
        sheet = workbook.createSheet("Data");
        List<Field> fields = Arrays.asList(o.getClass().getDeclaredFields());
        Row headerRow = sheet.createRow(0);
        int i = 0;
        for (Field field : fields) {
            headerRow.createCell(i++).setCellValue(field.getName());
        }
        int rowIndex = 1;
        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowIndex++);
            int colNum = 0;
            for (Object field : rowData) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                } else if (field instanceof Float) {
                    cell.setCellValue((Float) field);
                } else if (field instanceof Long) {
                    cell.setCellValue((Long) field);
                } else if (field instanceof LocalDateTime) {
                    cell.setCellValue((LocalDateTime) field);
                } else if (field instanceof LocalDate) {
                    cell.setCellValue((LocalDate) field);
                } else if (field instanceof Date) {
                    cell.setCellValue(String.valueOf(field));
                } else if (field instanceof BigDecimal) {
                    cell.setCellValue(String.valueOf(field));
                }
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
        response.getOutputStream().close();
    }
}
