# Supermarket Wine

We will extract information about wines from the website of a supermarket (www.dia.es) and explore a little bit the obtained database.

Extracts wine information from the  website of a supermarket (www.dia.es) such as 
- The given identifier,
- The title or description given to the product,
- The price and currency of the product,
- The capacity and its units,
- The rating if any.

Although it is possible to obtain the price per volume it is only used when capacity cannot extracted. (TODO: Think about using as a double check).

It is used the library jsoup to connect to the webpage and parse it HTML via the created DiaWineWebScrap class. From this parsing, the information is retrieved and stored in WineData.

There have been done some assumptions of the correctness of the extracted data. When we explore the data we will see if there are some things to fix.
