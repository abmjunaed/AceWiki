package importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import ch.uzh.ifi.attempto.acewiki.Wiki;
import ch.uzh.ifi.attempto.acewiki.core.DefaultWordIndex;
import ch.uzh.ifi.attempto.acewiki.core.InvalidWordException;
import ch.uzh.ifi.attempto.acewiki.core.LexiconChanger;
import ch.uzh.ifi.attempto.acewiki.core.OntologyElement;
import ch.uzh.ifi.attempto.acewiki.core.OntologyTextElement;
import ch.uzh.ifi.attempto.acewiki.gui.SentenceEditorHandler;
import ch.uzh.ifi.attempto.base.TextElement;
import ch.uzh.ifi.attempto.base.TextOperator;
import ch.uzh.ifi.attempto.preditor.PreditorWindow;
import javafx.util.Pair;
import nextapp.echo.app.event.ActionEvent;

public class ImporterModel{

	private Wiki wiki;
	PreditorWindow preEditorWindow;
	private LexiconChanger lexiconChanger;
	private OntologyElement element;
	private String uploadedOwl;
	private String refactoredOwl;// so that owl-verbalizer can work with it
	private String csvResponse;
	private List<String>					invaliAceWikiSentences = new ArrayList<String>();
	private List<ArrayList<TextElement>> 	validSentences = new ArrayList<ArrayList<TextElement>>();
	public ErrorMessages invalid;
	private Set<String>lexiconTypes							 = Sets.newHashSet();//this Set is not from java,it is from guava, so that we can initialize in one line.
	private Map<String,String>verbalizerTagToWikiLexiconType = ImmutableMap.<String, String>builder()
																.put("cn_sg","noun")
																.put("cn_pl","noun")
																.put("tv_sg","trverb")
																.put("tv_pl","trverb")
																.put("tv_vbg","trverb")
																.put("pn_sg","propername")
																.build();
	/**
	 * 1st two are keys, third is value
	 * 1st string for type of lexicon, e.g. f,cn_sg ...
	 * 2nd string for the lexicon word
	 * boolean: if the lexicon is inserted in the application
	 */
	private Table<String, String, Boolean>lexicons = HashBasedTable.create();
	
	public ImporterModel () {
		
	}
	public ImporterModel(Wiki wiki) {
		this.wiki = wiki;
		//create the allowed lexicon types from the verbalizerTagToWikiLexiconType
		lexiconTypes=verbalizerTagToWikiLexiconType.keySet();
		
	}

	public String getUploadedOwl() {
		return uploadedOwl;
	}

	public void setUploadedOwl(String uploadedOwl) {
		this.uploadedOwl = uploadedOwl;
	}

	public String getRefactoredOwl() {
		return refactoredOwl;
	}

	public void setRefactoredOwl(String modifiedOwl) {
		this.refactoredOwl = modifiedOwl;
	}

	public String getCsvResponse() {
		return csvResponse;
	}

	public void setCsvResponse(String csvResponse) {
		this.csvResponse = csvResponse;
	}
	
	public List<ArrayList<TextElement>> getValidSentences() {
		return validSentences;
	}

	public void setValidSentences(List<ArrayList<TextElement>> validSentences) {
		this.validSentences = validSentences;
	}


	/**
	 * Refactor e.g. rewrite disjointwith, replace annotation etc. So that
	 * owl-verbalizer can work with it
	 */
	public void refactorUploadedOwl() {
		// TODO: refactor
		
		refactoredOwl = uploadedOwl;
	}
	
	public void createLexicon(String csvResponse) {
		this.setCsvResponse(csvResponse);
		String type, word;
		String[] sentences = csvResponse.split("(\\r\\n\\r\\n)");// two new
																	// lines
		for (String s : sentences) {
			String[] words = s.split("(\\r\\n)");// split each line, becasue in
													// csv format, tag is given
													// in each line

			List<Pair<String, String>> taggedSentence = new ArrayList<>();

			boolean error = false;
			for (String w : words) {
				String[] tabSeparated = w.split("\\t", 2);// split according to
															// first tab. 2
															// means, 2-1=1 time
															// the split will be
															// applied according
															// to the given
															// pattern
				type = tabSeparated[0];
				/*
				 * lexicon can be after / or # e.g.
				 * http://www.example.org/test#own
				 * http://localhost:9080/airbus/vocabularies/modus2place/A350/equipments2monuments/depth
				 */
				String[] content = tabSeparated[1].split("/");
				word = content[content.length - 1];// take word after last /
				String[] tmp = word.split("#");
				word = tmp[tmp.length - 1];
				if (this.lexiconTypes.contains(type)) {
					insertLexicon(type, word);
					taggedSentence.add(new Pair<String, String>(type, word));
				} else {
					addErrorMessage(type, tabSeparated[1]);
					taggedSentence.add(new Pair<String, String>(type, tabSeparated[1]));
					error = true;
				}

			}

			handleTaggedSentence(taggedSentence, s);

		}

//		printFailedLexicons();
		
		lexicons.clear();
	}

