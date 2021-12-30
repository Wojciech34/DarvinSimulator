package World;

import java.util.*;
import java.lang.*;

public class Animal {
    public Vector2d position;
    public int energy;
    public MapDirection direction;
    private final Random generator = new Random();
    public LinkedList<Integer> genotype;
    public List<IPositionChangeObserver> observers = new LinkedList<>();
    public int numberOfChildren = 0;
    public int numberOfOffspring = 0;
    public boolean setToTrack = false;
    public boolean beingTracked = false;
    public Animal father;
    public AbstractMap map;
    public boolean alive = true;
    public int dayOfBirth;


    public Animal(Vector2d position, int energy, LinkedList<Integer> genotype){
        this.position = position;
        this.energy = energy;
        this.genotype = genotype;
        this.direction = MapDirection.values()[generator.nextInt(8)];
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }
    private void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal){
        for (IPositionChangeObserver observer : observers){
            observer.positionChanged(oldPosition, newPosition, animal);
        }
    }


    public void animalMove(){
        int randomMove = generator.nextInt(32);
        MoveDirection moveToHandle = MoveDirection.values()[genotype.get(randomMove)];
        if (this.energy <= 0) {}
        switch (moveToHandle){
            case FORWARD -> {
                if (this.map.canMoveTo(this.position.add(this.direction.toUnitVector()))) {
                this.positionChanged(this.position, this.map.howToMove(this.position.add(this.direction.toUnitVector())), this);
                }
          //      this.position = this.map.howToMove(this.position.add(this.direction.toUnitVector()));
            }
            case BACKWARD -> {
                if (this.map.canMoveTo(this.position.subtract(this.direction.toUnitVector()))) {
                this.positionChanged(this.position, this.map.howToMove(this.position.subtract(this.direction.toUnitVector())), this);
                }
          //      this.position = this.map.howToMove(this.position.subtract(this.direction.toUnitVector()));
            }
            case ANGLE45 -> this.direction = MapDirection.values()[(this.direction.ordinal()+1)%8];
            case ANGLE90 -> this.direction = MapDirection.values()[(this.direction.ordinal()+2)%8];
            case ANGLE135 -> this.direction = MapDirection.values()[(this.direction.ordinal()+3)%8];
            case ANGLE225 -> this.direction = MapDirection.values()[(this.direction.ordinal()+5)%8];
            case ANGLE270 -> this.direction = MapDirection.values()[(this.direction.ordinal()+6)%8];
            case ANGLE315 -> this.direction = MapDirection.values()[(this.direction.ordinal()+7)%8];
        }

    }
}
