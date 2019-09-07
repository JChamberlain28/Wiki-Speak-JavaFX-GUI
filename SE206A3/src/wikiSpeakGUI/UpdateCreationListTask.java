package wikiSpeakGUI;

import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

public class UpdateCreationListTask extends Task<Void> {


	private ListView<String> _listToUpdate;
	private String[] lines;
	private Button _delButton;
	private Button _playButton;
	private Text _creationNoText;






	public UpdateCreationListTask(ListView<String> listToUpdate, Button delButton, Button playButton, Text creationNoText) {
		
		_listToUpdate = listToUpdate;

		_delButton = delButton;
		_playButton = playButton;
		_creationNoText = creationNoText;

	}

	@Override
	protected Void call() throws Exception {

		// stores current creations in an array
		CommandFactory listFileCommand = new CommandFactory();
		List<String> listFileResult = listFileCommand.sendCommand("./listCreations.sh", true);

		lines = listFileResult.get(0).split("\\r?\\n");



		return null;
	}


	@Override
	protected void done() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				
				// adds each creation to the ListView
				_listToUpdate.getItems().clear();		
				for (String i : lines) {
					_listToUpdate.getItems().add(i);
				}

				

				// update number of creations Text
				int noOfCreations;
				if (lines[0].equals("(No creations currently exist)")) {
					noOfCreations = 0;
				}
				else {
					noOfCreations = lines.length;
				}
				_creationNoText.setText("Number of creations: " + noOfCreations);
				
				// auto selects the first item in the list (default selection)
				_listToUpdate.getSelectionModel().select(0);


				// disable play or delete button when no creations exist
				if (_listToUpdate.getSelectionModel().getSelectedItems().get(0).equals("(No creations currently exist)")) {
					_delButton.setDisable(true);
					_playButton.setDisable(true);
				}
				else {
					_delButton.setDisable(false);
					_playButton.setDisable(false);
				}




			}

		});

	}

}
