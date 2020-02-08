package app.representation;

import io.vavr.Function3;

import java.util.HashMap;
import java.util.Map;

/**
 * Reprezentacja obrazu w postaci macierzy punktów i wartości
 */
public class ImageMap {
    private Map<Integer, Map<Integer, Canals>> map = new HashMap<>();

    /**
     * Nadpisywanie wartości koloru w danym punkcie
     * @param x współrzędna x
     * @param y współrzędna y
     * @param canals wartość RGB w danym punkcie
     */
    public void put(int x, int y, Canals canals) {
        Map<Integer, Canals> innerMap = map.get(x);

        if (innerMap == null) {
            innerMap = new HashMap<>();
        }

        innerMap.put(y, canals);

        map.put(x, innerMap);
    }

    /**
     * Pobranie wartości RGB danego piksela
     * @param x współrzędna x
     * @param y współrzędna y
     * @return wartość RGB
     */
    public Canals get(int x, int y) {
        return map.get(x).get(y);
    }

    /**
     * Pobiera wysokość obrazu
     * @return wysokość
     */
    public int height() {
        return map.get(0).size();
    }

    /**
     * Pobiera szerokość obrazu
     * @return szerokość
     */
    public int width() {
        return map.size();
    }

    /**
     * Operacja jednopunktowa na obrazie
     * @param operator funkcja operacji
     */
    public void singlePointOperation(Function3<Integer, Integer, Canals, Canals> operator) {
        map.forEach((x, value) -> {
            value.forEach((y, canals) -> {
                put(x, y, operator.apply(x, y, canals));
            });
        });
    }

    /**
     * Pobranie wartości RGB piksela, lub czerń dla pikseli sąsiadujących z krawędzią obrazu
     * @param x współrzędna x
     * @param y współrzędna y
     * @return wartość RGB
     */
    public Canals getCanalValueOrBlack(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            return new Canals(0, 0, 0);
        }
        return map.get(x).get(y);
    }

}
