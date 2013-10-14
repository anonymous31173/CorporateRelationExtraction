package corp.scratch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import corp.data.CorpMetaData;
import corp.data.Gazetteer;
import corp.data.annotation.AnnotationCache;
import corp.data.annotation.CorpDocumentSet;
import corp.data.feature.CorpRelFeature;
import corp.data.feature.CorpRelFeatureNGramSentence;
import corp.data.feature.CorpRelFeaturizedDataSet;
import corp.util.CorpProperties;
import corp.util.LatentFactions;
import corp.util.OutputWriter;
import corp.util.StringUtil;

public class RunLatentFactions {
	public static void main(String[] args) {
		String name = args[0];
		int numFactions = Integer.parseInt(args[1]);
		int iterations = Integer.parseInt(args[2]);
		int maxThreads = Integer.parseInt(args[3]);
		int maxDocuments = Integer.parseInt(args[4]);
		
		OutputWriter output = new OutputWriter();
		CorpProperties properties = new CorpProperties("corp.properties");
		
		Gazetteer stopWordGazetteer = new Gazetteer("StopWord", properties.getStopWordGazetteerPath());
		Gazetteer tickerGazetteer = new Gazetteer("BloombergCorpTickerGazetteer", properties.getBloombergCorpTickerGazetteerPath());

		StringUtil.StringTransform stopWordCleanFn =  StringUtil.getStopWordsCleanFn(stopWordGazetteer);
		StringUtil.StringTransform corpKeyFn = StringUtil.getCorpKeyFn(tickerGazetteer,  stopWordCleanFn);
		
		LatentFactions latentFactions = new LatentFactions(
				name, 
				properties.getLatentFactionsCommandPath(), 
				new File(properties.getLatentFactionsSourceDirectory()), 
				maxThreads, 
				numFactions, 
				iterations, 
				corpKeyFn, 
				output);
		
		System.out.println("Loading documents...");
		
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
				32,
				0,
				maxDocuments,
				new OutputWriter(),
				true,
				new CorpMetaData("Corp", properties.getCorpMetaDataPath())
		);
		
		List<CorpRelFeature> features = new ArrayList<CorpRelFeature>();
		features.add(new CorpRelFeatureNGramSentence(1, 0));
		
		CorpRelFeaturizedDataSet data = new CorpRelFeaturizedDataSet(documentSet, features, maxThreads, output);
		
		latentFactions.setData(data);
		
		output.debugWriteln(String.valueOf(latentFactions.load()));
	}
}