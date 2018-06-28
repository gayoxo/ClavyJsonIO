/**
 * 
 */
package fdi.ucm.es.exportparser.json;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.CompleteImportRuntimeException;
import fdi.ucm.server.modelComplete.SaveCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.CompleteLogAndUpdates;

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
	private static final Pattern regexAmbito = Pattern.compile("^[0-9]+(,[0-9]+)*$");

	
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
			
//			HTMLprocess oda= new HTMLprocess(ListaDeDocumentos,Salvar,SOURCE_FOLDER,CL,TextoIn);
//			
//			
//			oda.preocess();
				

				CL.getLogLines().add("Descarga el zip");


			return CL;


		} catch (CompleteImportRuntimeException e) {
			System.err.println("Exception HTML " +e.getGENERIC_ERROR());
			e.printStackTrace();
			throw e;
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
		System.out.println(testList("6658,6658,6658,asd"));
		System.out.println(testList("6658"));
	}

	private static boolean testList(String number) {
		if (number==null||number.isEmpty())
			return true;
		 Matcher matcher = regexAmbito.matcher(number);
		return matcher.matches();
	}
	
	
	
}
