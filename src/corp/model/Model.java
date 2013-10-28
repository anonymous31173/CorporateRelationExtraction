package corp.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import corp.data.CorpDataTools;
import corp.data.annotation.CorpRelDatum;
import corp.data.annotation.CorpRelLabelPath;
import corp.data.feature.CorpRelFeaturizedDataSet;
import edu.stanford.nlp.util.Pair;

public abstract class Model {
	protected String modelPath;
	protected List<CorpRelLabelPath> validPaths;
	protected Map<String, Double> hyperParameters;
	
	public List<CorpRelLabelPath> getValidLabelPaths() {
		return this.validPaths;
	}
	
	public List<Pair<CorpRelDatum, CorpRelLabelPath>> classify(CorpRelFeaturizedDataSet data) {
		List<Pair<CorpRelDatum, CorpRelLabelPath>> classifiedData = new ArrayList<Pair<CorpRelDatum, CorpRelLabelPath>>();
		List<Pair<CorpRelDatum, Map<CorpRelLabelPath, Double>>> posterior = posterior(data);
	
		for (Pair<CorpRelDatum, Map<CorpRelLabelPath, Double>> datumPosterior : posterior) {
			Map<CorpRelLabelPath, Double> p = datumPosterior.second();
			double max = Double.NEGATIVE_INFINITY;
			CorpRelLabelPath argMax = null;
			for (Entry<CorpRelLabelPath, Double> entry : p.entrySet()) {
				if (entry.getValue() > max) {
					max = entry.getValue();
					argMax = entry.getKey();
				}
			}
			classifiedData.add(new Pair<CorpRelDatum, CorpRelLabelPath>(datumPosterior.first(), argMax));
		}
	
		return classifiedData;
	}
	
	public void setHyperParameters(Map<String, Double> values) {
		for (Entry<String, Double> entry : values.entrySet()) {
			setHyperParameter(entry.getKey(), entry.getValue());
		}
	}
	
	public void setHyperParameter(String parameter, double value) {
		this.hyperParameters.put(parameter, value);
	}
	
	public double getHyperParameter(String parameter) {
		return this.hyperParameters.get(parameter);
	}
	
	public boolean hasHyperParameter(String parameter) {
		return this.hyperParameters.containsKey(parameter);
	}
	
	public abstract boolean deserialize(String modelPath, CorpDataTools dataTools);
	public abstract boolean train(CorpRelFeaturizedDataSet data, String outputPath);
	public abstract List<Pair<CorpRelDatum, Map<CorpRelLabelPath, Double>>> posterior(CorpRelFeaturizedDataSet data);
	public abstract Model clone();

	protected boolean serializeParameters() {
		if (this.modelPath == null)
			return false;
		
        try {
    		BufferedWriter w = new BufferedWriter(new FileWriter(this.modelPath + ".p"));  		
    		
    		for (CorpRelLabelPath path : this.validPaths) {
    			w.write(path.toString() + "\t");
    		}
    		w.write("\n");
    		
    		for (Entry<String, Double> hyperParameter : this.hyperParameters.entrySet()) {
    			w.write(hyperParameter.getKey() + "\t" + hyperParameter.getValue() + "\n");
    		}
    		
            w.close();
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
	}
	
	protected boolean deserializeParameters() {
		if (this.modelPath == null)
			return false;
		
        try {
    		BufferedReader r = new BufferedReader(new FileReader(this.modelPath + ".p"));  		
    		
    		String validPathsLine = r.readLine();
    		String[] validPathStrs = validPathsLine.trim().split("\t");
    		this.validPaths = new ArrayList<CorpRelLabelPath>();
    		for (String validPathStr : validPathStrs) {
    			this.validPaths.add(CorpRelLabelPath.fromString(validPathStr));
    		}
    		
    		String hyperParameterLine = null;
    		this.hyperParameters = new HashMap<String, Double>();
    		while ((hyperParameterLine = r.readLine()) != null) {
    			String[] hyperParameterParts = hyperParameterLine.split("\t");
    			setHyperParameter(hyperParameterParts[0], Double.parseDouble(hyperParameterParts[1]));
    		}

    		r.close();
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
	}
}
