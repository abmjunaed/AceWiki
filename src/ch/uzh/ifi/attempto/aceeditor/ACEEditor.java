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

package ch.uzh.ifi.attempto.aceeditor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.Window;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.filetransfer.Download;
import nextapp.echo2.app.filetransfer.DownloadProvider;
import nextapp.echo2.webcontainer.command.BrowserRedirectCommand;
import ch.uzh.ifi.attempto.echocomp.MessageWindow;
import ch.uzh.ifi.attempto.echocomp.TextAreaWindow;
import ch.uzh.ifi.attempto.echocomp.UploadWindow;
import ch.uzh.ifi.attempto.echocomp.WindowPane;
import ch.uzh.ifi.attempto.preditor.ContextChecker;
import ch.uzh.ifi.attempto.preditor.EnglishContextChecker;
import ch.uzh.ifi.attempto.preditor.PreditorWindow;
import ch.uzh.ifi.attempto.preditor.TextContainer;
import ch.uzh.ifi.attempto.preditor.TextElement;
import echopointng.KeyStrokeListener;
import echopointng.KeyStrokes;

/**
 * This is the main class of the ACE Editor web application. The ACE Editor allows users to write
 * sentences in ACE by the use of a predictive editor. Users can extend the lexicon and they can
 * upload their own lexica.
 * 
 * @author Tobias Kuhn
 */
public class ACEEditor extends Window implements ActionListener, KeyStrokes {

	private static final long serialVersionUID = -684743065195237612L;

	private static final ContextChecker contextChecker = new EnglishContextChecker(true, true);
	private static Properties properties;

	private boolean editMode;
	private LexiconHandler lexiconHandler;
	private Map<String, String> parameters;

	private TextEntry selectedEntry;
	private TextEntry finalEntry = new TextEntry(null, this);
	private TextEntry clipboard;
	
	private Column textColumn = new Column();
	private Column mainColumn = new Column();
	private MenuBar menuBar;
	private KeyStrokeListener keyStrokeListener = new KeyStrokeListener();
	
	/**
	 * Creates a new ACE Editor application.
	 * 
	 * @param parameters A set of parameters in the form of name/value pairs.
	 */
	public ACEEditor(Map<String, String> parameters) {
		setTitle("ACE Editor");
		this.parameters = parameters;
		
		lexiconHandler = new LexiconHandler(parameters.get("lexicon"));

		SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL);
		splitPane.setSeparatorPosition(new Extent(25));

		menuBar = new MenuBar(this);
		menuBar.setSelected("Default Expanded", true);
		menuBar.setSelected("Default Paraphrase", true);
		menuBar.setSelected("Default Syntax Boxes", true);
		menuBar.setSelected("Default Pretty-Printed DRS", true);
		menuBar.setEnabled("Paste", false);
		splitPane.add(menuBar);

		textColumn.setInsets(new Insets(0, 5));
		textColumn.add(finalEntry);

		mainColumn.add(textColumn);

		// Up and down keys for moving the selection:
		keyStrokeListener.addKeyCombination(VK_UP, "Up Pressed");
		keyStrokeListener.addKeyCombination(VK_DOWN, "Down Pressed");

		// Space key for expand/collapse or add:
		keyStrokeListener.addKeyCombination(VK_SPACE, "Space Pressed");

		// Backspace key for delete:
		keyStrokeListener.addKeyCombination(VK_BACK_SPACE, "Backspace Pressed");

		// Function key + A for add:
		keyStrokeListener.addKeyCombination(VK_A | CONTROL_MASK, "Func-A Pressed");
		keyStrokeListener.addKeyCombination(VK_A | META_MASK, "Func-A Pressed");
		keyStrokeListener.addKeyCombination(VK_A | ALT_MASK, "Func-A Pressed");

		// Function key + M for modify:
		keyStrokeListener.addKeyCombination(VK_M | CONTROL_MASK, "Func-M Pressed");
		keyStrokeListener.addKeyCombination(VK_M | META_MASK, "Func-M Pressed");
		keyStrokeListener.addKeyCombination(VK_M | ALT_MASK, "Func-M Pressed");

		// Function key + X for cut:
		keyStrokeListener.addKeyCombination(VK_X | CONTROL_MASK, "Func-X Pressed");
		keyStrokeListener.addKeyCombination(VK_X | META_MASK, "Func-X Pressed");
		keyStrokeListener.addKeyCombination(VK_X | ALT_MASK, "Func-X Pressed");

