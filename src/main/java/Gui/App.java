package Gui;

import World.Animal;
import World.MapWall;
import World.MapWrapped;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.LinkedList;

public class App extends Application {
    private final int sceneWidth = 500;
    private final int sceneHeight = 500;
    private final TextField dataHeight = new TextField("50");
    private final TextField dataWidth = new TextField("50");
    private final TextField startEnergy = new TextField("50");
    private final TextField moveEnergy = new TextField("1");
    private final TextField plantEnergy = new TextField("30");
    private final TextField jungleHeight = new TextField("20");
    private final TextField jungleWidth = new TextField("20");
    private final TextField firstAnimals = new TextField("10");
    private SimulationEngine engine1;
    private SimulationEngine engine2;
    private boolean wallMapStarted = false;
    private boolean wrappedMapStarted = false;
    private FileWriterClass fileW;
    private FileWriterClass fileR;
    private Stage stage;


    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vbox = new VBox();
        Label label1 = new Label("map height");
        HBox hbox1 = new HBox(label1, dataHeight);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setSpacing(30);
        Label label2 = new Label("map width");
        HBox hbox2 = new HBox(label2, dataWidth);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.setSpacing(30);
        Label label3 = new Label("start energy");
        HBox hbox3 = new HBox(label3, startEnergy);
        hbox3.setAlignment(Pos.CENTER);
        hbox3.setSpacing(30);
        Label label4 = new Label("move energy");
        HBox hbox4 = new HBox(label4, moveEnergy);
        hbox4.setAlignment(Pos.CENTER);
        hbox4.setSpacing(30);
        Label label5 = new Label("plant energy");
        HBox hbox5 = new HBox(label5, plantEnergy);
        hbox5.setAlignment(Pos.CENTER);
        hbox5.setSpacing(30);
        Label label6 = new Label("jungle height");
        HBox hbox6 = new HBox(label6, jungleHeight);
        hbox6.setAlignment(Pos.CENTER);
        hbox6.setSpacing(30);
        Label label7 = new Label("jungle width");
        HBox hbox7 = new HBox(label7, jungleWidth);
        hbox7.setAlignment(Pos.CENTER);
        hbox7.setSpacing(30);
        Label label8 = new Label("number of first animals");
        HBox hbox8 = new HBox(label8, firstAnimals);
        hbox8.setAlignment(Pos.CENTER);
        hbox8.setSpacing(30);
        vbox.getChildren().addAll(hbox1,hbox2,hbox3,hbox4,hbox5,hbox6,hbox7,hbox8);
        Button buttonStartNW = new Button("start Normal WallMap");
        buttonStartNW.setOnAction(t ->{
            if (!wallMapStarted){
                this.fileW = new FileWriterClass("WallMap");
                fileW.startWrite();
            createWallMap(false);
            wallMapStarted = true;
            Thread thread = new Thread((Runnable) this.engine1);
            thread.start();}
            else{System.out.println("Wall Map is already started");}
        });
        Button buttonStartMW = new Button("start Magic WallMap");
        buttonStartMW.setOnAction(t ->{
            if (!wallMapStarted){
                this.fileW = new FileWriterClass("WallMap");
                fileW.startWrite();
                createWallMap(true);
                wallMapStarted = true;
                Thread thread = new Thread((Runnable) this.engine1);
                thread.start();}
            else{System.out.println("Wall Map is already started");}
        });
        Button buttonStartNR = new Button("start Normal WrappedMap");
        buttonStartNR.setOnAction(t ->{
            if (!wrappedMapStarted){
                this.fileR = new FileWriterClass("WrappedMap");
                fileR.startWrite();
                createWrappedMap(false);
                wrappedMapStarted = true;
                Thread thread = new Thread((Runnable) this.engine2);
                thread.start();}
            else{System.out.println("Wrapped Map is already started");}
        });
        Button buttonStartMR = new Button("start Magic WrappedMap");
        buttonStartMR.setOnAction(t ->{
            if (!wrappedMapStarted){
                this.fileR = new FileWriterClass("WrappedMap");
                fileR.startWrite();
                createWrappedMap(true);
                wrappedMapStarted = true;
                Thread thread = new Thread((Runnable) this.engine2);
                thread.start();}
            else{System.out.println("Wrapped Map is already started");}
        });
        Button buttonStopW = new Button("stop Wall Map");
        buttonStopW.setOnAction(t -> {
            engine1.flag = false;
        });
        Button buttonContinueW = new Button("continue Wall Map");
        buttonContinueW.setOnAction(t -> {
            engine1.flag = true;
            Thread thread = new Thread((Runnable) this.engine1);
            thread.start();
        });
        Button buttonDominantW = new Button("dominant genotype at Wall Map");
        buttonDominantW.setOnAction(t -> {
            if (wallMapStarted && !this.engine1.flag){
                distinguishAnimalsW(this.engine1.data.animalsToVis);
            }else{
                System.out.println("Map must be created and stopped");
            }
                });
        Button buttonDominantR = new Button("dominant genotype at Wrapped Map");
        buttonDominantR.setOnAction(t -> {
            if (wrappedMapStarted && !this.engine2.flag){
                distinguishAnimalsR(this.engine2.data.animalsToVis);
            }else{
                System.out.println("Map must be created and stopped");
            }
        });
        Button buttonStopR = new Button("stop Wrapped Map");
        buttonStopR.setOnAction(t -> {
            engine2.flag = false;
        });
        Button buttonContinueR = new Button("continue Wrapped Map");
        buttonContinueR.setOnAction(t -> {
            engine2.flag = true;
            Thread thread = new Thread((Runnable) this.engine2);
            thread.start();
        });
        Button buttonEnd = new Button("End");
        buttonEnd.setOnAction(t ->{
            if (wallMapStarted){this.engine1.close();}
            if (wrappedMapStarted){this.engine2.close();}
            this.stage.close();
            System.exit(0);
        });
        HBox hbox9 = new HBox();
        hbox9.getChildren().addAll(buttonStartNW,buttonStartNR);
        hbox9.setAlignment(Pos.CENTER);
        hbox9.setSpacing(30);
        HBox hbox10 = new HBox();
        hbox10.getChildren().addAll(buttonStartMW,buttonStartMR);
        hbox10.setAlignment(Pos.CENTER);
        hbox10.setSpacing(30);
        HBox hbox11 = new HBox();
        hbox11.getChildren().addAll(buttonContinueW,buttonContinueR);
        hbox11.setAlignment(Pos.CENTER);
        hbox11.setSpacing(30);
        HBox hbox12 = new HBox();
        hbox12.getChildren().addAll(buttonStopW,buttonStopR);
        hbox12.setAlignment(Pos.CENTER);
        hbox12.setSpacing(30);
        HBox hbox13 = new HBox();
        hbox13.getChildren().addAll(buttonDominantW,buttonDominantR);
        hbox13.setAlignment(Pos.CENTER);
        hbox13.setSpacing(30);
        vbox.getChildren().addAll(hbox9,hbox10,hbox11,hbox12,hbox13,buttonEnd);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(10);
        this.stage = new Stage();
        Scene firstScene = new Scene(vbox, sceneWidth, sceneHeight);
        this.stage.setScene(firstScene);
        this.stage.setTitle("Enter data");
        this.stage.show();
    }

    private void createWallMap(boolean magicW){
        try {
            int n1 = Integer.parseInt(dataHeight.getText());
            int n2 = Integer.parseInt(dataWidth.getText());
            int n3 = Integer.parseInt(startEnergy.getText());
            int n4 = Integer.parseInt(moveEnergy.getText());
            int n5 = Integer.parseInt(plantEnergy.getText());
            int n6 = Integer.parseInt(jungleHeight.getText());
            int n7 = Integer.parseInt(jungleWidth.getText());
            int n8 = Integer.parseInt(firstAnimals.getText());
            MapWall map = new MapWall(n1, n2, n6, n7, n3, n4, n5);
            map.startAdventure(n8);
            this.engine1 = new SimulationEngine(map, n1, n2);
            this.engine1.data = new ExtraData(map);
            this.engine1.magicMode = magicW;
            this.engine1.addFile(this.fileW);
        } catch (IllegalArgumentException e) {
            System.out.println("wrong data");
        }
    }
    private void createWrappedMap(boolean magicR){
        try {
            int n1 = Integer.parseInt(dataHeight.getText());
            int n2 = Integer.parseInt(dataWidth.getText());
            int n3 = Integer.parseInt(startEnergy.getText());
            int n4 = Integer.parseInt(moveEnergy.getText());
            int n5 = Integer.parseInt(plantEnergy.getText());
            int n6 = Integer.parseInt(jungleHeight.getText());
            int n7 = Integer.parseInt(jungleWidth.getText());
            int n8 = Integer.parseInt(firstAnimals.getText());
            MapWrapped map = new MapWrapped(n1, n2, n6, n7, n3, n4, n5);
            map.startAdventure(n8);
            this.engine2 = new SimulationEngine(map, n1, n2);
            this.engine2.data = new ExtraData(map);
            this.engine2.magicMode = magicR;
            this.engine2.addFile(this.fileR);
        } catch (IllegalArgumentException e) {
            System.out.println("wrong data");
        }
    }
    private void distinguishAnimalsW(LinkedList<Animal> animals){
        MapVisualizer mapV = this.engine1.mp;
        mapV.colorGrid(animals);
    }
    private void distinguishAnimalsR(LinkedList<Animal> animals){
        MapVisualizer mapV = this.engine2.mp;
        mapV.colorGrid(animals);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
