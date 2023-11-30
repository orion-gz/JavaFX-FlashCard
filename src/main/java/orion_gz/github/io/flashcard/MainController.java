package orion_gz.github.io.flashcard;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.svg.SVGGlyph;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.animation.Interpolator.EASE_BOTH;

public class MainController {
    private Stage stage;

    /* data member */
    private Data.FlashCardSet selectedSet;
    private Data.FlashCard selectedCard;
    private ObservableList<Data.FlashCardSet> flashCardSets;
    /* data member */

    /* Layout */
    @FXML
    VBox listCard1;

    @FXML
    VBox listCard2;

    @FXML
    StackPane header1;

    @FXML
    StackPane header2;

    @FXML
    StackPane body1;

    @FXML
    StackPane body2;

    @FXML
    StackPane definitionArea;
    /* Layout */

    /* ListView */
    @FXML
    private JFXListView<Data.FlashCardSet> setsListView;

    @FXML
    private JFXListView<Data.FlashCard> cardsListView;
    /* ListView */

    /* Button */
    @FXML
    JFXButton quizBtn;

    @FXML
    JFXButton memorizeBtn;

    @FXML
    JFXButton closeWindowBtn;

    @FXML
    JFXButton addSetBtn;

    @FXML
    JFXButton removeSetBtn;

    @FXML
    JFXButton addItemBtn;

    @FXML
    JFXButton removeItemBtn;

    @FXML
    JFXButton saveBtn;
    /* Button */

    /* TextArea */
    @FXML
    JFXTextArea wordTextArea;

    @FXML
    JFXTextArea definitionTextArea;

    @FXML
    JFXTextArea hintTextArea;

    @FXML
    JFXTextArea memoTextArea;
    /* TextArea */

    // get stage from main class
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // initialize function
    @FXML
    public void initialize() {
        initWindow();
        initCardUI();
        initCardList();
    }

