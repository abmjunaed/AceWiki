// This file is part of AceWiki.
// Copyright 2008-2013, AceWiki developers.
// 
// AceWiki is free software: you can redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
// 
// AceWiki is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
// even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with AceWiki. If
// not, see http://www.gnu.org/licenses/.

package ch.uzh.ifi.attempto.acewiki.core;

import java.util.List;

import ch.uzh.ifi.attempto.acewiki.aceowl.ACEOWLOntoElement;
import ch.uzh.ifi.attempto.acewiki.aceowl.NounConcept;
import ch.uzh.ifi.attempto.base.TextElement;
import ch.uzh.ifi.attempto.base.TextOperator;

/**
 * This class is a partial implementation of a sentence.
 * 
 * @author Tobias Kuhn
 * @author Kaarel Kaljurand
 */
public abstract class AbstractSentence extends AbstractStatement implements Sentence {

	private boolean integrated = false;

	public List<TextElement> getTextElements(String language) {
		return getTextContainer(language).getTextElements();
	}

	public boolean isIntegrated() {
		return integrated;
	}

	public void setIntegrated(boolean integrated) {
		this.integrated = integrated;
	}

	public boolean isImmutable() {
		return getArticle() == null;
	}

	public int getNumberOfRepresentations() {
		return 1;
	}

	public String getText(String language) {
		String t = "";
		TextElement prev = null;
		TextOperator textOperator = getTextOperator(language);
		for (TextElement te : getTextElements(language)) {
			String glue = "";
			if (prev != null) {
				glue = textOperator.getGlue(prev, te);
			}
			if (te instanceof PrettyTextElement) {
				t += glue + ((PrettyTextElement) te).getUnderscoredText();
			} else {
				t += glue + te.getText();
			}
			prev = te;
		}
		return t;
	}
	
	/**
	 * We create lexicon according to the csv output of owl-verbalizer. So we
	 * already know type of each word. So while sending a sentence to APE, tag
	 * all the words so that APE can parse properly.
	 * Returns the sentence were words are tagged according to its type. e.g.
	 * every n: Noun v: verb...
	 * @param language
	 * @return tagged sentence
	 */
	public String getTaggedText(String language) {
		String t = "";
		TextElement prev = null;
		TextOperator textOperator = getTextOperator(language);
		for (TextElement te : getTextElements(language)) {
			String glue = "";
			if (prev != null) {
				glue = textOperator.getGlue(prev, te);
				t += glue;// most of the cases, glue is space
			}
			if (te instanceof OntologyTextElement) {
				OntologyElement oe = ((OntologyTextElement) te).getOntologyElement();
				if (oe instanceof ACEOWLOntoElement) {
					if (oe.getInternalType().equals("noun")) {
						t += "n:";
					} else if (oe.getInternalType().equals("trverb")) {
						t += "v:";
					}
				}
			}
			if (te instanceof PrettyTextElement) {
				t += ((PrettyTextElement) te).getUnderscoredText();
			} else {
				t += te.getText();
			}
			prev = te;
		}
		return t;
	}

}
