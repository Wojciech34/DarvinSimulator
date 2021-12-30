package Gui;

import World.AbstractMap;
import World.Animal;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.LinkedList;
import java.util.List;

public class ExtraData {
    private final AbstractMap map;
    private final List<Integer> averageAnimalLife = new LinkedList<>();
    private final List<Integer> averageAnimalNumberOfChildren = new LinkedList<>();
    private final List<Integer> averageEnergyLevel = new LinkedList<>();
    private final List<Integer> numberOfAnimals = new LinkedList<>();
    private final List<Integer> numberOfGrass = new LinkedList<>();
    private LinkedList<LinkedList<Integer>> dominantGenotypes;
    public LinkedList<Animal> animalsToVis;
    private int counterAnimal = 0;
    private int counterGrass = 0;
    private int aAnimalLife = 0;
    private int aAnimalNofC = 0;
    private int aAnimalE = 0;

    public ExtraData(AbstractMap map){
        this.map = map;
    }
    public void updateData(){
        this.counterAnimal += this.map.animalsCounter;
        this.counterGrass += this.map.grassCounter;
        int n = 0;
        int m = 0;
        this.dominantGenotypes = new LinkedList<>();
        this.animalsToVis = new LinkedList<>();
        int maxNumberOfGenotypes = 0;
        for (List<Animal> animalsList : this.map.animals.values()){
            for (Animal animal : animalsList){
                n += animal.energy;
                m += animal.numberOfChildren;
            }
        }
        for (List<Animal> animalList : this.map.animalGenotypes.values()){
            if (animalList.size() > maxNumberOfGenotypes){
                maxNumberOfGenotypes = animalList.size();}
        }
        for (List<Animal> animalList : this.map.animalGenotypes.values()){
            if (animalList.size() == maxNumberOfGenotypes){
                for (Animal animal : animalList){
                    animalsToVis.add(animal);
                    if (!this.dominantGenotypes.contains(animal.genotype)){
                        this.dominantGenotypes.add(animal.genotype);
                    }
                }
            }
        }
        this.numberOfAnimals.add(this.map.animalsCounter);
        if (this.numberOfAnimals.size() > 30){
            this.numberOfAnimals.remove(0);
        }
        this.numberOfGrass.add(this.map.grassCounter);
        if (this.numberOfGrass.size() > 30){
            this.numberOfGrass.remove(0);
        }
        if (this.map.numberOfDeadAnimals > 0){
        this.averageAnimalLife.add(this.map.numberOfDaysDeadAnimalsLived / this.map.numberOfDeadAnimals);} else{
            this.averageAnimalLife.add(0);
        }
        if (this.averageAnimalLife.size() > 30){
            this.averageAnimalLife.remove(0);
        }
        if (this.map.animalsCounter > 0){
        this.averageAnimalNumberOfChildren.add(m / this.map.animalsCounter);
        this.averageEnergyLevel.add(n / this.map.animalsCounter);
            } else {
            this.averageAnimalNumberOfChildren.add(0);
            this.averageEnergyLevel.add(0);
        }
        if (this.averageAnimalNumberOfChildren.size() > 30){
            this.averageAnimalNumberOfChildren.remove(0);
        }
        if (this.averageEnergyLevel.size() > 30){
            this.averageEnergyLevel.remove(0);
        }
        this.aAnimalLife += this.averageAnimalLife.get(this.averageAnimalLife.size()-1);
        this.aAnimalE += this.averageEnergyLevel.get(this.averageEnergyLevel.size()-1);
        this.aAnimalNofC += this.averageAnimalNumberOfChildren.get(this.averageAnimalNumberOfChildren.size()-1);
    }
    public Scene getData(){
        LineChart<Number, Number> lineChartEnergy = createChart(this.averageEnergyLevel,
                "day","average animal energy");
        LineChart<Number, Number> lineChartLife = createChart(this.averageAnimalLife,
                "day","average death animal life");
        LineChart<Number, Number> lineChartChildren = createChart(this.averageAnimalNumberOfChildren,
                "day","average alive animal`s children");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(lineChartEnergy,lineChartLife,lineChartChildren);
        LineChart<Number, Number> lineChartAnimals = createChart(this.numberOfAnimals,
                "day","number of animals");
        LineChart<Number, Number> lineChartGrass = createChart(this.numberOfGrass,
                "day","number of grass");
        VBox vboxG = new VBox();
        vboxG.getChildren().add(new Label("dominant genotypes"));
        for (LinkedList<Integer> genotype : this.dominantGenotypes){
            Label label = new Label(genotype.toString());
            vboxG.getChildren().add(label);
        }
        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(lineChartAnimals,lineChartGrass);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(hbox,hbox2,vboxG);
        Scene scene = new Scene(vbox, 600,600);
        return scene;
    }
    private LineChart createChart(List<Integer> data, String xName, String yName){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(this.map.dayCounter-data.size()+1);
        xAxis.setUpperBound(this.map.dayCounter);
        xAxis.setLabel(xName);
        yAxis.setLabel(yName);
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("series");
        for (int i = 0; i < data.size(); i++){
            series.getData().add(new XYChart.Data(this.map.dayCounter-data.size()+1+i, data.get(i)));
        }
        lineChart.getData().add(series);
        return lineChart;
    }
    public String getDataToFile(){
        String result = "";
        result += String.valueOf(map.dayCounter)+"  ";
        result += String.valueOf(map.animalsCounter)+"  ";
        result += String.valueOf(map.grassCounter)+"  ";
        result += String.valueOf(this.averageEnergyLevel.get(this.averageEnergyLevel.size()-1))+"  ";
        result += String.valueOf(this.averageAnimalLife.get(this.averageAnimalLife.size()-1))+"  ";
        result += String.valueOf(this.averageAnimalNumberOfChildren.get(this.averageAnimalNumberOfChildren.size()-1))+"  ";
        return result;
    }
    public String lastLineToFile(){
        String result = "    ";
        result += String.valueOf(this.counterAnimal/(this.map.dayCounter+1))+"  ";
        result += String.valueOf(this.counterGrass/(this.map.dayCounter+1))+"  ";
        result += String.valueOf(this.aAnimalE/(this.map.dayCounter+1))+"  ";
        result += String.valueOf(this.aAnimalLife/(this.map.dayCounter+1))+"  ";
        result += String.valueOf(this.aAnimalNofC/(this.map.dayCounter+1))+"  ";
        return result;
    }

}
