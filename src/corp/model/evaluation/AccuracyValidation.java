package corp.model.evaluation;

import java.util.List;

import corp.data.annotation.CorpRelLabel;
import corp.data.feature.CorpRelFeaturizedDataSet;
import corp.data.feature.CorpRelFeaturizedDatum;
import corp.model.Model;
import edu.stanford.nlp.util.Pair;

public class AccuracyValidation {
	private Model model;
	private CorpRelFeaturizedDataSet trainData;
	private CorpRelFeaturizedDataSet testData;
	private String outputPath;
	private ConfusionMatrix confusionMatrix;
	
	public AccuracyValidation(Model model, 
							  CorpRelFeaturizedDataSet trainData,
							  CorpRelFeaturizedDataSet testData,
							  String outputPath) {
		this.model = model;
		this.trainData = trainData;
		this.testData = testData;
		this.outputPath = outputPath;
	}
	
	public double run() {
		System.out.println("Training model and outputting to " + this.outputPath);
		if (!this.model.train(this.trainData, this.outputPath))
			return -1.0;
		
		System.out.println("Model classifying data (" + this.outputPath + ")");
		
		List<Pair<CorpRelFeaturizedDatum, CorpRelLabel>> classifiedData =  this.model.classify(this.testData);
		if (classifiedData == null)
			return -1.0;
		
		System.out.println("Computing model score (" + this.outputPath + ")");
		
		double correct = 0;
		for (Pair<CorpRelFeaturizedDatum, CorpRelLabel> classifiedDatum : classifiedData) {
			correct += classifiedDatum.first().getLabel(this.model.getValidLabels()) == classifiedDatum.second() ? 1.0 : 0;
		}
		
		this.confusionMatrix = new ConfusionMatrix(this.model.getValidLabels());
		this.confusionMatrix.addData(classifiedData);
		
		return correct/classifiedData.size();
	}
	
	public ConfusionMatrix getConfusionMatrix() {
		return this.confusionMatrix;
	}
}
