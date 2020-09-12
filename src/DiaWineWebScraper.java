import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DiaWineWebScraper {
    
    private LinkedList<WineData> wineCatalogue;
    private HashMap<String, String> wineUrls;
    
    public DiaWineWebScraper() {
        wineCatalogue = new LinkedList<WineData>();
        wineUrls = new HashMap<String, String>();
        wineUrls.put("red", "https://www.dia.es/compra-online/productos/bebidas/vinos-tintos/c/WEB.008.070.00000?show=All");
        wineUrls.put("white", "https://www.dia.es/compra-online/productos/bebidas/vinos-blancos/c/WEB.008.068.00000?show=All");
        wineUrls.put("rosé", "https://www.dia.es/compra-online/productos/bebidas/vinos-rosados/c/WEB.008.069.00000?show=All");
    }
    
    public LinkedList<WineData> getWineCatalogue (){
        return new LinkedList<WineData>(wineCatalogue);
    }
    
    public void createWineCatalogue () {
        for (String type : wineUrls.keySet()) {
            addWinesOfTypeToCatalog(type);            
        }        
    }
    
    public void addWinesOfTypeToCatalog (String type) {     
        Elements products = scratcProductsOfType(type);

        for (Element wineProduct:products)
        {
            if (isProduct(wineProduct)) {
                wineCatalogue.add(convertJsoupElementToWineData(wineProduct));
                wineCatalogue.getLast().type = type;
            }
        }
    }
    
    public Elements scratcProductsOfType (String type) {
        String query = "div.prod_grid"; // This refers to the HTML blocks with the desired information.
        Document diaWebsite;
        Elements products = new Elements();
        
        try {
            diaWebsite = Jsoup.connect(wineUrls.get(type)).get();
            products = diaWebsite.select(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    public WineData convertJsoupElementToWineData (Element wineProduct) {
        WineData wine = new WineData();
        
        wine.id = retrieveWineId(wineProduct);
        wine.title = retrieveWineTitle(wineProduct);
        wine.price = retrieveWinePrice(wineProduct);
        wine.currency = retrieveWinePriceCurrency(wineProduct);
        wine.capacity = retrieveWineCapacity(wineProduct);
        wine.capacityUnits = retrieveWineCapacityUnits(wineProduct);
        wine.rating = retrieveWineRating(wineProduct);
        
        return wine;
    }
    
    public boolean isProduct (Element wineProduct) {
        // All products must have a data-productcode.
        return !wineProduct.attr("data-productcode").isEmpty();
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
    
    public double retrieveWineCapacity (Element wineProduct) {
        // In the details can be found the capacity of the product, 
        // since the two last words are the capacity and its units.
        double capacity;
        String description = wineProduct.getElementsByClass("details").text();
        String[] splitedDescription = description.split(" ");
        try {
            capacity = Double.parseDouble(splitedDescription[splitedDescription.length - 2]);
        } 
        catch (NumberFormatException e) {
            capacity = extractCapacityFromPriceAndPricePerCapacityUnit(wineProduct);
        }
        return capacity;
    }
    
    public String retrieveWineCapacityUnits (Element wineProduct) {
        // In the details can be found the capacity of the product, 
        // since the two last words are the capacity and its units.
        String description = wineProduct.getElementsByClass("details").text();
        String[] splitedDescription = description.split(" ");
        String units = splitedDescription[splitedDescription.length - 1];
        if (isValidWineUnits(units))
            return units;
        else // TODO Ensure that only happens when capacity is obtained from extractCapacityFromPriceAndPricePerCapacityUnit
            return extractCapacityUnitsFromPriceAndPricePerCapacityUnit(wineProduct);
    }
    
    public double retrieveWineRating (Element wineProduct) {
        String rating = wineProduct.getElementsByClass("rateyo-readonly").attr("data-rating");
        
        if (rating.isEmpty())
            return Double.NaN; // TODO Is it the best way to proceed if there is no rating?
        else
            return Double.parseDouble(rating);
    }
    
    public boolean isValidWineUnits (String units) {
        return units.equals("ml") | units.equals("cl") | units.equals("l") | units.equals("lt");
    }
    
    public double extractCapacityFromPriceAndPricePerCapacityUnit (Element wineProduct){
        return retrieveWinePrice(wineProduct) / retrieveWinePricePerCapacityUnit (wineProduct);
    }
    
    public String extractCapacityUnitsFromPriceAndPricePerCapacityUnit (Element wineProduct){
        String currency = retrieveWinePriceCurrency(wineProduct);
        String UnitsOfpricePerCapacityUnit = retrieveWineUnitsOfPricePerCapacityUnit(wineProduct);
        String currencyFromUnitsOfpricePerCapacityUnit = 
                extractCurrencyFromUnitsOfpricePerCapacityUnit(UnitsOfpricePerCapacityUnit);
        String capacityUnitsFromUnitsOfpricePerCapacityUnit = 
                extractCapacityUnitsFromUnitsOfpricePerCapacityUnit (UnitsOfpricePerCapacityUnit);
        
        if (currency.equals(currencyFromUnitsOfpricePerCapacityUnit))
            return capacityUnitsFromUnitsOfpricePerCapacityUnit;
        else
            return ""; // TODO Throw an exception?
    }
    
    public double retrieveWinePricePerCapacityUnit (Element wineProduct) {
        // Although name of the tag is pricePerKilogram, it should be price per unit of volume.
        return Double.parseDouble(wineProduct.getElementsByClass("pricePerKilogram")
                .text().split(" ")[0].replace(',', '.').replace('(', '\u0000'));
    }
    
    public String retrieveWineUnitsOfPricePerCapacityUnit (Element wineProduct) {
        return wineProduct.getElementsByClass("pricePerKilogram")
                .text().split(" ")[1].replaceAll(".\\)", "");
    }
    
    public String extractCurrencyFromUnitsOfpricePerCapacityUnit (String UnitsOfpricePerCapacityUnit) {
        return UnitsOfpricePerCapacityUnit.split("/")[0];
    }
    
    public String extractCapacityUnitsFromUnitsOfpricePerCapacityUnit (String UnitsOfpricePerCapacityUnit) {
        String capacityUnits = UnitsOfpricePerCapacityUnit.split("/")[1];
        if (isValidWineUnits(capacityUnits))
            return capacityUnits;
        else 
            return "";
    }
}
