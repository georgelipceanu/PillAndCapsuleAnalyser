package org.example.pillandcapsuleanalyser.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.example.pillandcapsuleanalyser.HelloApplication;
import org.example.pillandcapsuleanalyser.MyUnionFind;
import org.example.pillandcapsuleanalyser.models.PillDetails;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    public static MainController mainController;
    private FileChooser fileChooser = new FileChooser();
    @FXML
    private TextField name, minSize,maxSize;
    @FXML
    private Tooltip tooltip;
    @FXML
    private ImageView imageView, bAndWView;
    @FXML
    private Slider hueS, satS, brightS, maxSatS, maxBrightS;
    @FXML
    private ListView<String> sysDetails;
    private Image original;
    private WritableImage rectImage;
    private PixelReader pixelReader;

    public PixelReader getPixelReader() {
        return pixelReader;
    }

    private int width,height=0;
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    private int[] imageArray, tempArray;
    public int[] getImageArray() {
        return imageArray;
    }
    public void setImageArray(int[] imageArray) {
        this.imageArray = imageArray;
    }

    private Color currentPillColor;
    private HashMap<Integer, PillDetails> pills = new HashMap<>();

    public HashMap<Integer, PillDetails> getPills() {
        return pills;
    }

    public void setPills(HashMap<Integer, PillDetails> pills) {
        this.pills = pills;
    }

    private MyUnionFind uf;
    public MyUnionFind getUf(){
        return uf;
    }
    private boolean pillToBeConfirmed = false;
    private ArrayList<Integer> rootsSelected = new ArrayList<>();

    public void refreshSysDetails(){
        sysDetails.getItems().clear();
        sysDetails.getItems().add("Total Pills: " + pills.size());
        ArrayList<String> pillsAdded = new ArrayList<>();
        if (!pills.isEmpty()) {
            sysDetails.getItems().add("PILLS:");
            for (PillDetails p : pills.values()) {
                if (!pillsAdded.contains(p.getName())) {
                    pillsAdded.add(p.getName());
                    sysDetails.getItems().add("----------------------------");
                    sysDetails.getItems().add("-- Name: " + p.getName());
                    ArrayList<String> pillDetailsList = new ArrayList<>();
                    int totalSize = 0;
                    int numOfSamePill = 0;
                    for (PillDetails p1 : pills.values()) {
                        if (p1.getName().equals(p.getName())) {
                            totalSize += p1.getSize();
                            numOfSamePill++;
                            pillDetailsList.add("----Capsule No.: " + p1.getCapsuleNumber() + '\n'+
                                    "----Size: "+p1.getSize());
                        }
                    }
                    sysDetails.getItems().add("--Average Size: " + totalSize / numOfSamePill);
                    sysDetails.getItems().add("--Amount of These Pills: "+numOfSamePill);
                    sysDetails.getItems().add("--INDIVIDUAL DETAILS: ");
                    sysDetails.getItems().addAll(pillDetailsList);
                    sysDetails.getItems().add("----------------------------");
                }
            }
        }
    }
    @FXML
    public void openFileMenu() {
        File file = fileChooser.showOpenDialog(new Stage());
        fileChooser.setInitialDirectory(new File("C:\\Users"));
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            original = image;
            pixelReader = original.getPixelReader();
            height = (int) image.getHeight();
            width = (int) image.getWidth();
            imageArray = new int[height * width];
            bAndWView.setImage(null);
            uf = new MyUnionFind(width * height);
            refreshSysDetails();
        }
    }

    @FXML
    public void pillClick(MouseEvent e) {
        double xInVBox = e.getX();
        double yInVBox = e.getY();

        if (xInVBox >= 0 && xInVBox <= imageView.getFitWidth() &&
                yInVBox >= 0 && yInVBox <= imageView.getFitHeight()) { // check if the click is within the imageview

            double ratio = width / imageView.getFitWidth();//getting ratio of image:imageview
            int xOfImage = (int) (xInVBox * ratio);
            int yOfImage = (int) (yInVBox * ratio);

            if (imageArray[((yOfImage) * (width)) + xOfImage] > 0) {
                drawRectanglesOverSelectedPill(xOfImage, yOfImage);
                imageViewToolTip(e,xOfImage,yOfImage);
            } else {
                currentPillColor = original.getPixelReader().getColor(xOfImage, yOfImage);
                blackAndWhiteClick(currentPillColor);
            }

            System.out.println(xOfImage + " " + yOfImage);
        }
    }

    public void blackAndWhiteClick(Color color) {
        WritableImage image = new WritableImage(width, height);
        tempArray = new int[height * width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double hue = pixelReader.getColor(x, y).getHue();

                if (color.getHue() >= hue - hueS.getValue() && color.getHue() <= hue + hueS.getValue() &&
                        pixelReader.getColor(x, y).getBrightness() >= brightS.getValue() &&
                        pixelReader.getColor(x, y).getSaturation() >= satS.getValue() &&
                        pixelReader.getColor(x, y).getBrightness() <= maxBrightS.getValue() &&
                        pixelReader.getColor(x, y).getSaturation() <= maxSatS.getValue() &&
                        imageArray[(y*width)+x]<=0) {//change pixels that don't match to black, ones that do to white
                    image.getPixelWriter().setColor(x, y, Color.WHITE);
                    tempArray[((y) * (width)) + x] = -1;
                } else {
                    image.getPixelWriter().setColor(x, y, Color.BLACK);
                    tempArray[((y) * (width)) + x] = 0;
                }
            }
        }
        pillToBeConfirmed = true;
        bAndWView.setImage(image);
    }

    public void confirmPill() {
        if (!name.getText().isEmpty()) {
            int size = 0;
            int size1=0;
            boolean validSize = true;
            try {
                size = Integer.parseInt(minSize.getText());//checking if min size entered is valid
            } catch (NumberFormatException e) {
                validSize = false;
            }
            try {
                size1 = Integer.parseInt(maxSize.getText());//checking if max size entered is valid
            }catch (NumberFormatException e) {
                validSize = false;
            }

            boolean uniqueName = true;
            for (PillDetails p : pills.values()) {
                if (p.getName().equalsIgnoreCase(name.getText())) {//checking if name is unique
                    uniqueName = false;
                    break;
                }
            }

            if (pillToBeConfirmed && uniqueName && validSize && size > 0) {
                for (int y = 0; y < height; y++) {//union adjacent white pixels
                    for (int x = 0; x < width; x++) {
                        int currentPixel = tempArray[((y) * (width)) + x];
                        int currentIndex = ((y) * (width)) + x;
                        if (currentPixel == -1) {
                            tempArray[currentIndex] = uf.find(currentIndex);//setting each pixel to the root
                            if (x + 1 < width && tempArray[((y) * (width)) + x + 1] == -1) {// check if the pixel to the right is white
                                int rightIndex = ((y) * (width)) + x + 1;
                                uf.union(currentIndex, rightIndex);
                            }
                            if (y + 1 < height && tempArray[((y + 1) * (width)) + x] == -1) {// check if the pixel below is white
                                int belowIndex = (y + 1) * width + x;
                                uf.union(currentIndex, belowIndex);
                            }
                        }
                    }
                }
                int pillNumber = 1;//for counting capsules
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {//numbering pills from left to right
                        int root = tempArray[(y) * width + x];
                        if (!pills.containsKey(root)) {
                            PillDetails pillToAdd = new PillDetails(name.getText(), pillNumber, uf.getNodeSizes()[root], root, currentPillColor);
                            if (pillToAdd.getSize() >= size && pillToAdd.getSize() <= size1 && pillToAdd.getSize()>1) {
                                pills.put(root, pillToAdd);
                                pillNumber++;
                            } else {
                                uf.getNodeSizes()[(y) * width + x] = 1;//setting size back to one
                                uf.getParent()[(y) * width + x] = (y) * width + x;//setting parent back to itself
                            }
                        } else pills.get(root).getIndices().add((y) * width + x);
                    }
                }
                bAndWView.setImage(null);
                pillToBeConfirmed = false;//resetting fields for another pill click
                imageArray = new int[width * height];//re-instantiating imageArray
                for (PillDetails p : pills.values())
                    for (int i : p.getIndices())
                        imageArray[i] = p.getRoot(); //refilling array with roots
            }
        }
        refreshSysDetails();
    }

    public void drawRectanglesOverSelectedPill(int x, int y) {
        imageView.setImage(null);
        Image image = original;
        Canvas canvas = new Canvas(width, height);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(image, 0, 0, width, height);

        PillDetails pill = pills.get(imageArray[(y * width) + x]);
        for (PillDetails p : pills.values()) {//adding all pills of the type that was clicked on to pillsToBeRectangled
            if (p.getName().equalsIgnoreCase(pill.getName()) || rootsSelected.contains(p.getRoot())) {
                ArrayList<Integer> xValues = new ArrayList<>();
                ArrayList<Integer> yValues = new ArrayList<>();
                for (int i : p.getIndices()) {
                    xValues.add(i % width);//getting x value from index
                    yValues.add((i - (i % width)) / width);//getting y value from index
                }
                Collections.sort(xValues);
                Collections.sort(yValues);//sort from lowest to highest

                int originX = xValues.get(0);
                int originY = yValues.get(0);//top left of rect
                int X1 = xValues.get(xValues.size() - 1);
                int Y1 = yValues.get(yValues.size() - 1);//bottom right of rect
                int widthOfRect = X1 - originX;//calculating rectangle parameters
                int heightOfRect = Y1 - originY;
                if (!rootsSelected.contains(p.getRoot())) rootsSelected.add(p.getRoot());

                graphicsContext.setFill(Color.TRANSPARENT);
                graphicsContext.setStroke(Color.WHITE);
                graphicsContext.setLineWidth(3);
                graphicsContext.strokeRect(originX, originY, widthOfRect, heightOfRect);//draw rectangle
            }
        }

        rectImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());//storing base rectangle image without numbers
        canvas.snapshot(null, rectImage);

        Canvas canvas1 = new Canvas(width, height);//new canvas for labelling pills on screen
        GraphicsContext graphicsContext1 = canvas1.getGraphicsContext2D();
        graphicsContext1.drawImage(rectImage, 0, 0, width, height);

        Collections.sort(rootsSelected);//sorts roots from lowest to highest
        int capsuleNumber = 1;

        for (int r : rootsSelected){
            int xOfRoot = r % width;
            int yOfRoot = (r-xOfRoot)/width;
            graphicsContext1.setFill(Color.WHITE);
            graphicsContext1.setFont(Font.font(35));
            graphicsContext1.fillText(String.valueOf(capsuleNumber++), xOfRoot, yOfRoot+50);//numbering pills
        }

        WritableImage numImage = new WritableImage((int) canvas1.getWidth(), (int) canvas1.getHeight());
        canvas1.snapshot(null,numImage);
        imageView.setImage(numImage);//setting image of rectangles and numbers
    }
    @FXML
    public void clearRectangles(){
        rootsSelected.clear();
        imageView.setImage(original);
    }

    public void imageViewToolTip(MouseEvent e, int x, int y) {
        if (imageArray != null && imageArray[(y * width) + x] > 0) //checking mouse clicked on a pill in the system
            tooltip = new Tooltip("Name: " + pills.get(imageArray[(y * width) + x]).getName() + '\n' +
                    "Estimate Size (pixels): " + pills.get(imageArray[(y * width) + x]).getSize() + '\n' +
                    "Capsule Number: " + pills.get(imageArray[(y * width) + x]).getCapsuleNumber());
        tooltip.show(imageView, e.getScreenX(),e.getScreenY());
    }
    @FXML
    private void removeToolTip() {
        if (tooltip!=null) tooltip.hide();//hide after mouse moved
    }

    public Image bAndWViewImage() {
        WritableImage image = new WritableImage(width, height);
        for (int y = 0; y < height; y++) {//change pixels that don't match to black, ones that do to white
            for (int x = 0; x < width; x++) {
                if (imageArray[(y) * width + x] > 0) image.getPixelWriter().setColor(x, y, Color.WHITE);
                else image.getPixelWriter().setColor(x, y, Color.BLACK);
            }
        }
        return image;
    }
    public Image colorImage() {
        WritableImage image = new WritableImage(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imageArray[(y) * width + x] != 0) {
                    image.getPixelWriter().setColor(x, y, pills.get(imageArray[(y) * width + x]).getColor());
                } else image.getPixelWriter().setColor(x, y, Color.BLACK);
            }
        }
        return image;
    }
    public Image randomColorImage() {
        int[][] colours = new int[pills.size()][3];//3 rgb values per pill
        WritableImage image = new WritableImage(width, height);
        for (int i = 0; i < pills.size(); i++) {//assigning random values between 0-255 for rgb
            Random random = new Random();
            colours[i][0] = random.nextInt(256);
            colours[i][1] = random.nextInt(256);
            colours[i][2] = random.nextInt(256);
        }
        ArrayList<Integer> roots = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imageArray[(y) * width + x] > 0) {
                    int currentRoot = imageArray[(y) * width + x];
                    if (!roots.contains(currentRoot)) {//adding the root to the list of roots
                        roots.add(currentRoot);
                    }
                    int index = roots.indexOf(currentRoot);//using index to keep the same colour across the whole pill
                    Color color = Color.rgb(colours[index][0], colours[index][1], colours[index][2]);
                    image.getPixelWriter().setColor(x, y, color);
                } else image.getPixelWriter().setColor(x, y, Color.BLACK);
            }
        }
        return image;
    }
    @FXML
    public void randomColorOption() {
        clearViews();
        ViewController.viewController.mainView.setImage(randomColorImage());
        HelloApplication.mainStage.setScene(HelloApplication.viewS);
    }
    @FXML
    public void colorOption() {
        clearViews();
        ViewController.viewController.mainView.setImage(colorImage());
        HelloApplication.mainStage.setScene(HelloApplication.viewS);
    }
    @FXML
    public void bAndWViewOption() {
        clearViews();
        ViewController.viewController.mainView.setImage(bAndWViewImage());
        HelloApplication.mainStage.setScene(HelloApplication.viewS);
    }
    @FXML
    public void allViewsOption() {
        clearViews();
        ViewController.viewController.mainView.setImage(original);
        ViewController.viewController.bAndWView.setImage(bAndWViewImage());
        ViewController.viewController.colorView.setImage(colorImage());
        ViewController.viewController.randColorView.setImage(randomColorImage());
        HelloApplication.mainStage.setScene(HelloApplication.viewS);
    }
    public void clearViews(){
        ViewController.viewController.mainView.setImage(null);
        ViewController.viewController.bAndWView.setImage(null);
        ViewController.viewController.colorView.setImage(null);
        ViewController.viewController.randColorView.setImage(null);
    }

    @FXML
    public void twoToneScene(){
        TwoToneController.twoToneController.getImageView().setImage(original);
        HelloApplication.mainStage.setScene(HelloApplication.twoToneS);
    }

    @FXML
    public void cancel(){
        for (PillDetails i : pills.values())
            System.out.println(i);
        pillToBeConfirmed=false;
        bAndWView.setImage(null);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainController=this;
    }
}
