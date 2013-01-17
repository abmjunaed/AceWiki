// This file is part of AceWiki.
// Copyright 2008-2012, AceWiki developers.
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

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to modify or create topics.
 * 
 * @author Kaarel Kaljurand
 * @author Tobias Kuhn
 */
public class TopicChanger implements LexiconChanger {

	public String getDescription() {
		return "Every article is a sequence of statements and comments.";
	}

	public List<LexiconDetail> getDetails(OntologyElement el) {
		GeneralTopic topic = (GeneralTopic) el;
		List<LexiconDetail> l = new ArrayList<LexiconDetail>();
		l.add(new LexiconDetail(
				"Name of the article",
				"",
				topic.getWord()
				));
		return l;
	}

	public void save(OntologyElement el, int wordNumber, List<Object> newValues, Ontology ontology)
			throws InvalidWordException {
		GeneralTopic topic = (GeneralTopic) el;
		String word = (String) newValues.get(0);
		OntologyElement oe = ontology.getElement(word);
		if (oe != null && oe != topic) {
			throw new InvalidWordException("The word '" + word + "' is already used. " +
				"Please use a different one.");
		}
		ontology.change(topic, newValues.get(0).toString());
	}

}
