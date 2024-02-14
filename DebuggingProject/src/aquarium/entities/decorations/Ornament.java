package aquarium.entities.decorations;

public class Ornament extends BaseDecoration{
    private static int COMFORT = 1;
    private static double PRICE = 5;
    public Ornament() { //po tekstu zadatka ovaj konstruktor ne prima parametre, vec se uzimaju default vrednosti
        super(COMFORT, PRICE);
    }
}
