package pl.radomiej.formula;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class ResourceHelper {
	public static String getFormulaScriptFromResource(String formulaFileName) {
		try {
			URL url = formulaFileName.getClass().getResource("/" + formulaFileName + ".fml");
			File myFile = new File(url.toURI());
			String content = FileUtils.readFileToString(myFile, "UTF-8"); 
			return content; 
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
