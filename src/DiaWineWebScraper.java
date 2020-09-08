import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DiaWineWebScraper {
    
    private LinkedList<WineData> wines;
    private HashMap<String, String> wineUrls = new HashMap<String, String>();
    
    public DiaWineWebScraper() {
        wines = new LinkedList<WineData>();
        wineUrls.put("red", "https://www.dia.es/compra-online/productos/bebidas/vinos-tintos/c/WEB.008.070.00000?show=All");
        wineUrls.put("white", "https://www.dia.es/compra-online/productos/bebidas/vinos-blancos/c/WEB.008.068.00000?show=All");
        wineUrls.put("rosé", "https://www.dia.es/compra-online/productos/bebidas/vinos-rosados/c/WEB.008.069.00000?show=All");
    }
    
    public void createWineCatalog () {
        for (String type : wineUrls.keySet()) {
            addWinesOfTypeToCatalog(type);            
        }        
    }
    
    public void addWinesOfTypeToCatalog (String type) {
        Document diaWebsite;
        Elements products;
        String query = "div.prod_grid";
        
        try {
            diaWebsite = Jsoup.connect(wineUrls.get(type)).get();
            products = diaWebsite.select(query);
            
            for (Element wineProduct:products)
            {
                wines.add(convertJsoupElementToWineData(wineProduct));
                wines.getLast().type = type;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public WineData convertJsoupElementToWineData (Element wineProduct) {
        WineData wine = new WineData();
        
        wine.id = retrieveWineId(wineProduct);
        wine.title = retrieveWineTitle(wineProduct);
        wine.price = retrieveWinePrice(wineProduct);
        wine.currency = retrieveWinePriceCurrency(wineProduct);
        wine.capacity = retrieveWineCapacity(wineProduct);
        wine.capacityUnits = retrieveWineCapacityUnits(wineProduct);
        wine.pricePerCapacityUnit = retrieveWinePricePerCapacityUnit(wineProduct); 
        wine.rating = retrieveWineRating(wineProduct);
        
        return wine;
    }
    
    public int retrieveWineId (Element wineProduct) {
        // TODO Throws NumberFormatException
        return Integer.parseInt(wineProduct.attr("data-productcode"));
    }
    
    public String retrieveWineTitle (Element wineProduct) {
        return wineProduct.getElementsByClass("details").text();
    }
    
    public double retrieveWinePrice (Element wineProduct) {
     // TODO Throws NumberFormatException
        return Double.parseDouble(wineProduct.getElementsByClass("price")
                .text().split(" ")[0].replace(',', '.'));
    }     
    
    public String retrieveWinePriceCurrency (Element wineProduct) {
        return wineProduct.getElementsByClass("price").text().split(" ")[1];
    } 
    
    public double retrieveWinePricePerCapacityUnit (Element wineProduct) {
        // TODO Check Units. 
        return Double.parseDouble(wineProduct.getElementsByClass("pricePerKilogram")
                .text().split(" ")[0].replace(',', '.').replace('(', '\u0000'));
    }
    
    public double retrieveWineRating (Element wineProduct) {
        String rating = wineProduct.getElementsByClass("rateyo-readonly").attr("data-rating");
        // TODO Is it the best way to proceed if there is no rating?
        if (rating.isEmpty())
            return Double.NaN;
        else
            return Double.parseDouble(rating);
    }
    
    public double retrieveWineCapacity (Element wineProduct) {
        // In the details can be found the capacity of the product, 
        // since the two last words are the capacity and its units.
        String description = wineProduct.getElementsByClass("details").text();
        String[] splitedDescription = description.split(" ");
        // TODO Check that indeed it is obtained the capacity, otherwise compute from its price per litter.
        return Double.parseDouble(splitedDescription[splitedDescription.length - 2]);
    }
    
    public String retrieveWineCapacityUnits (Element wineProduct) {
        // In the details can be found the capacity of the product, 
        // since the two last words are the capacity and its units.
        String description = wineProduct.getElementsByClass("details").text();
        String[] splitedDescription = description.split(" ");
        return splitedDescription[splitedDescription.length - 1];
    }
    

}
