package Document;

import DB.Access.IDBConnection;
import DB.Classes.Masznahme;
import de.die_gfi.projektunterrichtsprotokolle.ProtocolController;
import org.apache.poi.xwpf.usermodel.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class DocumentMaker {
    public static final String TEMPLATESOURCE = ProtocolController.TEMPLATESOURCE;

    private static HashMap<String, String> createReplaceMap(Masznahme masznahme, String ref) {
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put(":0", masznahme.getName());
        replaceMap.put(":1", "" + masznahme.getAuftragsnr());
        replaceMap.put(":2", masznahme.getSeminarleitung());
        replaceMap.put(":3", ref);
        replaceMap.put(":4", masznahme.getUnterrichtsort());
        replaceMap.put(":5", "DATEMISSING");
        replaceMap.put(":6", "DATEMISSING");

        return replaceMap;
    }

    static String day = "";
    static String month = "";

    public static void templateFiller(IDBConnection dbcon, Masznahme masznahme, String dates, @Nullable String ref, String outFolder, boolean efficientDateSorting, int currentYear, boolean createZipFile) throws Exception {

        String[] dateList = dateListParser(dates);
        String startDate = ((efficientDateSorting) ? LocalDate.parse(dateList[0], DateTimeFormatter.ofPattern("dd.MM.yyyy")).format(DateTimeFormatter.ofPattern("yyyyMMdd")) : dateList[0]);
        String endDate = ((efficientDateSorting) ? LocalDate.parse(dateList[dateList.length - 1] + LocalDate.now().getYear(), DateTimeFormatter.ofPattern("dd.MM.yyyy")).format(DateTimeFormatter.ofPattern("yyyyMMdd")) : dateList[dateList.length - 1]);
        String zipname = outFolder + "\\" + startDate + "-" + endDate
                + "_" + masznahme.getName() + "_" + "Unterrichtsprotokolle.zip";
        ZipOutputStream zOut = null;
        if(createZipFile) {
             zOut = new ZipOutputStream(new FileOutputStream(zipname));
        }

        HashMap<String, String> replaceMap = createReplaceMap(masznahme, ref);
        boolean firstDate = true;
        for (String date : dateList) {
            date = dateStringBuilder(date, currentYear, firstDate);
            String timestamp = (efficientDateSorting) ? currentYear + month + day : date;
            String fileName = timestamp + "_" + masznahme.getName() + "_" + "Unterrichtsprotokoll.docx";

            generateDocument(fileName, outFolder, date, currentYear, firstDate, replaceMap);
            if (createZipFile) {
                File trg = new File(outFolder + "\\" + fileName);
                zipFile(trg, fileName, zOut);
            }
            firstDate = false;

        }
        if(createZipFile) {
            zOut.close();
        }
    }

    private static void generateDocument(String fileName, String outFolder, String date, int currentYear, boolean firstDate, HashMap<String, String> replaceMap) throws IOException {
        XWPFDocument document = new XWPFDocument(new FileInputStream(TEMPLATESOURCE));



        System.out.println("Aktuelles Datum (date): " + date);
        replaceMap.put(":5", date);
        replaceMap.put(":6", date);
        fillInData(document, replaceMap);

        //output
        FileOutputStream stream = new FileOutputStream(outFolder + "\\" + fileName);
        document.write(stream);
        stream.close();
        document.close();
    }

    public static String[] dateListParser(String allDates) {
        String[] parsedDates;
        allDates = allDates.replaceAll(", ", " ");
        allDates = allDates.replaceAll("\\s+", ",");
        parsedDates = allDates.split(",");
        return parsedDates;
    }

    public static String dateStringBuilder(String chosenDate, int currentYear, boolean firstDate) {
        String parsedDate;
        chosenDate = (chosenDate.isEmpty()) ? LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : chosenDate;
        String[] partsOfDate = chosenDate.split("\\.");
        if (partsOfDate[0].contains("01") && partsOfDate[1].contains("01") && !firstDate) {
            currentYear++;
        }
        day = partsOfDate[0];
        month = partsOfDate[1];
        parsedDate = (partsOfDate.length < 3) ? partsOfDate[0] + "." + partsOfDate[1] + "." + currentYear : partsOfDate[0] + "." +
                        partsOfDate[1] + "." + ((partsOfDate[2].length() == 4) ? partsOfDate[2] : "20" + partsOfDate[2]);
        return parsedDate;
    }

    private static void fillInData(XWPFDocument document, HashMap<String, String> replaceMap) {
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

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

}
