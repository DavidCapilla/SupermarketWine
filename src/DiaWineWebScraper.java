import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DiaWineWebScraper {
    
    private LinkedList<WineData> wines;
    private String redWineUrl;
    private String whiteWineUrl;
    private String roseWineUrl;
    
    public DiaWineWebScraper() {
        redWineUrl = "https://www.dia.es/compra-online/productos/bebidas/vinos-tintos/c/WEB.008.070.00000?show=All";
        whiteWineUrl = "";
        roseWineUrl = "";
    }

    public void scrapeData (String url) {
        Document diaWebsite;
        Elements products;
        String query = "div.prod_grid";
        
        try {
            diaWebsite = Jsoup.connect(url).get();
            products = diaWebsite.select(query);
            
            for (Element wineProduct:products)
            {
                
                //print(i + "; " + wineList.attr("data-productcode") + "; " + wineList.getElementsByClass("details").text()+ "; " + wineList.getElementsByClass("price").text() + "; " + wineList.getElementsByClass("pricePerKilogram").text() + "; " + wineList.getElementsByClass("rateyo-readonly").attr("data-rating") );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public WineData convertJsoupElementToWineData (Element wineProduct) {
        WineData wine = new WineData();
        
        wine.id = Integer.parseInt(wineProduct.attr("data-productcode")); // TODO Throws NumberFormatException
        wine.title = wineProduct.getElementsByClass("details").text();
        wine.price = wineProduct.getElementsByClass("price").text();
        wine.pricePerCapacityUnit = wineProduct.getElementsByClass("pricePerKilogram").text(); 
        wine.rating = wineProduct.getElementsByClass("rateyo-readonly").attr("data-rating");
        wine.capacity =;
        // TODO set wine type when it corresponds.

        
        return new WineData();
    }
    

}
