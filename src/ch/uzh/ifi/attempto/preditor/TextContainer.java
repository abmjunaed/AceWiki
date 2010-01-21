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

package ch.uzh.ifi.attempto.preditor;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a text container that stores a sequence of text elements that represent a
 * (partial) text or sentence.
 * 
 * @author Tobias Kuhn
 */
public class TextContainer {
	
	private List<TextElement> elements = new ArrayList<TextElement>();
	private ContextChecker contextChecker;
	
	/**
	 * Creates a new text container.
	 */
	public TextContainer() {
	}
	
	/**
	 * Creates a new text container using the given context checker.
	 * 
	 * @param contextChecker The context checker to be used.
	 */
	public TextContainer(ContextChecker contextChecker) {
		setContextChecker(contextChecker);
	}
	
	/**
	 * Creates a new text container that contains the given text elements.
	 * 
	 * @param elements The elements to be added to the new text container.
	 */
	public TextContainer(TextElement... elements) {
		for (TextElement el : elements) {
			addElement(el);
		}
	}
	
	/**
	 * Creates a new text container that uses the given context checker and that contains the
	 * given text elements.
	 * 
	 * @param contextChecker The context checker to be used.
	 * @param elements The elements to be added to the new text container.
	 */
	public TextContainer(ContextChecker contextChecker, TextElement... elements) {
		for (TextElement el : elements) {
			addElement(el);
		}
		setContextChecker(contextChecker);
	}
	
	/**
	 * Returns the number of text elements of this text container.
	 * 
	 * @return The number of text elements.
	 */
	public int getTextElementsCount() {
		return elements.size();
	}
	
	/**
	 * Returns the text element with the given index.
	 * @param index The index of the text element to be returned.
	 * @return The text element.
	 */
	public TextElement getTextElement(int index) {
		return elements.get(index);
	}
	
	/**
	 * Returns the sequence of text elements.
	 * 
	 * @return A list containing the text elements.
	 */
	public List<TextElement> getTextElements() {
		return new ArrayList<TextElement>(elements);
	}
	
	/**
	 * Sets the text elements.
	 * @param elements A list of text elements.
	 */
	public void setTextElements(List<TextElement> elements) {
		this.elements = new ArrayList<TextElement>(elements);
	}
	
	/**
	 * Adds the text element to the end of the sequence.
	 * 
	 * @param el The text element to be added.
	 */
	public void addElement(TextElement el) {
		el.setTextContainer(this);
		elements.add(el);
	}
	
	/**
	 * Removes all text elements.
	 */
	public void removeAllElements() {
		for (TextElement te : elements) te.removeTextContainer();
		elements.clear();
	}
	
	/**
	 * Removes the last text element of the sequence if it is not empty.
	 */
	public void removeLastElement() {
		if (elements.size() > 0) {
			int last = elements.size() - 1;
			elements.get(last).removeTextContainer();
			elements.remove(last);
		}
	}
	
	/**
	 * Returns the text that is represented by the sequence of text element as a string.
	 * 
	 * @return The text.
	 */
	public String getText() {
		String text = "";
		for (TextElement e : elements) {
			if (e.getText().matches("[.?!]")) {
				text += e.getText();
			} else {
				text += " " + e.getText();
			}
		}
		if (text.startsWith(" ")) {
			text = text.substring(1);
		}
		return text;
	}
	
	/**
	 * Sets the context checker.
	 * 
	 * @param contextChecker The new context checker.
	 */
	public void setContextChecker(ContextChecker contextChecker) {
		this.contextChecker = contextChecker;
	}
	
	/**
	 * Returns the context checker of this text container.
	 * 
	 * @return The context checker.
	 */
	public ContextChecker getContextChecker() {
		return contextChecker;
	}
	
	/**
	 * Returns the position of the given text element within this text container or -1 if the
	 * text element is not contained by this text container. Note that the elements are checked for
	 * identity, not for equality.
	 * 
	 * @param textElement The text element.
	 * @return The index of the text element.
	 */
	public int getIndexOf(TextElement textElement) {
		// indexOf(...) does not work, because it uses the equals-method, but we need to check for identity:
		int index = -1;
		for (int i = 0 ; i < elements.size() ; i++) {
			if (elements.get(i) == textElement) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	String getTextElementText(TextElement te) {
		if (contextChecker == null) {
			return te.getOriginalText();
		} else {
			String preceding = null;
			String following = null;
			int pos = getIndexOf(te);
			if (pos > 0) {
				preceding = elements.get(pos-1).getOriginalText();
			}
			if (pos < elements.size()-1) {
				following = elements.get(pos+1).getOriginalText();
			}
			return contextChecker.getTextInContext(te, preceding, following);
		}
	}

}
