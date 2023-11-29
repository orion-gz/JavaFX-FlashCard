package orion_gz.github.io.flashcard;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.svg.SVGGlyph;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javafx.animation.Interpolator.EASE_BOTH;

public class QuizController {
    private Stage stage;
    private Data.FlashCardSet set;
    private ObservableList<Data.FlashCard> quizCards;
    private List<String> myAnswer;
    private int quizNum;
    private int idx;
    private boolean hintVisible;

    /* Label */
    @FXML
    Label title;

    @FXML
    Label word;

    @FXML
    Label hint;
    /* Label */

    @FXML
    JFXTextArea answerTextArea;

    /* Button */
    @FXML
    JFXButton showHintBtn;

    @FXML
    JFXButton closeDialogBtn;

    @FXML
    JFXButton nextBtn;

    @FXML
    JFXButton prevBtn;
    /* Button */

    public void displayQuizDialog(Data.FlashCardSet set) {
        this.set = set;
        hintVisible = false;
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuizDialog.fxml"));
            Parent dialog = loader.load();
            Scene scene = new Scene(dialog);

            final ObservableList<String> stylesheets = scene.getStylesheets();
            stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                    JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                    QuizController.class.getResource("components.css").toExternalForm());
            stage.setScene(scene);

            title = (Label) scene.lookup("#title");
            title.setText(set.getSetName());

            word = (Label) scene.lookup("#word");
            hint = (Label) scene.lookup("#hint");

            showHintBtn = (JFXButton) scene.lookup("#showHintBtn");
            showHintBtn.setOnAction((e) -> {
                if (hintVisible) {
                    hint.setVisible(false);
                    hintVisible = false;
                }
                else {
                    hint.setVisible(true);
                    hintVisible = true;
                }
            });

            closeDialogBtn = (JFXButton) scene.lookup("#closeDialogBtn");
            closeDialogBtn.setOnAction((e) -> stage.close());

            startQuiz();

            prevBtn = (JFXButton) scene.lookup("#prevBtn");
            prevBtn.setDisable(true);
            prevBtn.setOnAction((e) -> {
                if (idx > 0) {
                    showCard(quizCards.get(--idx));
                    hintVisible = false;
                    hint.setVisible(false);
                    prevBtn.setDisable(false);
                    if (idx < quizNum)
                        nextBtn.setText("Next");
                    if (idx == 0)
                        prevBtn.setDisable(true);
                }
                else
                    prevBtn.setDisable(true);
            });

            nextBtn = (JFXButton) scene.lookup("#nextBtn");
            nextBtn.setOnAction((e) -> {
                if (idx < quizNum - 1) {
                    showCard(quizCards.get(++idx));
                    hintVisible = false;
                    hint.setVisible(false);
                    prevBtn.setDisable(false);
                    nextBtn.setDisable(false);
                    answerTextArea.getText();
                    answerTextArea.clear();
                    if (idx == quizNum - 1)
                        nextBtn.setText("Complete");
                }
                else {
                    stage.close();
                }
            });

            stage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        initUI();
    }

    private void initUI() {
        showHintBtn.setStyle("-fx-background-radius: 40;-fx-background-color: #4CAF50;");
        showHintBtn.setPrefSize(40, 40);

        showHintBtn.setRipplerFill(Color.valueOf("#43A047"));
        showHintBtn.setScaleX(0);
        showHintBtn.setScaleY(0);

        SVGGlyph bulb_glyph = new SVGGlyph(-1,
                "Hint",
                "M297.2 248.9C311.6 228.3 320 203.2 320 176c0-70.7-57.3-128-128-128S64 105.3 64 176c0 27.2 8.4 52.3 22.8 72.9c3.7 5.3 8.1 11.3 12.8 17.7l0 0c12.9 17.7 28.3 38.9 39.8 59.8c10.4 19 15.7 38.8 18.3 57.5H109c-2.2-12-5.9-23.7-11.8-34.5c-9.9-18-22.2-34.9-34.5-51.8l0 0 0 0c-5.2-7.1-10.4-14.2-15.4-21.4C27.6 247.9 16 213.3 16 176C16 78.8 94.8 0 192 0s176 78.8 176 176c0 37.3-11.6 71.9-31.4 100.3c-5 7.2-10.2 14.3-15.4 21.4l0 0 0 0c-12.3 16.8-24.6 33.7-34.5 51.8c-5.9 10.8-9.6 22.5-11.8 34.5H226.4c2.6-18.7 7.9-38.6 18.3-57.5c11.5-20.9 26.9-42.1 39.8-59.8l0 0 0 0 0 0c4.7-6.4 9-12.4 12.7-17.7zM192 128c-26.5 0-48 21.5-48 48c0 8.8-7.2 16-16 16s-16-7.2-16-16c0-44.2 35.8-80 80-80c8.8 0 16 7.2 16 16s-7.2 16-16 16zm0 384c-44.2 0-80-35.8-80-80V416H272v16c0 44.2-35.8 80-80 80z",
                Color.WHITE);
        bulb_glyph.setSize(20, 25);
        showHintBtn.setGraphic(bulb_glyph);

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(240),
                new KeyValue(showHintBtn.scaleXProperty(),
                        1,
                        EASE_BOTH),
                new KeyValue(showHintBtn.scaleYProperty(),
                        1,
                        EASE_BOTH)));
        animation.setDelay(Duration.millis(100 * 1 + 1000));
        animation.play();
    }

    private void startQuiz() {
        quizCards = FXCollections.observableArrayList(set.getCards());
        FXCollections.shuffle(quizCards);
        quizNum = quizCards.size();
        myAnswer = new ArrayList<>();
        idx = 0;
        showCard(quizCards.get(idx));
    }

    private void showCard(Data.FlashCard card) {
        word.setText(card.getWord());
        hint.setText(card.getHint());
    }
}
