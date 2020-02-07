package app.operation;

import app.representation.Canals;
import app.representation.ImageMap;
import app.util.ImageConverter;
import javafx.scene.image.Image;

public class RgbSegmentation {
    private Image inputImage;
    private ImageMap map;

    public RgbSegmentation(Image image, Canals from, Canals to) {
        this.inputImage = image;

        ImageMap imageMap = new ImageConverter().toImageMap(image);
        imageMap.singlePointOperation((x, y, canals) -> {
            if (fitsInRange(canals, from, to)) {
                return new Canals(255, 255, 255);
            }
            return new Canals(0, 0, 0);
        });

        this.map = imageMap;
    }

    private boolean fitsInRange(Canals canals, Canals from, Canals to) {
        return canals.red() >= from.red() && canals.red() <= to.red() &&
                canals.blue() >= from.blue() && canals.blue() <= to.blue() &&
                canals.green() >= from.green() && canals.green() <= to.green();
    }

    public Image map() {
        return new ImageConverter().toImage(map);
    }
}
