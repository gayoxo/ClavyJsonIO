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
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteFile;
import fdi.ucm.server.modelComplete.collection.document.CompleteLinkElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteOperationalValue;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElementFile;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElementURL;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteResourceElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

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
	private String Salida;

	
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
			
			Salida = processCollection(Salvar);
			
			System.out.println("ho");
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(Salida);
			String prettyJsonString = gson.toJson(je);
			
			Salida=prettyJsonString;
//			HTMLprocess oda= new HTMLprocess(ListaDeDocumentos,Salvar,SOURCE_FOLDER,CL,TextoIn);
//			
//			
//			oda.preocess();
				

				CL.getLogLines().add("Descarga el zip");

				FileWriter fw=new FileWriter(FileIO);
				fw.write(Salida);
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
			insertDocuments(Col,salvar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Col.toString();
	}

	private void insertDocuments(JSONObject col, CompleteCollection salvar) throws JSONException {
		JSONArray docsL=new JSONArray();
		col.put(JSONNAMES.COLLECTION_DOCUMENTS, docsL);
		for (int i = 0; i < salvar.getEstructuras().size(); i++) {
			CompleteDocuments documento = salvar.getEstructuras().get(i);
			JSONObject DocumentJ=new JSONObject();
			DocumentJ.put(JSONNAMES.DOCUMENTS_ID, documento.getClavilenoid());
			DocumentJ.put(JSONNAMES.DOCUMENTS_DESC,documento.getDescriptionText());
			DocumentJ.put(JSONNAMES.DOCUMENTS_ICON,documento.getIcon());
			insertdocumentViewVal(DocumentJ,documento);
			insertdocumentSons(DocumentJ,documento);
			docsL.put(DocumentJ);
		}
		
	}

	private void insertdocumentSons(JSONObject documentJ, CompleteDocuments documento) throws JSONException {
		JSONArray sons=new JSONArray();
		documentJ.put(JSONNAMES.DOCUMENT_ELEMENTS, sons);
		for (int i = 0; i < documento.getDescription().size(); i++) {
			CompleteElement elev = documento.getDescription().get(i);
			JSONObject elemnTj=new JSONObject();
			elemnTj.put(JSONNAMES.ELEMENT_ID, elev.getClavilenoid());
			if (elev.getHastype()!=null)
				elemnTj.put(JSONNAMES.ELEMENT_VALUE_TYPE_ID,elev.getHastype().getClavilenoid());
			
			JSONArray views=new JSONArray();
			elemnTj.put(JSONNAMES.ELEMENT_VIEW, views);
			for (int j = 0; j < elev.getShows().size(); i++) {
				CompleteOperationalValue GOv = elev.getShows().get(j);
				JSONObject Operational=new JSONObject();
				Operational.put(JSONNAMES.VIEW_VALUE_ID, GOv.getClavilenoid());
				if (GOv.getType()!=null)
					Operational.put(JSONNAMES.VIEW_VALUE_TYPE_ID,GOv.getType().getClavilenoid());
				Operational.put(JSONNAMES.VIEW_VALUE_VALUE,GOv.getValue());
				views.put(Operational);
			}
			
			
			if (elev instanceof CompleteTextElement)
				elemnTj.put(JSONNAMES.ELEMENT_VALUE, ((CompleteTextElement)elev).getValue());
			
			if (elev instanceof CompleteLinkElement&&((CompleteLinkElement)elev).getValue()!=null)
				elemnTj.put(JSONNAMES.ELEMENT_VALUE, ((CompleteLinkElement)elev).getValue().getClavilenoid());
			
			if (elev instanceof CompleteResourceElementFile&&((CompleteResourceElementFile)elev).getValue()!=null)
				elemnTj.put(JSONNAMES.ELEMENT_VALUE, ((CompleteResourceElementFile)elev).getValue().getClavilenoid());
			
			if (elev instanceof CompleteResourceElementURL&&((CompleteResourceElementURL)elev).getValue()!=null)
				elemnTj.put(JSONNAMES.ELEMENT_VALUE, ((CompleteResourceElementURL)elev).getValue());
			
			
			sons.put(elemnTj);
		}
		
	}

	private void insertdocumentViewVal(JSONObject documentJ, CompleteDocuments documento) throws JSONException {
		JSONArray views=new JSONArray();
		documentJ.put(JSONNAMES.DOCUMENT_VIEW, views);
		for (int i = 0; i < documento.getViewsValues().size(); i++) {
			CompleteOperationalValue GOv = documento.getViewsValues().get(i);
			JSONObject Operational=new JSONObject();
			Operational.put(JSONNAMES.VIEW_VALUE_ID, GOv.getClavilenoid());
			if (GOv.getType()!=null)
				Operational.put(JSONNAMES.VIEW_VALUE_TYPE_ID,GOv.getType().getClavilenoid());
			Operational.put(JSONNAMES.VIEW_VALUE_VALUE,GOv.getValue());
			views.put(Operational);
		}
		
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
		for (int i = 0; i < grammar.getSons().size(); i++) {
			CompleteElementType GOv = grammar.getSons().get(i);
			addson(GOv,Sons);

		}
	}

	private void addson(CompleteElementType gOv, JSONArray sons) throws JSONException {
		JSONObject son=new JSONObject();
		son.put(JSONNAMES.STRUCTURE_ID, gOv.getClavilenoid());
		son.put(JSONNAMES.STRUCTURE_NAME,gOv.getName());
		if (gOv.getBFather()!=null)
			son.put(JSONNAMES.STRUCTURE_B_FATHER,gOv.getBFather().getClavilenoid());
		if (gOv.getBSon()!=null)
			son.put(JSONNAMES.STRUCTURE_B_SON,gOv.getBSon().getClavilenoid());
		if (gOv.getClassOfIterator()!=null)
			son.put(JSONNAMES.STRUCTURE_COI,gOv.getClassOfIterator().getClavilenoid());
		son.put(JSONNAMES.STRUCTURE_BROWSEABLE,gOv.isBrowseable());
		son.put(JSONNAMES.STRUCTURE_MULTIVALUED,gOv.isMultivalued());
		son.put(JSONNAMES.STRUCTURE_SELECTABLE,gOv.isSelectable());
		son.put(JSONNAMES.STRUCTURE_BEFILTER,gOv.isBeFilter());

		JSONArray Sonsint=new JSONArray();
		son.put(JSONNAMES.STRUCTURE_SONS, Sonsint);
		for (int i = 0; i < gOv.getSons().size(); i++) {
			CompleteElementType GOv = gOv.getSons().get(i);
			addson(GOv,Sonsint);

		}
		
		JSONArray views=new JSONArray();
		son.put(JSONNAMES.STRUCTURE_VIEW, views);
		for (int i = 0; i < gOv.getShows().size(); i++) {
			CompleteOperationalValueType GOv = gOv.getShows().get(i);
			JSONObject Operational=new JSONObject();
			Operational.put(JSONNAMES.VIEW_TYPE_ID, GOv.getClavilenoid());
			Operational.put(JSONNAMES.VIEW_TYPE_NAME,GOv.getName());
			Operational.put(JSONNAMES.VIEW_TYPE_DEF,GOv.getDefault());
			Operational.put(JSONNAMES.VIEW_TYPE_VIEW,GOv.getView());
			views.put(Operational);
		}
		
		if (gOv instanceof CompleteTextElementType)
			son.put(JSONNAMES.STRUCTURE_TYPE, JSONNAMES.STRUCTURE_TYPE_T);
		else
		if (gOv instanceof CompleteLinkElementType)
			son.put(JSONNAMES.STRUCTURE_TYPE, JSONNAMES.STRUCTURE_TYPE_L);
		else
		if (gOv instanceof CompleteResourceElementType)
			son.put(JSONNAMES.STRUCTURE_TYPE, JSONNAMES.STRUCTURE_TYPE_R);
		else
			son.put(JSONNAMES.STRUCTURE_TYPE, JSONNAMES.STRUCTURE_TYPE_X);
		
		sons.put(son);
		
	}

	private void insertgrammarView(JSONObject grammarJ, CompleteGrammar grammar) throws JSONException {
		JSONArray views=new JSONArray();
		grammarJ.put(JSONNAMES.GRAMMARS_VIEW, views);
		for (int i = 0; i < grammar.getViews().size(); i++) {
			CompleteOperationalValueType GOv = grammar.getViews().get(i);
			JSONObject Operational=new JSONObject();
			Operational.put(JSONNAMES.VIEW_TYPE_ID, GOv.getClavilenoid());
			Operational.put(JSONNAMES.VIEW_TYPE_NAME,GOv.getName());
			Operational.put(JSONNAMES.VIEW_TYPE_DEF,GOv.getDefault());
			Operational.put(JSONNAMES.VIEW_TYPE_VIEW,GOv.getView());
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

	
 public String getSalida() {
	return Salida;
}
	
	public static void main(String[] args) {
		SaveCollectionJSON SV=new SaveCollectionJSON();
		CompleteCollection CC=CreateCollectionBase();
		SV.processCollecccion(CC, "");
		System.out.println(SV.getSalida());

		
	}

	private static CompleteCollection CreateCollectionBase() {
		CompleteCollection C=new CompleteCollection("nombre coleccion", "descripcion");
		C.setClavilenoid(1l);
		int n = (new Random()).nextInt(20);
		for (int i = 0; i < n; i++) 
			C.getSectionValues().add(new CompleteFile(new Long(i),"File"+ i+"path", C));
		
		List<CompleteFile> Files=new LinkedList<>(C.getSectionValues());
		List<CompleteElementType> Elements=new LinkedList<CompleteElementType>();

		
		int n2 = (new Random()).nextInt(20);
		for (int i = 0; i < n2; i++) 
			{	CompleteGrammar GG=new CompleteGrammar(new Long(i), "Gram"+ i+"name","Gram"+ i+"desc",C);
				

			int n3 = (new Random()).nextInt(10);
			for (int j = 0; j < n3; j++) 
				{	CompleteOperationalValueType OP=new CompleteOperationalValueType(new Long(j),"View"+ j+"name","View"+ j+"def","View"+j+"view");
					GG.getViews().add(OP);
				}
				
			ArrayList<CompleteElementType> hijos=new ArrayList<CompleteElementType>();	
			generahijos(hijos,GG,Elements,new Integer(20));
			GG.setSons(hijos);
			
				C.getMetamodelGrammar().add(GG);
			}
		
		return C;
	}

	private static void generahijos(ArrayList<CompleteElementType> hijos, CompleteGrammar gG,
			List<CompleteElementType> elements, Integer integer) {
		int n = (new Random()).nextInt(integer);
		for (int i = 0; i < n; i++) 
			{
			CompleteElementType CET=new CompleteElementType(new Long(i), "Element"+i, gG);
			hijos.add(CET);
			elements.add(CET);
			
			CET.setBeFilter((new Random()).nextBoolean());
			CET.setMultivalued((new Random()).nextBoolean());
			CET.setSelectable((new Random()).nextBoolean());
			CET.setBrowseable((new Random()).nextBoolean());
			
			int n3 = (new Random()).nextInt(10);
			for (int j = 0; j < n3; j++) 
				{	
					CompleteOperationalValueType OP=new CompleteOperationalValueType(new Long(j),"View"+ j+"name","View"+ j+"def","View"+j+"view");
					CET.getShows().add(OP);
				}
			
			
			if ((new Random()).nextBoolean())
			{
				ArrayList<CompleteElementType> hijos1=new ArrayList<CompleteElementType>();	
				generahijos(hijos1,gG,elements,new Integer(integer/2));
				CET.setSons(hijos1);
			}
			
			
			}
		
	}


	
	
	
}
