package orion_gz.github.io.flashcard;

import com.jfoenix.controls.*;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.DialogPane;

public class CardSets {
    public void showAddSetDialog(ObservableList<Data.FlashCardSet> flashCardSets, JFXListView<Data.FlashCardSet> setsListView) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Flashcard Set");
        dialog.setHeaderText("Enter Set Name");
        dialog.showAndWait().ifPresent(setName -> {
            Data.FlashCardSet newSet = new Data.FlashCardSet(setName);
            flashCardSets.add(newSet);
            setsListView.getSelectionModel().select(newSet);
            Data.saveFlashCards(flashCardSets);
        });
    }
    public void showRemoveSet(Data.FlashCardSet selectedSet, ObservableList<Data.FlashCardSet> flashCardSets, JFXListView<Data.FlashCardSet> setsListView, JFXListView<Data.FlashCard> cardsListView) {
        if (selectedSet != null) {
            flashCardSets.remove(selectedSet);
            Data.saveFlashCards(flashCardSets);
            setsListView.getSelectionModel().clearSelection();
            cardsListView.getItems().clear();
        }
    }
}
