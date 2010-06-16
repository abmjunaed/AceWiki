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

package ch.uzh.ifi.attempto.acewiki.gui.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import nextapp.echo2.app.Column;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Font;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import ch.uzh.ifi.attempto.acewiki.Wiki;
import ch.uzh.ifi.attempto.acewiki.core.ontology.OntologyElement;
import ch.uzh.ifi.attempto.acewiki.gui.IndexBar;
import ch.uzh.ifi.attempto.acewiki.gui.ListItem;
import ch.uzh.ifi.attempto.acewiki.gui.Title;
import ch.uzh.ifi.attempto.acewiki.gui.WikiLink;
import ch.uzh.ifi.attempto.echocomp.SolidLabel;
import ch.uzh.ifi.attempto.echocomp.VSpace;

/**
 * This class represents an page that shows an index of all articles that exist in the wiki.
 * 
 * @author Tobias Kuhn
 */
public class IndexPage extends WikiPage implements ActionListener {

	private static final long serialVersionUID = 6061966610996079528L;
	
	private static final int pageSize = 25;
	
	private String chosenChar = "A";
	private int chosenPage = 0;
	private HashMap<String, OntologyElement> entries = new HashMap<String, OntologyElement>();
	
	private Column indexColumn = new Column();
	private IndexBar letterIndexBar;
	private IndexBar numberIndexBar;
	
	/**
	 * Creates a new index page.
	 * 
	 * @param wiki The wiki instance.
	 */
	public IndexPage(Wiki wiki) {
		super(wiki, new Title("Index", true));
		
		addTab("Main Page", this);
		addSelectedTab("Index");
		addTab("Search", this);
		
		add(new VSpace(20));
		
		letterIndexBar = new IndexBar("First letter:", this);
		add(letterIndexBar);
		
		numberIndexBar = new IndexBar("Page:", 0, this);
		add(numberIndexBar);
		
		indexColumn.setInsets(new Insets(10, 5, 5, 20));
		indexColumn.setCellSpacing(new Extent(2));
		add(indexColumn);
	}
	
	protected void doUpdate() {
		indexColumn.removeAll();
		
		entries.clear();
		for (OntologyElement e : getWiki().getOntologyElements()) {
			for (String indexWord : e.getIndexEntries()) {
				if (indexWord.toUpperCase().startsWith(chosenChar)) {
					entries.put(indexWord, e);
				}
			}
		}
		
		if (entries.size() == 0) {
			numberIndexBar.setVisible(false);
			indexColumn.add(new SolidLabel(
					"(no entry starting with '" + chosenChar + "')",
					Font.ITALIC,
					10
				));
		} else {
			int i = ((entries.size()-1) / pageSize) + 1;
			if (chosenPage > i) chosenPage = 0;
			numberIndexBar.setNumbers(i);
			numberIndexBar.setActiveButton(chosenPage);
			updatePage();
		}
	}
	
	private void updatePage() {
		indexColumn.removeAll();
		
		List<String> indexWords = new ArrayList<String>(entries.keySet());
		Collections.sort(indexWords);
		
		numberIndexBar.setVisible(entries.size() > pageSize);
		
		int max = entries.size();
		if (max > (chosenPage + 1) * pageSize) max = (chosenPage + 1) * pageSize;
		
		for (int i = chosenPage * pageSize; i < max; i++) {
			String t = indexWords.get(i);
			OntologyElement el = entries.get(t);
			indexColumn.add(new ListItem(new WikiLink(el, t, getWiki(), false)));
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == letterIndexBar) {
			chosenChar = e.getActionCommand();
			log("page", "pressed: first letter " + chosenChar);
			chosenPage = 0;
			update();
		} else if (e.getSource() == numberIndexBar) {
			chosenPage = Integer.parseInt(e.getActionCommand()) - 1;
			log("page", "pressed: page " + (chosenPage+1));
			updatePage();
		} else if ("Main Page".equals(e.getActionCommand())) {
			getWiki().showStartPage();
		} else if ("Search".equals(e.getActionCommand())) {
			getWiki().showSearchPage();
		}
	}
	
	public boolean equals(Object obj) {
		return obj instanceof IndexPage;
	}
	
	public String toString() {
		return "-INDEX-";
	}

}