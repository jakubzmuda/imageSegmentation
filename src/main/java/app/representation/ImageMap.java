package app.representation;

import io.vavr.Function3;

import java.util.HashMap;
import java.util.Map;

public class ImageMap {
    private Map<Integer, Map<Integer, Canals>> map = new HashMap<>();

    public void put(int x, int y, Canals canals) {
        Map<Integer, Canals> innerMap = map.get(x);

        if (innerMap == null) {
            innerMap = new HashMap<>();
        }

        innerMap.put(y, canals);

        map.put(x, innerMap);
    }

    public Canals get(int x, int y) {
        return map.get(x).get(y);
    }

    public int getGray(int x, int y) {
        return map.get(x).get(y).red;
    }

    public Canals maxColorCanalValues() {
        Canals currentBiggest = new Canals(0, 0, 0);
        for (Map.Entry<Integer, Map<Integer, Canals>> e : map.entrySet()) {
            Integer x = e.getKey();
            Map<Integer, Canals> value = e.getValue();
            for (Map.Entry<Integer, Canals> entry : value.entrySet()) {
                Integer y = entry.getKey();
                Canals canals = entry.getValue();
                currentBiggest = new Canals(
                        Math.max(canals.red, currentBiggest.red),
                        Math.max(canals.green, currentBiggest.green),
                        Math.max(canals.blue, currentBiggest.blue));
            }
        }
        return currentBiggest;
    }

    public Canals minColorValues() {
        Canals currentLowest = new Canals(255, 255, 255);
        for (Map.Entry<Integer, Map<Integer, Canals>> e : map.entrySet()) {
            Integer x = e.getKey();
            Map<Integer, Canals> value = e.getValue();
            for (Map.Entry<Integer, Canals> entry : value.entrySet()) {
                Integer y = entry.getKey();
                Canals canals = entry.getValue();
                currentLowest = new Canals(
                        Math.min(canals.red, currentLowest.red),
                        Math.min(canals.green, currentLowest.green),
                        Math.min(canals.blue, currentLowest.blue));
            }
        }
        return currentLowest;
    }

    public int height() {
        return map.get(0).size();
    }

    public int width() {
        return map.size();
    }

    public void singlePointOperation(Function3<Integer, Integer, Canals, Canals> operator) {
        map.forEach((x, value) -> {
            value.forEach((y, canals) -> {
                put(x, y, operator.apply(x, y, canals));
            });
        });
    }

    public Canals getCanalValueOrBlack(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            return new Canals(0, 0, 0);
        }
        return map.get(x).get(y);
    }

}
