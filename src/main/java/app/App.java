package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class App extends Application {

    private static int appWidth = 1200;
    private static int appHeight = 900;
    private static long imageContainerWidth = Math.round(appWidth);

    private Image image;
    private ImageView imageView;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        VBox vBox = buildMainBox();
        primaryStage.setTitle("Image segmentation");
        primaryStage.setScene(new Scene(vBox, appWidth, appHeight));
        primaryStage.setResizable(false);
        primaryStage.show();
        this.primaryStage = primaryStage;
    }

    private VBox buildMainBox() {
        ImageView imageView = new ImageView();
        this.imageView = imageView;
        StackPane stackImageView = new StackPane(imageView);
        MenuBar menuBar = buildMenuBox(imageView);
        VBox mainBox = new VBox(menuBar);
        ScrollPane imageContainer = buildImageContainer(stackImageView);

        imageContainer.setFitToHeight(true);
        imageContainer.setFitToWidth(true);

        GridPane gridPane = new GridPane();
        gridPane.add(imageContainer, 0, 0);

        gridPane.setAlignment(Pos.TOP_CENTER);

        mainBox.getChildren().add(gridPane);
        return mainBox;
    }

    private ScrollPane buildImageContainer(Pane imageView) {
        ScrollPane imageContainer = new ScrollPane(imageView);
        imageContainer.setPrefWidth(imageContainerWidth);
        imageContainer.setPrefHeight(appHeight);
        return imageContainer;
    }

    private MenuBar buildMenuBox(ImageView imageView) {
        Menu fileMenu = buildFileMenuTab(imageView);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu);
        return menuBar;
    }

    public void updateImage(Image newImage) {
        image = newImage;
        imageView.setImage(newImage);
    }

    private Menu buildFileMenuTab(ImageView imageView) {
        MenuItem openImageItem = buildOpenImageMenuItem(imageView);
        MenuItem closeItem = buildCloseAppMenuItem();
        Menu menu = new Menu("Plik");
        menu.getItems().addAll(openImageItem, closeItem);
        return menu;
    }

    private MenuItem buildCloseAppMenuItem() {
        MenuItem closeItem = new MenuItem("Zamknij");
        closeItem.setOnAction(e -> {
            Platform.exit();
        });
        return closeItem;
    }

    private MenuItem buildOpenImageMenuItem(ImageView imageView) {
        MenuItem openImageItem = new MenuItem("Otwórz");

        openImageItem.setOnAction(t -> {
            FileChooser fileChooser = new FileChooser(); //nocommit
            File file = fileChooser.showOpenDialog(null);
//            ClassLoader classLoader = getClass().getClassLoader(); // fast load
//            File file = new File(classLoader.getResource("blackAndWhite.bmp").getFile());

            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                updateImage(image);
            } catch (IOException ignored) {
            }
        });
        return openImageItem;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