		// Function key + C for copy:
		keyStrokeListener.addKeyCombination(VK_C | CONTROL_MASK, "Func-C Pressed");
		keyStrokeListener.addKeyCombination(VK_C | META_MASK, "Func-C Pressed");
		keyStrokeListener.addKeyCombination(VK_C | ALT_MASK, "Func-C Pressed");

		// Function key + V for paste:
		keyStrokeListener.addKeyCombination(VK_V | CONTROL_MASK, "Func-V Pressed");
		keyStrokeListener.addKeyCombination(VK_V | META_MASK, "Func-V Pressed");
		keyStrokeListener.addKeyCombination(VK_V | ALT_MASK, "Func-V Pressed");

		// Function key + O for open:
		keyStrokeListener.addKeyCombination(VK_O | CONTROL_MASK, "Func-O Pressed");
		keyStrokeListener.addKeyCombination(VK_O | META_MASK, "Func-O Pressed");
		keyStrokeListener.addKeyCombination(VK_O | ALT_MASK, "Func-O Pressed");

		// Function key + S for save:
		keyStrokeListener.addKeyCombination(VK_S | CONTROL_MASK, "Func-S Pressed");
		keyStrokeListener.addKeyCombination(VK_S | META_MASK, "Func-S Pressed");
		keyStrokeListener.addKeyCombination(VK_S | ALT_MASK, "Func-S Pressed");

		keyStrokeListener.addActionListener(this);
		mainColumn.add(keyStrokeListener);

		splitPane.add(mainColumn);
		getContent().add(splitPane);

