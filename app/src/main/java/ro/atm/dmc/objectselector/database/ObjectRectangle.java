package ro.atm.dmc.objectselector.database;

public class ObjectRectangle {
    private int id;
    private String name;
    private int firstCornerX;
    private int firstCornerY;
    private int secondCornerX;
    private int secondCornerY;

    public ObjectRectangle(int id, String name, int firstCornerX, int firstCornerY, int secondCornerX, int secondCornerY) {
        this.id = id;
        this.name = name;
        this.firstCornerX = firstCornerX;
        this.firstCornerY = firstCornerY;
        this.secondCornerX = secondCornerX;
        this.secondCornerY = secondCornerY;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFirstCornerX() {
        return firstCornerX;
    }

    public int getFirstCornerY() {
        return firstCornerY;
    }

    public int getSecondCornerX() {
        return secondCornerX;
    }

    public int getSecondCornerY() {
        return secondCornerY;
    }

    @Override
    public String toString() {
        return  name +
                "," + firstCornerX +
                "," + firstCornerY +
                "," + secondCornerX +
                "," + secondCornerY +
                '\n';
    }
}
