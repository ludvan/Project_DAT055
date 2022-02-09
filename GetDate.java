import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

//TODO:
//Fixa exceptions
//Fixa konstruktor

public class GetDate {
	public static URLConnection uc;
	
	public GetDate() {}

	public String FetchDate() {
		try {
			URL url = new URL("https://www.vilketdatum.se/"); 
			uc = url.openConnection();
		}
		catch (MalformedURLException e) { System.out.println("Exception"); }
		catch (IOException e) { System.out.println("Exception"); }
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String line;
			while ( (line = in.readLine()) != null ) {
				if(line.contains("<div id=\"date\" class=\"date\">")) {
					line = line.replaceAll("<div id=\"date\" class=\"date\">", "").replaceAll("</div>", "");
					line = line.trim();
					return line;
				}
			}
		} 
		catch (IOException e) { System.out.println("Exception"); }
		return null;
	}

}
