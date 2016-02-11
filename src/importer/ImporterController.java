package importer;

import ch.uzh.ifi.attempto.acewiki.Wiki;
import ch.uzh.ifi.attempto.echocomp.UploadWindow;
import ch.uzh.ifi.attempto.preditor.PreditorWindow;
import nextapp.echo.app.Window;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

public class ImporterController extends Window implements ActionListener {

	private Wiki wiki;
	PreditorWindow preEditorWindow;
	UploadWindow uw;
	Verbalizer verbalizer;
	private ImporterModel model;

	public ImporterController(Wiki wiki) {
		this.wiki = wiki;
		verbalizer = new Verbalizer();
		model = new ImporterModel(wiki);
	}

	private void uploadOwlFile() {
		String title = "Import Owl ontology";
		String message = "Choose an owl/xml file to import:";
		// String actionCommand = "Load Lexicon";

		uw = new UploadWindow(title, message, null, this);
		// uw.setActionCommand(actionCommand);
		uw.setMaxFileSize(1048576);
		uw.hideOpenButton();
		wiki.showWindow(uw);
	}

	public void importButtonClicked() {
		// upload owl
		uploadOwlFile();
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		String cmd = e.getActionCommand();

		if (src instanceof UploadWindow && cmd.equals(UploadWindow.uploadCompleted)) {
			// close the uploader window
			wiki.removeWindow(uw);
			
			importOwl();
		}
	}

	public void importOwl() {
		String uploadedOwl = uw.getFileContent();
		model.setUploadedOwl(uploadedOwl);
		// post operation on owl e.g. rewrite disjointwith, replace annotation
		// etc.
		model.refactorUploadedOwl();

		// call owl-verbalizer in CSV mode

		// create lexicon (do this inside model)

		// call owl-verbalizer in ACE sentence mode
		String response = verbalizer.call(model.getRefactoredOwl());
		System.out.println(response);

		// filter out the ACE sentences which are according to AceWiki grammar
		model.handleVerbalizerResponse(response);

		// insert valid sentences in the wiki
		model.insertSentencesInWiki();

	}

}
