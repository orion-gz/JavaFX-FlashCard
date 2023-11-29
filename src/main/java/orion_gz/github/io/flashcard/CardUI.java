package orion_gz.github.io.flashcard;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.svg.SVGGlyph;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.control.Label;

import static javafx.animation.Interpolator.EASE_BOTH;

public class CardUI extends StackPane {
    public static StackPane getCardUI(){
        StackPane child = new StackPane();
        child.setPrefWidth(230);
        child.setPrefHeight(400);
        JFXDepthManager.setDepth(child, 1);

        // create content
        StackPane header = new StackPane();
        String headerColor = getDefaultColor((int)Math.ceil(Math.random() * 100 % 12));
        header.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: " + headerColor);

        JFXListView<Label> jlist = new JFXListView<>();
        ObservableList<Label> ol = FXCollections.observableArrayList();
        ol.add(new Label("Hello World!"));
        ol.add(new Label("Hello Java!"));
        ol.add(new Label("Hello JavaFX!"));

        jlist.setItems(ol);
        jlist.expandedProperty().set(true);
        jlist.setDepth(1);
        jlist.setStyle("-fx-start-margin: 5; -jfx-vertical-gap: 10; -fx-background-color:" + headerColor);

        header.getChildren().addAll(jlist);

        VBox.setVgrow(header, Priority.ALWAYS);
        StackPane body = new StackPane();
        body.setMinHeight(Math.random() * 20 + 30);
        VBox content = new VBox();
        content.getChildren().addAll(header, body);
        body.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: rgb(255,255,255,0.87);");

        // create button
        JFXButton button = new JFXButton("");
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.setStyle("-fx-background-radius: 40;-fx-background-color: " + getDefaultColor((int) ((Math.random() * 12) % 12)));
        button.setPrefSize(40, 40);
        button.setRipplerFill(Color.valueOf(headerColor));
        button.setScaleX(0);
        button.setScaleY(0);
        SVGGlyph glyph = new SVGGlyph(-1,
                "test",
                "M 11 2 L 11 11 L 2 11 L 2 13 L 11 13 L 11 22 L 13 22 L 13 13 L 22 13 L 22 11 L 13 11 L 13 2 Z",
                Color.WHITE);
        glyph.setSize(20, 20);
        button.setGraphic(glyph);
        button.translateYProperty().bind(Bindings.createDoubleBinding(() -> {
            return header.getBoundsInParent().getHeight() - button.getHeight() / 2;
        }, header.boundsInParentProperty(), button.heightProperty()));
        StackPane.setMargin(button, new Insets(0, 110, 0, 0));
        StackPane.setAlignment(button, Pos.TOP_RIGHT);

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(240),
                new KeyValue(button.scaleXProperty(),
                        1,
                        EASE_BOTH),
                new KeyValue(button.scaleYProperty(),
                        1,
                        EASE_BOTH)));
        animation.setDelay(Duration.millis(100 * 1 + 1000));
        animation.play();

        child.getChildren().addAll(content, button);
        return child;
    }

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
}