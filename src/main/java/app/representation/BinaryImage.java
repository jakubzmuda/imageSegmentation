package app.representation;

import app.util.ImageConverter;
import javafx.scene.image.Image;

/**
 * Reprezentacja binarnej mapy obrazu
 */
public class BinaryImage {

    private ImageMap imageMap;

    private BinaryImage(ImageMap imageMap) {
        this.imageMap = imageMap;
    }

    /**
     * Tworzy całą białą mapę obrazu
     * @param width szerokość nowej mapy
     * @param height wysokość nowej mapy
     * @return nowa mapa
     */
    public static BinaryImage white (long width, long height) {
        ImageMap rawMap = new ImageMap();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rawMap.put(i, j, new Canals(255, 255, 255));
            }
        }

        return new BinaryImage(rawMap);
    }

    /**
     * Tworzy całą czarną mapę obrazu
     * @param width szerokość nowej mapy
     * @param height wysokość nowej mapy
     * @return nowa mapa
     */
    public static BinaryImage black (long width, long height) {
        ImageMap rawMap = new ImageMap();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rawMap.put(i, j, new Canals(0, 0, 0));
            }
        }

        return new BinaryImage(rawMap);
    }

    /**
     * Tworzy obraz na podstawie binarnej reprezentacji mapy
     * @return obraz
     */
    public Image asImage() {
        return new ImageConverter().toImage(imageMap);
    }
}
