/**
 * 
 */
package fdi.ucm.server.importparser.medpix;

import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class CompleteElementTypetopicIDTC {

	private CompleteLinkElementType Element;
	private CompleteGrammar CG;
	private CompleteElementType padre;
	
	
	public CompleteElementTypetopicIDTC(String name, CompleteElementType topicID, CompleteGrammar cG) {
		Element=new CompleteLinkElementType(name,topicID,cG);
		this.CG=cG;
		this.padre=topicID;
		Element.setMultivalued(true);
	}
	
	public CompleteElementTypetopicIDTC(
			CompleteElementTypetopicIDTC completeElementTypeencounterIDImage) {
		Element=new CompleteLinkElementType(completeElementTypeencounterIDImage.getElement().getName(),completeElementTypeencounterIDImage.getPadre(),completeElementTypeencounterIDImage.getCG());
		this.CG=completeElementTypeencounterIDImage.getElement().getCollectionFather();
		Element.setMultivalued(true);
		Element.setClassOfIterator(completeElementTypeencounterIDImage.getElement());
	}
	
	public CompleteLinkElementType getElement() {
		return Element;
	}

	public void setElement(CompleteLinkElementType element) {
		Element = element;
	}

	public CompleteGrammar getCG() {
		return CG;
	}

	public void setCG(CompleteGrammar cG) {
		CG = cG;
	}

	public CompleteElementType getPadre() {
		return padre;
	}

	public void setPadre(CompleteElementType padre) {
		this.padre = padre;
	}
	
	

}
