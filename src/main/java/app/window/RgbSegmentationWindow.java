package app.window;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RgbSegmentationWindow {

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Segmentacja w modelu RGB");

        GridPane container = new GridPane();
        Scene scene = new Scene(container, 600, 200);

        container.add(new Label("min"), 1, 0);
        container.add(new Label("max"), 2, 0);

        Label redLabel = new Label("czerwony");
        TextField redMinField = new TextField("0");
        TextField redMaxField = new TextField("255");
        container.add(redLabel, 0, 1);
        container.add(redMinField, 1, 1);
        container.add(redMaxField, 2, 1);

        Label greenLabel = new Label("zielony");
        TextField greenMinField = new TextField("0");
        TextField greenMaxField = new TextField("255");
        container.add(greenLabel, 0, 2);
        container.add(greenMinField, 1, 2);
        container.add(greenMaxField, 2, 2);

        Label blueLabel = new Label("niebieski");
        TextField blueMinField = new TextField("0");
        TextField blueMaxField = new TextField("255");
        container.add(blueLabel, 0, 3);
        container.add(blueMinField, 1, 3);
        container.add(blueMaxField, 2, 3);

        Button doIt = new Button("Kontynuuj");
        doIt.setOnAction((event) -> {
//            Image newImage = new ImageOperations().threshold(image, Integer.parseInt(redMinField.getText()), Integer.parseInt(minField.getText()), Integer.parseInt(maxField.getText()));
//            updateImage(newImage);
//            stage.close();
        });

        container.setAlignment(Pos.CENTER);
        container.setHgap(8);
        container.setVgap(8);
        container.add(doIt, 1, 4);
        stage.setScene(scene);
        stage.show();
    }
}
