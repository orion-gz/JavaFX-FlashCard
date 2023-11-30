package orion_gz.github.io.flashcard;

import com.jfoenix.assets.JFoenixResources;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;

public class Main extends Application {
    // for draggable window
    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // undecorated Window
        primaryStage.initStyle(StageStyle.UNDECORATED);

        try {
            // get fxml file to load ui
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("FlashCard");

            // set scene
            Scene scene = new Scene(root);

            // set draggable event
            scene.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            scene.setOnMouseDragged((MouseEvent event) -> {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });

            // set scene stylesheet (JFoenix Library)
            final ObservableList<String> stylesheets = scene.getStylesheets();
            stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                    JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                    MainController.class.getResource("components.css").toExternalForm());

            // deliver stage to controller
            MainController controller = loader.getController();
            controller.setStage(primaryStage);

            // show stage
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}