    // get data and set item in list
    private void initCardList() {
        // data from ObjectInputStream - FileInputStream
        flashCardSets = Data.readFlashCards();
        setsListView.setItems(flashCardSets);

        // list select event
        setsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedSet = newValue;
            showFlashCards(newValue);
        });

        // list select event
        cardsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedCard = newValue;
            showCardDefinition(newValue);
        });

        // add/remove set event
        addSetBtn.setOnAction((e) -> new AddSetController().displayAddDialog(flashCardSets, setsListView));
        removeSetBtn.setOnAction((e) -> new AddSetController().removeSet(setsListView.getSelectionModel().getSelectedItem(), flashCardSets, setsListView, cardsListView));
    }

    // initialize toolbar (quiz btn, memorize btn, close btn)
    private void initWindow() {
        // Quiz Mode Button
        quizBtn.setOnAction((e) -> new QuizController().displayQuizDialog(selectedSet));
        // Memorize Mode Button
        memorizeBtn.setOnAction((e) -> new MemorizeController().displayMemorizeDialog(selectedSet));

        // Window Close Button
        SVGGlyph times_glyph = new SVGGlyph(-1,
                "times",
                "M242.72 256l100.07-100.07c12.28-12.28 12.28-32.19 0-44.48l-22.24-22.24c-12.28-12.28-32.19-12.28-44.48 0L176 189.28 75.93 89.21c-12.28-12.28-32.19-12.28-44.48 0L9.21 111.45c-12.28 12.28-12.28 32.19 0 44.48L109.28 256 9.21 356.07c-12.28 12.28-12.28 32.19 0 44.48l22.24 22.24c12.28 12.28 32.2 12.28 44.48 0L176 322.72l100.07 100.07c12.28 12.28 32.2 12.28 44.48 0l22.24-22.24c12.28-12.28 12.28-32.19 0-44.48L242.72 256z",
                Color.WHITE);
        times_glyph.setSize(15, 15);
        closeWindowBtn.setGraphic(times_glyph);
        closeWindowBtn.setOnAction((e) -> stage.close());
    }

    // initialize setsListView, cardsListView to card ui
    private void initCardUI() {
        JFXDepthManager.setDepth(definitionArea, 2);
        ObservableList<VBox> listCard = FXCollections.observableArrayList(listCard1, listCard2);
        ObservableList<JFXListView> list = FXCollections.observableArrayList(setsListView, cardsListView);
        ObservableList<StackPane> header = FXCollections.observableArrayList(header1, header2);
        ObservableList<StackPane> body = FXCollections.observableArrayList(body1, body2);
        ObservableList<JFXButton> addBtn = FXCollections.observableArrayList(addSetBtn, addItemBtn);
        ObservableList<JFXButton> rmBtn = FXCollections.observableArrayList(removeSetBtn, removeItemBtn);

        for (int i = 0; i < 2; ++i) {
            String headerColor = getDefaultColor((int)Math.ceil(Math.random() * 100 % 12));
            JFXDepthManager.setDepth(listCard.get(i), 2);
            list.get(i).setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: " + headerColor);

            VBox.setVgrow(header.get(i), Priority.ALWAYS);
            body.get(i).setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: rgb(255,255,255,0.87);");

            addBtn.get(i).setStyle("-fx-background-radius: 40;-fx-background-color:" + getDefaultColor((int) ((Math.random() * 12) % 12)));
            addBtn.get(i).setPrefSize(40, 40);

            addBtn.get(i).setRipplerFill(Color.valueOf(headerColor));
            addBtn.get(i).setScaleX(0);
            addBtn.get(i).setScaleY(0);

            rmBtn.get(i).setStyle("-fx-background-radius: 40;-fx-background-color:" + getDefaultColor((int) ((Math.random() * 12) % 12)));
            rmBtn.get(i).setPrefSize(40, 40);

            rmBtn.get(i).setRipplerFill(Color.valueOf(headerColor));
            rmBtn.get(i).setScaleX(0);
            rmBtn.get(i).setScaleY(0);

            SVGGlyph plus_glyph = new SVGGlyph(-1,
                    "plus",
                    "M256 80c0-17.7-14.3-32-32-32s-32 14.3-32 32V224H48c-17.7 0-32 14.3-32 32s14.3 32 32 32H192V432c0 17.7 14.3 32 32 32s32-14.3 32-32V288H400c17.7 0 32-14.3 32-32s-14.3-32-32-32H256V80z",
                    Color.WHITE);
            plus_glyph.setSize(20, 20);
            addBtn.get(i).setGraphic(plus_glyph);

            SVGGlyph minus_glyph = new SVGGlyph(-1,
                    "minus",
                    "M432 256c0 17.7-14.3 32-32 32L48 288c-17.7 0-32-14.3-32-32s14.3-32 32-32l352 0c17.7 0 32 14.3 32 32z",
                    Color.WHITE);
            minus_glyph.setSize(20, 4);
            rmBtn.get(i).setGraphic(minus_glyph);

            Timeline animation1 = new Timeline(new KeyFrame(Duration.millis(240),
                    new KeyValue(addBtn.get(i).scaleXProperty(),
                            1,
                            EASE_BOTH),
                    new KeyValue(addBtn.get(i).scaleYProperty(),
                            1,
                            EASE_BOTH)));
            animation1.setDelay(Duration.millis(100 * i + 1000));
            animation1.play();

            Timeline animation2 = new Timeline(new KeyFrame(Duration.millis(240),
                    new KeyValue(rmBtn.get(i).scaleXProperty(),
                            1,
                            EASE_BOTH),
                    new KeyValue(rmBtn.get(i).scaleYProperty(),
                            1,
                            EASE_BOTH)));
            animation2.setDelay(Duration.millis(200 * i + 1000));
            animation2.play();
        }
    }

    // get color
    private static String getDefaultColor(int i) {
        String color = "#FFFFFF";
        switch (i) {
            case 0:
                color = "#8F3F7E";
                break;
            case 1:
                color = "#B5305F";
                break;
            case 2:
                color = "#CE584A";
                break;
            case 3:
                color = "#DB8D5C";
                break;
            case 4:
                color = "#DA854E";
                break;
            case 5:
                color = "#E9AB44";
                break;
            case 6:
                color = "#FEE435";
                break;
            case 7:
                color = "#99C286";
                break;
            case 8:
                color = "#01A05E";
                break;
            case 9:
                color = "#4A8895";
                break;
            case 10:
                color = "#16669B";
                break;
            case 11:
                color = "#2F65A5";
                break;
            case 12:
                color = "#4E6A9C";
                break;
            default:
                break;
        }
        return color;
    }

    // show flash card in selected set
    private void showFlashCards(Data.FlashCardSet selectedSet) {
        if (!selectedSet.isEmpty()) {
            initView();
            updateCardsListView(selectedSet);
            cardsListView.getSelectionModel().selectFirst();
            showCardDefinition(cardsListView.getSelectionModel().getSelectedItem());
        } else {
            setQuizStatus(false);
            setMemorizeStatus(false);
            definitionArea.setVisible(false);
            cardsListView.setItems(null);
        }

        addItemBtn.setOnAction((e1) -> {
            initView();
            definitionArea.setVisible(true);
            saveBtn.setOnAction((e2) -> {
                saveNewCardData(selectedSet);
                clearTextArea();
                updateCardsListView(selectedSet);
            });
        });
    }

    // set in setsListView is Empty, than disable quiz btn
    private void setQuizStatus(boolean status) {
        if (status)
            quizBtn.setDisable(false);
        else
            quizBtn.setDisable(true);
    }
    // set in setsListView is Empty, than disable memorize btn
    private void setMemorizeStatus(boolean status) {
        if (status)
            memorizeBtn.setDisable(false);
        else
            memorizeBtn.setDisable(true);
    }

    // update listview
    private void updateCardsListView(Data.FlashCardSet selectedSet) {
        cardsListView.setItems(selectedSet.getCards());
    }

    // initalize view visible
    private void initView() {
        //cardsListArea.setVisible(true);
        setQuizStatus(true);
        setMemorizeStatus(true);
        definitionArea.setVisible(true);
        clearTextArea();
    }

    // clear textarea
    private void clearTextArea() {
        wordTextArea.clear();
        definitionTextArea.clear();
        hintTextArea.clear();
        memoTextArea.clear();
    }

    // save new card
    private void saveNewCardData(Data.FlashCardSet selectedSet) {
        Data.FlashCard newCard = new Data.FlashCard(wordTextArea.getText(), definitionTextArea.getText(), hintTextArea.getText(), memoTextArea.getText());
        selectedSet.addCard(newCard);
        Data.saveFlashCards(flashCardSets);
    }

    // edit card
    private void editCardData(Data.FlashCard selectedCard) {
        selectedCard.setWord(wordTextArea.getText());
        selectedCard.setDefinition(definitionTextArea.getText());
        selectedCard.setHint(hintTextArea.getText());
        selectedCard.setMemo(memoTextArea.getText());
        Data.saveFlashCards(flashCardSets);
    }

    // remove card
    private void removeSelectedCard(Data.FlashCard selectedCard) {
        Data.FlashCardSet selectedSet = setsListView.getSelectionModel().getSelectedItem();
        if (selectedSet != null && selectedCard != null) {
            selectedSet.removeCard(selectedCard);
            Data.saveFlashCards(flashCardSets);
        }
    }

    // show card definition(word, definition, hint, memo)
    private void showCardDefinition(Data.FlashCard selectedCard) {
        if (selectedCard != null) {
            wordTextArea.setText(selectedCard.getWord());
            definitionTextArea.setText(selectedCard.getDefinition());
            hintTextArea.setText(selectedCard.getHint());
            memoTextArea.setText(selectedCard.getMemo());

            saveBtn.setOnAction((e) -> {
                editCardData(selectedCard);
            });
        } else {
            definitionTextArea.setText("");
        }
    }
}
