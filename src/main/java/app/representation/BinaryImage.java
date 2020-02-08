package app.representation;

import app.util.ImageConverter;
import javafx.scene.image.Image;

public class BinaryImage {

    private ImageMap imageMap;

    private BinaryImage(ImageMap imageMap) {
        this.imageMap = imageMap;
    }

    public static BinaryImage white (long width, long height) {
        ImageMap rawMap = new ImageMap();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rawMap.put(i, j, new Canals(255, 255, 255));
            }
        }

        return new BinaryImage(rawMap);
    }

    public static BinaryImage black (long width, long height) {
        ImageMap rawMap = new ImageMap();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rawMap.put(i, j, new Canals(0, 0, 0));
            }
        }

        return new BinaryImage(rawMap);
    }

    public Image asImage() {
        return new ImageConverter().toImage(imageMap);
    }
}
