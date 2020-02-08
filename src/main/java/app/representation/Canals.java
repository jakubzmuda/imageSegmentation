package app.representation;

/**
 * Reprezentacja RGB piksela
 */
public class Canals {
    final int red;
    final int green;
    final int blue;

    public Canals(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int red() {
        return red;
    }

    public int green() {
        return green;
    }

    public int blue() {
        return blue;
    }
}
