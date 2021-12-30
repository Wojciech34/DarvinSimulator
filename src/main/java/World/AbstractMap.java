package World;

import java.util.*;

public abstract class AbstractMap implements IPositionChangeObserver{
    public int MapWidth; // map positions x : 0,1,2 .. MapWidth-1
    public int MapHigh; // map positions y : 0,1,2 .. MapHigh-1
    protected Vector2d JungleLowerLeft; // Lower left vertex of the jungle
    protected Vector2d JungleUpperRight; // Upper right vertex of the jungle
    public HashMap<Vector2d, LinkedList<Animal>> animals = new HashMap<>(); // more than one animal can be on the one field
    public HashMap<LinkedList<Integer>, LinkedList<Animal>> animalGenotypes = new HashMap<>();
    public ArrayList<Vector2d> grass = new ArrayList<Vector2d>(); // taken position indicates there is a grass
    public int startEnergy;
    public int requiredEnergyToBeParent;
    protected int moveEnergy;
    protected int plantEnergy;
    private final List<Vector2d> freePlacesSavanna = new LinkedList<Vector2d>();
    private final List<Vector2d> freePlacesJungle = new LinkedList<Vector2d>();
    public int dayCounter = 0;
    public int animalsCounter = 0;
    public int grassCounter = 0;
    public boolean existTrackedAnimal = false;
    public Animal trackedAnimal;
    public int numberOfDeadAnimals = 0;
    public int numberOfDaysDeadAnimalsLived = 0;
    private final Random generator = new Random();

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        LinkedList<Animal> temp = this.animals.get(oldPosition);
        temp.remove(animal);
        this.animals.remove(oldPosition);
        this.animals.put(oldPosition, temp);
        eatingGrass(animal);
        animal.position = newPosition;
        placeAnimal(animal);
        if (this.animals.containsKey(newPosition)) {
            giveNewLife(this.animals.get(newPosition));
        }
        this.freePlacesSavanna.remove(newPosition);
        this.freePlacesJungle.remove(newPosition);
        if (!this.animals.containsKey(oldPosition) && !this.grass.contains(oldPosition)){
            if (oldPosition.precedes(JungleUpperRight) && oldPosition.follows(JungleLowerLeft)){
                this.freePlacesJungle.add(oldPosition);
            } else {
                this.freePlacesSavanna.add(oldPosition);
            }
        }
    }
    public void magicModeOn(){
        List<LinkedList<Integer>> genotypeList = new LinkedList<>();
        this.animals.forEach((key, animalList) -> {
            animalList.forEach(animal -> genotypeList.add(animal.genotype));
        });
        genotypeList.forEach(genotype ->{
            int j = generator.nextInt(freePlacesJungle.size() + freePlacesSavanna.size());
            this.animalsCounter +=1;
            if (j < freePlacesJungle.size()){
                Animal animal = new Animal(freePlacesJungle.get(j), this.startEnergy, genotype);
                animal.map = this;
                animal.addObserver(this);
                placeAnimal(animal);
            }
            else {
                j = freePlacesJungle.size() + freePlacesSavanna.size() - j;
                Animal animal = new Animal(freePlacesSavanna.get(j), this.startEnergy, genotype);
                animal.map = this;
                animal.addObserver(this);
                placeAnimal(animal);
            }
        });
    }
    public void startAdventure(int n){
        for (int i = 0; i < this.MapWidth; i++){
            for (int j = 0; j < this.MapHigh; j++){
                Vector2d vector = new Vector2d(i,j);
                if (! this.animals.containsKey(vector) && ! this.grass.contains(vector)){
                    if (vector.precedes(this.JungleUpperRight) && vector.follows(this.JungleLowerLeft)){
                        freePlacesJungle.add(vector);
                    } else {
                        freePlacesSavanna.add(vector);
                    }
                }
            }
        }
        for (int i = 0; i < n; i++){
            LinkedList<Integer> list = new LinkedList<>();
            for (int k = 0; k < 32; k++){
                list.add(generator.nextInt(8));
            }
            list.sort(new Comparator<Integer>(){
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1-o2;
                }
            });
            int j = generator.nextInt(freePlacesJungle.size() + freePlacesSavanna.size());
            this.animalsCounter +=1;
            if (j < freePlacesJungle.size()){
                Animal animal = new Animal(freePlacesJungle.get(j), this.startEnergy, list);
                animal.map = this;
                animal.addObserver(this);
                animal.dayOfBirth = this.dayCounter;
                placeAnimal(animal);
            }
            else {
                j = freePlacesJungle.size() + freePlacesSavanna.size() - j;
                Animal animal = new Animal(freePlacesSavanna.get(j), this.startEnergy, list);
                animal.map = this;
                animal.addObserver(this);
                animal.dayOfBirth = this.dayCounter;
                placeAnimal(animal);
            }
        }
    }
    public void giveNewLife(LinkedList<Animal> animals){
        if (animals.size() > 1){ // number of animals at that field is higher than 1
            Animal parent1 = animals.get(0);
            int idx = 0;
            int counter = 0;
            for (Animal animal : animals){ // getting first parent, parent1.energy >= parent2.energy
                if (animal.energy > parent1.energy){parent1 = animal; idx = counter;}
                counter += 1;
            }
            counter = 0;
            Animal parent2 = animals.get(0);
            if (idx == 0){
                parent2 = animals.get(1);
            }
            for (Animal animal : animals){ // getting second parent
                if ((animal.energy > parent2.energy) && (counter != idx)){parent2 = animal;}
                counter +=1 ;
            }
            if ((parent1.energy >= requiredEnergyToBeParent) && (parent2.energy >= requiredEnergyToBeParent)){
                int sumEnergy = parent1.energy + parent2.energy;
                LinkedList<Integer> newGenotype = new LinkedList<>();
                if (generator.nextBoolean()){
                    int intersection = (parent1.energy / sumEnergy) * 32;
                    for (int i=0; i < intersection; i++){
                        newGenotype.add(parent1.genotype.get(i));
                    }
                    for (int i=intersection; i<32; i++){
                        newGenotype.add(parent2.genotype.get(i));
                    }
                } else{
                    int intersection = (parent2.energy / sumEnergy) * 32;
                    for (int i=0; i < intersection; i++){
                        newGenotype.add(parent2.genotype.get(i));
                    }
                    for (int i=intersection; i<32; i++){
                        newGenotype.add(parent1.genotype.get(i));
                    }
                }
                parent1.energy *= 3;
                parent1.energy /= 4;
                parent2.energy *= 3;
                parent2.energy /= 4;
                parent1.numberOfChildren += 1;
                parent2.numberOfChildren += 1;
                Animal newAnimal = new Animal(parent1.position, this.startEnergy, newGenotype);
                newAnimal.map = parent1.map;
                newAnimal.addObserver(this);
                newAnimal.dayOfBirth = this.dayCounter;
                if (parent1.setToTrack){
                    newAnimal.beingTracked = true;
                    newAnimal.father = parent1;
                    parent1.numberOfChildren += 1;
                    parent1.numberOfOffspring += 1;
                }
                if (parent2.setToTrack){
                    newAnimal.beingTracked = true;
                    newAnimal.father = parent2;
                    parent2.numberOfChildren += 1;
                    parent2.numberOfOffspring += 1;
                }
                if (!parent1.setToTrack && !parent2.setToTrack && (parent1.beingTracked || parent2.beingTracked)){
                    Animal parent;
                    if (parent1.beingTracked){
                        parent = parent1;
                    } else {
                        parent = parent2;
                    }
                    parent.father.numberOfOffspring += 1;
                    newAnimal.beingTracked = true;
                    newAnimal.father = parent.father;
                }
                this.animalsCounter +=1;
                placeAnimal(newAnimal);
            }
        }
    }

    public void eatingGrass(Animal animal){
        if (this.grass.contains(animal.position)) {
            animal.energy += plantEnergy;
            this.grassCounter -=1;
            this.grass.remove(animal.position);
        }
    }

    public void placeAnimal(Animal animal){
        LinkedList<Animal> temp;
        if (this.animals.containsKey(animal.position)){
            temp = this.animals.get(animal.position);
            this.animals.remove(animal.position);
        }
        else{
            temp = new LinkedList<Animal>();
        }
        LinkedList<Animal> temp2;
        if (this.animalGenotypes.containsKey(animal.genotype) &&
                !this.animalGenotypes.get(animal.genotype).contains(animal)){
            temp2 = this.animalGenotypes.get(animal.genotype);
            this.animalGenotypes.remove(animal.genotype);
            temp2.add(animal);
            this.animalGenotypes.put(animal.genotype, temp2);
        }
        else{ if (!this.animalGenotypes.containsKey(animal.genotype)){
            temp2 = new LinkedList<Animal>();
        temp2.add(animal);
        this.animalGenotypes.put(animal.genotype, temp2);}}
        this.freePlacesSavanna.remove(animal.position);
        this.freePlacesJungle.remove(animal.position);
        temp.add(animal);
        this.animals.put(animal.position, temp);
    }

    public void growGrass(){ // adding 2 grass fields at map
        if (freePlacesSavanna.size() > 0){
            int i = generator.nextInt(freePlacesSavanna.size());
            this.grass.add(freePlacesSavanna.get(i));
            this.grassCounter +=1;
        }
        if (freePlacesJungle.size() > 0){
            int i = generator.nextInt((freePlacesJungle.size()));
            this.grass.add(freePlacesJungle.get(i));
            this.grassCounter +=1;
        }
    }
    public boolean canMoveTo(Vector2d position){
        if (position.getX() < MapWidth && position.getX() >= 0 && position.getY() < MapHigh && position.getY() >= 0){
            return true;
        }
        else return false;
    };
    public void animalsPerformMoves(){
        List<Animal> temp = new LinkedList<>();
        for (List<Animal> list: this.animals.values()){
            for (int i = 0; i < list.size(); i++){
                temp.add(list.get(i));
            }
        }
        temp.forEach(a -> {
            a.animalMove();
            a.energy -= moveEnergy;
            if (a.energy < moveEnergy){
               LinkedList<Animal> tempAnimals = this.animals.get(a.position);
               this.animals.remove(a.position);
               tempAnimals.remove(a);
               this.animals.put(a.position, tempAnimals);
               LinkedList<Animal> tempAnimals2 = this.animalGenotypes.get(a.genotype);
               this.animalGenotypes.remove(a.genotype);
               tempAnimals2.remove(a);
               this.animalGenotypes.put(a.genotype, tempAnimals2);
               this.animalsCounter -= 1;
               a.alive = false;
               this.numberOfDeadAnimals += 1;
               this.numberOfDaysDeadAnimalsLived += this.dayCounter - a.dayOfBirth;
            }
        });
    }
    public abstract Vector2d howToMove(Vector2d position);
    public void nextDay(){
        growGrass();
        animalsPerformMoves();
        this.dayCounter +=1;
    }

}
