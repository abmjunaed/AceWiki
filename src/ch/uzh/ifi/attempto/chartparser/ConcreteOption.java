// This file is part of AceWiki.
// Copyright 2008-2010, Tobias Kuhn.
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

package ch.uzh.ifi.attempto.chartparser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an option (in a concrete way) how a partial sentence can be continued
 * according to given grammar. Such a concrete option consists of a word in the form of a
 * terminal category standing for a possible next token, and optionally of a pre-terminal category
 * from which the terminal category was derived.
 * 
 * @see AbstractOption
 * @see NextTokenOptions
 * @author Tobias Kuhn
 */
public class ConcreteOption {
	
	private final Terminal word;
	private final Preterminal category;
	private final String identifier;
	
	ConcreteOption(Grammar grammar, Terminal word, Preterminal category) {
		this.word = word;
		this.category = category;
		identifier = calculateIdentifier(grammar.getUsedFeatureNames());
	}
	
	ConcreteOption(Grammar grammar, LexicalRule lexRule) {
		this(grammar, lexRule.getWord(), lexRule.getCategory());
	}
	
	/**
	 * Returns the word of this concrete option.
	 * 
	 * @return The word in the form of a terminal category.
	 */
	public Terminal getWord() {
		return word;
	}
	
	/**
	 * Returns the pre-terminal category of this concrete option, or null if no pre-terminal
	 * category was involved.
	 * 
	 * @return The pre-terminal category.
	 */
	public Preterminal getCategory() {
		return category;
	}
	
	String calculateIdentifier(String[] usedFeatureNames) {
		if (category == null) {
			return word + " <-";
		} else {
			List<Integer> vars = new ArrayList<Integer>();
			List<Integer> mvars = new ArrayList<Integer>();
			
			vars.clear();
			mvars.clear();
			category.collectVars(vars, mvars);
			return word + " <- " + category.getIdentifier(mvars, usedFeatureNames);
		}
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof ConcreteOption)) return false;
		ConcreteOption other = (ConcreteOption) obj;
		return this.identifier.equals(other.identifier);
	}
	
	public int hashCode() {
		return identifier.hashCode();
	}
	
	public String toString() {
		if (category == null) {
			return word + " <-";
		} else {
			return word + " <- " + category;
		}
	}

}
