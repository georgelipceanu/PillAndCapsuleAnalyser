package org.example.pillandcapsuleanalyser;

import javafx.scene.paint.Color;
import org.example.pillandcapsuleanalyser.controllers.MainController;
import org.example.pillandcapsuleanalyser.models.PillDetails;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Measurement(iterations=1)
@Warmup(iterations=1)
@Fork(value=1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)

public class PillBenchmark {
    int width=6,height=6;
    MyUnionFind uf=new MyUnionFind(width*height);
    int[] imageArray = new int[width*height];
    @Setup(Level.Invocation)
    public void setup() {
        MainController.mainController=new MainController();
        MainController.mainController.setHeight(6);
        MainController.mainController.setWidth(6);
        MainController.mainController.setImageArray(imageArray);

        imageArray[2]=-1;
        imageArray[3]=-1;
        imageArray[9]=-1;
        imageArray[8]=-1;

        imageArray[25]=-1;
        imageArray[26]=-1;
        imageArray[33]=-1;
        imageArray[34]=-1;

        PillDetails p = new PillDetails("oxy",1,4,2, Color.GREEN);
        PillDetails p2 = new PillDetails("oxy",2,4,25, Color.GREEN);

        p.getIndices().add(2);
        p.getIndices().add(3);
        p.getIndices().add(8);
        p.getIndices().add(9);

        p2.getIndices().add(25);
        p2.getIndices().add(26);
        p2.getIndices().add(33);
        p2.getIndices().add(34);

        uf.union(2,3);
        uf.union(2,8);
        uf.union(8,9);
        uf.union(25,26);
        uf.union(25,33);
        uf.union(33,34);

        MainController.mainController.setPills(new HashMap<>());
        MainController.mainController.getPills().put(2,p);
        MainController.mainController.getPills().put(25,p2);
    }


    @Benchmark
    public void unionPixels(){
        uf.union(10,11);
    }

    @Benchmark
    public void findRoot(){
        uf.find(9);
    }

    @Benchmark
    public void randomColorImageOption(){
        MainController.mainController.randomColorImage();
    }
    @Benchmark
    public void blackAndWhiteImageOption(){
        MainController.mainController.bAndWViewImage();
    }

    public static void main(String[] args) throws RunnerException, IOException {
        Main.main(args);
    }
}
