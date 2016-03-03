package importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import ch.uzh.ifi.attempto.acewiki.Wiki;
import ch.uzh.ifi.attempto.acewiki.aceowl.ACEDeclaration;
import ch.uzh.ifi.attempto.acewiki.core.InvalidWordException;
import ch.uzh.ifi.attempto.acewiki.core.LexiconChanger;
import ch.uzh.ifi.attempto.acewiki.core.Ontology;
import ch.uzh.ifi.attempto.acewiki.core.OntologyElement;
import ch.uzh.ifi.attempto.acewiki.core.OntologyTextElement;
import ch.uzh.ifi.attempto.acewiki.core.Sentence;
import ch.uzh.ifi.attempto.acewiki.core.Statement;
import ch.uzh.ifi.attempto.acewiki.gui.SentenceEditorHandler;
import ch.uzh.ifi.attempto.ape.FunctionWords;
import ch.uzh.ifi.attempto.base.TextElement;
import ch.uzh.ifi.attempto.base.TextOperator;
import ch.uzh.ifi.attempto.preditor.PreditorWindow;
import javafx.util.Pair;

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
		/* each sentence separated by tow new lines */
		String[] sentences = csvResponse.split("(\\r\\n\\r\\n)");
		for (String s : sentences) {
			/* split each line, becasue in csv format, tag is given in each line */
			String[] words = s.split("(\\r\\n)");
			List<Pair<String, String>> taggedSentence = new ArrayList<>();

			boolean error = false;
			for (String w : words) {
				
				/*
				 * split according to first tab. 2 means, 2-1=1 time 
				 * the split will be applied according to the given
				 * pattern
				 */
				String[] tabSeparated = w.split("\\t", 2);
				type = tabSeparated[0];
				/*
				 * lexicon can be after / or # e.g.
				 * http://www.example.org/test#own
				 * http://localhost:9080/airbus/vocabularies/modus2place/A350/equipments2monuments/depth
				 */
				String[] content = tabSeparated[1].split("/");
				word = content[content.length - 1];// take word after last
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
		/* if it is not in the lexicon list */
		if (!lexicons.contains(lexiconType, lexicon)) {
			lexicons.put(lexiconType, lexicon, false); // initialize with false

			/* try to insert the lexicon in the application */
			List<Object> newValues = new ArrayList<Object>();
			if (lexiconType.equals("propername")) {
				newValues.add(lexicon);
				newValues.add(false);
				newValues.add("");
				newValues.add(false);
			} else if (lexiconType.equals("noun")) {
				newValues.add(lexicon); 				// singular
				/*
				 * TODO: To make plural, add ‘s’ at end of the word for now. In
				 * future use some library to make plural Split the words(if
				 * there are two words and then try to make the plural of the
				 * second word and then merge those words.) e.g. LateralGalley.
				 * Plural of Galley is Gallies . So the plural of LateralGalley
				 * will be LateralGallies.
				 */
				newValues.add(lexicon + "s"); 			// plural.
			} else if (lexiconType.equals("trverb")) {
				// TODO: create 3rd singular,bare infinitive and past participle
				// in proper way
				newValues.add(lexicon + "s"); 			// 3rd singular
				newValues.add(lexicon); 				// bare infinitive
				newValues.add(lexicon + "ed"); 			// past participle
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
				lexicons.put(lexiconType, lexicon, true);
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

			/*
			 * remove the full stop at last index because e.g. Monument. is not
			 * a lexicon, lexicon is Monument (i.e. without full stop at end)
			 */
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

		/* test if this sentence can be added in Acewiki */
		if (preEditorWindow.parser.isComplete()) {
			this.validSentences.add((ArrayList<TextElement>) preEditorWindow.getTextContainer().getTextElements());
		} else {
			this.invaliAceWikiSentences.add(String.join(" ", words));
		}

		preEditorWindow.clearTokens();
	}

	/**
	 * Owl-verbalizer in tagged mode sends:
	 * 	"every thing", which should be "everything" to work with APE
	 * 	"a thing" should be "something"
	 * @param sentence the tagged sentence from owl verbalizer
	 * @return
	 */
	private List<Pair<String, String>> preProcessTaggedSentence(ArrayList<Pair<String, String>> sentence) {
		int len = sentence.size();
		for (int i = 0; i < len - 1; i++) {

			/* convert "a thing" to "something" */
			if (sentence.get(i).getValue().equalsIgnoreCase("a")
					&& sentence.get(i + 1).getValue().equalsIgnoreCase("thing")) {
				String key = sentence.get(i).getKey();
				sentence.set(i, new Pair<String, String>(key, "something"));
				sentence.remove(++i);

			}

			/* convert "every thing" to "everything" */
			else if (sentence.get(i).getValue().equalsIgnoreCase("every")
					&& sentence.get(i + 1).getValue().equalsIgnoreCase("thing")) {
				String key = sentence.get(i).getKey();
				sentence.set(i, new Pair<String, String>(key, "everything"));
				sentence.remove(++i);

			}
		}

		return sentence;
	}

	/**
	 * 
	 * @param sentence
	 *            List of Pair<type_of_word, word>. This is the csv format from
	 *            owl-verbalizer
	 */
	public void handleTaggedSentence(List<Pair<String, String>> sentence, String s) {
		preEditorWindow = SentenceEditorHandler.generateCreationWindow(null, wiki.articlePage);
		String w = "";

		TextOperator to = wiki.getLanguageHandler().getTextOperator();
		TextElement te;

		preProcessTaggedSentence((ArrayList<Pair<String, String>>) sentence);
		s="";
		for (Pair<String, String> pair : sentence) {
			s+=pair.getValue()+" ";
		}
		for (Pair<String, String> word : sentence) {
			OntologyElement oe = wiki.getOntology().getElement(word.getValue());

			/*
			 * a word should be either functional word(e.g. every, at least...), or it should have the tag "f", or it should be a lexicon i.e. ontology element
			 * it it has anything else, i.e. bug, comment, prefix etc. then it is not a valid ACE sentence
			 */
			if (oe == null && !FunctionWords.isFunctionWord(word.getValue()) && !word.getKey().equals("f")) { 
				this.invaliAceWikiSentences.add(s); 
				return;
			}
			// check type and form of word and use appropriate surface form
			if (word.getKey().equals("cn_sg") || word.getKey().equals("tv_sg")) {
				w = oe.getWord(0);	//singular for noun, third singular for verb
			} else if (word.getKey().equals("cn_pl") || word.getKey().equals("tv_pl")) {
				w = oe.getWord(1);	//plural for noun, bare infinitive for verb
			} else if (word.getKey().equals("tv_vbg")) {
				w = oe.getWord(2);	//past participle for verb
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
		
		//preEditorWindow.parser.isComplete() does not work when I put floating point number in the ACE sentence
		this.validSentences.add((ArrayList<TextElement>) preEditorWindow.getTextContainer().getTextElements());
		
		// test if this sentence can be added in Acewiki
		//if can add, store it in a list
		//below isComplete() does not work for floating point numbers
//		if (preEditorWindow.parser.isComplete()) {
//			this.validSentences.add((ArrayList<TextElement>) preEditorWindow.getTextContainer().getTextElements());
//		} else {
//			this.invaliAceWikiSentences.add(s);
//		}

		// clean the textcontainer and parser after adding a sentence
		preEditorWindow.clearTokens();
	}

	public void insertSentencesInWiki() {
		final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ImporterModel.class);
		
		/*
		 *  add all the valid sentences at once, so that the wiki refreashes only
		 *  once. Otherwise if we add one one by, then it reafreshes for each
		 *  sentence and takes a lot of time and some synchronization problem occurs
		 */  
		
		Ontology ontology = wiki.getOntology();
		// StatementFactory statementFactory = ontology.getStatementFactory();
		List<Sentence> sentences = new ArrayList<Sentence>();
		Sentence sentence = null;

		if (this.validSentences.size() > 0) {
			for (ArrayList<TextElement> validSen : this.validSentences) {
				String s = "";
				boolean first = true;
				for (TextElement textElement : validSen) {
					if (!first) {
						s += " " + textElement.getOriginalText();
					} else {
						
						/* get text with underscore, if any */
						s += textElement.getOriginalText();
						first = false;
					}
					// for (TextElement te : validSen) {
					// te = to.createTextElement(w);
					preEditorWindow.textElementSelected(textElement);
					// }
				}
				Statement statement = null;
				try {
					sentence = new ACEDeclaration(s); // gets a sentence type
					sentence.init(ontology, wiki.articlePage.getArticle());
					sentence.setIntegrated(true);
					statement = sentence;

				} catch (Exception e) {
					log.warn("Bad statement: ", e);
				}
				if (statement == null) {
					log.warn("Cannot read statement: {}", s);
				} else {
					sentences.add(sentence);
				}

			}
			SentenceEditorHandler senHandler = new SentenceEditorHandler(null, wiki.articlePage, false);
			senHandler.setNewSentences(sentences);
			senHandler.assertSentences();
			/*
			 * try { article.add(null, new ArrayList<Statement>(newSentences));
			 * } catch (InconsistencyException ex) { inconsistent = true; }
			 */

		}
		//TEMP
		/*
		PredictiveParser parser = preEditorWindow.getPredictiveParser();
		List<Sentence> newSentences = wiki.getOntology().getStatementFactory().extractSentences(
				wiki.getLanguageHandler(),
				preEditorWindow.getTextContainer(),
				parser,
				wiki.articlePage.getArticle()
			);
		*/
		// add all sentences
		//preEditorWindow.notifyActionListeners(new ActionEvent(preEditorWindow, "OK"));

	}

	public void printFailedAceSentences() {
		System.out.println("######### Failed sentences starts: ######### ");
		for (String invalAceSen : this.invaliAceWikiSentences) {
			System.out.println(invalAceSen);
		}
		System.out.println("######### Failed sentences ends ######### ");

	}

	public void printFailedLexicons() {
		System.out.println("######### Failed lexicon starts: ######### ");
		boolean failed = false;
		for (Table.Cell<String, String, Boolean> cell : lexicons.cellSet()) {
			if (!cell.getValue()) {
				System.out.println(cell.getColumnKey() + " " + cell.getRowKey());
				failed = true;
			}
		}
		if (!failed)
			System.out.println("NONE");
		System.out.println("######### Failed lexicon ends #########");

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
