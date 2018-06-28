/**
 * 
 */
package fdi.ucm.server.importparser.medpixbytopic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fdi.ucm.server.importparser.medpixnosimple.CompleteElementTypeencounterIDImage;
import fdi.ucm.server.importparser.medpixnosimple.CompleteElementTypetopicIDTC;
import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.LoadCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteLinkElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElementURL;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteResourceElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class LoadCollectionMedPixNoSimple extends LoadCollection{

	
	private List<CompleteElementTypetopicIDTC> ListTopicID;
	private List<CompleteElementTypeencounterIDImage> ListImageEncounterTopics;
	private List<CompleteElementTypeencounterIDImage> ListImageEncounter;
	private CompleteCollection CC;
	private ArrayList<String> Logs;
//	private CompleteLinkElementType encounterIDL;
	private HashSet<String> encounterID;
	private HashMap<String,List<CompleteDocuments>> topicID;
//	private CompleteLinkElementType encounterIDLC;
//	private CompleteLinkElementType topicIDTC;
	public static boolean consoleDebug=false;
	private CompleteLinkElementType topicIDIDLC;
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoadCollectionMedPixNoSimple LC=new LoadCollectionMedPixNoSimple();
		LoadCollectionMedPixNoSimple.consoleDebug=true;
		
		
		
		CompleteCollectionAndLog Salida=LC.processCollecccion(new ArrayList<String>());
		if (Salida!=null)
			{
			
			System.out.println("Correcto");
			
			for (String warning : Salida.getLogLines())
				System.err.println(warning);

			
			System.exit(0);
			
			}
		else
			{
			System.err.println("Error");
			System.exit(-1);
			}
	}

	

	@Override
	public CompleteCollectionAndLog processCollecccion(ArrayList<String> dateEntrada) {
		try {
			CompleteCollectionAndLog Salida=new CompleteCollectionAndLog();
			CC=new CompleteCollection("MedPix", new Date()+"");
			Salida.setCollection(CC);
			Logs=new ArrayList<String>();
			Salida.setLogLines(Logs);
			encounterID=new  HashSet<String>();
			topicID=new HashMap<String,List<CompleteDocuments>>();
			ListImageEncounter=new ArrayList<CompleteElementTypeencounterIDImage>();
			ListImageEncounterTopics=new ArrayList<CompleteElementTypeencounterIDImage>();
			ListTopicID=new ArrayList<CompleteElementTypetopicIDTC>();
			
			ProcesaCasos();
			ProcesaCasoID();
			ProcesaTopics();
			//AQUI se puede trabajar
			
			
			return Salida;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	private void ProcesaTopics() {
		CompleteGrammar CG=new CompleteGrammar("Topics", "Topics", CC);
		CC.getMetamodelGrammar().add(CG);
		
		HashMap<String,CompleteElementType> tabla= ProcesaGramaticaTopics(CG);
		ProcesaValoresTopics(tabla);
		
	}



	private void ProcesaValoresTopics(HashMap<String, CompleteElementType> tabla) {
		for (Entry<String, List<CompleteDocuments>> Entryvalues : topicID.entrySet()) {
			String IDvalues=Entryvalues.getKey();
			List<CompleteDocuments> IDDoc=Entryvalues.getValue();
			try {
	        	URL F=new URL("https://medpix.nlm.nih.gov/rest/topic?topicID="+IDvalues);
	       	 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      	  DocumentBuilder db = dbf.newDocumentBuilder();
	      	  Document doc = db.parse(F.openStream());
	      	  doc.getDocumentElement().normalize();
	      	     	
	      	  
	      	
	      	  
//	      	  NodeList nodeLstT = doc.getElementsByTagName("TopicRest");
	      		  Node fstNode = doc.getElementsByTagName("TopicRest").item(0);
	      		  if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
	      			  {
	      				Element eElement = (Element) fstNode;
	      				
	      				CompleteDocuments cd=new CompleteDocuments(CC, "", "");
	    				CC.getEstructuras().add(cd);
	      				
	      				
	      				for (Entry<String, CompleteElementType> entryTabla : tabla.entrySet()) {
	      					String Valor;
	      					try {
	      						Valor= eElement.getElementsByTagName(entryTabla.getKey()).item(0).getTextContent();
							} catch (Exception e) {
								System.err.println(F.toString());
								System.err.println(entryTabla.getKey());
								e.printStackTrace();
								throw e;
							}
	      					 
	      					if (Valor!=null&&!Valor.isEmpty())
	      					{
	      					if (entryTabla.getValue() instanceof CompleteTextElementType)
	      					{
	      						
	      						
	      						CompleteTextElement TE=new CompleteTextElement((CompleteTextElementType) entryTabla.getValue(), Valor);
	      						cd.getDescription().add(TE);
	      						TE.setDocumentsFather(cd);
	      						
	      						if (entryTabla.getKey().equals("title"))
	      							cd.setDescriptionText(Valor);
	      						
	      						
	      						
	      						
	      						if (entryTabla.getKey().equals("topicID"))
	      							{
	      							
	      							if (consoleDebug&&ListTopicID.size()<IDDoc.size())
	    	      						System.out.println((ListTopicID.size()+"-"+IDDoc.size()));
	      							
	      							while (ListTopicID.size()<IDDoc.size())
      			      			  	{
      			      				CompleteElementTypetopicIDTC cona = ListTopicID.get(0);
      			      			CompleteElementTypetopicIDTC nuevo = new CompleteElementTypetopicIDTC(cona);
      			      				ArrayList<CompleteElementType> nueva=new ArrayList<>();
      			      				
      			      				
      			      				boolean found=false;
      			      			boolean inserta=false;
      			      				boolean insertado=false;
      			      				for (CompleteElementType completeElementType : cona.getElement().getFather().getSons()) {
      			      					
      			      					if (completeElementType.getClassOfIterator()==null&&completeElementType==cona.getElement())
      			      						found=true;
      			      					else if (found&&(completeElementType.getClassOfIterator()==null||!completeElementType.getClassOfIterator().equals(cona.getElement())))
      			      						inserta=true;
									
											if (inserta)
												{
												nueva.add(nuevo.getElement());
												insertado=true;
												inserta=false;
												found=false;
												}
										
										nueva.add(completeElementType);
										
									}
      			      				
      			      				if (!insertado)
      			      					nueva.add(nuevo.getElement());
      			      				
      			      				cona.getElement().getFather().setSons(nueva);
//      			      				
      			      			ListTopicID.add(nuevo);
      			      				
      			      			  	}
	      							
	      							
	      							for (int j = 0; j < IDDoc.size(); j++) {
	      								
	      								if (j>0&&consoleDebug)
	      									System.out.println(IDDoc.size());
	      									
	      								
										CompleteDocuments completeDocuments=IDDoc.get(j);
										
	      								CompleteLinkElement CLEC=new CompleteLinkElement(topicIDIDLC, cd);
	      								completeDocuments.getDescription().add(CLEC);
		      							CLEC.setDocumentsFather(completeDocuments);
		      							
		      							
		      							
		      							
		      							 CompleteElementTypetopicIDTC ImageMio = ListTopicID.get(j);
		      							
		      							CompleteLinkElement CLE=new CompleteLinkElement(ImageMio.getElement(), completeDocuments);
		      							cd.getDescription().add(CLE);
		      							CLE.setDocumentsFather(cd);	
		      							
									}
	      							
	      							
	      							
	      							
	      							
	      							
	      							}
	      						
	      					}else if (entryTabla.getValue() instanceof CompleteResourceElementType)
	      					{
	      						CompleteResourceElementURL TE;
	      						if (entryTabla.getKey().equals("url"))
	      						{
	      							TE=new CompleteResourceElementURL((CompleteResourceElementType) entryTabla.getValue(), Valor);
	      						}
	      						else
	      						{
	      							TE=new CompleteResourceElementURL((CompleteResourceElementType) entryTabla.getValue(), "https://medpix.nlm.nih.gov"+Valor);
	      						}
	      						
	      						cd.getDescription().add(TE);
	      						TE.setDocumentsFather(cd);

//	      						if (entryTabla.getKey().equals("imageThumbURL"))
//	      							cd.setIcon("https://medpix.nlm.nih.gov"+Valor);
	      						
	      					}
	      						
	      						
	      					}
	      					else
	      						if (consoleDebug)
	      						System.out.println("Topic (topicID: "+IDvalues+") : Error por falta de datos para parametro "+entryTabla.getKey() );
						
	      				}
	      				
	      				
	      				try {
	      					NodeList ListaImagenes=((Element) eElement.getElementsByTagName("imageList").item(0)).getElementsByTagName("imageList");
	      					
	      					if (consoleDebug&&ListImageEncounterTopics.size()<ListaImagenes.getLength())
	      						System.out.println((ListImageEncounterTopics.size()+"-"+ListaImagenes.getLength()));
	      					
	      					while (ListImageEncounterTopics.size()<ListaImagenes.getLength())
			      			  	{
			      				CompleteElementTypeencounterIDImage cona = ListImageEncounterTopics.get(0);
			      				CompleteElementTypeencounterIDImage nuevo = new CompleteElementTypeencounterIDImage(cona);
			      				ArrayList<CompleteElementType> nueva=new ArrayList<>();
			      				
			      				
			      				boolean found=false;
			      				boolean inserta=false;
			      				boolean insertado=false;
			      				for (CompleteElementType completeElementType : cona.getElement().getCollectionFather().getSons()) {
			      					
			      					if (completeElementType.getClassOfIterator()==null&&completeElementType==cona.getElement())
			      						found=true;
			      					else
				      					if (found&&(completeElementType.getClassOfIterator()==null||!completeElementType.getClassOfIterator().equals(cona.getElement())))
				      						inserta=true;
								
			      					
			      					
									if (inserta)
									{
									nueva.add(nuevo.getElement());
									insertado=true;
									inserta=false;
									found=false;
									}
								
								nueva.add(completeElementType);
								
							}
			      				
			      				if (!insertado)
  			      					nueva.add(nuevo.getElement());
			      				
			      				cona.getElement().getCollectionFather().setSons(nueva);
			      				
			      			ListImageEncounterTopics.add(nuevo);
			      				
			      			  	}
	      					
	      					
	      					for (int i = 0; i < ListaImagenes.getLength(); i++) {
	      						 Node imagenNode = ListaImagenes.item(i);
	      			      		  if (imagenNode.getNodeType() == Node.ELEMENT_NODE) {
	      			      			  
	      			      			Element imagenNodeElem = (Element) imagenNode;
	      			      			  if (imagenNodeElem!=null)
	      			      			  {
	      			      			  
	      			      			  
	      			      			  CompleteElementTypeencounterIDImage ImageMio = ListImageEncounterTopics.get(i);
	      			      			  
	      			      			for (Entry<String, CompleteElementType> entryTabla : ImageMio.getTablaHijos().entrySet()) {
	      			      
	      			      	
	      		      					String Valor = imagenNodeElem.getElementsByTagName(entryTabla.getKey()).item(0).getTextContent();
	      		      					if (Valor!=null&&!Valor.isEmpty())
	      		      					{
	      		      					if (entryTabla.getValue() instanceof CompleteTextElementType)
	      		      					{
	      		      						CompleteTextElement TE=new CompleteTextElement((CompleteTextElementType) entryTabla.getValue(), Valor);
	      		      						cd.getDescription().add(TE);
	      		      						TE.setDocumentsFather(cd);
	      		      						
	      		      						
	      		      					}else if (entryTabla.getValue() instanceof CompleteResourceElementType)
	      		      					{
	      		      						CompleteResourceElementURL TE=new CompleteResourceElementURL((CompleteResourceElementType) entryTabla.getValue(), "https://medpix.nlm.nih.gov"+Valor);
	      		      						cd.getDescription().add(TE);
	      		      						TE.setDocumentsFather(cd);
	      		      						
	      		      					if (entryTabla.getKey().equals("assignedImage")&&i==0)
	    	      							cd.setIcon("https://medpix.nlm.nih.gov"+Valor);
	      		      						
	      		      						
	      		      					}
	      		      						
	      		      						
	      		      					
	      		      					
	      		      					}else
	      		      					if (consoleDebug)
	      		      						System.out.println("Topic (topicID: "+IDvalues+") : Error por falta de datos (imagenes) para parametro "+entryTabla.getKey() );

	      			      				
	      		      					
	      							
	      		      				}
	      			      			  
	      			      		  }
	      			      		  }
							}
						} catch (Exception e) {
							if (consoleDebug)
								e.printStackTrace();
							Logs.add("Error con la carga imagenes del documento->topicID: "+IDvalues);
						}
	
	      				
	      				
	      				
	      		  
	      		  }
	      	//	  ListaSer.add(fstNode.getFirstChild().getNodeValue());
	         	  }
	       	  
			} catch (Exception e) {
				e.printStackTrace();
				Logs.add("Error con la carga de documento->encounterID: "+IDvalues);
//				throw new RuntimeException("No tiene editor o los elementos son incorrectos");
			}
		}
	}



	private void ProcesaCasoID() {
		CompleteGrammar CG=new CompleteGrammar("CasosCompleto", "CasosCompleto", CC);
		CC.getMetamodelGrammar().add(CG);
		
		HashMap<String,CompleteElementType> tabla= ProcesaGramaticaCasoID(CG);
		ProcesaValoresCasoIDJson(tabla);
	}



	
private void ProcesaValoresCasoIDJson(HashMap<String, CompleteElementType> tabla) {
		
		System.out.println(encounterID.size());
		int ite=1;
		for (String Entryvalues : encounterID) {
		
			
			System.out.println(ite++ + "/"+encounterID.size());
			
			String IDvalues=Entryvalues;
			
			
			try {
			URL F=new URL("https://medpix.nlm.nih.gov/rest/encounter.json?encounterID="+IDvalues);
			
			InputStream is = F.openStream();
    	    
    	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
    	      String jsonText = readAll(rd);
    	      JSONObject json = new JSONObject(jsonText);

    	      CompleteDocuments cd=new CompleteDocuments(CC, "", "");
				CC.getEstructuras().add(cd);
    	      
				
				for (Entry<String, CompleteElementType> entryTabla : tabla.entrySet()) {
					String Valor = json.get(entryTabla.getKey()).toString();
					if (Valor!="null"&&!Valor.isEmpty())
					{
						if (entryTabla.getValue() instanceof CompleteTextElementType)
      					{
      						CompleteTextElement TE=new CompleteTextElement((CompleteTextElementType) entryTabla.getValue(), Valor);
      						cd.getDescription().add(TE);
      						TE.setDocumentsFather(cd);
      						
      						if (entryTabla.getKey().equals("history"))
      							cd.setDescriptionText(Valor);
      						
      						
      						
      						
//      						if (entryTabla.getKey().equals("encounterID"))
//      							{
//      							
//      							CompleteLinkElement CLE=new CompleteLinkElement(encounterIDLC, IDDoc);
//      							cd.getDescription().add(CLE);
//      							CLE.setDocumentsFather(cd);	
//      							
//      							CompleteLinkElement CLEC=new CompleteLinkElement(encounterIDL, cd);
//      							IDDoc.getDescription().add(CLEC);
//      							CLEC.setDocumentsFather(IDDoc);
//      							
//      							
//      							}
      						
      						
      						if (entryTabla.getKey().equals("topicID"))
  							{
      						List<CompleteDocuments> Lista=topicID.get(Valor);
      						if (Lista==null)
      							Lista=new ArrayList<CompleteDocuments>();
      						else
      							if (consoleDebug)
      								System.out.println("mas elementos para el valor->"+Valor);
      						Lista.add(cd);
      						
		      					topicID.put(Valor, Lista);
  							}
      						
      					}else if (entryTabla.getValue() instanceof CompleteResourceElementType)
      					{
      						CompleteResourceElementURL TE=new CompleteResourceElementURL((CompleteResourceElementType) entryTabla.getValue(), "https://medpix.nlm.nih.gov"+Valor);
      						cd.getDescription().add(TE);
      						TE.setDocumentsFather(cd);
      						
//      						if (entryTabla.getKey().equals("imageThumbURL"))
//      							cd.setIcon("https://medpix.nlm.nih.gov"+Valor);
      						
      					}
      						
      						
      					}
      					else
      						if (consoleDebug)
      						System.out.println("Documento (encounterID: "+IDvalues+") : Error por falta de datos para parametro "+entryTabla.getKey() );
					
					
				}
				
				
				JSONArray imagenes = json.getJSONArray("imageList");
				
				for (int i = 0; i < imagenes.length(); i++) {
						 
			      			  

			      			  while (ListImageEncounter.size()<=i)
			      			  	{
			      				CompleteElementTypeencounterIDImage cona = ListImageEncounter.get(0);
			      				CompleteElementTypeencounterIDImage nuevo = new CompleteElementTypeencounterIDImage(cona);
			      				ArrayList<CompleteElementType> nueva=new ArrayList<>();
			      				
			      				
			      				boolean found=false;
			      			boolean insertado=false;
			      		boolean inserta=false;
			      				for (CompleteElementType completeElementType : cona.getElement().getCollectionFather().getSons()) {
			      					
			      					if (completeElementType.getClassOfIterator()==null&&completeElementType==cona.getElement())
			      						found=true;
			      				
			      					else if (found&&(completeElementType.getClassOfIterator()==null||!completeElementType.getClassOfIterator().equals(cona.getElement())))
	      						inserta=true;
			      				
			      				
									if (inserta)
									{
									nueva.add(nuevo.getElement());
									insertado=true;
									inserta=false;
									found=false;
									}
								
								nueva.add(completeElementType);
								
							}
			      				
			      			if (!insertado)
		      					nueva.add(nuevo.getElement());
			      				
			      				cona.getElement().getCollectionFather().setSons(nueva);
			      				
			      				ListImageEncounter.add(nuevo);
			      				
			      			  	}
			      			  
			      			  
			      			  
			      			  
			      			JSONObject imagenNode = imagenes.getJSONObject(i);
			      			  CompleteElementTypeencounterIDImage ImageMio = ListImageEncounter.get(i);
			      			  
			      			for (Entry<String, CompleteElementType> entryTabla : ImageMio.getTablaHijos().entrySet()) {
			      
			      				String Valor = imagenNode.get(entryTabla.getKey()).toString();
		      					if (Valor!=null&&!Valor.isEmpty())
		      					{
		      					if (entryTabla.getValue() instanceof CompleteTextElementType)
		      					{
		      						CompleteTextElement TE=new CompleteTextElement((CompleteTextElementType) entryTabla.getValue(), Valor);
		      						cd.getDescription().add(TE);
		      						TE.setDocumentsFather(cd);
		      						
 		      					
		      						
		      					}else if (entryTabla.getValue() instanceof CompleteResourceElementType)
		      					{
		      						CompleteResourceElementURL TE=new CompleteResourceElementURL((CompleteResourceElementType) entryTabla.getValue(), "https://medpix.nlm.nih.gov"+Valor);
		      						cd.getDescription().add(TE);
		      						TE.setDocumentsFather(cd);
		      						
		      					if (entryTabla.getKey().equals("thumbImageURL")&&i==0)
     							cd.setIcon("https://medpix.nlm.nih.gov"+Valor);
		      						
		      						
		      					}
		      						
		      						
		      					
		      					
		      					}else
		      					if (consoleDebug)
		      						System.out.println("Documento (encounterID: "+IDvalues+") : Error por falta de datos (imagenes) para parametro "+entryTabla.getKey() );

			      				
			      		  }
				}
				
				
				JSONArray media = json.getJSONArray("mediaList");
				System.out.println(media.toString());
				
				
	      
	       	  
			} catch (Exception e) {
				e.printStackTrace();
				Logs.add("Error con la carga de documento->encounterID: "+IDvalues);
//				throw new RuntimeException("No tiene editor o los elementos son incorrectos");
			}
		}
		
		
		
	}
	





	private void ProcesaCasos() {


		ProcesaValores();
		
	}



	private void ProcesaValores() {
		
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		
		for (char c : alphabet) {
			 try {
		        	URL F=new URL("https://medpix.nlm.nih.gov/rest/encounter/diagnosis.json?diagnosis="+c);
		        	
		        	System.out.println(F.toString());
		        	
		        	 InputStream is = F.openStream();
		        	    try {
		        	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		        	      String jsonText = readAll(rd);
//		        	      System.out.println(jsonText);
		        	      JSONArray json = new JSONArray(jsonText);
//		        	      System.out.println(json.toString());
//		        	      System.out.println(json.get("encounterID"));
		        	      for (int i = 0; i < json.length(); i++) {
		        	    	  JSONObject JO=json.getJSONObject(i);
		        	    	  if (JO.get("encounterID")!=null)
		        	    		  encounterID.add(JO.get("encounterID").toString());
						}
		        	      
		        	    } finally {
		        	      is.close();
		        	    }
		       	  
				} catch (Exception e) {
					if (consoleDebug)
					e.printStackTrace();
					Logs.add("Error con la carga de listas de documento");
//					throw new RuntimeException("No tiene editor o los elementos son incorrectos");
				}
		}
		
       
        
	}

	 private static String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
		  }



	private HashMap<String, CompleteElementType> ProcesaGramaticaTopics(CompleteGrammar cG) {
		HashMap<String, CompleteElementType> Salida=new HashMap<String, CompleteElementType>();
		
		CompleteElementType topicID=new CompleteElementType("topicID", cG);
		cG.getSons().add(topicID);

		
		CompleteTextElementType topicIDT=new CompleteTextElementType("topicID", topicID, cG);
		topicID.getSons().add(topicIDT);
		Salida.put("topicID", topicIDT);
		
		
		CompleteElementTypetopicIDTC CDTIC=new CompleteElementTypetopicIDTC("Cases", topicID, cG);
		topicID.getSons().add(CDTIC.getElement());
		
		ListTopicID.add(CDTIC);
		
		
		CompleteTextElementType factoid=new CompleteTextElementType("factoid", cG);
		cG.getSons().add(factoid);
		Salida.put("factoid", factoid);
		
		CompleteTextElementType preacr=new CompleteTextElementType("preacr", cG);
		cG.getSons().add(preacr);
		Salida.put("preacr", preacr);
		
		CompleteTextElementType postacr=new CompleteTextElementType("postacr", cG);
		cG.getSons().add(postacr);
		Salida.put("postacr", postacr);
		
		CompleteTextElementType acrCode=new CompleteTextElementType("acrCode", cG);
		cG.getSons().add(acrCode);
		Salida.put("acrCode", acrCode);
		
		CompleteTextElementType reference=new CompleteTextElementType("reference", cG);
		cG.getSons().add(reference);
		Salida.put("reference", reference);
		
		CompleteTextElementType location=new CompleteTextElementType("location", cG);
		cG.getSons().add(location);
		Salida.put("location", location);
		
		CompleteTextElementType subLocation=new CompleteTextElementType("subLocation", cG);
		cG.getSons().add(subLocation);
		Salida.put("subLocation", subLocation);
		
		CompleteTextElementType categoryID=new CompleteTextElementType("categoryID", cG);
		cG.getSons().add(categoryID);
		Salida.put("categoryID", categoryID);
		
		CompleteTextElementType subCategory=new CompleteTextElementType("subCategory", cG);
		cG.getSons().add(subCategory);
		Salida.put("subCategory", subCategory);
		
		CompleteTextElementType subCategoryID=new CompleteTextElementType("subCategoryID", cG);
		cG.getSons().add(subCategoryID);
		Salida.put("subCategoryID", subCategoryID);
		
		CompleteTextElementType author=new CompleteTextElementType("author", cG);
		cG.getSons().add(author);
		Salida.put("author", author);
				
		CompleteTextElementType submitName=new CompleteTextElementType("submitName", cG);
		cG.getSons().add(submitName);
		Salida.put("submitName", submitName);
		
		CompleteTextElementType submitID=new CompleteTextElementType("submitID", cG);
		cG.getSons().add(submitID);
		Salida.put("submitID", submitID);
		
		CompleteTextElementType submitEmail=new CompleteTextElementType("submitEmail", cG);
		cG.getSons().add(submitEmail);
		Salida.put("submitEmail", submitEmail);
		
		CompleteTextElementType submitAffiliation=new CompleteTextElementType("submitAffiliation", cG);
		cG.getSons().add(submitAffiliation);
		Salida.put("submitAffiliation", submitAffiliation);
		
		CompleteResourceElementType submitImage=new CompleteResourceElementType("submitImage", cG);
		cG.getSons().add(submitImage);
		Salida.put("submitImage", submitImage);
		
		CompleteTextElementType approverName=new CompleteTextElementType("approverName", cG);
		cG.getSons().add(approverName);
		Salida.put("approverName", approverName);
		
		CompleteTextElementType approverID=new CompleteTextElementType("approverID", cG);
		cG.getSons().add(approverID);
		Salida.put("approverID", approverID);
		
		CompleteTextElementType approverEmail=new CompleteTextElementType("approverEmail", cG);
		cG.getSons().add(approverEmail);
		Salida.put("approverEmail", approverEmail);
		
		CompleteTextElementType approverAffiliation=new CompleteTextElementType("approverAffiliation", cG);
		cG.getSons().add(approverAffiliation);
		Salida.put("approverAffiliation", approverAffiliation);
		
		CompleteResourceElementType approverImage=new CompleteResourceElementType("approverImage", cG);
		cG.getSons().add(approverImage);
		Salida.put("approverImage", approverImage);		
	
		CompleteTextElementType assignedName=new CompleteTextElementType("assignedName", cG);
		cG.getSons().add(assignedName);
		Salida.put("assignedName", assignedName);
		
		CompleteTextElementType assignedAffiliation=new CompleteTextElementType("assignedAffiliation", cG);
		cG.getSons().add(assignedAffiliation);
		Salida.put("assignedAffiliation", assignedAffiliation);
		
		CompleteResourceElementType assignedImage=new CompleteResourceElementType("assignedImage", cG);
		cG.getSons().add(assignedImage);
		Salida.put("assignedImage", assignedImage);
		
		CompleteTextElementType keyword1=new CompleteTextElementType("keyword", cG);
		cG.getSons().add(keyword1);
		Salida.put("keyword1", keyword1);
		
		CompleteTextElementType keyword2=new CompleteTextElementType("keyword", cG);
		keyword2.setClassOfIterator(keyword1);
		cG.getSons().add(keyword2);
		Salida.put("keyword2", keyword2);
		
		CompleteTextElementType keyword3=new CompleteTextElementType("keyword", cG);
		keyword3.setClassOfIterator(keyword1);
		cG.getSons().add(keyword3);
		Salida.put("keyword3", keyword3);
		
		CompleteTextElementType title=new CompleteTextElementType("title", cG);
		cG.getSons().add(title);
		Salida.put("title", title);
		
		CompleteResourceElementType url=new CompleteResourceElementType("url", cG);
		cG.getSons().add(url);
		Salida.put("url", url);
		
		CompleteTextElementType relatedTopics=new CompleteTextElementType("relatedTopics", cG);
		cG.getSons().add(relatedTopics);
		Salida.put("relatedTopics", relatedTopics);
		
		CompleteElementTypeencounterIDImage imageList=new CompleteElementTypeencounterIDImage("imageList", cG);
		cG.getSons().add(imageList.getElement());		
		ListImageEncounterTopics.add(imageList);
		
		CompleteTextElementType contributorsCSV=new CompleteTextElementType("contributorsCSV", cG);
		cG.getSons().add(contributorsCSV);
		Salida.put("contributorsCSV", contributorsCSV);
		
		CompleteTextElementType affiliation=new CompleteTextElementType("affiliation", cG);
		cG.getSons().add(affiliation);
		Salida.put("affiliation", affiliation);
		
		CompleteTextElementType affiliationID=new CompleteTextElementType("affiliationID", cG);
		cG.getSons().add(affiliationID);
		Salida.put("affiliationID", affiliationID);
		
		CompleteResourceElementType affiliationLogo=new CompleteResourceElementType("affiliationLogo", cG);
		cG.getSons().add(affiliationLogo);
		Salida.put("affiliationLogo", affiliationLogo);
		
		return Salida;
	}
	
	
	
	private HashMap<String, CompleteElementType> ProcesaGramaticaCasoID(CompleteGrammar cG) {
		HashMap<String, CompleteElementType> Salida=new HashMap<String, CompleteElementType>();
		
		CompleteElementType encounterID=new CompleteElementType("encounterID", cG);
		cG.getSons().add(encounterID);

		
		CompleteTextElementType encounterIDT=new CompleteTextElementType("encounterID", encounterID, cG);
		encounterID.getSons().add(encounterIDT);
		Salida.put("encounterID", encounterIDT);
		

		
		CompleteTextElementType dxHow=new CompleteTextElementType("dxHow", cG);
		cG.getSons().add(dxHow);
		Salida.put("dxHow", dxHow);
		
		CompleteTextElementType history=new CompleteTextElementType("history", cG);
		cG.getSons().add(history);
		Salida.put("history", history);
		
		CompleteTextElementType age=new CompleteTextElementType("age", cG);
		cG.getSons().add(age);
		Salida.put("age", age);
		
		CompleteTextElementType sex=new CompleteTextElementType("sex", cG);
		cG.getSons().add(sex);
		Salida.put("sex", sex);
		
		CompleteTextElementType race=new CompleteTextElementType("race", cG);
		cG.getSons().add(race);
		Salida.put("race", race);
		
		CompleteTextElementType diagnosis=new CompleteTextElementType("diagnosis", cG);
		cG.getSons().add(diagnosis);
		Salida.put("diagnosis", diagnosis);
		
		CompleteTextElementType exam=new CompleteTextElementType("exam", cG);
		cG.getSons().add(exam);
		Salida.put("exam", exam);
		
		CompleteTextElementType authorID=new CompleteTextElementType("authorID", cG);
		cG.getSons().add(authorID);
		Salida.put("authorID", authorID);
		
		CompleteTextElementType authorName=new CompleteTextElementType("authorName", cG);
		cG.getSons().add(authorName);
		Salida.put("authorName", authorName);
		
		CompleteTextElementType authorAffiliation=new CompleteTextElementType("authorAffiliation", cG);
		cG.getSons().add(authorAffiliation);
		Salida.put("authorAffiliation", authorAffiliation);
		
		CompleteResourceElementType authorImage=new CompleteResourceElementType("authorImage", cG);
		cG.getSons().add(authorImage);
		Salida.put("authorImage", authorImage);
				
		CompleteTextElementType authorEmail=new CompleteTextElementType("authorEmail", cG);
		cG.getSons().add(authorEmail);
		Salida.put("authorEmail", authorEmail);
		
		CompleteTextElementType approverID=new CompleteTextElementType("approverID", cG);
		cG.getSons().add(approverID);
		Salida.put("approverID", approverID);
		
		CompleteTextElementType approverEmail=new CompleteTextElementType("approverEmail", cG);
		cG.getSons().add(approverEmail);
		Salida.put("approverEmail", approverEmail);
		
		CompleteTextElementType approverName=new CompleteTextElementType("approverName", cG);
		cG.getSons().add(approverName);
		Salida.put("approverName", approverName);
		
		CompleteTextElementType approverAffiliation=new CompleteTextElementType("approverAffiliation", cG);
		cG.getSons().add(approverAffiliation);
		Salida.put("approverAffiliation", approverAffiliation);
		
		CompleteResourceElementType approverImage=new CompleteResourceElementType("approverImage", cG);
		cG.getSons().add(approverImage);
		Salida.put("approverImage", approverImage);
		
		CompleteTextElementType findings=new CompleteTextElementType("findings", cG);
		cG.getSons().add(findings);
		Salida.put("findings", findings);
		
		CompleteTextElementType ddx=new CompleteTextElementType("ddx", cG);
		cG.getSons().add(ddx);
		Salida.put("ddx", ddx);
		
		CompleteTextElementType txFollowup=new CompleteTextElementType("txFollowup", cG);
		cG.getSons().add(txFollowup);
		Salida.put("txFollowup", txFollowup);
		
		CompleteTextElementType discussion=new CompleteTextElementType("discussion", cG);
		cG.getSons().add(discussion);
		Salida.put("discussion", discussion);
		
		
		CompleteElementType topicID=new CompleteElementType("topicID", cG);
		cG.getSons().add(topicID);

		
		CompleteTextElementType topicIDT=new CompleteTextElementType("topicIDT", topicID, cG);
		topicID.getSons().add(topicIDT);
		Salida.put("topicID", topicIDT);
		
		topicIDIDLC = new CompleteLinkElementType("topicID", topicID, cG);
		topicID.getSons().add(topicIDIDLC);
		
	
		CompleteTextElementType mCaseID=new CompleteTextElementType("mCaseID", cG);
		cG.getSons().add(mCaseID);
		Salida.put("mCaseID", mCaseID);
		
		CompleteElementTypeencounterIDImage imageList=new CompleteElementTypeencounterIDImage("imageList", cG);
		cG.getSons().add(imageList.getElement());
		
		ListImageEncounter.add(imageList);
			
		CompleteTextElementType error=new CompleteTextElementType("error", cG);
		cG.getSons().add(error);
		Salida.put("error", error);
		
		CompleteTextElementType contributorsCSV=new CompleteTextElementType("contributorsCSV", cG);
		cG.getSons().add(contributorsCSV);
		Salida.put("contributorsCSV", contributorsCSV);
		
		CompleteTextElementType affiliation=new CompleteTextElementType("affiliation", cG);
		cG.getSons().add(affiliation);
		Salida.put("affiliation", affiliation);
		
		CompleteTextElementType affiliationID=new CompleteTextElementType("affiliationID", cG);
		cG.getSons().add(affiliationID);
		Salida.put("affiliationID", affiliationID);
		
		CompleteResourceElementType affiliationLogo=new CompleteResourceElementType("affiliationLogo", cG);
		cG.getSons().add(affiliationLogo);
		Salida.put("affiliationLogo", affiliationLogo);
		
		CompleteResourceElementType mediaList=new CompleteResourceElementType("mediaList", cG);
		cG.getSons().add(mediaList);
		Salida.put("mediaList", mediaList);
		
		return Salida;
	}


	@Override
	public ArrayList<ImportExportPair> getConfiguracion() {
		return new ArrayList<ImportExportPair>();
	}

	@Override
	public String getName() {
		return "MedPix Complete (Sin Ficha Simple)";
	}

	@Override
	public boolean getCloneLocalFiles() {
		return false;
	}

}
