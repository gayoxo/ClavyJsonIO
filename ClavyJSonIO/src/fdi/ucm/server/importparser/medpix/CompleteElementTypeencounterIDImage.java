/**
 * 
 */
package fdi.ucm.server.importparser.medpix;

import java.util.HashMap;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteResourceElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class CompleteElementTypeencounterIDImage {

	private CompleteElementType Element;
	private HashMap<String, CompleteElementType> TablaHijos;
	private CompleteGrammar CG;

	public CompleteElementTypeencounterIDImage(String name, CompleteGrammar cG) {
		Element=new CompleteElementType(name,cG);
		this.CG=cG;
		Element.setMultivalued(true);
		TablaHijos=new HashMap<String, CompleteElementType>();
		processHijos(new HashMap<String, CompleteElementType>());
	}

	public CompleteElementTypeencounterIDImage(
			CompleteElementTypeencounterIDImage completeElementTypeencounterIDImage) {
		Element=new CompleteElementType(completeElementTypeencounterIDImage.getElement().getName(),completeElementTypeencounterIDImage.getElement().getCollectionFather());
		this.CG=completeElementTypeencounterIDImage.getElement().getCollectionFather();
		Element.setMultivalued(true);
		Element.setClassOfIterator(completeElementTypeencounterIDImage.getElement());
		TablaHijos=new HashMap<String, CompleteElementType>();
		processHijos(completeElementTypeencounterIDImage.getTablaHijos());
	}

	private void processHijos(HashMap<String, CompleteElementType> TablaClassOf) {
		
		CompleteTextElementType imageID=new CompleteTextElementType("imageID",Element, CG);
		Element.getSons().add(imageID);
		TablaHijos.put("imageID", imageID);
		if (TablaClassOf.get("imageID")!=null)
			imageID.setClassOfIterator(TablaClassOf.get("imageID"));
		
		CompleteTextElementType tmpImageID=new CompleteTextElementType("tmpImageID",Element, CG);
		Element.getSons().add(tmpImageID);
		TablaHijos.put("tmpImageID", tmpImageID);
		if (TablaClassOf.get("tmpImageID")!=null)
			tmpImageID.setClassOfIterator(TablaClassOf.get("tmpImageID"));
		
		CompleteTextElementType imageCaption=new CompleteTextElementType("imageCaption",Element, CG);
		Element.getSons().add(imageCaption);
		TablaHijos.put("imageCaption", imageCaption);
		if (TablaClassOf.get("imageCaption")!=null)
			imageCaption.setClassOfIterator(TablaClassOf.get("imageCaption"));

		CompleteResourceElementType imageURL=new CompleteResourceElementType("imageURL",Element, CG);
		Element.getSons().add(imageURL);
		TablaHijos.put("imageURL", imageURL);
		if (TablaClassOf.get("imageURL")!=null)
			imageURL.setClassOfIterator(TablaClassOf.get("imageURL"));
		
		CompleteResourceElementType thumbImageURL=new CompleteResourceElementType("thumbImageURL",Element, CG);
		Element.getSons().add(thumbImageURL);
		TablaHijos.put("thumbImageURL", thumbImageURL);
		if (TablaClassOf.get("thumbImageURL")!=null)
			thumbImageURL.setClassOfIterator(TablaClassOf.get("thumbImageURL"));	
		
		CompleteTextElementType source=new CompleteTextElementType("source",Element, CG);
		Element.getSons().add(source);
		TablaHijos.put("source", source);
		if (TablaClassOf.get("source")!=null)
			source.setClassOfIterator(TablaClassOf.get("source"));
		
		CompleteTextElementType title=new CompleteTextElementType("title",Element, CG);
		Element.getSons().add(title);
		TablaHijos.put("title", title);
		if (TablaClassOf.get("title")!=null)
			title.setClassOfIterator(TablaClassOf.get("title"));
		
		CompleteTextElementType modality=new CompleteTextElementType("modality",Element, CG);
		Element.getSons().add(modality);
		TablaHijos.put("modality", modality);
		if (TablaClassOf.get("modality")!=null)
			modality.setClassOfIterator(TablaClassOf.get("modality"));
		
		CompleteTextElementType plane=new CompleteTextElementType("plane",Element, CG);
		Element.getSons().add(plane);
		TablaHijos.put("plane", plane);
		if (TablaClassOf.get("plane")!=null)
			plane.setClassOfIterator(TablaClassOf.get("plane"));
		
		CompleteTextElementType preacr=new CompleteTextElementType("preacr",Element, CG);
		Element.getSons().add(preacr);
		TablaHijos.put("plane", preacr);
		if (TablaClassOf.get("preacr")!=null)
			preacr.setClassOfIterator(TablaClassOf.get("preacr"));
		
		CompleteTextElementType postacr=new CompleteTextElementType("postacr",Element, CG);
		Element.getSons().add(postacr);
		TablaHijos.put("postacr", postacr);
		if (TablaClassOf.get("postacr")!=null)
			postacr.setClassOfIterator(TablaClassOf.get("postacr"));
		
		CompleteTextElementType acrCode=new CompleteTextElementType("acrCode",Element, CG);
		Element.getSons().add(acrCode);
		TablaHijos.put("acrCode", acrCode);
		if (TablaClassOf.get("acrCode")!=null)
			acrCode.setClassOfIterator(TablaClassOf.get("acrCode"));
		
		CompleteTextElementType authorID=new CompleteTextElementType("authorID",Element, CG);
		Element.getSons().add(authorID);
		TablaHijos.put("authorID", authorID);
		if (TablaClassOf.get("authorID")!=null)
			authorID.setClassOfIterator(TablaClassOf.get("authorID"));
		
		CompleteTextElementType authorName=new CompleteTextElementType("authorName",Element, CG);
		Element.getSons().add(authorName);
		TablaHijos.put("authorName", authorName);
		if (TablaClassOf.get("authorName")!=null)
			authorName.setClassOfIterator(TablaClassOf.get("authorName"));
		
		CompleteTextElementType authorAffiliation=new CompleteTextElementType("authorAffiliation",Element, CG);
		Element.getSons().add(authorAffiliation);
		TablaHijos.put("authorAffiliation", authorAffiliation);
		if (TablaClassOf.get("authorAffiliation")!=null)
			authorAffiliation.setClassOfIterator(TablaClassOf.get("authorAffiliation"));
		
		CompleteTextElementType authorImage=new CompleteTextElementType("authorImage",Element, CG);
		Element.getSons().add(authorImage);
		TablaHijos.put("authorImage", authorImage);
		if (TablaClassOf.get("authorImage")!=null)
			authorImage.setClassOfIterator(TablaClassOf.get("authorImage"));
		
		CompleteTextElementType authorEmail=new CompleteTextElementType("authorEmail",Element, CG);
		Element.getSons().add(authorEmail);
		TablaHijos.put("authorEmail", authorEmail);
		if (TablaClassOf.get("authorEmail")!=null)
			authorEmail.setClassOfIterator(TablaClassOf.get("authorEmail"));
		
		CompleteTextElementType approverID=new CompleteTextElementType("approverID",Element, CG);
		Element.getSons().add(approverID);
		TablaHijos.put("approverID", approverID);
		if (TablaClassOf.get("approverID")!=null)
			approverID.setClassOfIterator(TablaClassOf.get("approverID"));
		
		CompleteTextElementType approverEmail=new CompleteTextElementType("approverEmail",Element, CG);
		Element.getSons().add(approverEmail);
		TablaHijos.put("approverEmail", approverEmail);
		if (TablaClassOf.get("approverEmail")!=null)
			approverEmail.setClassOfIterator(TablaClassOf.get("approverEmail"));
		
		CompleteTextElementType approverName=new CompleteTextElementType("approverName",Element, CG);
		Element.getSons().add(approverName);
		TablaHijos.put("approverName", approverName);
		if (TablaClassOf.get("approverName")!=null)
			approverName.setClassOfIterator(TablaClassOf.get("approverName"));
		
		CompleteTextElementType approverAffiliation=new CompleteTextElementType("approverAffiliation",Element, CG);
		Element.getSons().add(approverAffiliation);
		TablaHijos.put("approverAffiliation", approverAffiliation);
		if (TablaClassOf.get("approverAffiliation")!=null)
			approverAffiliation.setClassOfIterator(TablaClassOf.get("approverAffiliation"));
		
		CompleteResourceElementType approverImage=new CompleteResourceElementType("approverImage",Element, CG);
		Element.getSons().add(approverImage);
		TablaHijos.put("approverImage", approverImage);
		if (TablaClassOf.get("approverImage")!=null)
			approverImage.setClassOfIterator(TablaClassOf.get("approverImage"));
		
		CompleteTextElementType figurePart=new CompleteTextElementType("figurePart",Element, CG);
		Element.getSons().add(figurePart);
		TablaHijos.put("figurePart", figurePart);
		if (TablaClassOf.get("figurePart")!=null)
			figurePart.setClassOfIterator(TablaClassOf.get("figurePart"));
		
		CompleteTextElementType error=new CompleteTextElementType("error",Element, CG);
		Element.getSons().add(error);
		TablaHijos.put("error", error);
		if (TablaClassOf.get("error")!=null)
			error.setClassOfIterator(TablaClassOf.get("error"));
		
		CompleteTextElementType size=new CompleteTextElementType("size",Element, CG);
		Element.getSons().add(size);
		TablaHijos.put("size", size);
		if (TablaClassOf.get("size")!=null)
			size.setClassOfIterator(TablaClassOf.get("size"));
		
		CompleteTextElementType imageName=new CompleteTextElementType("imageName",Element, CG);
		Element.getSons().add(imageName);
		TablaHijos.put("imageName", imageName);
		if (TablaClassOf.get("imageName")!=null)
			imageName.setClassOfIterator(TablaClassOf.get("imageName"));
		
		CompleteTextElementType show=new CompleteTextElementType("show",Element, CG);
		Element.getSons().add(show);
		TablaHijos.put("show", show);
		if (TablaClassOf.get("show")!=null)
			show.setClassOfIterator(TablaClassOf.get("show"));
		
		CompleteTextElementType deleted=new CompleteTextElementType("deleted",Element, CG);
		Element.getSons().add(deleted);
		TablaHijos.put("deleted", deleted);
		if (TablaClassOf.get("deleted")!=null)
			deleted.setClassOfIterator(TablaClassOf.get("deleted"));
		
		CompleteTextElementType encounter=new CompleteTextElementType("encounter",Element, CG);
		Element.getSons().add(encounter);
		TablaHijos.put("encounter", encounter);
		if (TablaClassOf.get("encounter")!=null)
			encounter.setClassOfIterator(TablaClassOf.get("encounter"));
		
		CompleteTextElementType topic=new CompleteTextElementType("topic",Element, CG);
		Element.getSons().add(topic);
		TablaHijos.put("topic", topic);
		if (TablaClassOf.get("topic")!=null)
			topic.setClassOfIterator(TablaClassOf.get("topic"));
		
		CompleteTextElementType added=new CompleteTextElementType("added",Element, CG);
		Element.getSons().add(added);
		TablaHijos.put("added", added);
		if (TablaClassOf.get("added")!=null)
			added.setClassOfIterator(TablaClassOf.get("added"));
		
	}

	public CompleteElementType getElement() {
		return Element;
	}

	public void setElement(CompleteElementType element) {
		Element = element;
	}

	public HashMap<String, CompleteElementType> getTablaHijos() {
		return TablaHijos;
	}

	public void setTablaHijos(HashMap<String, CompleteElementType> tablaHijos) {
		TablaHijos = tablaHijos;
	}


}
