package app.window;

import app.App;
import app.operation.RgbSegmentation;
import app.representation.BinaryImage;
import app.representation.Canals;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

    private Image markersImage;
    private ImageView markersImageView;

    private Image resultImage;
    private ImageView resultImageView;

    private TextField redMinField;
    private TextField redMaxField;

    private TextField greenMinField;
    private TextField greenMaxField;

    private TextField blueMinField;
    private TextField blueMaxField;

    private CheckBox shouldGroup;
    private Label numberOfGroups;

    public RgbSegmentationWindow(App app, Image inputImage) {
        this.app = app;
        this.inputImage = inputImage;
    }

    public void show() {
        stage = new Stage();
        stage.setTitle("Segmentacja w modelu RGB");

        GridPane configurationPanel = buildConfigurationPanel();
        HBox previewPanel = buildPreviewPanel();

        shouldGroup = new CheckBox("Segmentować?");
        shouldGroup.setMinHeight(30);

        numberOfGroups = new Label("Liczba wyodrębnionych segmentów: 0");
        numberOfGroups.setMinHeight(30);
        HBox actionsPanel = buildActionsPanel();

        VBox container = new VBox(configurationPanel, previewPanel, numberOfGroups, shouldGroup, actionsPanel);
        container.setAlignment(Pos.CENTER);

        Scene scene = new Scene(container, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    private HBox buildActionsPanel() {
        Button previewButton = new Button("Podgląd");
        previewButton.setOnAction((event) -> {
            RgbSegmentation rgbSegmentation = new RgbSegmentation(inputImage, thresholdFrom(), thresholdTo(), shouldGroup.isSelected());
            Image map = rgbSegmentation.map();
            Image result = rgbSegmentation.result();
            Image markers = rgbSegmentation.markers();
            updateMap(map);
            updateMarkersImage(markers);
            updateResultImage(result);
            this.numberOfGroups.setText("Liczba wyodrębnionych segmentów: " + rgbSegmentation.numberOfGroups());
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

    public void updateMarkersImage(Image newImage) {
        markersImage = newImage;
        markersImageView.setImage(markersImage);
    }

    private HBox buildPreviewPanel() {
        inputImageView = new ImageView(inputImage);
        VBox inputBox = new VBox(new Label("Obraz wejściowy"), inputImageView);
        inputBox.setSpacing(8);
        inputBox.setAlignment(Pos.CENTER);

        mapImage = BinaryImage.white(Math.round(inputImage.getWidth()), Math.round(inputImage.getHeight())).asImage();
        mapImageView = new ImageView(mapImage);
        VBox mapBox = new VBox(new Label("Mapa obrazu"), mapImageView);
        mapBox.setSpacing(8);
        mapBox.setAlignment(Pos.CENTER);

        markersImage = BinaryImage.white(Math.round(inputImage.getWidth()), Math.round(inputImage.getHeight())).asImage();
        markersImageView = new ImageView(markersImage);
        VBox markersBox = new VBox(new Label("Segmenty"), markersImageView);
        markersBox.setSpacing(8);
        markersBox.setAlignment(Pos.CENTER);

        resultImage = inputImage;
        resultImageView = new ImageView(resultImage);
        VBox resultBox = new VBox(new Label("Obraz wynikowy"), resultImageView);
        resultBox.setSpacing(8);
        resultBox.setAlignment(Pos.CENTER);

        HBox panel = new HBox(inputBox, mapBox, markersBox, resultBox);
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
