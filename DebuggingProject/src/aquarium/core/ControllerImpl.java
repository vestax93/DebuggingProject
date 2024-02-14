package aquarium.core;

import aquarium.entities.aquariums.Aquarium;
import aquarium.entities.aquariums.FreshwaterAquarium;
import aquarium.entities.aquariums.SaltwaterAquarium;
import aquarium.entities.decorations.Decoration;
import aquarium.entities.decorations.Ornament;
import aquarium.entities.decorations.Plant;
import aquarium.entities.fish.Fish;
import aquarium.entities.fish.FreshwaterFish;
import aquarium.entities.fish.SaltwaterFish;
import aquarium.repositories.DecorationRepository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static aquarium.common.ConstantMessages.*;
import static aquarium.common.ExceptionMessages.*;


public class ControllerImpl implements Controller{
    private DecorationRepository decorations;
    private Map<String, Aquarium> aquariums;

    public ControllerImpl() {
        this.decorations = new DecorationRepository();
        this.aquariums = new LinkedHashMap<>();
    }

    @Override
    public String addAquarium(String aquariumType, String aquariumName) {
        Aquarium aquarium;
        if(aquariumType.equals("FreshwaterAquarium")){
            aquarium = new FreshwaterAquarium(aquariumName);
        } else if(aquariumType.equals("SaltwaterAquarium")){
            aquarium = new SaltwaterAquarium(aquariumName);
        } else {
            throw new IllegalArgumentException("Invalid aquarium type.");
        }
        this.aquariums.put(aquariumName,aquarium);

        return String.format(SUCCESSFULLY_ADDED_AQUARIUM_TYPE, aquariumType);
    }

    @Override
    public String addDecoration(String type) {
        Decoration decoration;
        if(type.equals("Ornament")){
            decoration = new Ornament();
        } else if (type.equals("Plant")){
            decoration = new Plant();
        } else {
            throw new IllegalArgumentException(INVALID_DECORATION_TYPE);
        }
        this.decorations.add(decoration);
        return String.format(SUCCESSFULLY_ADDED_DECORATION_TYPE,type);
    }

    @Override
    public String insertDecoration(String aquariumName, String decorationType) {
        Decoration decoration = this.decorations.findByType(decorationType);
        boolean removed = this.decorations.remove(decoration);
        if(!removed){
            throw new IllegalArgumentException(String.format(NO_DECORATION_FOUND,decorationType));
        }
        Aquarium aquarium = this.aquariums.get(aquariumName);
        aquarium.addDecoration(decoration);
        return String.format(SUCCESSFULLY_ADDED_DECORATION_IN_AQUARIUM,decorationType, aquariumName);

    }

    @Override
    public String addFish(String aquariumName, String fishType, String fishName, String fishSpecies, double price) {
        Fish fish;
        Aquarium aquarium = this.aquariums.get(aquariumName);
        if (fishType.equals("FreshwaterFish")) {
            if (!aquarium.getClass().getSimpleName().equals("FreshwaterAquarium")) {
                return WATER_NOT_SUITABLE;
            }
            fish = new FreshwaterFish(fishName, fishSpecies, price);
        } else if (fishType.equals("SaltwaterFish")) {
            if (!aquarium.getClass().getSimpleName().equals("SaltwaterAquarium")) {
                return WATER_NOT_SUITABLE;
            }
            fish = new SaltwaterFish(fishName, fishSpecies, price);
        } else {
            throw new IllegalArgumentException(INVALID_FISH_TYPE);
        }
        aquarium.addFish(fish);
        return String.format(SUCCESSFULLY_ADDED_FISH_IN_AQUARIUM,fishType,aquariumName);
    }

    @Override
    public String feedFish(String aquariumName) {
        Aquarium aquarium = this.aquariums.get(aquariumName);
        aquarium.feed();
        return String.format(FISH_FED, aquarium.getFish().size());
    }

    @Override
    public String calculateValue(String aquariumName) {
        Aquarium aquarium = aquariums.get(aquariumName);
        double sumDecorations = aquarium.getDecorations().stream().mapToDouble(Decoration::getPrice).sum();
        double sumFish = aquarium.getFish().stream().mapToDouble(Fish::getPrice).sum();
        return String.format(VALUE_AQUARIUM, aquariumName, sumDecorations + sumFish);
    }

    @Override
    public String report() {
        return aquariums.values().stream().map(Aquarium::getInfo).collect(Collectors.joining(System.lineSeparator()));
    }
}
