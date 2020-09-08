
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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