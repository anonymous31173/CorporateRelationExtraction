package corp.util;

import java.io.FileReader;
import java.util.Properties;

public class CorpProperties {
	private String corpRelDirPath;
	private String stanfordAnnotationDirPath;
	private int stanfordAnnotationCacheSize;
	private String cregDataDirPath;
	private String cregCommandPath;
	private String corpGazettePath;
	private String nonCorpGazettePath;
	
	public CorpProperties(String propertiesPath) {
		try {
			FileReader reader = new FileReader(propertiesPath);
			Properties properties = new Properties();
			properties.load(reader);
			
			this.corpRelDirPath = properties.getProperty("corpRelDirPath");
			this.stanfordAnnotationDirPath = properties.getProperty("stanfordAnnotationDirPath");
			this.stanfordAnnotationCacheSize = Integer.valueOf(properties.getProperty("stanfordAnnotationCacheSize"));
			this.cregDataDirPath = properties.getProperty("cregDataDirPath");
			this.cregCommandPath = properties.getProperty("cregCommandPath");
			this.corpGazettePath = properties.getProperty("corpGazettePath");
			this.nonCorpGazettePath = properties.getProperty("nonCorpGazettePath");
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getCorpRelDirPath() {
		return this.corpRelDirPath;
	}
	
	public String getStanfordAnnotationDirPath() {
		return this.stanfordAnnotationDirPath;
	}
	
	public int getStanfordAnnotationCacheSize() {
		return this.stanfordAnnotationCacheSize;
	}
	
	public String getCregDataDirPath() {
		return this.cregDataDirPath;
	}
	
	public String getCregCommandPath() {
		return this.cregCommandPath;
	}
	
	public String getCorpGazettePath() {
		return this.corpGazettePath;
	}
	
	public String getNonCorpGazettePath() {
		return this.nonCorpGazettePath;
	}
}
