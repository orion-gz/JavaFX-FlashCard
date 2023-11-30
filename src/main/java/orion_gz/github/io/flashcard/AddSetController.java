package orion_gz.github.io.flashcard;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddSetController {
    /* Text Area */
    @FXML
    JFXTextArea setName;

    /* Button */
    @FXML
    JFXButton cancelBtn;

    @FXML
    JFXButton okBtn;
    /* Button */

    // display dialog
    // add flash card set dialog
    public void displayAddDialog(ObservableList<Data.FlashCardSet> flashCardSets, JFXListView<Data.FlashCardSet> setsListView) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddDialog.fxml"));
            Parent dialog = loader.load();
            Scene scene = new Scene(dialog);

            final ObservableList<String> stylesheets = scene.getStylesheets();
            stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                    JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                    AddSetController.class.getResource("components.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            setName = (JFXTextArea) scene.lookup("#setName");
            cancelBtn = (JFXButton) scene.lookup("#cancelBtn");
            cancelBtn.setOnAction((e) -> stage.close());

            okBtn = (JFXButton) scene.lookup("#okBtn");
            okBtn.setOnAction((e) -> {
                Data.FlashCardSet newSet = new Data.FlashCardSet(setName.getText());
                flashCardSets.add(newSet);
                setsListView.getSelectionModel().select(newSet);
                Data.saveFlashCards(flashCardSets);
                stage.close();
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // remove flashcard set
    public void removeSet(Data.FlashCardSet selectedSet, ObservableList<Data.FlashCardSet> flashCardSets, JFXListView<Data.FlashCardSet> setsListView, JFXListView<Data.FlashCard> cardsListView) {
        if (selectedSet != null) {
            flashCardSets.remove(selectedSet);
            Data.saveFlashCards(flashCardSets);
            setsListView.getSelectionModel().clearSelection();
            cardsListView.getItems().clear();
        }
    }
}
