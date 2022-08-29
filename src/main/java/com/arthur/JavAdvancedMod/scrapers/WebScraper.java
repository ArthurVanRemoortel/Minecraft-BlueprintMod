package com.arthur.JavAdvancedMod.scrapers;

import com.arthur.JavAdvancedMod.util.BlueprintData;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebScraper {

    public static JsonObject downloadJson(String baseUrl){
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            HtmlPage page = client.getPage(baseUrl);
            List<DomAttr> items = page.getByXPath("//script/@src") ;
            if(items.isEmpty()){
                System.out.println("No items found !");
            } else {
                String scriptUrl = items.get(items.size() - 1).getValue();
                WebResponse scriptPageResponse = client.getPage(scriptUrl).getWebResponse();
                String script = scriptPageResponse.getContentAsString();
                String blueprint_data = script.substring(script.indexOf(" = ") + 3);
                return new JsonParser().parse(blueprint_data).getAsJsonObject();
            }
        } catch(Exception e){
            // Website is down sometimes or it might have changed and broke the scraping.
            e.printStackTrace();
        }
        return null;
    }

    public static BlueprintData getBlueprint(String url){
        JsonObject jsonObject = downloadJson(url);
        if (jsonObject != null) {
            return new BlueprintData(url, jsonObject);
        }
        return null;
    }
}
