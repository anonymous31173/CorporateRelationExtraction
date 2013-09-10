package corp.data.annotation;

import java.util.Collection;
import java.util.List;

public class CorpRelDatum {
	private String authorCorpName;
	private List<CorpDocumentTokenSpan> otherOrgTokenSpans;
	private CorpRelLabel[] labelPath;
	
	public CorpRelDatum(String authorCorpName, List<CorpDocumentTokenSpan> otherOrgTokenSpans) {
		if (otherOrgTokenSpans.size() == 0)
			throw new IllegalArgumentException();
		
		this.authorCorpName = authorCorpName;
		this.otherOrgTokenSpans = otherOrgTokenSpans;
	}
	
	public CorpRelDatum(CorpRelDatum copy) {
		this.authorCorpName = copy.authorCorpName;
		this.otherOrgTokenSpans = copy.otherOrgTokenSpans;
		this.labelPath = copy.labelPath;
	}
	
	public boolean setLabelPath(CorpRelLabel[] labelPath) {
		this.labelPath = labelPath;
		return true;
	}
	
	public String getAuthorCorpName() {
		return this.authorCorpName;
	}
	
	public List<CorpDocumentTokenSpan> getOtherOrgTokenSpans() {
		return this.otherOrgTokenSpans;
	}
	
	public CorpDocument getDocument() {
		return this.otherOrgTokenSpans.get(0).getDocument();
	}
	
	public CorpRelLabel[] getLabelPath() {
		return this.labelPath;
	}
	
	public CorpRelLabel getLastLabel() {
		if (this.labelPath == null)
			return null;
		return this.labelPath[this.labelPath.length-1];
	}
	
	public CorpRelLabel getLabel(Collection<CorpRelLabel> validLabels) {
		for (CorpRelLabel label : this.labelPath) {
			if (validLabels.contains(label))
				return label;
		}
		
		return null;
	}
	
	public String toString() {
		StringBuilder datumStr = new StringBuilder();
		
		if (this.labelPath != null) {
			datumStr.append("Label: ");
			
			for (CorpRelLabel label : this.labelPath)
				datumStr.append(label).append("-");
			
			datumStr.delete(datumStr.length() - 1, datumStr.length());
			datumStr.append("\n");
		}
		
		datumStr.append("Author Corporation: " + this.authorCorpName).append("\n");
		datumStr.append("Mentioned Organizations: \n");
		for (CorpDocumentTokenSpan tokenSpan : this.otherOrgTokenSpans) {
			datumStr.append(tokenSpan.toString());
			datumStr.append("\n");
		}
		
		return datumStr.toString();
	}
}
