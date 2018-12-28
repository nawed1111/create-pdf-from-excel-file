
import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class pdfCreator {
	
	public static void main(String args[]) throws Exception {
		PDPage page;
		PDPageContentStream contentStream;
		float fontSize = 7;
        float margin = 50;
        float leading = 1.5f * fontSize;
        PDFont font = PDType1Font.HELVETICA;
        page = new PDPage();
        page.setMediaBox(PDRectangle.A4);
        PDRectangle pageSize = page.getMediaBox();
        float startX = pageSize.getLowerLeftX() + margin;
        float startY = pageSize.getUpperRightY() - margin;
        
	    	FileInputStream f1 = new FileInputStream(new File("path"));
	        XSSFWorkbook wb = new XSSFWorkbook(f1);
	    	 PDDocument document = new PDDocument();
	    	 Locator loc = new Locator();
	    	 loc.workbook = wb;
	         for (int i=0; i<loc.workbook.getNumberOfSheets(); i++){
	        	loc.sheet = loc.workbook.getSheetAt(i);
	            if(i<2){
	            	document.addPage(new PDPage());
	            }
	            else {

	                document.addPage(page);
	                contentStream = new PDPageContentStream(document, page);
	                contentStream.setFont(font, fontSize);
	                contentStream.beginText();
	                contentStream.newLineAtOffset(startX, startY);
	                float spaceLeft= startY;
	         for (int j=0; j<loc.sheet.getPhysicalNumberOfRows(); j++) {
	        	 loc.row = loc.sheet.getRow(j);
	        	 for (int k=0; k<loc.row.getPhysicalNumberOfCells(); k++) {
	        		 loc.cell = loc.row.getCell(k);
	        		 String text = checkDataInCell(loc);
	        		 if(j==0)
	        		 contentStream.showText(text+"          ");
	        		 else
	        		 contentStream.showText(text+"       ");	 
	     	      }
	                contentStream.newLineAtOffset(0, -leading);
	                spaceLeft -= leading;
	                if(spaceLeft <= margin){
	                	contentStream.endText();
		    	        contentStream.close();
	                    page = new PDPage();
	                    page.setMediaBox(PDRectangle.A4);
	                    document.addPage(page);
	                    spaceLeft = startY; 
	                    contentStream = new PDPageContentStream(document, page);
	                    contentStream.beginText();
	                    contentStream.setFont(font, fontSize);
	                    contentStream.newLineAtOffset(startX, startY);
	                }
	         }
	         contentStream.endText();
	         contentStream.close();	         
	    } 
	  }      
	         document.save("path");
	         
	         wb.close();
	         document.close();	
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
