package corp.data.feature;

import corp.data.Gazetteer;
import corp.util.StringUtil;

/**
 * For organization mention m and gazetteer G, 
 * CorpRelFeatureGazetteerPrefixTokens computes
 * 
 * max_{g\in G} 1(O(m) shares k prefix tokens with G)
 * 
 * Or 
 * 
 * max_{g\in G} 1(A(m) shares k prefix tokens with G)
 * 
 * Where O(m) is the mentioned organization and A(m) is the authoring 
 * corporation.  Whether A(m) or O(m) is used is determined by the
 * "input type".
 * 
 * @author Bill McDowell
 *
 */
public class CorpRelFeatureGazetteerPrefixTokens  extends CorpRelFeatureGazetteer {
	private int minTokens;
	
	public CorpRelFeatureGazetteerPrefixTokens(Gazetteer gazetteer, CorpRelFeatureGazetteer.InputType inputType, int minTokens) {
		this.extremumType = CorpRelFeatureGazetteer.ExtremumType.Maximum;
		this.inputType = inputType;
		this.minTokens = minTokens;
		this.namePrefix = "PrefixTokens_Min" + this.minTokens;
		this.gazetteer = gazetteer;
	}

	@Override
	protected double extremum(String str) {
		double tokenPrefixCount = this.gazetteer.max(str, new StringUtil.StringPairMeasure() {
			public double compute(String str1, String str2) {
				return StringUtil.prefixTokenOverlap(str1, str2);
			}
		});
		
		if (tokenPrefixCount >= this.minTokens)
			return 1.0;
		else
			return 0.0;
	}

	@Override
	public CorpRelFeature clone() {
		return new CorpRelFeatureGazetteerPrefixTokens(this.gazetteer, this.inputType, this.minTokens);
	}
	
	@Override
	public String toString(boolean withInit) {
		return "GazetteerPrefixTokens(gazetteer=" + this.gazetteer.getName() + "Gazetteer, inputType=" + this.inputType + ", minTokens=" + this.minTokens + ")";
	}
}
