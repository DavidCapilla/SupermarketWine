
public class SupermarketWine {
    
    public static void main(String args[]){
        print("running...");
        DiaWineWebScraper theDiaWineWebScraper = new DiaWineWebScraper();
        theDiaWineWebScraper.createWineCatalog();
        print("done");
    }
    
    public static void print(String string) {
        System.out.println(string);
    }

}