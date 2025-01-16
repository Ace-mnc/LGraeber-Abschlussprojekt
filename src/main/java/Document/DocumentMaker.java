package Document;

import DB.Access.IDBConnection;
import DB.Classes.Masznahme;
import org.apache.poi.xwpf.usermodel.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class DocumentMaker {
    public static final String TEMPLATESOURCE = "C:\\Users\\Admin\\Documents\\testFolder\\template\\Vorlage-Anwesenheitsliste.docx";

    private static HashMap<String, String> createReplaceMap(Masznahme masznahme, String ref) {
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put(":0", masznahme.getName());
        replaceMap.put(":1", ""+masznahme.getAuftragsnr());
        replaceMap.put(":2", masznahme.getSeminarleitung());
        replaceMap.put(":3", ref);
        replaceMap.put(":4", masznahme.getUnterrichtsort());
        replaceMap.put(":5", "date");
        replaceMap.put(":6", "");
        replaceMap.put(":13","date");

        return replaceMap;
    }

    static String day = "";
    static String month = "";
    public static void templateFiller(IDBConnection dbcon, Masznahme masznahme, String dates, @Nullable String ref , String outFolder, boolean efficient, int startYR) throws Exception {



        String[] dateList = dateListParser(dates);
        HashMap<String, String> replaceMap = createReplaceMap(masznahme, ref);
        int currentYear = startYR;
        boolean firstDate = true;
        for (String date : dateList) {
            XWPFDocument document = new XWPFDocument(new FileInputStream(TEMPLATESOURCE));

            templateNumberer(document);

            date= dateParser(date,currentYear,firstDate);

            firstDate = false;
            System.out.println("Aktuelles Datum (date): " + date);
            replaceMap.put(":4", date);
            templateLooper(document, replaceMap);

            //output


            String timestamp = (efficient) ? currentYear + month + day : date;
            String out = outFolder + "\\" + timestamp + "_" + masznahme.getName() + "_" + "Unterrichtsprotokoll.docx";

            FileOutputStream stream = new FileOutputStream(out);
            document.write(stream);
            stream.close();
            document.close();
        }
    }

    public static String[] dateListParser(String allDates){
        String[] parsedDates;
        allDates = allDates.replaceAll(", "," ");
        allDates = allDates.replaceAll("\\s+", ",");
        parsedDates = allDates.split(",");
        return parsedDates;
    }

    public static String dateParser(String chosenDate,int currentYear,boolean firstDate){
        String parsedDate;
        chosenDate = (chosenDate.isEmpty()) ? LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : chosenDate;
        String[] partsOfDate = chosenDate.split("\\.");
        if (partsOfDate[0].contains("01")&&partsOfDate[1].contains("01") && !firstDate) {
            currentYear++;
        }
        day = partsOfDate[0];
        month = partsOfDate[1];
        parsedDate = (partsOfDate.length < 3) ? partsOfDate[0]+"."+partsOfDate[1]+"." + currentYear : partsOfDate[0]+"."+partsOfDate[1]+"."+( (partsOfDate[2].length()==4) ? partsOfDate[2]:"20"+partsOfDate[2]);
        return parsedDate;
    }

    private static void templateLooper(XWPFDocument document, HashMap<String, String> replaceMap) {
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun runA : paragraph.getRuns()) {
                            if (replaceMap.containsKey(runA.text())) {
                                runA.setText(replaceMap.get(runA.text()), 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void templateNumberer(XWPFDocument document) {
        int a = 0;
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun runA : paragraph.getRuns()) {
                            runA.setText(""+a,0);
                            a++;
                        }
                    }
                }
            }
        }
    }
}
