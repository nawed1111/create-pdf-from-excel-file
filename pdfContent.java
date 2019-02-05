import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

public class pdfContent {

    private static final String FILE_NAME = "path\\abc.pdf";

    public static void main(String[] args) {

        PdfReader reader;

        try {

            reader = new PdfReader(FILE_NAME);

            // pageNumber = 1
            String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);
            System.out.println(textFromPage);
            String arr[] = new String[10];
            arr = textFromPage.split("\\n");
            for(int i=0;i<arr.length;i++){
            if(arr[i].contains("Career Level"))
            System.out.println(arr[i]);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
