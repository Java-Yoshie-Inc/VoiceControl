package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Wikipedia {

	private static final String encoding = "UTF-8";

	public static String getInformation(String term) throws UnsupportedEncodingException, IOException {
		String searchText = term + " wikipedia";
		System.out.println("Searching for " + term + "...");

		Document google = Jsoup.connect("https://www.google.com/search?q=" + URLEncoder.encode(searchText, encoding))
				.userAgent("Mozilla/5.0").get();

		String wikipediaURL = google.getElementsByTag("cite").get(0).text();

		String wikipediaApiJSON = "https://www.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="
				+ URLEncoder.encode(wikipediaURL.substring(wikipediaURL.lastIndexOf("/") + 1, wikipediaURL.length()),
						encoding);

		HttpURLConnection httpcon = (HttpURLConnection) new URL(wikipediaApiJSON).openConnection();
		httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");
		BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));

		String responseSB = in.lines().collect(Collectors.joining());
		in.close();

		String result = responseSB.split("extract\":\"")[1];
		result = result.substring(0, result.length() - 5);
		if(result == null || result.isEmpty()) {
			result = "I am sorry. I wasn't able to find anything specific about " + term + ".";
		}
		result = result.replace("\\n", "");
		result = result.replace(term + " may also refer to:", "");
		result = result.replace(capitalize(term) + " may also refer to:", "");
		return result;
	}
	
	public static String capitalize(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

}
