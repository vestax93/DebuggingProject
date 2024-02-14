package aquarium.entities.fish;

public class SaltwaterFish extends BaseFish{
    private static int INITIAL_SIZE = 5;
    public SaltwaterFish(String name, String species, double price) {
        super(name, species, price);
        setSize(5);
    }

    @Override
    public void eat() {
        setSize(2);
    }
}
