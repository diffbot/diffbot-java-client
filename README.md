# Diffbot API Java client

## I- Preface

This API offers, via  [diffbot](http://diffbot.com/), the possibility to map unstructured web data into java classes or its manipulation as raw JSON.

It offers three approaches to handling data received from the diffbot API :
* Filling java classes with json data using [Jackson](http://jackson.codehaus.org/‎)'s pojo marshalling.
* Raw json manipulation through [JSONObjec](http://www.json.org/javadoc/org/json/JSONObject.html).
* Raw json manipulation through jackson's [JsonNode](http://jackson.codehaus.org/1.7.9/javadoc/org/codehaus/jackson/JsonNode.html).

## II - Installation

### II-1 Installation requirements:

* Maven 3 : for compiling the project and managing dependencies
* The Java Development Kit v 1.7 (JDK 1.7) : the jdk should be properly installed and configured to be used with maven
* Internet connection : internet connection is required for downloading dependencies through maven and executing the demo and the unit tests.

### II-2 Adding the maven dependencies

1. Compiling the project
In order to make the diffbot-java library available in your local maven repository, run maven's `install` command from within the diffbot-java directory :
```Shell
$> mvn install
```

2. Adding the dependency
Now diffbot-java library dependency is accessible via the following maven dependency :
```XML
	<dependency>
        <groupId>diffbot</groupId>
        <artifactId>diffbot-java</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```

3. Example maven pom file ( from the demo project ) :
```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>diffbot</groupId>
    <artifactId>diffbot-java-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
        <dependency>
            <groupId>diffbot</groupId>
            <artifactId>diffbot-java</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

## III- Configuration

A developer token must be provided in order to be able access the diffbot api. When using the `diffbot-java` client api it can be done through the client's constructor.

```Java
	...
    public static void main(String[] args ){
        String testToken="3....9c359";//set your api token here
        // Create DiffbotClient instance with the appropriate token
        DiffbotClient articlesClient = new DiffbotClient(testToken);
     ...
     }
     ...
```

Optionally , it is possible to choose a specific api version to work with :
```Java
	...
    public static void main(String[] args ){
        String testToken="3....9c359";//set your api token here
        // Create DiffbotClient instance with the appropriate token and version
        DiffbotClient articlesClient = new DiffbotClient(testToken,"2");
     ...
     }
     ...
```
The second constructor parameter specifies the desired api version.

## IV- Third party dependencies
This api uses :
* Apache's HttpComponents - HttpClient for http operations
* Jackson framework for pojo-json marshalling
* Jackson's JsonNode as an option for raw json manipulation
* json.org's JSONObject as an option for raw json manipulation


## V- Usage
This API follows the convention over configuration principle in order to allow access to the [diffbot](http://diffbot.com/) API.

### V-1- Understanding diffbot's RESTful API
Diffbot offers a RESTful API for turning unstructured web pages into structured json data.

This is example JSON data made by the article API at https://api.diffbot.com/v2/article :

```JSON
{
  "type": "article",
  "icon": "http://www.diffbot.com/favicon.ico",
  "title": "Diffbot's New Product API Teaches Robots to Shop Online",
  "author": "John Davi",
  "date": "Wed, 31 Jul 2013 08:00:00 GMT",
  "media": [
    {
      "primary": "true",
      "link": "http://www.youtube.com/embed/lfcri5ungRo?feature=oembed",
      "type": "video"
    }
  ],
  "tags": [
    "e-commerce",
    "SaaS"
  ],
  "url": "http://blog.diffbot.com/diffbots-new-product-api-teaches-robots-to-shop-online/",
  "humanLanguage": "en",
  "text": "Diffbot's human wranglers are proud today to announce the release of our newest product: an API for... products!\nThe Product API can be used for extracting clean, structured data from any e-commerce product page. It automatically makes available all the product data you'd expect: price, discount/savings amount, shipping cost, product description, any relevant product images, SKU and/or other product IDs.\nEven cooler: pair the Product API with Crawlbot, our intelligent site-spidering tool, and let Diffbot determine which pages are products, then automatically structure the entire catalog. Here's a quick demonstration of Crawlbot at work:\nWe've developed the Product API over the course of two years, building upon our core vision technology that's extracted structured data from billions of web pages, and training our machine learning systems using data from tens of thousands of unique shopping sites. We can't wait for you to try it out.\nWhat are you waiting for? Check out the Product API documentation and dive on in! If you need a token, check out our pricing and plans (including our Free plan).\nQuestions? Hit us up at support@diffbot.com ."
}
```
### V-2- Understanding diffbot-java API

Using diffbot-java client library this data can be :
* Mapped to any class with fields names matching json fields (type,icon,title,author ...).
* Or manipulated as raw json data using the JSONObject API or Jackson's JsonNode

#### V-2-1 Using the article API

>The Article API is used to extract clean article text from news article, blog post and similar text-heavy web pages.

This is an example java class that could be filled with data from the articles API according to fields' names :

```Java
public class BlogPost {
    private String title;
    private String author;
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
```

In order to fill this class with data all we need to do is  :

```Java
	...
	
    public static void main(String[] args ){

        String testToken="3....9c359";//set your api token here 

        // Create DiffbotClient instance with the appropriate token
        DiffbotClient client = new DiffbotClient(testToken);

        /*
         article data is loaded into the BlogPost class fields depending on the fields' names
         if the class has a certain field available in the RESTful article resource then the field is filled with the
         appropriate data
         */
       
        BlogPost b= (BlogPost) client.getArticle(BlogPost.class,"http://www.xconomy.com/san-francisco/2012/07/25/diffbot-is-using-computer-vision-to-reinvent-the-semantic-web/");
        System.out.println(b.toString());

    }
	...
```



#### V-2-2 Using the products API

>The Product API analyzes a shopping or e-commerce product page and returns information on the product.

This is example JSON data made by the product API at http://api.diffbot.com/v2/product :

```JSON
{
  "type": "product",
  "products": [
    {
      "title": "iRobot 650 Roomba Vacuuming Robot",
      "description": "The new iRobot Roomba 650 Vacuum Cleaning Robot provides a superior level of cleaning with less work for you. With AeroVac Technology and a new brush design, Roomba 650 is better equipped to handle fibers like hair, pet fur, lint and carpet fuzz. Materials: Vacuum Cleaning Robot Dimensions: 17 inches long x 18 inches wide x 5 inches high Weight: 11 pounds Included parts: One (1)Roomba 650 Vacuum Cleaning Robot With AeroVac Bin, one (1) Self-Charging Home Base, one (1) Battery Charger, one (1) Extra AeroVac Filter, one (1) Auto Virtual Wall and one (1) Brush Cleaning Tool Power source: Battery Model: iRobot Roomba 650",
      "offerPrice": "$399.99",
      "productId": "15268099",
      "media": [
        {
          "height": 320,
          "width": 320,
          "primary": true,
          "link": "http://ak1.ostkcdn.com/images/products/7886009/cc8883ce-f6a0-44a7-836b-b55b4f9ce1ef_320.jpg",
          "caption": "The new iRobot Roomba 650 Vacuum Cleaning Robot provides a superior level of cleaning with less work for you. With AeroVac Technology and a new brush design, Roomba 650 is better equipped to handle fibers like hair, pet fur, lint and carpet fuzz.",
          "type": "image",
          "xpath": "/HTML/BODY/DIV[@id='product-page']/DIV[@id='bd']/DIV[@id='pageContainer']/DIV[@id='productWrap']/DIV[@id='prod_leftCol']/DIV[@id='prod_main']/DIV[@id='prod_mainLeft']/DIV[@id='proImageContainer']/DIV[@id='proImageHero']/DIV[@class='proImageStack']/DIV[@class='proImageCenter']/IMG"
        }
      ]
    }
  ],
  "url": "http://www.overstock.com/Home-Garden/iRobot-650-Roomba-Vacuuming-Robot/7886009/product.html"
}
```

This is an example java class that could be filled with data from the articles API according to fields' names :

```Java
package com.diffbot.clients;

/**
 * Created by wadi chemkhi on 10/01/14.
 * Email : wadi.chemkhi@gmail.com
 */
public class Product {
    String title;
    String description;
    String offerPrice;
    String regularPrice;
    String saveAmount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getSaveAmount() {
        return saveAmount;
    }

    public void setSaveAmount(String saveAmount) {
        this.saveAmount = saveAmount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", offerPrice='" + offerPrice + '\'' +
                ", regularPrice='" + regularPrice + '\'' +
                ", saveAmount='" + saveAmount + '\'' +
                '}';
    }
}

```

A List of java POJOs can be generated according to the product api response data through the `DiffbotClient` .

```Java
	...

    public static void main(String[] args ){

        String testToken="3....9c359";//set your api token here

        // Create DiffbotClient instance with the appropriate token
        DiffbotClient client = new DiffbotClient(testToken);

        // Received products data (json array) is loaded into a List of Product POJOs depending on the class fields' names
        List<Product> l= (List) client.getProducts(Product.class,"http://www.overstock.com/Home-Garden/iRobot-650-Roomba-Vacuuming-Robot/7886009/product.html");

        for(Product p :l)
        System.out.println(p.toString());

    }
	...
```


#### V-3-3 Calling any API using it's name

##### The `DiffbotClient.callApi` method :

This method offers the possibility to call any available diffbot api using it's name.
There two overloaded signatures for this method :

1.`public Object callApi(String api,ResponseType responseType,String url) throws IOException`

This method returns a raw json manipulation Object that can be either json.org's `JSONObject` or Jackson's `JsonNode`, if preferred.
The choice can be made using the DiffbotClient.ResponseType enumeration : `public enum ResponseType{ Jackson, JSONObject }`

Usage example :
```java
DiffbotClient client = new DiffbotClient(testToken);
JsonNode a= (JsonNode) client.callApi("article",DiffbotClient.ResponseType.Jackson,"http://www.xconomy.com/san-francisco/2012/07/25/diffbot-is-using-computer-vision-to-reinvent-the-semantic-web/");
```

2.`public Object callApi(String api,Class<?> clazz ,String url) throws IOException`

This method returns an instance of the `clazz` class filled with data returned from the REST api call

Usage example :
```java
DiffbotClient client = new DiffbotClient(testToken);
BlogPost a= (BlogPost) client.callApi("article",BlogPost.class,"http://wadi-chemkhi.blogspot.com/2013/09/la-conquete-de-lexcellence.html");
```

##### Calling Custom API  API using it's name

Using diffbot's `Custom API Toolkit` it's possible to define custom APIs to extract data from web sites according to custom rules.

These custom APIs are accessible via the `DiffbotClient.callApi` method when provided with appropriate custom API method.

Usage example :
```java
DiffbotClient client = new DiffbotClient(testToken);
BlogPost a= (BlogPost) client.callApi("CustomAPI",BlogPost.class,"http://wadi-chemkhi.blogspot.com/2013/09/la-conquete-de-lexcellence.html");
```

Please notice that the custom API named `"CustomAPI"` must be configured on the account accessible by the provided token.

#### V-3-4 Accessing the Analyse API

`public Object analyze(Class<?> clazz ,String url) throws IOException` :
Analyzes the provided url and maps the result in a POJO of type `clazz`.

`public Object analyze(ResponseType responseType ,String url) throws IOException` :
Analyzes the provided url and returns a raw json manipulation object depending on the specified ResponseType choice.

Example
```java
DiffbotClient client = new DiffbotClient(testToken);
JsonNode a= (JsonNode) client.analyze(DiffbotClient.ResponseType.Jackson,"https://github.com/wadi-chemkhi/diffbot-java-client");
```

#### V-3-5 Submitting a crawl job

## VI- Runing the Demo


Please notice that the previous steps are necessary in order to compile and run the demo.
 
Demo code :

```Java
package com.diffbot.clients.demo;

import com.diffbot.clients.DiffbotClient;

/**
 * Created by wadi chemkhi on 02/01/14.
 * Email : wadi.chemkhi@gmail.com
 */
public class Demo {

    public static void main(String[] args ){

        String testToken="353883355a5c7ff1793b14f81e19c359";

        // Create DiffbotClient instance with the appropriate token
        DiffbotClient articlesClient = new DiffbotClient(testToken);

        /*
         article data is loaded into the Article class fields depending on the fields' names
         if the class has a certain field available in the RESTful article resource then the field is filled with the
         appropriate data
         */
        Article a= (Article) articlesClient.getArticle(Article.class,"http://www.xconomy.com/san-francisco/2012/07/25/diffbot-is-using-computer-vision-to-reinvent-the-semantic-web/");
        System.out.println(a.toString());

        /*
         Same thing with the BlogPost class. Fields are filled with the appropriate data depending on their names
         */
        BlogPost b= (BlogPost) articlesClient.getArticle(BlogPost.class,"http://www.xconomy.com/san-francisco/2012/07/25/diffbot-is-using-computer-vision-to-reinvent-the-semantic-web/");
        System.out.println(b.toString());

    }
}
```

Compile the demo with maven from within the demo folder :

```Shell 
$> mvn compile
```

See it working by using the maven exec command :

```Shell 
$> mvn exec:java
```
