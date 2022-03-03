/**
 * Fetches the current date in sweden as a String from https://www.vilketdatum.se/
 * @author Ebba Håkansson
 * @version 22-02-23
 */



package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class GetDate {
	public static URLConnection uc;

	/**
	 * Creates a GetDate object
	 * @author Ebba Håkansson
	 */
	public GetDate() {
	}

	/**
	 * Fetches the web page and returns the date found in the HTML code
	 * @return the date
	 * @author Ebba Håkansson
	 */
	public String FetchDate() {
		try {
			URL url = new URL("https://www.vilketdatum.se/");
			uc = url.openConnection();
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				// Finds the date line in the code
				if (line.contains("<div id=\"date\" class=\"date\">")) {
					// Remove start and end tags and keep only the date
					line = line.replaceAll("<div id=\"date\" class=\"date\">", "").replaceAll("</div>", "");
					line = line.trim(); // Remove whitespace
					return line;
				}
			}
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
		return null;
	}

}
