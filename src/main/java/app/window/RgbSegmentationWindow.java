package app.window;

import app.App;
import app.operation.RgbSegmentation;
import app.representation.BinaryImage;
import app.representation.Canals;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RgbSegmentationWindow {

    private App app;

    private Stage stage;

    private Image inputImage;
    private ImageView inputImageView;

    private Image mapImage;
    private ImageView mapImageView;

    private Image resultImage;
    private ImageView resultImageView;

    private TextField redMinField;
    private TextField redMaxField;

    private TextField greenMinField;
    private TextField greenMaxField;

    private TextField blueMinField;
    private TextField blueMaxField;


    public RgbSegmentationWindow(App app, Image inputImage) {
        this.app = app;
        this.inputImage = inputImage;
    }

    public void show() {
        stage = new Stage();
        stage.setTitle("Segmentacja w modelu RGB");

        GridPane configurationPanel = buildConfigurationPanel();
        HBox previewPanel = buildPreviewPanel();
        HBox actionsPanel = buildActionsPanel();

        VBox container = new VBox(configurationPanel, previewPanel, actionsPanel);
        container.setAlignment(Pos.CENTER);

        Scene scene = new Scene(container, 800, 400);
        stage.setScene(scene);
        stage.show();
    }

    private HBox buildActionsPanel() {
        Button previewButton = new Button("Podgląd");
        previewButton.setOnAction((event) -> {
            RgbSegmentation rgbSegmentation = new RgbSegmentation(inputImage, thresholdFrom(), thresholdTo());
            Image map = rgbSegmentation.map();
            Image result = rgbSegmentation.result();
            updateMap(map);
            updateResultImage(result);
        });

        Button cancelButton = new Button("Anuluj");
        cancelButton.setOnAction((event) -> {
            stage.close();
        });

        Button applyButton = new Button("Potwierdź");
        applyButton.setOnAction((event) -> {
           app.updateImage(resultImage);
           stage.close();
        });

        HBox container = new HBox(previewButton, cancelButton, applyButton);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(16);
        return container;
    }

    public void updateMap(Image newImage) {
        mapImage = newImage;
        mapImageView.setImage(mapImage);
    }

    public void updateResultImage(Image newImage) {
        resultImage = newImage;
        resultImageView.setImage(resultImage);
    }

    private HBox buildPreviewPanel() {
        inputImageView = new ImageView(inputImage);

        mapImage = BinaryImage.white(Math.round(inputImage.getWidth()), Math.round(inputImage.getHeight())).asImage();
        mapImageView = new ImageView(mapImage);

        resultImage = inputImage;
        resultImageView = new ImageView(resultImage);

        HBox panel = new HBox(inputImageView, mapImageView, resultImageView);
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
        redMinField = new TextField("0");
        redMaxField = new TextField("255");
        container.add(redLabel, 0, 1);
        container.add(redMinField, 1, 1);
        container.add(redMaxField, 2, 1);

        Label greenLabel = new Label("zielony");
        greenMinField = new TextField("0");
        greenMaxField = new TextField("255");
        container.add(greenLabel, 0, 2);
        container.add(greenMinField, 1, 2);
        container.add(greenMaxField, 2, 2);

        Label blueLabel = new Label("niebieski");
        blueMinField = new TextField("0");
        blueMaxField = new TextField("255");
        container.add(blueLabel, 0, 3);
        container.add(blueMinField, 1, 3);
        container.add(blueMaxField, 2, 3);

        container.setAlignment(Pos.CENTER);
        container.setHgap(8);
        container.setVgap(8);

        return container;
    }

    private Canals thresholdFrom() {
        return new Canals(Integer.parseInt(redMinField.getText()), Integer.parseInt(greenMinField.getText()), Integer.parseInt(blueMinField.getText()));
    }

    private Canals thresholdTo() {
        return new Canals(Integer.parseInt(redMaxField.getText()), Integer.parseInt(greenMaxField.getText()), Integer.parseInt(blueMaxField.getText()));
    }
}
