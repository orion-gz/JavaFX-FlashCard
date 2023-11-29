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

import java.util.Collection;

import static javafx.animation.Interpolator.EASE_BOTH;

public class MemorizeController {
    private Stage stage;
    private Data.FlashCardSet set;
    private ObservableList<Data.FlashCard> memorizeCards;
    private int memorizeNum;
    private int idx;
    private boolean answerVisible;

    /* Label */
    @FXML
    Label title;

    @FXML
    Label word;

    @FXML
    Label hint;

    @FXML
    Label answer;
    /* Label */

    /* Button */
    @FXML
    JFXButton showAnswerBtn;

    @FXML
    JFXButton closeDialogBtn;

    @FXML
    JFXButton nextBtn;

    @FXML
    JFXButton prevBtn;
    /* Button */

    public void displayMemorizeDialog(Data.FlashCardSet set) {
        this.set = set;
        answerVisible = false;
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MemorizeDialog.fxml"));
            Parent dialog = loader.load();
            Scene scene = new Scene(dialog);

            final ObservableList<String> stylesheets = scene.getStylesheets();
            stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                    JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                    MemorizeController.class.getResource("components.css").toExternalForm());
            stage.setScene(scene);

            title = (Label) scene.lookup("#title");
            title.setText(set.getSetName());

            word = (Label) scene.lookup("#word");
            hint = (Label) scene.lookup("#hint");
            answer = (Label) scene.lookup(("#answer"));

            showAnswerBtn = (JFXButton) scene.lookup("#showAnswerBtn");
            showAnswerBtn.setOnAction((e) -> {
                if (answerVisible) {
                    answer.setVisible(false);
                    answerVisible = false;
                }
                else {
                    answer.setVisible(true);
                    answerVisible = true;
                }
            });

            closeDialogBtn = (JFXButton) scene.lookup("#closeDialogBtn");
            closeDialogBtn.setOnAction((e) -> stage.close());

            startMemorize();

            prevBtn = (JFXButton) scene.lookup("#prevBtn");
            prevBtn.setDisable(true);
            prevBtn.setOnAction((e) -> {
                if (idx > 0) {
                    showCard(memorizeCards.get(--idx));
                    answerVisible = false;
                    answer.setVisible(false);
                    prevBtn.setDisable(false);
                    if (idx < memorizeNum)
                        nextBtn.setText("Next");
                    if (idx == 0)
                        prevBtn.setDisable(true);
                }
                else
                    prevBtn.setDisable(true);
            });

            nextBtn = (JFXButton) scene.lookup("#nextBtn");
            nextBtn.setOnAction((e) -> {
                if (idx < memorizeNum - 1) {
                    showCard(memorizeCards.get(++idx));
                    answerVisible = false;
                    answer.setVisible(false);
                    prevBtn.setDisable(false);
                    nextBtn.setDisable(false);
                    if (idx == memorizeNum - 1)
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
        showAnswerBtn.setStyle("-fx-background-radius: 40;-fx-background-color: #4CAF50;");
        showAnswerBtn.setPrefSize(40, 40);

        showAnswerBtn.setRipplerFill(Color.valueOf("#43A047"));
        showAnswerBtn.setScaleX(0);
        showAnswerBtn.setScaleY(0);

        SVGGlyph eye_glyph = new SVGGlyph(-1,
                "Hint",
                "M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z",
                Color.WHITE);
        eye_glyph.setSize(20, 15);
        showAnswerBtn.setGraphic(eye_glyph);

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(240),
                new KeyValue(showAnswerBtn.scaleXProperty(),
                        1,
                        EASE_BOTH),
                new KeyValue(showAnswerBtn.scaleYProperty(),
                        1,
                        EASE_BOTH)));
        animation.setDelay(Duration.millis(100 * 1 + 1000));
        animation.play();
    }

    private void startMemorize() {
        memorizeCards = FXCollections.observableArrayList(set.getCards());
        FXCollections.shuffle(memorizeCards);
        memorizeNum = memorizeCards.size();
        idx = 0;
        showCard(memorizeCards.get(idx));
    }

    private void showCard(Data.FlashCard card) {
        word.setText(card.getWord());
        hint.setText(card.getHint());
        answer.setText(card.getDefinition());
    }
}
