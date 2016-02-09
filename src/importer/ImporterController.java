package importer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import ch.uzh.ifi.attempto.acewiki.Wiki;
import ch.uzh.ifi.attempto.acewiki.gui.SentenceEditorHandler;
import ch.uzh.ifi.attempto.base.TextElement;
import ch.uzh.ifi.attempto.base.TextOperator;
import ch.uzh.ifi.attempto.preditor.PreditorWindow;
import nextapp.echo.app.event.ActionEvent;



public class ImporterController {

	private Wiki wiki;
	PreditorWindow preEditorWindow;
	
	public ImporterController()
	{
		
	}
	
	public ImporterController(Wiki wiki)
	{
		this.wiki=wiki;
	}
	
	public void  importButtonClicked() {
		//import owl
		
		//post operation on owl e.g. rewrite disjointwith, replace annotation etc.
		
		
		//call owl-verbalizer in CSV mode
		
		//create lexicon (do this inside model)
		
		//call owl-verbalizer in ACE sentence mode
		setVerbalizerWebservice("http://localhost:5123");
		String owl ="<?xml version=\"1.0\"?>\n" +
				"<Ontology\n" +
				"	xmlns=\"http://www.w3.org/2002/07/owl#\"\n" +
				"	xml:base=\"http://org.semanticweb.ontologies/ont\"\n" +
				"	xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">\n" +
				"	<Prefix name=\"t\" IRI=\"http://www.example.org/t.owl#\"/>\n" +
				"	<SubClassOf>\n" +
				"		<Class IRI=\"/CCCA\"/>\n" +
				"		<Class IRI=\"/Monument\"/>\n" +
				"	</SubClassOf>\n" +
				"	<SubClassOf>\n" +
				"		<Class IRI=\"/TransversalGalley\"/>\n" +
				"		<Class IRI=\"/Galley\"/>\n" +				
				"	</SubClassOf>\n" +
				"</Ontology>\n";
		String response =call(owl);
		System.out.println(response);
		
		//add the ACE sentences in wiki
		preEditorWindow = SentenceEditorHandler.generateCreationWindow(null, wiki.articlePage);
//		wiki.showWindow(preEditorWindow);
		TextElement te;
		TextOperator to = wiki.getLanguageHandler().getTextOperator();
		String[] sentences = response.split("[\\r\\n]+");
		System.out.println("\n sentences:\n");
		for (String s : sentences) {

			//remove the  full stop at last index
			int len = s.length();
			s=s.substring(0, len-1);
			List<String> words = new ArrayList<String>(Arrays.asList(s.split(" ")));
//			String[] words = s.split(" ");
			words.add(".");//add full stop in another index
			for (String w : words) {
				te = to.createTextElement(w);
				preEditorWindow.textElementSelected(te);
			}
			
			//test if this sentence can be added in Acewiki
			
			//if cant add, store this sentence in a list
			
			//if can add, store it in another list
			
			
			System.out.println(s);
		}
		//add all sentences
		preEditorWindow.notifyActionListeners(new ActionEvent(preEditorWindow, "OK"));
		//clean the textcontainer after adding a sentence
//		preEditorWindow.clearTokens();
		
		
		//sentence
//		te = new TextElement("Every");
//		preEditorWindow.textElementSelected(te);
//		te = to.createTextElement("CCCA");
//		preEditorWindow.textElementSelected(te);
//		te = to.createTextElement("is");
//		preEditorWindow.textElementSelected(te);
//		te = to.createTextElement("a");
//		preEditorWindow.textElementSelected(te);
//		te = to.createTextElement("Monument");
//		preEditorWindow.textElementSelected(te);
//		//add sentence
//		preEditorWindow.notifyActionListeners(new ActionEvent(preEditorWindow, "OK"));
		
		//add sentence
//		te = to.createTextElement("No");
//		preEditorWindow.textElementSelected(te);
//		te = to.createTextElement("DoorA");
//		preEditorWindow.textElementSelected(te);
//		te = to.createTextElement("is");
//		preEditorWindow.textElementSelected(te);
//		te = to.createTextElement("a");
//		preEditorWindow.textElementSelected(te);
//		te = to.createTextElement("DoorC");
//		preEditorWindow.textElementSelected(te);
//		//add this sentence
//		preEditorWindow.notifyActionListeners(new ActionEvent(preEditorWindow, "OK"));

		
	}
	/**
	 * <p>This is a simple interface to the OWL verbalizer webservice.</p>
	 * 
	 * <p>The OWL verbalizer takes one obligatory parameter, <code>xml</code>, the value of which
	 * is the complete ontology as string. An optional parameter <code>format</code> specifies
	 * the desired output format.</p>
	 * 
	 * <p>The OWL verbalizer returns the input ontology represented in ACE, in the specified format.
	 * In case the verbalizer fails to process an OWL axiom in the ontology,
	 * an error message is returned.</p>
	 * 
	 * TODO: improve error message processing
	 * 
	 * @author Kaarel Kaljurand
	 *
	 */
	private static final int MAX_HTTP_GET_LENGTH = 1000;
	private static final String ERROR_MESSAGE = "Accessing OWL->ACE webservice failed";

