package app.window;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RgbSegmentationWindow {

    private int imageWidth = 200;
    private int imageHeight = 200;

    private Image inputImage;
    private ImageView inputImageView;

    public RgbSegmentationWindow(Image inputImage) {
        this.inputImage = inputImage;

    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Segmentacja w modelu RGB");

        GridPane configurationPanel = buildConfigurationPanel();
        HBox previewPanel = buildPreviewPanel();

        VBox container = new VBox(configurationPanel, previewPanel);
        container.setAlignment(Pos.CENTER);

        Scene scene = new Scene(container, 800, 400);
        stage.setScene(scene);
        stage.show();
    }

    private HBox buildPreviewPanel() {
        inputImageView = new ImageView(inputImage);

        HBox panel = new HBox(inputImageView);
        panel.setPadding(new Insets(16, 0, 16, 0));
        panel.setAlignment(Pos.CENTER);
        panel.setSpacing(16);
        return panel;
    }


    private GridPane buildConfigurationPanel() {
        GridPane container = new GridPane();

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

        Button doIt = new Button("Podgląd");
        doIt.setOnAction((event) -> {
//            Image newImage = new ImageOperations().threshold(image, Integer.parseInt(redMinField.getText()), Integer.parseInt(minField.getText()), Integer.parseInt(maxField.getText()));
//            updateImage(newImage);
//            stage.close();
        });

        container.setAlignment(Pos.CENTER);
        container.setHgap(8);
        container.setVgap(8);
        container.add(doIt, 1, 4);

        return container;
    }
}
