package Gui;

import World.AbstractMap;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimulationEngine implements Runnable {
    public AbstractMap map;
    public ExtraData data;
    public MapVisualizer mp;
    private final int cellWidth;
    private final int cellHeight;
    private final int sceneWidth = 500;
    private final int sceneHeight = 500;
    public boolean flag = true;
    private final Stage mainStage = new Stage();
    private final Stage trackedAnimalStage = new Stage();
    private final Stage extraDataStage = new Stage();
    public boolean magicMode = false;
    private int magicCounter = 0;
    private boolean flag2 = true;
    private int dayOfDeath;
    private FileWriterClass file;

    public SimulationEngine(AbstractMap map, int dataHeight, int dataWidth){
        this.map = map;
        this.cellWidth = sceneWidth/dataWidth;
        this.cellHeight = sceneHeight/dataHeight;
    }
    public void addFile(FileWriterClass file){
        this.file = file;
    }
    public void close(){
        this.file.writeToFile(data.lastLineToFile());
        this.mainStage.close();
        this.extraDataStage.close();
    }
    @Override
    public void run(){
        while (flag){
            MapVisualizer mp = new MapVisualizer(map, cellWidth, cellHeight);
            mp.createGrid();
            data.updateData();
            this.file.writeToFile(data.getDataToFile());
            this.mp = mp;
            Platform.runLater(() -> {
                this.extraDataStage.setScene(data.getData());
                this.extraDataStage.show();
                if (magicCounter < 3 && magicMode && this.map.animalsCounter == 5){
                    this.map.magicModeOn();
                    this.magicCounter += 1;
                    Stage infoStage = new Stage();
                    Label label = new Label("animals have been added");
                    label.setAlignment(Pos.CENTER);
                    Scene scene2 = new Scene(label, 200, 200);
                    infoStage.setScene(scene2);
                    infoStage.show();
                }
                if (this.map.existTrackedAnimal){
                    VBox vbox = new VBox();
                    Label label1 = new Label("number of children  "+ this.map.trackedAnimal.numberOfChildren);
                    Label label2 = new Label("number of offspring  "+ this.map.trackedAnimal.numberOfOffspring);
                    Label label3;
                    if (this.map.trackedAnimal.alive){
                        label3 = new Label("animal is alive");
                    } else {
                        if (flag2){
                            this.dayOfDeath = this.map.dayCounter;
                            flag2 = false;
                        }
                        label3 = new Label("animal died at  "+this.dayOfDeath +"  day");
                    }
                    vbox.getChildren().addAll(label1,label2,label3);
                    Scene scene3 = new Scene(vbox,200,200);
                    this.trackedAnimalStage.setScene(scene3);
                    this.trackedAnimalStage.show();
                }
                Scene scene = new Scene(mp.getGrid(),this.sceneWidth+20,this.sceneHeight+20);
                mainStage.setScene(scene);
                mainStage.show();
            });
            try{
                Thread.sleep(200);
            } catch (InterruptedException e){}
            this.map.nextDay();
        }
    }
}