	private void insertLexicon(String taggedType, String lexicon) {
		String lexiconType = verbalizerTagToWikiLexiconType.get(taggedType);
		if (taggedType.equals("f"))
			return;
		// if it is not in the lexicon list
		if (!lexicons.contains(lexiconType, lexicon)) {
			lexicons.put(lexiconType, lexicon, false);// initially it is not
														// added in the
														// application

			// try to insert the lexicon in the application
			List<Object> newValues = new ArrayList<Object>();
			if (lexiconType.equals("propername")) {
				newValues.add(lexicon);
				newValues.add(false);
				newValues.add("");
				newValues.add(false);
			} else if (lexiconType.equals("noun")) {
				newValues.add(lexicon);// singular
				newValues.add(lexicon + "s");// plural. add �s� at end of the
												// word for now. In future use
												// some library to make plural
												// Split the words(if there are
												// two words and then try to
												// make the plural of the second
												// word and then merge those
												// words.) e.g. LateralGalley.
												// Plural of Galley is Gallies .
												// So the plural of
												// LateralGalley will be
												// LateralGallies.
			} else if (lexiconType.equals("trverb")) {
				// TODO: create 3rd singular,bare infinitive and past participle
				// in proper way
				newValues.add(lexicon + "s");// 3rd singular
				newValues.add(lexicon);// bare infinitive
				newValues.add(lexicon + "ed");// past participle
			} else {
				return;
			}
			element = wiki.getOntology().getEngine().createOntologyElement(lexiconType);

			lexiconChanger = wiki.getLanguageHandler().getLexiconChanger(lexiconType);
			try {
				lexiconChanger.save(element, 0, newValues, wiki.getOntology());
				wiki.log("edit", element.toString());
				if (element.getOntology() == null) {
					wiki.getOntology().register(element);
				}

				// a text element is used to store the ontology element and the
				// word number in one object:
				OntologyTextElement te = new OntologyTextElement(element, 0);
				wiki.log("edit", "new word: " + te.getOntologyElement().getWord());
				lexicons.put(lexiconType, lexicon, true);// initially it is not
															// added in the
															// application
			} catch (InvalidWordException ex) {
				// lexicon is not added
			}

		}
	}
	
	private void addErrorMessage(String type, String msg) {
		if (ErrorMessages.errorMsgTypes.contains(type)) {
			ErrorMessages.errorMessages.get(type).add(msg);
		} else {
			ErrorMessages.errorMessages.get("others").add(msg);
		}
	}

	public void handleVerbalizerResponse(String response) {

		String[] sentences = response.split("[\\r\\n]+");
		// System.out.println("\n sentences:\n");
		for (String s : sentences) {

			// remove the full stop at last index because e.g. Monument. is not
			// a lexicon, lexicon is Monument (i.e. without full stop at end)
			int len = s.length();
			s = s.substring(0, len - 1);
			List<String> words = new ArrayList<String>(Arrays.asList(s.split(" ")));
			// String[] words = s.split(" ");
			words.add(".");// add full stop as another word

			// System.out.println(words);

			handleAceSentence(words);
		}

	}

	public void handleAceSentence(List<String> words) {
		preEditorWindow = SentenceEditorHandler.generateCreationWindow(null, wiki.articlePage);

		TextOperator to = wiki.getLanguageHandler().getTextOperator();
		TextElement te;
		for (String w : words) {
			te = to.createTextElement(w);
			/*
			 * new way: without using predictive editor. Look code in private
			 * void tokenize()
			 * /acewiki/src/ch/uzh/ifi/attempto/acewiki/aceowl/ACESentence.java
			 * private TextContainer textContainer;
			 * textContainer.addElement(te);
			 */
			// preEditorWindow.parser.addToken(w);
			preEditorWindow.textElementSelected(te);
		}

		// test if this sentence can be added in Acewiki
		// if can add, store it in another list
		if (preEditorWindow.parser.isComplete()) {
			this.validSentences.add((ArrayList<TextElement>) preEditorWindow.getTextContainer().getTextElements());
		}
		// if cant add, store this sentence in a list
		else {
			this.invaliAceWikiSentences.add(String.join(" ", words));
		}

		// clean the textcontainer and parser after adding a sentence
		preEditorWindow.clearTokens();

		// for all the valid text elements:

	}

