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

package ch.uzh.ifi.attempto.acewiki.gui;

import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Joiner;

import nextapp.echo.app.Insets;
import nextapp.echo.app.event.ActionListener;
import ch.uzh.ifi.attempto.acewiki.Wiki;
import ch.uzh.ifi.attempto.acewiki.core.OntologyElement;
import ch.uzh.ifi.attempto.echocomp.VSpace;
import ch.uzh.ifi.attempto.gfservice.GfServiceResultGrammar;


public class GrammarPage extends AbstractNavigationPage implements ActionListener {

	private static final long serialVersionUID = -2031690219932377941L;
	private static final Joiner JOINER_SPACE = Joiner.on(' ');
	private static final Joiner JOINER_COMMA = Joiner.on(", ");
	private NameValueTable table1, table2;
	private GfServiceResultGrammar mGrammar;
	private final Wiki mWiki;

	/**
	 * Creates a new grammar page.
	 * 
	 * @param wiki The wiki instance.
	 */
	public GrammarPage(Wiki wiki, GfServiceResultGrammar grammar) {
		super(wiki);
		mWiki = wiki;

		mGrammar = grammar;

		add(new Title("About grammar", true));
		addHorizontalLine();
		add(new VSpace(10));

		table1 = new NameValueTable();
		table1.setInsets(new Insets(10, 10, 10, 15));
		add(table1);

		addHeadline("Languages");
		table2 = new NameValueTable();
		table2.setInsets(new Insets(10, 10, 10, 15));
		add(table2);

		add(new VSpace(20));
	}

	protected void doUpdate() {
		table1.clear();
		table2.clear();

		table1.addEntry("Name", mGrammar.getName());
		table1.addEntry("Startcat", mGrammar.getStartcat());
		table1.addEntry("Categories", JOINER_COMMA.join(mGrammar.getCategories()));
		table1.addEntry("Functions", JOINER_COMMA.join(mGrammar.getFunctions()));

		for (Entry<String, Set<String>> entry : mGrammar.getLanguages().entrySet()) {
			OntologyElement ol = mWiki.getOntology().getElement(entry.getKey());
			if (ol == null) {
				table2.addEntry(entry.getKey(), JOINER_SPACE.join(entry.getValue()));
			} else {
				table2.addEntry(new WikiLink(ol, mWiki), JOINER_SPACE.join(entry.getValue()));
			}
		}
	}

	public boolean equals(Object obj) {
		return obj instanceof GrammarPage;
	}

	public String toString() {
		return "-GRAMMAR-";
	}

	@Override
	public boolean isSelected(String tabName) {
		return TAB_ABOUT_GRAMMAR.equals(tabName);
	}

}