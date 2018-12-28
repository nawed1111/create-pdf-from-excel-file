import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class pdf {
	
	public static void main(String[] args) throws Exception{
		FileInputStream f1 = new FileInputStream(new File("path"));
        XSSFWorkbook wb = new XSSFWorkbook(f1);
        
        Locator loc = new Locator();
   	 	loc.workbook = wb;
   	 	
		Document document = new Document(PageSize.A4.rotate(), 0, 0, 30, 30);
		
		 for (int i=0; i<loc.workbook.getNumberOfSheets(); i++){
			 if(i == 0 || i == 1){
				 continue;
			 }
			 	PdfPTable table = new PdfPTable(8);
				float widths[] = { 2, 2, 4, 12, 4, 5, 5, 2 };
				table.setWidths(widths);
				table.setHeaderRows(1);
				Phrase p ;
				PdfPCell cell;
		 
	        	loc.sheet = loc.workbook.getSheetAt(i);
	        	
	         for (int j=0; j<loc.sheet.getPhysicalNumberOfRows(); j++) {
	        	 loc.row = loc.sheet.getRow(j);
	        	 for (int k=0; k<loc.row.getPhysicalNumberOfCells(); k++) {
	        		 loc.cell = loc.row.getCell(k);
	        		 String text = checkDataInCell(loc);
	        		 if(j==0) {
		        		  p = new Phrase(text ,FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL));
		        		  cell = new PdfPCell(p);
		     			  cell.setBackgroundColor(new BaseColor(0, 173, 239));
		     			  table.addCell(cell);
	        		 }
	        		 else{
	        			 cell = new PdfPCell();
	        			 p = new Phrase(text ,FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL));
	     				 cell.addElement(p);
	     				 table.addCell(cell);
	        		 }
	     	      }
	          }
	         PdfWriter.getInstance(document, new FileOutputStream("path"));
				document.open();
				document.add(table);
				document.close();
				wb.close();
	}		
}
	private static class Locator {
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        XSSFRow row;
        Cell cell;
    }
	private static String checkDataInCell(Locator loc) { 
        
	    final CellType locCellType = loc.cell.getCellType();
	    String content;
	    switch(locCellType) {
	          case BLANK:
	          case STRING:
	          case ERROR:
	        	  content = loc.cell.getRichStringCellValue().getString();
	              break;
	          case NUMERIC:
	              int num = (int)loc.cell.getNumericCellValue();
	              content = Integer.toString(num);
	              break;
	          default:
	              throw new IllegalStateException("Unexpected cell type: " + locCellType);
	            }
	        return content;
	    }
}