	private String mWsUrl;

	/**
	 * <p>Constructs a new <code>VerbalizerWebservice</code> object
	 * on the basis of the URL of the OWL verbalizer webservice.</p>
	 * 
	 * @param wsUrl The URL of the OWL verbalizer webservice.
	 */
	public void setVerbalizerWebservice(String wsUrl) {
		mWsUrl = wsUrl;
	}


	/**
	 * <p>Calls the OWL verbalizer webservice by giving the string representation of
	 * an XML-formatted ontology as input. Returns the corresponding ACE text
	 * with possible error messages as comments.</p>
	 * 
	 * @param xml XML-representation of an OWL ontology.
	 * @param outputType Desired output format
	 * @return ACE text that corresponds to the ontology.
	 */
	public String call(String xml, OutputType outputType) {
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xml));
		nvps.add(new BasicNameValuePair("format", outputType.toString().toLowerCase()));
		return getResponseAsString(nvps);
	}


	/**
	 * <p>Calls the OWL verbalizer webservice by giving the string representation of
	 * an XML-formatted ontology as input. Returns the corresponding ACE text
	 * in the default output format.</p>
	 * 
	 * @param xml XML-representation of an OWL ontology.
	 * @return ACE text that corresponds to the ontology.
	 */
	public String call(String xml) {
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xml));
		return getResponseAsString(nvps);
	}


	private String getResponseAsString(List<NameValuePair> nvps) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpUriRequest request = getHttpUriRequest(nvps);
		return getEntity(httpclient, request);
	}

	/**
	 * <p>We create an HTTP GET query from the given parameters. If it turns out to be
	 * too long (which we expect to happen very infrequently) then we fall back to creating
	 * HTTP POST.</p>
	 * 
	 * @param nvps List of name-value pairs
	 * @return HTTP request (either GET or POST)
	 */
	private HttpUriRequest getHttpUriRequest(List<NameValuePair> nvps) {
		String getQuery = mWsUrl + "?" + URLEncodedUtils.format(nvps, HTTP.UTF_8);
		if (getQuery.length() > MAX_HTTP_GET_LENGTH) {
			HttpPost httppost = new HttpPost(mWsUrl);
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				// BUG: Assuming that this cannot happen
			}
			return httppost;
		}
		return new HttpGet(getQuery);
	}

	private String getEntity(DefaultHttpClient httpclient, HttpUriRequest httpRequest) {
		try {
			HttpResponse response = httpclient.execute(httpRequest);
			HttpEntity entity = response.getEntity();

			if (entity == null) {
				throw new RuntimeException(ERROR_MESSAGE + ": " + response.getStatusLine());
			}
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new RuntimeException(ERROR_MESSAGE + ": " + response.getStatusLine());
			}
			// The APE webservice returns the data in UTF8, even if it doesn't declare it.
			if (entity.getContentEncoding() == null) {
				return EntityUtils.toString(entity, HTTP.UTF_8);
			}
			return EntityUtils.toString(entity);

		} catch (ClientProtocolException e) {
			throw new RuntimeException(ERROR_MESSAGE + ": " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException(ERROR_MESSAGE + ": " + e.getMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}
	
}