	// TODO: work with tv_vbg

	/**
	 * 
	 * @param sentence
	 *            List of Pair<type_of_word, word>. This is the csv format from
	 *            owl-verbalizer
	 */
	public void handleTaggedSentence(List<Pair<String, String>> sentence, String s) {
		preEditorWindow = SentenceEditorHandler.generateCreationWindow(null, wiki.articlePage);
		String w = "";
		DefaultWordIndex df = new DefaultWordIndex();

		TextOperator to = wiki.getLanguageHandler().getTextOperator();
		TextElement te;

		for (Pair<String, String> word : sentence) {
			OntologyElement oe = wiki.getOntology().getElement(word.getValue());

			// check type and form of word and use appropriate surface form
			if (word.getKey().equals("cn_sg") || word.getKey().equals("tv_sg")) {
				w = oe.getWord(0);
			} else if (word.getKey().equals("cn_pl") || word.getKey().equals("tv_pl")) {
				w = oe.getWord(1);
			} else if (word.getKey().equals("tv_vbg")) {
				w = oe.getWord(2);
			} else {
				w = word.getValue();
			}

			te = to.createTextElement(w);

			/*
			 * new way: without using predictive editor. Look code in private
			 * void tokenize()
			 * /acewiki/src/ch/uzh/ifi/attempto/acewiki/aceowl/ACESentence.java
			 * private TextContainer textContainer;
			 * textContainer.addElement(te);
			 */
			// preEditorWindow.parser.addToken(w);
			preEditorWindow.textElementSelected(te);
		}

		// test if this sentence can be added in Acewiki
		// if can add, store it in a list
		if (preEditorWindow.parser.isComplete()) {
			this.validSentences.add((ArrayList<TextElement>) preEditorWindow.getTextContainer().getTextElements());
		} else {
			this.invaliAceWikiSentences.add(s);
		}

		// clean the textcontainer and parser after adding a sentence
		preEditorWindow.clearTokens();
	}

	public void insertSentencesInWiki() {
		// add all the valid sentences at once, so that the wiki refreashes only
		// onece. Otherwise if we add one one by, then it reafreshes for each
		// sentence and takes a lot of time and some synchronization problem
		// occurs
		if (this.validSentences.size() > 0) {
			for (ArrayList<TextElement> validSen : this.validSentences) {
				for (TextElement te : validSen) {
					// te = to.createTextElement(w);
					preEditorWindow.textElementSelected(te);
				}
			}
		}
		// add all sentences
		preEditorWindow.notifyActionListeners(new ActionEvent(preEditorWindow, "OK"));

	}

	public void printFailedAceSentences() {
		System.out.println("#########Failed sentences starts:######### ");
		for (String invalAceSen : this.invaliAceWikiSentences) {
			System.out.println(invalAceSen);
		}
		System.out.println("#########Failed sentences ends######### ");

	}

	public void printFailedLexicons() {
		System.out.println("#########Failed lexicon starts:######### ");
		boolean failed = false;
		for (Table.Cell<String, String, Boolean> cell : lexicons.cellSet()) {
			if (!cell.getValue()) {
				System.out.println(cell.getColumnKey() + " " + cell.getRowKey());
				failed = true;
			}
		}
		if (!failed)
			System.out.println("NONE");
		System.out.println("#########Failed lexicon ends#########");

	}

	public void addHardcodedSentences() {
		// sentence
		// te = new TextElement("Every");
		// preEditorWindow.textElementSelected(te);
		// te = to.createTextElement("CCCA");
		// preEditorWindow.textElementSelected(te);
		// te = to.createTextElement("is");
		// preEditorWindow.textElementSelected(te);
		// te = to.createTextElement("a");
		// preEditorWindow.textElementSelected(te);
		// te = to.createTextElement("Monument");
		// preEditorWindow.textElementSelected(te);
		// //add sentence
		// preEditorWindow.notifyActionListeners(new
		// ActionEvent(preEditorWindow, "OK"));

		// add sentence
		// te = to.createTextElement("No");
		// preEditorWindow.textElementSelected(te);
		// te = to.createTextElement("DoorA");
		// preEditorWindow.textElementSelected(te);
		// te = to.createTextElement("is");
		// preEditorWindow.textElementSelected(te);
		// te = to.createTextElement("a");
		// preEditorWindow.textElementSelected(te);
		// te = to.createTextElement("DoorC");
		// preEditorWindow.textElementSelected(te);
		// //add this sentence
		// preEditorWindow.notifyActionListeners(new
		// ActionEvent(preEditorWindow, "OK"));

	}

}
