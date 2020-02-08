package app.operation;

import app.representation.BinaryImage;
import app.representation.Canals;
import app.representation.ImageMap;
import app.util.ImageConverter;
import io.vavr.Tuple2;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RgbSegmentation {
    private Image inputImage;
    private ImageMap map;
    private ImageMap markers;

    public RgbSegmentation(Image image, Canals from, Canals to) {
        this.inputImage = image;

        ImageMap binaryMap = new ImageConverter().toImageMap(image);
        binaryMap.singlePointOperation((x, y, canals) -> {
            if (fitsInRange(canals, from, to)) {
                return new Canals(255, 255, 255);
            }
            return new Canals(0, 0, 0);
        });

        this.map = binaryMap;

        this.prepareMarkers(binaryMap);
    }

    private void prepareMarkers(ImageMap binaryMap) {
        List<Tuple2<Integer, Integer>> availablePoints = availableMarkerPoints(binaryMap);

        List<List<Tuple2<Integer, Integer>>> groups = initialMarkerGroups(availablePoints);

        outerLoop:
        while (true) {
            for (List<Tuple2<Integer, Integer>> group : groups) {
                for (Tuple2<Integer, Integer> point : group) {
                    for (Tuple2<Integer, Integer> potentialNewNeighbour : availablePointNeighbourhood(point, binaryMap)) {
                        // to nie czy w jakiejkolwiek innej liscie jest ten punkt, jesli tak to zmerdżuj listy
                        // czy nie jest juz w tej liscie
                        boolean alreadyInThisGroup = group.stream().noneMatch(pt -> pt.equals(potentialNewNeighbour));
                        if (!alreadyInThisGroup) {
                            // znalezc grupe
                            // zmergowac grupe
                            // usunac grupe
                            List<Tuple2<Integer, Integer>> previousGroup = groups.stream().filter(grp -> grp.contains(potentialNewNeighbour)).findFirst().get();
                            group.addAll(previousGroup);
                            groups.remove(previousGroup);
                            continue outerLoop;
                        }
                    }
                }
            }
            break;
        }

        System.out.println("Number of groups = " + groups.size());

        ImageMap markersMap = new ImageConverter().toImageMap(BinaryImage.black(binaryMap.width(), binaryMap.height()).asImage());

        for (List<Tuple2<Integer, Integer>> group : groups) {
            for (Tuple2<Integer, Integer> point : group) {
                int red = new Random().nextInt(255);
                int green = new Random().nextInt(255);
                int blue = new Random().nextInt(255);
                Canals canals = new Canals(red, green, blue);
                markersMap.put(point._1, point._2, canals);
            }
        }

        this.markers = markersMap;
    }

    private boolean fitsInRange(Canals canals, Canals from, Canals to) {
        return canals.red() >= from.red() && canals.red() <= to.red() &&
                canals.blue() >= from.blue() && canals.blue() <= to.blue() &&
                canals.green() >= from.green() && canals.green() <= to.green();
    }

    public Image map() {
        return new ImageConverter().toImage(map);
    }

    public Image markers() {
        return new ImageConverter().toImage(markers);
    }

    public Image result() {
        ImageConverter converter = new ImageConverter();
        ImageMap map = converter.toImageMap(inputImage);

        map.singlePointOperation((x, y, canals) -> {
            if (this.map.get(x, y).red() == 255) {
                return canals;
            }
            return new Canals(0, 0, 0);
        });

        return converter.toImage(map);
    }

    private List<List<Tuple2<Integer, Integer>>> initialMarkerGroups(List<Tuple2<Integer, Integer>> availablePoints) {
        List<List<Tuple2<Integer, Integer>>> groups = new ArrayList<>();
        for (Tuple2<Integer, Integer> point : availablePoints) {
            List<Tuple2<Integer, Integer>> list = new ArrayList<>();
            list.add(point);
            groups.add(list);
        }

        return groups;
    }

    private List<Tuple2<Integer, Integer>> availableMarkerPoints(ImageMap binaryImage) {
        List<Tuple2<Integer, Integer>> availablePoints = new ArrayList<>();

        binaryImage.singlePointOperation((x, y, canals) -> {
            if (canals.red() == 255) {
                availablePoints.add(new Tuple2<>(x, y));
            }
            return canals;
        });

        return availablePoints;
    }

    private List<Tuple2<Integer, Integer>> availablePointNeighbourhood(Tuple2<Integer, Integer> point, ImageMap binaryMap) {
        List<Tuple2<Integer, Integer>> list = new ArrayList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (binaryMap.getCanalValueOrBlack(point._1 - i, point._2 - j).red() == 255) {
                    list.add(new Tuple2<>(point._1 - i, point._2 - j));
                }
            }
        }

        return list;
    }
}
