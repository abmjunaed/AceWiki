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

import java.util.List;

/**
 * This class represents a sentence for a multilingual AceWiki engine.
 * 
 * @author Kaarel Kaljurand
 * @author Tobias Kuhn
 */
public abstract class MultilingualSentence extends AbstractSentence {

	/**
	 * Returns a list of sentence details describing translations into all other languages.
	 * 
	 * @param currentLanguage The current language (to be excluded).
	 * @return A list of sentence details on translations.
	 */
	public abstract List<SentenceDetail> getTranslations(String currentLanguage);

}
