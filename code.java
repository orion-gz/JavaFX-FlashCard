import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Random;

public class Code extends Application {

    private static final String SETS_FILE = "flashcardSets.dat";

    private ObservableList<FlashcardSet> flashcardSets;
    private ListView<FlashcardSet> setsListView;
    private ListView<Flashcard> cardsListView;
    private TextArea definitionTextArea;
    private CheckBox randomModeCheckbox;
    private Button startQuizButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        flashcardSets = readFlashcardSetsFromFile();

        setsListView = new ListView<>(flashcardSets);
        setsListView.setPrefWidth(200);
        setsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showFlashcards(newValue));

        cardsListView = new ListView<>();
        cardsListView.setPrefWidth(200);
        cardsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showCardDefinition(newValue));

        definitionTextArea = new TextArea();
        definitionTextArea.setEditable(false);

        randomModeCheckbox = new CheckBox("Random Mode");

        Button addSetButton = new Button("Add Set");
        addSetButton.setOnAction(e -> showAddSetDialog());

        Button removeSetButton = new Button("Remove Set");
        removeSetButton.setOnAction(e -> removeSelectedSet(setsListView.getSelectionModel().getSelectedItem()));

        Button addCardButton = new Button("Add Card");
        addCardButton.setOnAction(e -> showAddCardDialog());

        Button removeCardButton = new Button("Remove Card");
        removeCardButton.setOnAction(e -> removeSelectedCard(cardsListView.getSelectionModel().getSelectedItem()));

        startQuizButton = new Button("Start Quiz");
        startQuizButton.setOnAction(e -> startQuiz());

        BorderPane root = new BorderPane();
        root.setLeft(new VBox(10, setsListView, addSetButton, removeSetButton));
        root.setCenter(new VBox(10, cardsListView, new HBox(10, addCardButton, removeCardButton)));
        root.setRight(new VBox(10, new HBox(10, randomModeCheckbox, startQuizButton), definitionTextArea));
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 700, 400);
        primaryStage.setTitle("Flashcard App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showRandomCard() {
        FlashcardSet selectedSet = setsListView.getSelectionModel().getSelectedItem();
        if (selectedSet != null && !selectedSet.getCards().isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(selectedSet.getCards().size());
            cardsListView.getSelectionModel().select(randomIndex);
        } else {
            definitionTextArea.setText("No flashcards available in the selected set.");
        }
    }

    private void showFlashcards(FlashcardSet selectedSet) {
        if (selectedSet != null) {
            cardsListView.setItems(selectedSet.getCards());
            cardsListView.getSelectionModel().selectFirst();
            showCardDefinition(cardsListView.getSelectionModel().getSelectedItem());
            startQuizButton.setDisable(false);
        } else {
            cardsListView.getItems().clear();
            definitionTextArea.setText("");
            startQuizButton.setDisable(true);
        }
    }

    private void showCardDefinition(Flashcard selectedCard) {
        if (selectedCard != null) {
            definitionTextArea.setText(selectedCard.getDefinition());
        } else {
            definitionTextArea.setText("");
        }
    }

    private void showAddSetDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Flashcard Set");
        dialog.setHeaderText("Enter Set Name");
        dialog.showAndWait().ifPresent(setName -> {
            FlashcardSet newSet = new FlashcardSet(setName);
            flashcardSets.add(newSet);
            setsListView.getSelectionModel().select(newSet);
            saveFlashcardSetsToFile();
        });
    }

    private void removeSelectedSet(FlashcardSet selectedSet) {
        if (selectedSet != null) {
            flashcardSets.remove(selectedSet);
            setsListView.getSelectionModel().clearSelection();
            cardsListView.getItems().clear();
            definitionTextArea.setText("");
            startQuizButton.setDisable(true);
            saveFlashcardSetsToFile();
        }
    }

    private void showAddCardDialog() {
        FlashcardSet selectedSet = setsListView.getSelectionModel().getSelectedItem();
        if (selectedSet != null) {
            Dialog<Flashcard> dialog = new Dialog<>();
            dialog.setTitle("Add Flashcard");
            dialog.setHeaderText("Enter Word, Definition, Hint, Memo");

            TextField wordTextField = new TextField();
            TextArea definitionTextArea = new TextArea();
            TextField hintTextField = new TextField();
            TextField memoTextField = new TextField();

            dialog.getDialogPane().setContent(new VBox(10, new Label("Word:"), wordTextField, new Label("Definition:"), definitionTextArea,
                    new Label("Hint:"), hintTextField, new Label("Memo:"), memoTextField));

            ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == addButton) {
                    return new Flashcard(wordTextField.getText(), definitionTextArea.getText(), hintTextField.getText(), memoTextField.getText());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(card -> {
                if (card != null) {
                    selectedSet.addCard(card);
                    saveFlashcardSetsToFile();
                }
            });
        }
    }

    private void removeSelectedCard(Flashcard selectedCard) {
        FlashcardSet selectedSet = setsListView.getSelectionModel().getSelectedItem();
        if (selectedSet != null && selectedCard != null) {
            selectedSet.removeCard(selectedCard);
            saveFlashcardSetsToFile();
        }
    }

    private void startQuiz() {
        FlashcardSet selectedSet = setsListView.getSelectionModel().getSelectedItem();
        if (selectedSet != null && !selectedSet.getCards().isEmpty()) {
            ObservableList<Flashcard> quizCards;
            if (randomModeCheckbox.isSelected()) {
                quizCards = FXCollections.observableArrayList(selectedSet.getCards());
                FXCollections.shuffle(quizCards);
            } else {
                quizCards = selectedSet.getCards();
            }

            QuizDialog quizDialog = new QuizDialog(quizCards);
            quizDialog.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Quiz Information");
            alert.setHeaderText(null);
            alert.setContentText("Please add cards to the selected set to start the quiz.");
            alert.showAndWait();
        }
    }

    private ObservableList<FlashcardSet> readFlashcardSetsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SETS_FILE))) {
            return FXCollections.observableArrayList((FlashcardSet[]) ois.readObject());
        } catch (FileNotFoundException e) {
            // Ignore if the file doesn't exist yet
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    private void saveFlashcardSetsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SETS_FILE))) {
            oos.writeObject(flashcardSets.toArray(new FlashcardSet[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class FlashcardSet implements Serializable {

        private String setName;
        private ObservableList<Flashcard> cards;

        public FlashcardSet(String setName) {
            this.setName = setName;
            this.cards = FXCollections.observableArrayList();
        }

        public String getSetName() {
            return setName;
        }

        public ObservableList<Flashcard> getCards() {
            return cards;
        }

        public void addCard(Flashcard card) {
            cards.add(card);
        }

        public void removeCard(Flashcard card) {
            cards.remove(card);
        }

        @Override
        public String toString() {
            return setName;
        }
    }

    public static class Flashcard implements Serializable {

        private String word;
        private String definition;
        private String hint;
        private String memo;

        public Flashcard(String word, String definition, String hint, String memo) {
            this.word = word;
            this.definition = definition;
            this.hint = hint;
            this.memo = memo;
        }

        public String getWord() {
            return word;
        }

        public String getDefinition() {
            return definition;
        }

        public String getHint() {
            return hint;
        }

        public String getMemo() {
            return memo;
        }

        @Override
        public String toString() {
            return word;
        }
    }

    public static class QuizDialog extends Dialog<Void> {

        public QuizDialog(ObservableList<Flashcard> quizCards) {
            setTitle("Quiz Mode");
            setHeaderText(null);

            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            getDialogPane().getButtonTypes().add(closeButton);

            BorderPane quizPane = new BorderPane();
            Flashcard currentCard = quizCards.get(0);
            Label wordLabel = new Label(currentCard.getWord());
            TextArea definitionArea = new TextArea(currentCard.getDefinition());
            definitionArea.setEditable(false);

            Button nextButton = new Button("Next");
            nextButton.setOnAction(event -> {
                int currentIndex = quizCards.indexOf(currentCard);
                if (currentIndex < quizCards.size() - 1) {
                    currentCard = quizCards.get(currentIndex + 1);
                    wordLabel.setText(currentCard.getWord());
                    definitionArea.setText(currentCard.getDefinition());
                } else {
                    setResult(null);
                    close();
                }
            });

            quizPane.setTop(wordLabel);
            quizPane.setCenter(definitionArea);
            quizPane.setBottom(nextButton);
            quizPane.setPadding(new Insets(10));

            getDialogPane().setContent(quizPane);
        }
    }
}
