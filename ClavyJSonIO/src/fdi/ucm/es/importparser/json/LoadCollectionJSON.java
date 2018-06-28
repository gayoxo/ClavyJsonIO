/**
 * 
 */
package fdi.ucm.es.importparser.json;

import java.util.ArrayList;
import fdi.ucm.server.modelComplete.ImportExportDataEnum;
import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.LoadCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class LoadCollectionJSON extends LoadCollection{

	
	public static boolean consoleDebug=false;
	private ArrayList<ImportExportPair> Parametros;
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoadCollectionJSON LC=new LoadCollectionJSON();
		LoadCollectionJSON.consoleDebug=true;
		
		
		
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
		String message="Exception JSON-> Params Null ";
		try {
			if (dateEntrada!=null)
			{
				
				
				String fileName = dateEntrada.get(0);
				 System.out.println(fileName);
				 CompleteCollectionAndLog Salida=new CompleteCollectionAndLog();

				 return Salida;
			}
			else
			{
				System.err.println(message);
				throw new RuntimeException(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(message);
			throw new RuntimeException(message);
		}
		
		
//		try {
//			CompleteCollectionAndLog Salida=new CompleteCollectionAndLog();
//			CC=new CompleteCollection("MedPix", new Date()+"");
//			Salida.setCollection(CC);
//			Logs=new ArrayList<String>();
//			Salida.setLogLines(Logs);
//			encounterID=new HashMap<String,CompleteDocuments>();
//			topicID=new HashMap<String,List<CompleteDocuments>>();
//			ListImageEncounter=new ArrayList<CompleteElementTypeencounterIDImage>();
//			ListImageEncounterTopics=new ArrayList<CompleteElementTypeencounterIDImage>();
//			ListTopicID=new ArrayList<CompleteElementTypetopicIDTC>();
//			
//			ProcesaCasos();
//			ProcesaCasoID();
//			ProcesaTopics();
//			//AQUI se puede trabajar
//			
//			
//			return Salida;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
		
	}


	@Override
	public ArrayList<ImportExportPair> getConfiguracion() {
		if (Parametros==null)
		{
			ArrayList<ImportExportPair> ListaCampos=new ArrayList<ImportExportPair>();
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.File, "InputCollection in JSON",true));
			Parametros=ListaCampos;
			return ListaCampos;
		}
		else return Parametros;
	}

	@Override
	public String getName() {
		return "JSON Collection Import";
	}

	@Override
	public boolean getCloneLocalFiles() {
		return false;
	}

}
