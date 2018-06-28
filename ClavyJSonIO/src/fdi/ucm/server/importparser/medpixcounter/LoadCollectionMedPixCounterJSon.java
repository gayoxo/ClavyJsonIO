/**
 * 
 */
package fdi.ucm.server.importparser.medpixcounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class LoadCollectionMedPixCounterJSon{

	
//	private List<CompleteElementTypetopicIDTC> ListTopicID;
//	private List<CompleteElementTypeencounterIDImage> ListImageEncounterTopics;
//	private List<CompleteElementTypeencounterIDImage> ListImageEncounter;
	private CompleteCollection CC;
	private ArrayList<String> Logs;
//	private CompleteLinkElementType encounterIDL;
	private HashSet<String> encounterID;
	private HashMap<String,List<JSONObject>> topicID;
	private long counter;
	private JSONArray SalidaAA;
	private StringBuffer Misal;
//	private CompleteLinkElementType encounterIDLC;
//	private CompleteLinkElementType topicIDTC;
	public static boolean consoleDebug=false;
//	private CompleteLinkElementType topicIDIDLC;
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoadCollectionMedPixCounterJSon LC=new LoadCollectionMedPixCounterJSon();
		LoadCollectionMedPixCounterJSon.consoleDebug=true;
		
		
		
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

	

	public CompleteCollectionAndLog processCollecccion(ArrayList<String> dateEntrada) {
		try {
			CompleteCollectionAndLog Salida=new CompleteCollectionAndLog();
			CC=new CompleteCollection("MedPix", new Date()+"");
			Salida.setCollection(CC);
			Logs=new ArrayList<String>();
			Salida.setLogLines(Logs);
			encounterID=new  HashSet<String>();
			topicID=new HashMap<String,List<JSONObject>>();
//			ListImageEncounter=new ArrayList<CompleteElementTypeencounterIDImage>();
//			ListImageEncounterTopics=new ArrayList<CompleteElementTypeencounterIDImage>();
//			ListTopicID=new ArrayList<CompleteElementTypetopicIDTC>();
			
			counter=0L;
			SalidaAA = new JSONArray();
			Misal=new StringBuffer();
			
			
			ProcesaCasos();
			ProcesaCasoID();
			
			
			
			System.out.println("D->"+encounterID.size());
			System.out.println("P->"+counter);
			System.out.println("M->"+Math.round(counter/encounterID.size()));
			
			try {
				PrintWriter writer = new PrintWriter("OutPretty.txt", "UTF-8");
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonParser jp = new JsonParser();
				JsonElement je = jp.parse(SalidaAA.toString());
				String prettyJsonString = gson.toJson(je);
				writer.println(prettyJsonString);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ProcesaTopics();
			
			try {
				PrintWriter writer = new PrintWriter("OutPretty2.txt", "UTF-8");
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonParser jp = new JsonParser();
				JsonElement je = jp.parse(SalidaAA.toString());
				String prettyJsonString = gson.toJson(je);
				writer.println(prettyJsonString);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			try {
				PrintWriter writer = new PrintWriter("OutPrettyText.txt", "UTF-8");
				writer.println(Misal.toString());
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return Salida;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}




	private void ProcesaTopics() {

		ProcesaValoresTopics();
		
	}
	
	
	private void ProcesaValoresTopics() {
		
		HashSet<String> tabla= ProcesaGramaticaTopics();
		
		int Tot = topicID.entrySet().size();
		int ite=0;
		for (Entry<String, List<JSONObject>> Entryvalues : topicID.entrySet()) {
			
			System.out.println(ite++ + "/"+Tot);
			
			String IDvalues=Entryvalues.getKey();
			List<JSONObject> IDDoc=Entryvalues.getValue();
			try {
	        	URL F=new URL("https://medpix.nlm.nih.gov/rest/topic.json?topicID="+IDvalues);
	        	
	        	InputStream is = F.openStream();
	    	    
	    	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	    	      String jsonText = readAll(rd);
	    	      JSONObject json = new JSONObject(jsonText);

	    	      JSONObject add=new JSONObject();
	    	      JSONArray key=new JSONArray();
	      				
	      				for (String entryTabla : tabla) {
	      					String Valor;
	      						Valor= json.get(entryTabla).toString();

	      					 
	      					if (Valor!="null"&&!Valor.isEmpty())
	      					{
	      						if (entryTabla.startsWith("keyword"))
	      							key.put(Valor);
	      						else
	      							add.put(entryTabla,Valor);
	      						
	      					}
//	      					else
//	      						if (consoleDebug)
//	      						System.out.println("Topic (topicID: "+IDvalues+") : Error por falta de datos para parametro "+entryTabla );
//						
	      				}
	      				
	      				add.put("keyword", key);
	      				
	      				for (JSONObject jsonObject : IDDoc) 
	      					jsonObject.put("Topic", add);
						
	      				
	      		  

	         	  
	       	  
			} catch (Exception e) {
				e.printStackTrace();
				Logs.add("Error con la carga de documento->encounterID: "+IDvalues);
//				throw new RuntimeException("No tiene editor o los elementos son incorrectos");
			}
		}
	}

	
	private HashSet<String> ProcesaGramaticaTopics() {
		HashSet<String> Salida=new HashSet<String>();
		
//		Salida.add("topicID");
//		Salida.add("factoid");
//		Salida.add("preacr");
//		Salida.add("postacr");
//		Salida.add("acrCode");
//		Salida.add("reference");
		Salida.add("location");
		Salida.add("subLocation");
//		Salida.add("categoryID");
		Salida.add("subCategory");
//		Salida.add("subCategoryID");
//		Salida.add("author");
//		Salida.add("submitName");
//		Salida.add("submitID");
//		Salida.add("submitEmail");
//		Salida.add("submitAffiliation");
//		Salida.add("submitImage");
//		Salida.add("approverName");
//		Salida.add("approverID");
//		Salida.add("approverEmail");
//		Salida.add("approverAffiliation");
//		Salida.add("approverImage");
//		Salida.add("assignedName");
//		Salida.add("assignedAffiliation");
//		Salida.add("assignedImage");
		Salida.add("keyword1");
		Salida.add("keyword2");
		Salida.add("keyword3");
		Salida.add("title");
//		Salida.add("url");
//		Salida.add("relatedTopics");
//		Salida.add("contributorsCSV");
//		Salida.add("affiliation");
//		Salida.add("affiliationID");
//		Salida.add("affiliationLogo");
		
		return Salida;
	}
	
	

	private void ProcesaCasoID() {
		
		ProcesaValoresCasoIDJson();
		
	}



	
private void ProcesaValoresCasoIDJson() {
	
	
		HashSet<String> tabla = ProcesaGramaticaCasoID();
		int ite=1;
		for (String Entryvalues : encounterID) {
		
			
			System.out.println(ite++ + "/"+encounterID.size());
			
			String IDvalues=Entryvalues;
			
			JSONObject jod=new JSONObject();
			
			try {
			URL F=new URL("https://medpix.nlm.nih.gov/rest/encounter.json?encounterID="+IDvalues);
			
			InputStream is = F.openStream();
    	    
    	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
    	      String jsonText = readAll(rd);
    	      JSONObject json = new JSONObject(jsonText);

    	      
				
				for (String entryTabla : tabla) {
					String Valor = json.get(entryTabla).toString();
					
					if (Valor!="null"&&!Valor.isEmpty())
					{
					if (entryTabla.equals("topicID"))
						{
						

						List<JSONObject> Lista=topicID.get(Valor);
						if (Lista==null)
							Lista=new ArrayList<JSONObject>();
//						else
//							if (consoleDebug)
//								System.out.println("mas elementos para el valor->"+Valor);
						Lista.add(jod);
						
      					topicID.put(Valor, Lista);
						
						
						}
					else
					{
						
							
					
					jod.put(entryTabla,Valor);
					
					counter=counter+Valor.split("\\s+|\n").length;			
					
					Misal.append(Valor+"\n");
					
					
					}
					}
				}
				
				
				SalidaAA.put(jod);
	       	  
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



	
	
	
	private HashSet<String> ProcesaGramaticaCasoID() {
		HashSet<String> Salida=new HashSet<String>();
		
	
		Salida.add("encounterID");	
		Salida.add("dxHow");
		Salida.add("history");
//		Salida.add("age");
//		Salida.add("sex");
//		Salida.add("race");
		Salida.add("diagnosis");
		Salida.add("exam");
//		Salida.add("authorID");
//		Salida.add("authorName");
//		Salida.add("authorAffiliation");
//		Salida.add("authorImage");
//		Salida.add("authorEmail");
//		Salida.add("approverID");
//		Salida.add("approverEmail");
//		Salida.add("approverName");
//		Salida.add("approverAffiliation");
//		Salida.add("approverImage");
		Salida.add("findings");
		Salida.add("ddx");
		Salida.add("txFollowup");
		Salida.add("discussion");
		Salida.add("topicID");
		Salida.add("mCaseID");
//		Salida.add("error");
//		Salida.add("contributorsCSV");
//		Salida.add("affiliation");
//		Salida.add("affiliationID");
//		Salida.add("affiliationLogo");
//		Salida.add("mediaList");
		
		return Salida;
	}



}
