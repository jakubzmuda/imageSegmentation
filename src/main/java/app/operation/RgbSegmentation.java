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

/**
 * Implementacja progowania i segmentacji w modelu RGB
 */
public class RgbSegmentation {

    /**
     * Minimalna akceptowalna wielkość grupy (segmentu)
     */
    private final int minimalGroupSize = 10;

    private Image inputImage;
    private ImageMap map;
    private ImageMap markers;
    private long numberOfGroups = 0;

    /**
     * Tworzy instancję klasy RgbSegmentation. Jest to klasa bezstanowa i od razu dokonuje obliczeń.
     *
     * @param image       wejściowy obraz
     * @param from        dolny zakres kolorów
     * @param to          górny zakres kolorów
     * @param shouldGroup przełącznik mówiący o tym czy należy dokonać segmentacji
     */
    public RgbSegmentation(Image image, Canals from, Canals to, boolean shouldGroup) {
        this.inputImage = image;

        ImageMap binaryMap = new ImageConverter().toImageMap(image);
        binaryMap.singlePointOperation((x, y, canals) -> {
            if (fitsInRange(canals, from, to)) {
                return new Canals(255, 255, 255);
            }
            return new Canals(0, 0, 0);
        });

        this.map = binaryMap;

        if (shouldGroup) {
            this.prepareMarkers(binaryMap);
        } else {
            this.markers = binaryMap;
        }
    }

    /**
     * Przygotowuje markery ze względu na daną mapę binarną
     * Opis działania:
     * Stwórz tyle grup, ile jest jasnych punktów w mapie binarnej
     * dla każdego punktu spójrz na sąsiedztwo i zobacz czy dany punkt już znajduje się w tej grupie.
     * Jeśli tak, pomin, jeśli nie, dołącz całą grupę.
     *
     * @param binaryMap mapa binarna obrazu po progowaniu
     */
    private void prepareMarkers(ImageMap binaryMap) {
        List<Tuple2<Integer, Integer>> availablePoints = availableMarkerPoints(binaryMap);

        List<List<Tuple2<Integer, Integer>>> groups = initialMarkerGroups(availablePoints);

        boolean anyNeighbourFound = true;

        while (anyNeighbourFound) {
            anyNeighbourFound = false;
            innerLoop:
            for (List<Tuple2<Integer, Integer>> group : groups) {
                for (Tuple2<Integer, Integer> point : group) {
                    for (Tuple2<Integer, Integer> potentialNewNeighbour : availablePointNeighbourhood(point, binaryMap)) {
                        boolean alreadyInThisGroup = group.stream().filter(pt -> pt.equals(potentialNewNeighbour)).count() == 1;
                        if (!alreadyInThisGroup) {
                            List<Tuple2<Integer, Integer>> previousGroup = groups.stream().filter(grp -> grp.contains(potentialNewNeighbour)).findFirst().get();
                            group.addAll(previousGroup);
                            groups.remove(previousGroup);
                            anyNeighbourFound = true;
                            break innerLoop;
                        }
                    }
                }
            }
        }

        this.numberOfGroups = groups.stream().filter(group -> group.size() >= minimalGroupSize).count();

        ImageMap markersMap = new ImageConverter().toImageMap(BinaryImage.black(binaryMap.width(), binaryMap.height()).asImage());

        for (List<Tuple2<Integer, Integer>> group : groups) {
            if (group.size() >= minimalGroupSize) {
                int red = new Random().nextInt(255);
                int green = new Random().nextInt(255);
                int blue = new Random().nextInt(255);
                for (Tuple2<Integer, Integer> point : group) {
                    Canals canals = new Canals(red, green, blue);
                    markersMap.put(point._1, point._2, canals);
                }
            }
        }

        this.markers = markersMap;
    }

    /**
     * Metoda sprawdzająca czy dany zakres kolorów mieści się w przedziale
     *
     * @param canals sprawdzany zakres kolorów
     * @param from   dolny przedział kolorów
     * @param to     górny przedział kolorów
     * @return wynik sprawdzenia
     */
    private boolean fitsInRange(Canals canals, Canals from, Canals to) {
        return canals.red() >= from.red() && canals.red() <= to.red() &&
                canals.blue() >= from.blue() && canals.blue() <= to.blue() &&
                canals.green() >= from.green() && canals.green() <= to.green();
    }

    /**
     * Akcesor reprezentacji graficznej binarnej mapy
     *
     * @return obraz
     */
    public Image map() {
        return new ImageConverter().toImage(map);
    }

    /**
     * Akcesor reprezentacji graficznej markerów
     *
     * @return obraz
     */
    public Image markers() {
        return new ImageConverter().toImage(markers);
    }

    /**
     * Zwraca obraz wyjściowy z nałożoną mapą binarną obrazu
     *
     * @return obraz
     */
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

    /**
     * Zamienia pojedyńcze punkty w grupy (segmenty) o wielkości jeden.
     *
     * @param availablePoints wszystkie punkty
     * @return wszystkie grupy
     */
    private List<List<Tuple2<Integer, Integer>>> initialMarkerGroups(List<Tuple2<Integer, Integer>> availablePoints) {
        List<List<Tuple2<Integer, Integer>>> groups = new ArrayList<>();
        for (Tuple2<Integer, Integer> point : availablePoints) {
            List<Tuple2<Integer, Integer>> list = new ArrayList<>();
            list.add(point);
            groups.add(list);
        }

        return groups;
    }

    /**
     * Dla danej mapy binarnej obrazu, sprawdza które punkty są zapalone i zbiera je
     *
     * @param binaryImage mapa binarna obrazu
     * @return lista zapalonych punktów
     */
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

    /**
     * Dla danego punktu zbiera z mapy binarnej obrazu punkty sąsiednie, jeśli są zapalone
     *
     * @param point     punkt sprawdzenia
     * @param binaryMap mapa binarna obrazu
     * @return lista zapalonych punktów sąsiednich
     */
    private List<Tuple2<Integer, Integer>> availablePointNeighbourhood(Tuple2<Integer, Integer> point, ImageMap binaryMap) {
        List<Tuple2<Integer, Integer>> list = new ArrayList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (binaryMap.getCanalValueOrBlack(point._1 + i, point._2 + j).red() == 255) {
                    list.add(new Tuple2<>(point._1 + i, point._2 + j));
                }
            }
        }

        return list;
    }

    /**
     * Akcesor liczby grup (segmentów)
     *
     * @return liczba grup (segmentów)
     */
    public long numberOfGroups() {
        return this.numberOfGroups;
    }
}
