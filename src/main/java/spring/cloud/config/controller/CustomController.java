package spring.cloud.config.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Properties;

import javax.ws.rs.Produces;

import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class CustomController {

	@Produces(MediaType.TEXT_PLAIN_VALUE)
	@RequestMapping("/custom")
	public  Properties getApplicationProperties(){
		
		 String sURL = "http://localhost:8888/userDataService/default"; //just a string
		 JSONObject json=null;
		 try {
			json = readJsonFromUrl(sURL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		try {
			
			Properties properties = toProperties(json.getJSONArray("propertySources").getJSONObject(0).getJSONObject("source"));			
			
			return properties;
			//return new ResponseEntity(properties, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return new ResponseEntity(new Properties(), HttpStatus.OK);
		return new Properties();
	}
	
	public Properties toProperties(JSONObject jo)  throws JSONException {
        Properties  properties = new Properties();
        if (jo != null) {
            Iterator<String> keys = jo.keys();
            while (keys.hasNext()) {
                String name = keys.next();
                properties.put(name, jo.getString(name));
            }
        }
        return properties;
    }
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {

	      InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
}