		select(finalEntry);
	}

	/**
	 * Returns whether parsing with the compiled lexicon of the APE executable is enabled.
	 * 
	 * @return true if parsing with the compiled lexicon is enabled.
	 */
	public boolean isParseWithClexEnabled() {
		return !"off".equals(getParameter("parse_with_clex"));
	}

	/**
	 * Returns whether the lexicon is immutable or can be changed by users.
	 * 
	 * @return true if the lexicon is immutable.
	 */
	public boolean isLexiconImmutable() {
		return !"off".equals(getParameter("immutable_lexicon"));
	}
	
	/**
	 * Returns the maximum file size (in bytes) for file upload. 0 means unlimited file size.
	 * 
	 * @return The maximum file size.
	 */
	public int getMaxUploadFileSize() {
		try {
			return Integer.parseInt(getParameter("max_upload_file_size"));
		} catch (NumberFormatException ex) {}
		return 0;
	}
	
	/**
	 * Returns the value of the given parameter. These parameters are defined in the web.xml file
	 * of the web application.
	 * 
	 * @param paramName The parameter name.
	 * @return The value of the parameter.
	 */
	public String getParameter(String paramName) {
		return parameters.get(paramName);
	}

	/**
	 * Returns the full text of the current content of this ACE Editor instance.
	 * 
	 * @return The full text.
	 */
	public String getFullText() {
		String text = "";
		for (Component c : textColumn.getComponents()) {
			String s = ((TextEntry) c).getText();
			if (c == finalEntry) break;
			if (s == null) s = "";
			text += s + "\n\n";
		}
		return text;
	}

	LexiconHandler getLexiconHandler() {
		return lexiconHandler;
	}

	void select(TextEntry entry) {
		if (selectedEntry != null) {
			selectedEntry.setSelected(false);
		}
		entry.setSelected(true);
		selectedEntry = entry;

		if (selectedEntry == finalEntry) {
			menuBar.setEnabled("Delete", false);
			menuBar.setEnabled("Cut", false);
		} else {
			menuBar.setEnabled("Delete", true);
			menuBar.setEnabled("Cut", true);
		}
		if (selectedEntry.isEmpty()) {
			menuBar.setEnabled("Modify...", false);
		} else {
			menuBar.setEnabled("Modify...", true);
		}
		if (selectedEntry.isEmpty() || selectedEntry.isComment()) {
			menuBar.setEnabled("Expanded", false);
			menuBar.setSelected("Expanded", false);
			for (String s : ResultItem.TYPES) {
				menuBar.setEnabled("Show " + s, false);
				menuBar.setSelected("Show " + s, false);
			}
		} else {
			menuBar.setEnabled("Expanded", true);
			menuBar.setSelected("Expanded", selectedEntry.isExpanded());
			for (String s : ResultItem.TYPES) {
				menuBar.setEnabled("Show " + s, true);
				menuBar.setSelected("Show " + s, selectedEntry.isResultItemVisible(s));
			}
		}
		menuBar.update();
	}

	void entryChanged(TextEntry entry) {
		if (entry == selectedEntry) {
			menuBar.setSelected("Expanded", selectedEntry.isExpanded());
			menuBar.update();
		}
	}
	
	void showWindow(WindowPane window) {
		getContent().add(window);
	}

	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		Object source = e.getSource();

		if (c.equals("About")) {
			String v = getInfo("aceeditor-version");
			String r = getInfo("aceeditor-release-stage");
			String d = getInfo("aceeditor-build-date");
			String a = getInfo("aceeditor-developer");
			showWindow(new MessageWindow(
					"ACE Editor",
					"ACE Editor " + v + " (" + r + "), " + d + ", " + a,
					"OK"
				));
		} else if (c.equals("Attempto Website")) {
			ApplicationInstance.getActive().enqueueCommand(
					new BrowserRedirectCommand("http://attempto.ifi.uzh.ch")
				);
		} else if (c.equals("Open Text...")) {
			openFile();
		} else if (c.equals("Save Text...")) {
			saveFile();
		} else if (c.equals("Load Lexicon...")) {
			loadLexicon(false);
		} else if (c.equals("Replace Lexicon...")) {
			loadLexicon(true);
		} else if (c.equals("Save Lexicon...")) {
			saveLexicon();
		} else if (c.equals("Add...")) {
			showEditor(false);
		} else if (c.equals("Add Comment...")) {
			showCommentEditor(false);
		} else if (c.equals("Add Separator")) {
			TextEntry newEntry = new TextEntry(null, this);
			textColumn.add(newEntry, textColumn.indexOf(selectedEntry));
			select(newEntry);
		} else if (c.equals("Modify...")) {
			if (selectedEntry.isComment()) {
				showCommentEditor(true);
			} else {
				showEditor(true);
			}
		} else if (c.equals("Delete")) {
			deleteSelectedEntry();
		} else if (c.equals("Cut")) {
			cutSelectedEntry();
		} else if (c.equals("Copy")) {
			copySelectedEntry();
		} else if (c.equals("Paste")) {
			pasteFromClipboard();
		} else if (c.equals("Expanded")) {
			selectedEntry.setExpanded(menuBar.isSelected("Expanded"));
		} else if (c.equals("Expand All")) {
			for (Component comp : textColumn.getComponents()) {
				((TextEntry) comp).setExpanded(true);
			}
		} else if (c.equals("Collapse All")) {
			for (Component comp : textColumn.getComponents()) {
				((TextEntry) comp).setExpanded(false);
			}
		} else if (c.startsWith("Show ")) {
			selectedEntry.setResultItemVisible(c.substring(5), menuBar.isSelected(c));
			selectedEntry.setExpanded(true);
		} else if (source instanceof PreditorWindow && c.equals("Cancel")) {
			PreditorWindow preditor = (PreditorWindow) source;
			preditor.setVisible(false);
			preditor.dispose();
			refreshKeyStrokeListener();
		} else if (source instanceof PreditorWindow && c.equals("OK")) {
			PreditorWindow preditor = (PreditorWindow) source;
			preditor.setContextChecker(contextChecker);
			TextContainer textContainer = preditor.getTextContainer();
			if (textContainer.getTextElementsCount() == 0) {
				preditor.dispose();
				preditor.setVisible(false);
				refreshKeyStrokeListener();
			} else {
				List<TextElement> finalElements = preditor.getPossibleNextTokens(".", "?");
				if (!finalElements.isEmpty()) textContainer.addElement(finalElements.get(0));
				List<TextElement> l = textContainer.getTextElements();
				if (l.isEmpty() || l.get(l.size() - 1).getText().matches("[.?]")) {
					if (editMode) {
						selectedEntry.setText(textContainer.getText());
						select(selectedEntry);
					} else {
						TextEntry newEntry = new TextEntry(
								textContainer.getText(),
								this,
								menuBar.isSelected("Default Expanded")
							);
						for (String s : ResultItem.TYPES) {
							newEntry.setResultItemVisible(s, menuBar.isSelected("Default " + s));
						}
						textColumn.add(newEntry, textColumn.indexOf(selectedEntry));
						select(newEntry);
					}
					preditor.dispose();
					preditor.setVisible(false);
					refreshKeyStrokeListener();
				} else {
					showWindow(new MessageWindow(
							"Error",
							"There are unfinished sentences.",
							"OK"
						));
				}
			}
		} else if (source instanceof TextAreaWindow && c.equals("OK")) {
			TextAreaWindow cew = (TextAreaWindow) source;
			if (editMode) {
				selectedEntry.setText("# " + cew.getText());
				select(selectedEntry);
			} else {
				TextEntry newEntry = new TextEntry("# " + cew.getText(), this, false);
				textColumn.add(newEntry, textColumn.indexOf(selectedEntry));
				select(newEntry);
			}
		} else if (c.equals("Upload File")) {
			String fileContent = ((UploadWindow) source).getFileContent();
			if (fileContent != null) {
				textColumn.removeAll();
				String[] l = fileContent.replaceAll("\\s*(#[^\\n]*\\n)", "\n\n$1\n")
						.split("\\n[ \\t\\x0B\\f\\r]*\\n");
				for (String line : l) {
					TextEntry newEntry = new TextEntry(line, this, false);
					textColumn.add(newEntry);
					for (String s : ResultItem.TYPES) {
						newEntry.setResultItemVisible(s, menuBar.isSelected("Default " + s));
					}
				}
				textColumn.add(finalEntry);
				select((TextEntry) textColumn.getComponent(0));
			}
		} else if (c.equals("Load Lexicon") || c.equals("Replace Lexicon")) {
			String fileContent = ((UploadWindow) source).getFileContent();
			if (fileContent != null) {
				if (c.equals("Replace Lexicon")) {
					textColumn.removeAll();
					textColumn.add(finalEntry);
					select(finalEntry);
					lexiconHandler.clear();
				}
				String[] l = (fileContent + " ").replaceAll("#[^\\n]*\\n", " ")
						.replaceAll("\\s+", " ").replaceFirst("^ ", "")
						.replaceAll("\\. ", ".\n").split("\\n");
				for (String line : l) {
					if (line.equals("")) continue;
					lexiconHandler.addWord(line);
				}

				if (c.equals("Replace Lexicon")) {
					showWindow(new MessageWindow(
							"Lexicon Replaced",
							"The lexicon has been replaced.",
							"OK"
						));
				} else {
					showWindow(new MessageWindow(
							"Lexicon Loaded",
							"The lexicon has been loaded.",
							"OK"
						));
				}
			}
		} else if (c.equals("Up Pressed")) {
			int i = textColumn.indexOf(selectedEntry);
			if (i > 0) {
				select((TextEntry) textColumn.getComponent(i-1));
			}
		} else if (c.equals("Down Pressed")) {
			int i = textColumn.indexOf(selectedEntry);
			if (i < textColumn.getComponentCount()-1) {
				select((TextEntry) textColumn.getComponent(i+1));
			}
		} else if (c.equals("Space Pressed")) {
			if (selectedEntry.isEmpty()) {
				showEditor(false);
			} else {
				if (selectedEntry.isExpanded()) {
					selectedEntry.setExpanded(false);
				} else {
					selectedEntry.setExpanded(true);
				}
			}
		} else if (c.equals("Backspace Pressed")) {
			deleteSelectedEntry();
		} else if (c.equals("Func-A Pressed")) {
			showEditor(false);
		} else if (c.equals("Func-M Pressed")) {
			if (selectedEntry.isComment()) {
				showCommentEditor(true);
			} else {
				showEditor(true);
			}
		} else if (c.equals("Func-X Pressed")) {
			cutSelectedEntry();
		} else if (c.equals("Func-C Pressed")) {
			copySelectedEntry();
		} else if (c.equals("Func-V Pressed")) {
			pasteFromClipboard();
		} else if (c.equals("Func-O Pressed")) {
			openFile();
		} else if (c.equals("Func-S Pressed")) {
			saveFile();
		}
	}

	private void refreshKeyStrokeListener() {
		// The different keystroke listeners somehow interfere with each other so that this
		// work-around is needed:
		mainColumn.remove(keyStrokeListener);
		mainColumn.add(keyStrokeListener);
	}

	private void showEditor(boolean edit) {
		if (edit && selectedEntry.isEmpty()) return;
		
		ACEEditorMenuCreator menuCreator = new ACEEditorMenuCreator(this, lexiconHandler);
		PreditorWindow preditor = new PreditorWindow(
				"ACE Text Editor",
				new ACEEditorGrammar(),
				"text",
				menuCreator
			);
		menuCreator.setPreditorWindow(preditor);
		preditor.setContextChecker(contextChecker);
		preditor.addActionListener(this);
		this.editMode = edit;
		if (edit) {
			preditor.addText(selectedEntry.getText() + " ");
		}
		showWindow(preditor);
	}

	private void showCommentEditor(boolean edit) {
		this.editMode = edit;
		if (edit) {
			showWindow(new TextAreaWindow(
					"Comment Editor",
					selectedEntry.getText().substring(2),
					this
				));
		} else {
			showWindow(new TextAreaWindow("Comment Editor", "", this));
		}
	}

	private void deleteSelectedEntry() {
		if (selectedEntry != finalEntry) {
			int i = textColumn.indexOf(selectedEntry);
			TextEntry nextEntry = (TextEntry) textColumn.getComponent(i+1);
			textColumn.remove(selectedEntry);
			select(nextEntry);
		}
	}

	private void copySelectedEntry() {
		clipboard = selectedEntry.copy();
		menuBar.setEnabled("Paste", true);
		menuBar.update();
	}

	private void cutSelectedEntry() {
		if (selectedEntry != finalEntry) {
			copySelectedEntry();
			deleteSelectedEntry();
		}
	}

	private void pasteFromClipboard() {
		if (clipboard != null) {
			TextEntry newEntry = clipboard.copy();
			textColumn.add(newEntry, textColumn.indexOf(selectedEntry));
			select(newEntry);
		}
	}

	private void openFile() {
		UploadWindow uw = new UploadWindow(
				"Open File",
				"Warning: This will delete the current content.\nChoose a file to open:",
				null,
				this
		);
		uw.setActionCommand("Upload File");
		uw.setMaxFileSize(getMaxUploadFileSize());
		showWindow(uw);
	}

	private void saveFile() {
		final String f = getFullText();
		DownloadProvider provider = new DownloadProvider() {

			public String getContentType() {
				return "text/plain";
			}

			public String getFileName() {
				return "text.ace.txt";
			}

			public int getSize() {
				return f.length();
			}

			public void writeFile(OutputStream out) throws IOException {
				out.write(f.getBytes());
				out.close();
			}

		};
		getApplicationInstance().enqueueCommand(new Download(provider, true));
	}

	private void loadLexicon(boolean replace) {
		String title = "Load Lexicon";
		String message = "Choose a lexicon file to load:";
		String actionCommand = "Load Lexicon";
		if (replace) {
			title = "Replace Lexicon";
			message = "Warning: This will delete the current content.\n" + message;
			actionCommand = "Replace Lexicon";
		}
		UploadWindow uw = new UploadWindow(title, message, null, this);
		uw.setActionCommand(actionCommand);
		uw.setMaxFileSize(getMaxUploadFileSize());
		showWindow(uw);
	}

	private void saveLexicon() {
		final String f = lexiconHandler.getLexiconFileContent();
		DownloadProvider provider = new DownloadProvider() {

			public String getContentType() {
				return "text/plain";
			}

			public String getFileName() {
				return "text.lex.pl";
			}

			public int getSize() {
				return f.length();
			}

			public void writeFile(OutputStream out) throws IOException {
				out.write(f.getBytes());
				out.close();
			}

		};
		getApplicationInstance().enqueueCommand(new Download(provider, true));
	}

	/**
	 * Returns information about ACE Editor, like the version number and the release date. This
	 * information is read from the file "aceeditor.properties".
	 * 
	 * @param key The key string.
	 * @return The value for the given key.
	 */
	public static String getInfo(String key) {
		if (properties == null) {
			String f = "ch/uzh/ifi/attempto/aceeditor/aceeditor.properties";
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(f);
			properties = new Properties();
			try {
				properties.load(in);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return properties.getProperty(key);
	}

}