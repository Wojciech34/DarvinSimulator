package Gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterClass {
    String fileName;
    public FileWriterClass(String fileName){
        this.fileName = fileName;
    }

    public void startWrite(){
        try{
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("day, number of animals, number of grass, average energy level," +
                " average life duration, average number of children");
        writer.close();}
        catch (IOException e){
            System.out.println("File not found");
        }
    }
    public void writeToFile(String text){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.newLine();
            writer.append(text);
            writer.close();}
        catch (IOException e){
            System.out.println("File not found");
        }
    }
}
