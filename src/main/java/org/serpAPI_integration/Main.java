package org.serpAPI_integration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
/**
 * Clase principal del proyecto.
 * @author Jazmin Feliú
 */


public class Main {
    public static void main(String[] args) {


            Map<String, String> parameter = new HashMap<>();

            parameter.put("api_key", "aqui_tu_api_key");
            parameter.put("engine", "google_scholar_author");
            parameter.put("hl", "en");
            parameter.put("author_id", "EicYvbwAAAAJ");
        try{

            var dir = "https://serpapi.com/search.json?engine="
                    +parameter.get("engine")+"&author_id="
                    +parameter.get("author_id")+"&hl="
                    +parameter.get("hl")+"&api_key="
                    +parameter.get("api_key");
            URL url = new URL(dir);

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.connect();

            int resCod = con.getResponseCode();
            if(resCod != 200){
                throw new RuntimeException("Ocurrió un error: "+ resCod);
            }else {
                InputStream is = url.openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String jsonText ="";
                String line;
                while ((line = bufferedReader.readLine())!= null){
                    jsonText += line + "\n";
                }
                is.close();
                bufferedReader.close();
                 System.out.println(jsonText);

                JSONParser parser = new JSONParser();
                Object obj = parser.parse(jsonText);
                JSONObject mainJSonObject = (JSONObject) obj;

                JSONObject jsonObjectMetadata = (JSONObject) mainJSonObject.get("search_metadata");

                String author_url = (String) jsonObjectMetadata.get( "google_scholar_author_url");
                System.out.println("Author URL : "+ author_url);


                JSONObject jsonObjectAuthor = (JSONObject) mainJSonObject.get("author");
                String name = (String) jsonObjectAuthor.get( "name");
                System.out.println("Name : "+ name);
                String affiliations = (String) jsonObjectAuthor.get("affiliations");
                System.out.println("Affiliations : "+ affiliations);

                JSONObject jsonObjectParameters = (JSONObject) mainJSonObject.get("search_parameters");

                String author_id = (String) jsonObjectParameters.get( "author_id");
                System.out.println("Author ID: "+ author_id);

                JSONArray jsonArrayArticles = (JSONArray) mainJSonObject.get("articles");
                System.out.println("Articles:");

                Integer qarticles = jsonArrayArticles.size();
                System.out.println("Total Articles: " + qarticles);

                for (int i = 0; i < jsonArrayArticles.size(); i++) {
                    JSONObject jsonArticle = (JSONObject) jsonArrayArticles.get(i);
                    System.out.println("      Article No: " + (i + 1));

                    String title = (String) jsonArticle.get("title");
                    System.out.println("      Title: " + title);

                    String link = (String) jsonArticle.get("link");
                    System.out.println("      Link: " + link);

                    String year = (String) jsonArticle.get("year");
                    System.out.println("      Year: " + year);

                    System.out.println();

                }
            }



        } catch(Exception e){
            e.printStackTrace();
        }


    }
}