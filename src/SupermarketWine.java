
public class SupermarketWine {
    
    public static void main(String args[]){
        System.out.println("running...");
        DiaWineWebScraper theDiaWineWebScraper = new DiaWineWebScraper();
        theDiaWineWebScraper.createWineCatalogue();
        WinesCsvWriter theWinesCsvWriter = new WinesCsvWriter();
        theWinesCsvWriter.writeWinesToCsv("DiaWines.csv", theDiaWineWebScraper.getWineCatalogue());
        System.out.println("done");
    }
}