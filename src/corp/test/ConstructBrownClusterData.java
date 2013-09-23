package corp.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import corp.data.annotation.AnnotationCache;
import corp.data.annotation.CorpDocument;
import corp.data.annotation.CorpDocumentSet;
import corp.util.CorpProperties;
import corp.util.OutputWriter;
import corp.util.StanfordUtil;
import corp.util.StringUtil;

public class ConstructBrownClusterData {
	public static void main(String[] args) {
		String outputPath = "/home/wmcdowel/sloan/Data/Brown/Source/";
		
		CorpProperties properties = new CorpProperties("corp.properties");
		
		AnnotationCache annotationCache = new AnnotationCache(
				properties.getStanfordAnnotationDirPath(),
				properties.getStanfordAnnotationCacheSize(),
				properties.getStanfordCoreMapDirPath(),
				properties.getStanfordCoreMapCacheSize(),
				new OutputWriter()
		);
			
		CorpDocumentSet documentSet = new CorpDocumentSet(
				properties.getCorpRelDirPath(), 
				annotationCache,
				4,
				0,
				new OutputWriter()
		);
			
        try {
    		BufferedWriter w = new BufferedWriter(new FileWriter(outputPath));
        
			List<CorpDocument> documents = documentSet.getDocuments();
			for (CorpDocument document : documents) {
				for (int i = 0; i < document.getSentenceCount(); i++) {
					List<String> tokenTexts = StanfordUtil.getSentenceTokenTexts(document.getSentenceAnnotation(i));
					StringBuilder sentenceStr = new StringBuilder();
					for (String tokenText : tokenTexts) {
						sentenceStr = sentenceStr.append(StringUtil.clean(tokenText)).append(" ");
					}
					w.write(sentenceStr.toString().trim() + "\n");
				}
			}
		
			w.close();
        } catch (IOException e) { e.printStackTrace(); }
	}
}
