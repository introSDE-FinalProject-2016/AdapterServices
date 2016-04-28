package introsde.finalproject.rest.adapterservices.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

@Stateless
@LocalBean
@Path("/adapter-service")
public class CollectionResource {

	/**
	 * Request #1: GET /person/instagram-pictures 
	 * Returns the list all pictures from Instagram API 
	 * 
	 * @return
	 */
	@GET
	@Path("/instagram-pictures")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInstagramPictures(){
		try {
			
			String path = "/person/instagram-pictures";
			String result_request_1 = "ERROR";
			String mediaType = MediaType.APPLICATION_JSON;
			
			//String[] hashTags = { "pioneeradventures", "vacationwolf",
				//	"lonelyplanet", "mysecretlondon", "earthofficial" };
			 String[] hashTags = {"fitmotivation", "nordicwalking" ,
					 				"nordicwalkingwa" , "fitnessquotes_"};
			// String[] hashTags = {"happiness", "happy", "smile", "motivation",
			// "nevergiveup"};

			final String ACCESS_TOKEN = "2304108306.1677ed0.1d3d7bec2ce04de4a8da1a37f0f62eaa";

			int random_hashtag = 0 + (int) (Math.random() * (hashTags.length - 1));

			String instagram_endpoint = "https://api.instagram.com/v1/tags/"
					+ hashTags[random_hashtag] + "/media/recent?access_token="
					+ ACCESS_TOKEN;

			String jsonResponse = "";

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(instagram_endpoint);
			HttpResponse response = client.execute(request);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			JSONObject o = new JSONObject(result.toString());

			if (response.getStatusLine().getStatusCode() == 200) {
				
				result_request_1 = "OK";
				
				jsonResponse += "{\"status\": \"OK\",";

				JSONArray arr = o.getJSONArray("data");
				int responseCount = arr.length();
				jsonResponse += "\"responseCount\": " + responseCount + ",";

				jsonResponse += "\"results\": [";

				for (int i = 0; i < arr.length(); i++) {

					String type = arr.getJSONObject(i).getString("type");

					if (type.equals("image")) {

						jsonResponse += "{";

						String standard_resolution_url = arr.getJSONObject(i)
								.getJSONObject("images")
								.getJSONObject("standard_resolution")
								.getString("url");

						String thumbnail = arr.getJSONObject(i)
								.getJSONObject("images")
								.getJSONObject("thumbnail").getString("url");

						String link_instagram = arr.getJSONObject(i).getString(
								"link");

						String random_tag = hashTags[random_hashtag];

						jsonResponse += "\"standard_resolution_url\": \""
								+ standard_resolution_url + "\",";
						
						jsonResponse += "\"thumbUrl\": \"" + thumbnail + "\",";

						jsonResponse += "\"link_instagram\": \""
								+ link_instagram + "\",";

						jsonResponse += "\"random_tag\": \"" + random_tag
								+ "\"";

						if (i == arr.length() - 1) {
							jsonResponse += "}";
						} else {
							jsonResponse += "},";
						}
					}
				}

				jsonResponse += "]}";
				
				templateRequest(1, "GET", path , response, result_request_1, mediaType);
				System.out.println(prettyJSONPrint(jsonResponse));
				
				return Response.ok(jsonResponse).build();
				
			} else {
				jsonResponse += "{\"status\": \"ERROR\"," + "\"error\": \""
						+ response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase() + "\"}";

				templateRequest(1, "GET", path , response, result_request_1, mediaType);
				System.out.println(prettyJSONPrint(jsonResponse));
				
				return Response.status(404).entity(jsonResponse).build();
			}
		} catch (Exception e) {
			System.out.println("Error Instagram API external");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(errorMessage(e)).build();
		}

	}

	/**
	 * Request #2: GET /person/motivation-quote 
	 * Returns one motivational quote from Forismatic API 
	 * 
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/motivation-quote")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMotivationQuote() throws Exception {
		try {
			
			String path = "/person/motivation-quote";
			String result_request_2 = "ERROR";
			String mediaType = MediaType.APPLICATION_JSON;
			
			String forismatic_endpoint = 
					"http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en";
			/*
			 * POST: method=getQuote&key=457653&format=xml&lang=en response:
			 <forismatic> <quote> <quoteText>Brevity the soul of wit</quoteText>
			 <quoteAuthor></quoteAuthor> <senderName>name or nickname of the quote
			 sender</senderName> <senderLink>email or website address of the quote
			 sender</senderLink> </quote> </forismatic>
			 
			// String quotedesign_endpoint =
			// "http://quotesondesign.com/api/3.0/api-3.0.json";
			
			 * { "id":1387, "quote":"The space between an idea and a concept is your
			 * lips. If you can't say it out loud you can't do it.",
			 * "author":"Nick Longo",
			 * "permalink":"http:\/\/quotesondesign.com\/nick-longo\/" }
			 */

			String jsonResponse = "";

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(forismatic_endpoint);
			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			JSONObject o = new JSONObject(result.toString());

			if (response.getStatusLine().getStatusCode() == 200) {
				
				result_request_2 = "OK";
				
				jsonResponse += "{\"status\": \"OK\",";

				jsonResponse += "\"result\": {";

				String quoteText = o.getString("quoteText");
				String quoteAuthor = o.getString("quoteAuthor");

				jsonResponse += "\"quote\": \"" + quoteText + "\",";

				if (quoteAuthor != null && !quoteAuthor.isEmpty()) {
					jsonResponse += "\"author\": \"" + quoteAuthor + "\"";
				} else {
					jsonResponse += "\"author\": \"Anonymous\"";
				}

				jsonResponse += "}\n" + "}";

				templateRequest(2, "GET", path , response, result_request_2, mediaType);
				System.out.println(prettyJSONPrint(jsonResponse));
				
				return Response.ok(jsonResponse).build();

			} else {
				jsonResponse += "{\"status\": \"ERROR\"," + "\"error\": \""
						+ response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase() + "\"}";
				
				templateRequest(2, "GET", path , response, result_request_2, mediaType);
				System.out.println(prettyJSONPrint(jsonResponse));
				
				return Response.status(404).entity(jsonResponse).build();
			}

		} catch (Exception e) {
			System.out.println("Error Quote Design API external");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(errorMessage(e)).build();
		}
	}

	/**
	 * This function returns a message about to types of the exception found
	 * @param e
	 * @return
	 */
	private String errorMessage(Exception e) {
		return "{ \n \"error\" : \"Error in Adapter Services, due to the exception: "
				+ e + "\"}";
	}
	
	
	/**
	 * This method prints the template for each request
	 * @param numberRequest of the request
	 * @param method of the request
	 * @param path of the request
	 * @param response of the request
	 * @param result of the request
	 * @param mediaType of the request
	 */
	public void templateRequest(int numberRequest, String method, String path, HttpResponse response, String result, String mediaType){
		mediaType = mediaType.toUpperCase();
		System.out.println("======================================================================================================");
		System.out.println("Request #" + numberRequest + ": "+ method + " " + path + " " + "Accept: " + mediaType + " " + "Content-Type: " + mediaType);
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + response.getStatusLine().getStatusCode());
		System.out.println();
	}
	
	
	/**
	 * This method prints pretty format for JSON
	 * @param jsonString
	 * @return
	 */
	public String prettyJSONPrint(String jsonString){
		JSONObject json = new JSONObject(jsonString); 
		return json.toString(4);	
	}
	
}
