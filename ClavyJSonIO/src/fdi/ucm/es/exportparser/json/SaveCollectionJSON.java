/**
 * 
 */
package fdi.ucm.es.exportparser.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.CompleteImportRuntimeException;
import fdi.ucm.server.modelComplete.SaveCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.CompleteLogAndUpdates;
import fdi.ucm.server.modelComplete.collection.document.CompleteFile;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;

/**
 * Clase que impementa el plugin de oda para Localhost
 * @author Joaquin Gayoso-Cabada
 *
 */
public class SaveCollectionJSON extends SaveCollection {

	private static final String JSON = "JSON Collection";
	private ArrayList<ImportExportPair> Parametros;
	private String Path;
	private String FileIO;
	private String SOURCE_FOLDER = ""; // SourceFolder path

	
	/**
	 * Constructor por defecto
	 */
		public SaveCollectionJSON() {
	}

	/* (non-Javadoc)
	 * @see fdi.ucm.server.SaveCollection#processCollecccion(fdi.ucm.shared.model.collection.Collection)
	 */
	@Override
	public CompleteLogAndUpdates processCollecccion(CompleteCollection Salvar,
			String PathTemporalFiles) throws CompleteImportRuntimeException{
		try {
			
			CompleteLogAndUpdates CL=new CompleteLogAndUpdates();
			

			Path=PathTemporalFiles;
			SOURCE_FOLDER=Path+"JSON"+File.separator;
			File Dir=new File(SOURCE_FOLDER);
			Dir.mkdirs();
			FileIO = Path+System.currentTimeMillis()+".json";
			
			String Salida = processCollection(Salvar);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(Salida);
			String prettyJsonString = gson.toJson(je);
			System.out.println(prettyJsonString);
//			HTMLprocess oda= new HTMLprocess(ListaDeDocumentos,Salvar,SOURCE_FOLDER,CL,TextoIn);
//			
//			
//			oda.preocess();
				

				CL.getLogLines().add("Descarga el zip");

				FileWriter fw=new FileWriter(FileIO);
				fw.write(prettyJsonString);
				fw.close(); 
			return CL;


		} catch (CompleteImportRuntimeException e) {
			System.err.println("Exception JSON " +e.getGENERIC_ERROR());
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			System.err.println("Exception HTML " +e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	private String processCollection(CompleteCollection salvar) {
		JSONObject Col=new JSONObject();
		try {
			Col.put(JSONNAMES.COLLECTION_ID, salvar.getClavilenoid());
			Col.put(JSONNAMES.COLLECTION_NAME, salvar.getName());
			Col.put(JSONNAMES.COLLECTION_DESC, salvar.getDescription());
			insertSections(Col,salvar);
			insertGrammars(Col,salvar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Col.toString();
	}

	private void insertGrammars(JSONObject col, CompleteCollection salvar) throws JSONException {
		JSONArray Gramm=new JSONArray();
		col.put(JSONNAMES.COLLECTION_GRAMMARS, Gramm);
		for (int i = 0; i < salvar.getMetamodelGrammar().size(); i++) {
			CompleteGrammar grammar = salvar.getMetamodelGrammar().get(i);
			JSONObject GrammarJ=new JSONObject();
			GrammarJ.put(JSONNAMES.GRAMMARS_ID, grammar.getClavilenoid());
			GrammarJ.put(JSONNAMES.GRAMMARS_NAME,grammar.getNombre());
			GrammarJ.put(JSONNAMES.GRAMMARS_DESC,grammar.getDescription());
			insertgrammarView(GrammarJ,grammar);
			insertgrammarSons(GrammarJ,grammar);
			Gramm.put(GrammarJ);
		}
		
	}

	private void insertgrammarSons(JSONObject grammarJ, CompleteGrammar grammar) throws JSONException {
		JSONArray Sons=new JSONArray();
		grammarJ.put(JSONNAMES.GRAMMARS_SONS, Sons);
		//TODO
	}

	private void insertgrammarView(JSONObject grammarJ, CompleteGrammar grammar) throws JSONException {
		JSONArray views=new JSONArray();
		grammarJ.put(JSONNAMES.GRAMMARS_VIEW, views);
		for (int i = 0; i < grammar.getViews().size(); i++) {
			CompleteOperationalValueType GOv = grammar.getViews().get(i);
			JSONObject Operational=new JSONObject();
			Operational.put(JSONNAMES.GRAMMARS_VIEW_ID, GOv.getClavilenoid());
			Operational.put(JSONNAMES.GRAMMARS_VIEW_NAME,GOv.getName());
			Operational.put(JSONNAMES.GRAMMARS_VIEW_DEF,GOv.getDefault());
			Operational.put(JSONNAMES.GRAMMARS_VIEW_VIEW,GOv.getView());
			views.put(Operational);
		}
	}

	private void insertSections(JSONObject col, CompleteCollection salvar) throws JSONException {
		JSONArray Sec=new JSONArray();
		col.put(JSONNAMES.COLLECTION_RESOURCES, Sec);
		for (int i = 0; i < salvar.getSectionValues().size(); i++) {
			CompleteFile file = salvar.getSectionValues().get(i);
			JSONObject File=new JSONObject();
			File.put(JSONNAMES.RESOURCES_ID, file.getClavilenoid());
			File.put(JSONNAMES.RESOURCES_PATH,file.getPath());
			Sec.put(File);
		}
		
	}

	/**
	 * QUitar caracteres especiales.
	 * @param str texto de entrada.
	 * @return texto sin caracteres especiales.
	 */
	public String RemoveSpecialCharacters(String str) {
		   StringBuilder sb = new StringBuilder();
		   for (int i = 0; i < str.length(); i++) {
			   char c = str.charAt(i);
			   if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_') {
			         sb.append(c);
			      }
		}
		   return sb.toString();
		}

	


	@Override
	public ArrayList<ImportExportPair> getConfiguracion() {
		if (Parametros==null)
		{
			ArrayList<ImportExportPair> ListaCampos=new ArrayList<ImportExportPair>();
//			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "Name of the export",true));
			Parametros=ListaCampos;
			return ListaCampos;
		}
		else return Parametros;
	}

	@Override
	public void setConfiguracion(ArrayList<String> DateEntrada) {
//		if (DateEntrada!=null)
//		{
//			
//
//			TextoIn=DateEntrada.get(0).trim();
//			
//
//		}
	}
		
//
//	private ArrayList<Long> generaListaDocuments(String string) {
//		String[] strings=string.split(",");
//		ArrayList<Long> Salida=new ArrayList<Long>();
//		for (String string2 : strings) {
//			try {
//				Long N=Long.parseLong(string2);
//				Salida.add(N);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//		return Salida;
//	}

	@Override
	public String getName() {
		return JSON;
	}


	@Override
	public boolean isFileOutput() {
		return true;
	}

	@Override
	public String FileOutput() {
		return FileIO;
	}

	@Override
	public void SetlocalTemporalFolder(String TemporalPath) {
		
	}

	
 
	
	public static void main(String[] args) {
		SaveCollectionJSON SV=new SaveCollectionJSON();
		CompleteCollection CC=CreateCollectionBase();
		SV.processCollecccion(CC, "");
	}

	private static CompleteCollection CreateCollectionBase() {
		CompleteCollection C=new CompleteCollection("nombre coleccion", "descripcion");
		C.setClavilenoid(1l);
		int n = (new Random()).nextInt(20);
		for (int i = 0; i < n; i++) 
			C.getSectionValues().add(new CompleteFile(new Long(i),"File"+ i+"path", C));
		
		List<CompleteFile> Files=new LinkedList<>(C.getSectionValues());
		
		int n2 = (new Random()).nextInt(20);
		for (int i = 0; i < n2; i++) 
			{	CompleteGrammar GG=new CompleteGrammar(new Long(i), "Gram"+ i+"name","Gram"+ i+"desc",C);
				

			int n3 = (new Random()).nextInt(10);
			for (int j = 0; j < n3; j++) 
				{	CompleteOperationalValueType OP=new CompleteOperationalValueType(new Long(j),"View"+ j+"name","View"+ j+"desc","View"+j+"padre");
					GG.getViews().add(OP);
				}
				
			
				C.getMetamodelGrammar().add(GG);
			}
		
		return C;
	}


	
	
	
}
