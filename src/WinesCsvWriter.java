import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class WinesCsvWriter {
    
    File csvFile;
    FileWriter csvFileWriter;
    char separator;
    
    public WinesCsvWriter () {
        separator = ',';
    }
    
    public WinesCsvWriter (char separator) {
        this.separator = separator;
    }
    
    public void writeWinesToCsv (String filename, LinkedList<WineData> wines) {
        openCsv(filename);
        for (WineData wineData : wines) {
            writeWineDataToCsv (wineData);
        }
        closeCsv();
    }
    
    public void openCsv (String filename) {
        openCsvFile(filename);
        openCsvFileWriter();
    }
    
    public void openCsvFile (String filename) {
        try {
            csvFile = new File(filename);
            csvFile.createNewFile(); // Ignore whether there is already a file with that name.
        } catch (IOException e) {
            System.out.println("Error while creating and opening the csv wine file.");
            e.printStackTrace();
        }
    }
    
    public void openCsvFileWriter () {
        try {
            csvFileWriter = new FileWriter(csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    public void closeCsv () {
        try {
            csvFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeWineDataToCsv (WineData wineData) {
        try {
            csvFileWriter.write(String.valueOf(wineData.id));
            csvFileWriter.write(separator);
            csvFileWriter.write(wineData.type);
            csvFileWriter.write(separator);
            csvFileWriter.write(wineData.title);
            csvFileWriter.write(separator);
            csvFileWriter.write(String.valueOf(wineData.price));
            csvFileWriter.write(separator);
            csvFileWriter.write(wineData.currency);
            csvFileWriter.write(separator);
            csvFileWriter.write(String.valueOf(wineData.capacity));
            csvFileWriter.write(separator);
            csvFileWriter.write(wineData.capacityUnits);
            csvFileWriter.write(separator);
            csvFileWriter.write(String.valueOf(wineData.rating));
            csvFileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}