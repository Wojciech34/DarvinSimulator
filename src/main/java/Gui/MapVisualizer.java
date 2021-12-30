package Gui;

import World.AbstractMap;
import World.Animal;
import World.Vector2d;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.List;

public class MapVisualizer {
    private final AbstractMap map;
    private final GridPane grid = new GridPane();
    private final int cellWidth;
    private final int cellHeight;
    private Animal temporaryAnimal;


    public MapVisualizer(AbstractMap map, int cellWidth, int cellHeight){
        this.map = map;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }
    public void function(){
        if(!this.map.existTrackedAnimal){
            this.map.existTrackedAnimal = true;
            this.temporaryAnimal.setToTrack = true;
            this.map.trackedAnimal = this.temporaryAnimal;
        }
    }

    public void createGrid(){
        for (int i = 0; i < map.MapWidth; i++){
            this.grid.getColumnConstraints().add(new ColumnConstraints(this.cellWidth));
        }
        for (int i = 0; i < map.MapHigh; i++){
            this.grid.getRowConstraints().add(new RowConstraints(this.cellHeight));
        }
        this.map.grass.forEach(t -> {
            Label label = new Label();
            label.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
            label.setMaxSize(cellWidth,cellHeight);
            label.setMinSize(cellWidth,cellHeight);
            grid.add(label, t.getX(), t.getY());
        });
        for (Vector2d vector : this.map.animals.keySet()){
            List<Animal> animalsList = this.map.animals.get(vector);
            if (animalsList.size() > 0){
            Button button = new Button();
            Animal animal = animalsList.get(0);
            for (Animal value : animalsList) {
                if (value.energy > animal.energy) {
                    animal = value;
                }
            }
            this.temporaryAnimal = animal;
            if (animal.energy > this.map.startEnergy){
                button.setBackground(new Background(new BackgroundFill(Color.DARKRED, null, null)));}
            if (animal.energy <= this.map.startEnergy && animal.energy >= this.map.requiredEnergyToBeParent){
                button.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));}
            if (animal.energy < this.map.requiredEnergyToBeParent){
                button.setBackground(new Background(new BackgroundFill(Color.DEEPPINK, null, null)));}
            button.setMaxSize(cellWidth,cellHeight);
            button.setMinSize(cellWidth,cellHeight);
            Label label = new Label(animal.genotype.toString());
            button.setOnAction(t -> {
                Stage stage = new Stage();
                VBox vbox = new VBox();
                Button trackButton = new Button("track");
                trackButton.setOnAction(event -> {
                    if(!this.map.existTrackedAnimal){
                    function();
                    }
                    else{
                        System.out.println("Only one animal can be tracked");
                    }
                });
                vbox.getChildren().addAll(label, trackButton);
                vbox.setAlignment(Pos.TOP_CENTER);
                Scene scene = new Scene(vbox, 400, 200);
                stage.setScene(scene);
                stage.show();
            });

            this.grid.add(button, vector.getX(), vector.getY());
        }};
    }
    public void colorGrid(LinkedList<Animal> animals){
        for (Animal animal: animals){
            Pane pane = new Pane();
            pane.setBackground(new Background(new BackgroundFill(Color.BLUE,null,null)));
            this.grid.add(pane, animal.position.getX(), animal.position.getY());
        }
    }
    public GridPane getGrid(){
        return this.grid;
    }
}
