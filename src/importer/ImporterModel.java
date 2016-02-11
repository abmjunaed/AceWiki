package importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.uzh.ifi.attempto.acewiki.Wiki;
import ch.uzh.ifi.attempto.acewiki.gui.SentenceEditorHandler;
import ch.uzh.ifi.attempto.base.TextElement;
import ch.uzh.ifi.attempto.base.TextOperator;
import ch.uzh.ifi.attempto.preditor.PreditorWindow;
import nextapp.echo.app.event.ActionEvent;

public class ImporterModel {

	private Wiki wiki;
	PreditorWindow preEditorWindow;
	TextOperator to;
	TextElement te;
	private String uploadedOwl;
	private String refactoredOwl;// so that owl-verbalizer can work with it
	private List<ArrayList<String>> validSentences = new ArrayList<ArrayList<String>>();
	private List<ArrayList<String>> invalidSentences = new ArrayList<ArrayList<String>>();

	public ImporterModel(Wiki wiki) {
		this.wiki = wiki;
		to = wiki.getLanguageHandler().getTextOperator();
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

	public List<ArrayList<String>> getValidSentences() {
		return validSentences;
	}

	public void setValidSentences(List<ArrayList<String>> validSentences) {
		this.validSentences = validSentences;
	}

	public List<ArrayList<String>> getInvalidSentences() {
		return invalidSentences;
	}

	public void setInvalidSentences(List<ArrayList<String>> invalidSentences) {
		this.invalidSentences = invalidSentences;
	}

	/**
	 * Refactor e.g. rewrite disjointwith, replace annotation etc. So that
	 * owl-verbalizer can work with it
	 */
	public void refactorUploadedOwl() {
		// TODO: refactor
		refactoredOwl = uploadedOwl;
	}

	public void handleVerbalizerResponse(String response) {

		String[] sentences = response.split("[\\r\\n]+");
		System.out.println("\n sentences:\n");
		for (String s : sentences) {

			// remove the full stop at last index because e.g. Monument. is not
			// a lesicon, lexicon is Monument (i.e. without full stop at end)
			int len = s.length();
			s = s.substring(0, len - 1);
			List<String> words = new ArrayList<String>(Arrays.asList(s.split(" ")));
			// String[] words = s.split(" ");
			words.add(".");// add full stop as another word

			System.out.println(words);

			handleAceSentence(words);
		}

	}

	public void handleAceSentence(List<String> words) {
		preEditorWindow = SentenceEditorHandler.generateCreationWindow(null, wiki.articlePage);

		for (String w : words) {
			te = to.createTextElement(w);
			// preEditorWindow.parser.addToken(w);
			preEditorWindow.textElementSelected(te);
		}

		// test if this sentence can be added in Acewiki
		// if can add, store it in another list
		if (preEditorWindow.parser.isComplete()) {
			this.validSentences.add((ArrayList<String>) words);
		}
		// if cant add, store this sentence in a list
		else {
			this.invalidSentences.add((ArrayList<String>) words);
		}

		// clean the textcontainer and parser after adding a sentence
		preEditorWindow.clearTokens();

		// for all the valid text elements:

	}

	public void insertSentencesInWiki() {
		// add the valid sentences in the text container of the perEditor
		if (this.validSentences.size() > 0) {
			for (ArrayList<String> validSen : this.validSentences) {
				for (String w : validSen) {
					te = to.createTextElement(w);
					preEditorWindow.textElementSelected(te);
				}
			}
		}
		// add all sentences
		preEditorWindow.notifyActionListeners(new ActionEvent(preEditorWindow, "OK"));

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
