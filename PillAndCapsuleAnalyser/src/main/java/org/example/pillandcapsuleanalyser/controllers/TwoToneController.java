package org.example.pillandcapsuleanalyser.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.example.pillandcapsuleanalyser.HelloApplication;
import org.example.pillandcapsuleanalyser.models.PillDetails;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TwoToneController implements Initializable {

    public static TwoToneController twoToneController;
    private int[] twoToneTempArray;
    @FXML
    private ImageView imageView, bAndWView,colorView1,colorView2;
    public ImageView getImageView() {
        return imageView;
    }
    @FXML
    private Slider hueS, satS, brightS,maxSatS, maxBrightS;
    @FXML
    private TextField name, minSize, maxSize;
    private boolean colorToBeConfirmed1,colorToBeConfirmed2,confirmed1,confirmed2 = false;
    private Color color1,color2;

    @FXML
    public void openFileMenu() {
        HelloApplication.mainStage.setScene(HelloApplication.mainS);
        MainController.mainController.openFileMenu();
    }
    @FXML
    public void bAndWViewOption() {
        MainController.mainController.bAndWViewOption();
    }
    @FXML
    public void colorOption() {
        MainController.mainController.randomColorOption();
    }
    @FXML
    public void randomColorOption(){
        MainController.mainController.randomColorOption();
    }
    @FXML
    public void pillClick(MouseEvent e) {
        double xInVBox = e.getX();
        double yInVBox = e.getY();

        if (xInVBox >= 0 && xInVBox <= imageView.getFitWidth() &&
                yInVBox >= 0 && yInVBox <= imageView.getFitHeight()) { // check if the click is within the imageview

            double ratio = MainController.mainController.getWidth() / imageView.getFitWidth();//getting ratio of image:imageview
            int xOfImage = (int) (xInVBox * ratio);
            int yOfImage = (int) (yInVBox * ratio);
            System.out.println(xOfImage + " " + yOfImage);
            if (!confirmed1 && !confirmed2) {//checking if first color has been processed yet
                if (MainController.mainController.getImageArray()[((yOfImage) * (MainController.mainController.getWidth())) + xOfImage] <= 0) {
                    color1 = MainController.mainController.getPixelReader().getColor(xOfImage, yOfImage);
                    blackAndWhiteClick1(color1);
                }
                System.out.println(xOfImage + " " + yOfImage);
            } else {//checking if second color has been processed yet
                if (MainController.mainController.getImageArray()[((yOfImage) * (MainController.mainController.getWidth())) + xOfImage] <= 0
                        && twoToneTempArray[((yOfImage) * (MainController.mainController.getWidth())) + xOfImage] <= 0) {
                    color2 = MainController.mainController.getPixelReader().getColor(xOfImage, yOfImage);
                    blackAndWhiteClick2(color2);
                }
            }
        }
    }
    public void blackAndWhiteClick1(Color color) {
        WritableImage image = new WritableImage(MainController.mainController.getWidth(), MainController.mainController.getHeight());
        twoToneTempArray = new int[MainController.mainController.getHeight() * MainController.mainController.getWidth()];
        for (int y = 0; y < MainController.mainController.getHeight(); y++) {
            for (int x = 0; x < MainController.mainController.getWidth(); x++) {
                double hue = MainController.mainController.getPixelReader().getColor(x, y).getHue();
                if (color.getHue() >= hue - hueS.getValue() && color.getHue() <= hue + hueS.getValue() &&
                        MainController.mainController.getPixelReader().getColor(x, y).getBrightness() >= brightS.getValue() &&
                        MainController.mainController.getPixelReader().getColor(x, y).getSaturation() >= satS.getValue()&&
                        MainController.mainController.getPixelReader().getColor(x, y).getBrightness() <= maxBrightS.getValue()&&
                        MainController.mainController.getPixelReader().getColor(x, y).getSaturation() <= maxSatS.getValue()&&
                        MainController.mainController.getImageArray()[(y*MainController.mainController.getWidth())+x]<=0) {//change pixels that don't match to black, ones that do to white
                    image.getPixelWriter().setColor(x, y, Color.WHITE);
                    twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x] = -2;
                } else {
                    image.getPixelWriter().setColor(x, y, Color.BLACK);
                    twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x] = 0;
                }
            }
        }
        WritableImage colorImage = new WritableImage(100,100);
        for (int y = 0; y < colorImage.getHeight(); y++)
            for (int x = 0; x < colorImage.getWidth(); x++)
                colorImage.getPixelWriter().setColor(x,y,color);

        color1=color;
        colorView1.setImage(colorImage);
        colorToBeConfirmed1 = true;
        bAndWView.setImage(image);
    }
    public void blackAndWhiteClick2(Color color) {
        bAndWView.setImage(null);
        WritableImage image = new WritableImage(MainController.mainController.getWidth(), MainController.mainController.getHeight());
        for (int y = 0; y < MainController.mainController.getHeight(); y++) {
            for (int x = 0; x < MainController.mainController.getWidth(); x++) {
                if (twoToneTempArray[(y * MainController.mainController.getWidth()) + x] == -1 ||
                        twoToneTempArray[(y * MainController.mainController.getWidth()) + x] == 0) {
                    twoToneTempArray[(y * MainController.mainController.getWidth()) + x] = 0;//resetting for adjustment clicks
                    image.getPixelWriter().setColor(x,y,Color.BLACK);
                }
                else if (twoToneTempArray[(y * MainController.mainController.getWidth()) + x] == -2)image.getPixelWriter().setColor(x,y,Color.WHITE);
            }
        }

        for (int y = 0; y < MainController.mainController.getHeight(); y++) {
            for (int x = 0; x < MainController.mainController.getWidth(); x++) {
                double hue = MainController.mainController.getPixelReader().getColor(x, y).getHue();
                if (color.getHue() >= hue - hueS.getValue() && color.getHue() <= hue + hueS.getValue() &&
                        MainController.mainController.getPixelReader().getColor(x, y).getBrightness() >= brightS.getValue() &&
                        MainController.mainController.getPixelReader().getColor(x, y).getSaturation() >= satS.getValue()&&
                        MainController.mainController.getImageArray()[(y*MainController.mainController.getWidth())+x]<=0 &&
                        (twoToneTempArray[(y*MainController.mainController.getWidth())+x]==0 || twoToneTempArray[(y*MainController.mainController.getWidth())+x]==-2)){//change pixels that don't match to black, ones that do to white
                    image.getPixelWriter().setColor(x, y, Color.WHITE);
                    twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x] = -1;//different value for second color
                } else if (!(MainController.mainController.getImageArray()[(y*MainController.mainController.getWidth())+x]<=0 ||
                        twoToneTempArray[(y*MainController.mainController.getWidth())+x]<=0)){
                    image.getPixelWriter().setColor(x, y, Color.BLACK);
                    twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x] = 0;
                }
            }
        }
        WritableImage colorImage = new WritableImage(100,100);
        for (int y = 0; y < colorImage.getHeight(); y++)
            for (int x = 0; x < colorImage.getWidth(); x++)
                colorImage.getPixelWriter().setColor(x,y,color);

        color2=color;
        colorView2.setImage(colorImage);
        colorToBeConfirmed2 = true;
        bAndWView.setImage(image);
    }

    @FXML
    public void confirmPill() {
        if (colorToBeConfirmed1){
            bAndWView.setImage(null);
            confirmed1=true;
            colorToBeConfirmed1 = false;
        } else if (colorToBeConfirmed2) {
            if (!name.getText().isEmpty()) {
                int size = 0;
                int size1 = 0;
                boolean validSize = true;
                try {
                    size = Integer.parseInt(minSize.getText());//checking if size entered is valid
                } catch (NumberFormatException e) {
                    validSize = false;
                }
                try {
                    size1 = Integer.parseInt(maxSize.getText());//checking if max size entered is valid
                }catch (NumberFormatException e) {
                    validSize = false;
                }

                boolean uniqueName = true;
                for (PillDetails p : MainController.mainController.getPills().values()) {
                    if (p.getName().equalsIgnoreCase(name.getText())) {//checking if name is unique
                        uniqueName = false;
                        break;
                    }
                }
                ArrayList<Integer> twoToneUnionedIndices=new ArrayList<>();

                if (uniqueName && validSize && size > 0) {
                    for (int y = 0; y < MainController.mainController.getHeight(); y++) {//union adjacent white pixels for separate colors
                        for (int x = 0; x < MainController.mainController.getWidth(); x++) {
                            int currentPixel = twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x];
                            int currentIndex = ((y) * (MainController.mainController.getWidth())) + x;
                            if (currentPixel == -1) {
                                twoToneTempArray[currentIndex] = MainController.mainController.getUf().find(currentIndex);//setting each pixel to the root
                                if (x + 1 < MainController.mainController.getWidth() && (twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -1 ||
                                        twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -2)) {// check if the pixel to the right is white
                                    int rightIndex = ((y) * (MainController.mainController.getWidth())) + x + 1;
                                    MainController.mainController.getUf().union(currentIndex, rightIndex);
                                    if (twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -2 &&
                                            !twoToneUnionedIndices.contains(MainController.mainController.getUf().find(currentIndex)))
                                        twoToneUnionedIndices.add(MainController.mainController.getUf().find(currentIndex));
                                }
                                if (y + 1 < MainController.mainController.getHeight() && (twoToneTempArray[((y + 1) * (MainController.mainController.getWidth())) + x] == -1 ||
                                        twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -2)) {// check if the pixel to the right is white)
                                    int belowIndex = (y + 1) * MainController.mainController.getWidth() + x;
                                    MainController.mainController.getUf().union(currentIndex, belowIndex);
                                    if (twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -2 &&
                                            !twoToneUnionedIndices.contains(MainController.mainController.getUf().find(currentIndex)))
                                        twoToneUnionedIndices.add(MainController.mainController.getUf().find(currentIndex));
                                }
                            } else if (currentPixel == -2) {
                                twoToneTempArray[currentIndex] = MainController.mainController.getUf().find(currentIndex);//setting each pixel to the root
                                if (x + 1 < MainController.mainController.getWidth() && (twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -1 ||
                                        twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -2)) {// check if the pixel to the right is white
                                    int rightIndex = ((y) * (MainController.mainController.getWidth())) + x + 1;
                                    MainController.mainController.getUf().union(currentIndex, rightIndex);
                                    if (twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -1 &&
                                            !twoToneUnionedIndices.contains(MainController.mainController.getUf().find(currentIndex)))
                                        twoToneUnionedIndices.add(MainController.mainController.getUf().find(currentIndex));
                                }
                                if (y + 1 < MainController.mainController.getHeight() && (twoToneTempArray[((y + 1) * (MainController.mainController.getWidth())) + x] == -1 ||
                                        twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -2)) {// check if the pixel below is white
                                    int belowIndex = (y + 1) * MainController.mainController.getWidth() + x;
                                    MainController.mainController.getUf().union(currentIndex, belowIndex);
                                    if (twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x + 1] == -1 &&
                                            !twoToneUnionedIndices.contains(MainController.mainController.getUf().find(currentIndex)))
                                        twoToneUnionedIndices.add(MainController.mainController.getUf().find(currentIndex));
                                }
                            }
                        }
                    }
//                    for (int y = 0; y < MainController.mainController.getHeight(); y++) {//union adjacent white pixels of separate colors together
//                        for (int x = 0; x < MainController.mainController.getWidth(); x++) {
//                            int currentPixel = twoToneTempArray[((y) * (MainController.mainController.getWidth())) + x];
//                            int currentIndex = ((y) * (MainController.mainController.getWidth())) + x;
//                            if (currentPixel == -1) {
//                                twoToneTempArray[currentIndex] = MainController.mainController.getUf().find(currentIndex);//setting each pixel to the root
//                                if (x + 1 < MainController.mainController.getWidth() && twoToneTempArray[currentIndex + 1] == -2) {// check if the pixel to the right is white
//                                    int rightIndex = ((y) * (MainController.mainController.getWidth())) + x + 1;
//                                    MainController.mainController.getUf().union(currentIndex, rightIndex);
//                                    twoToneUnionedIndices.add(currentIndex);
//                                } else if (y + 1 < MainController.mainController.getHeight() && twoToneTempArray[((y + 1) * (MainController.mainController.getWidth())) + x] == -2) {// check if the pixel below is white
//                                    int belowIndex = (y + 1) * MainController.mainController.getWidth() + x;
//                                    MainController.mainController.getUf().union(currentIndex, belowIndex);
//                                    twoToneUnionedIndices.add(currentIndex);
//                                } else if (x - 1 > MainController.mainController.getWidth() && twoToneTempArray[currentIndex - 1] == -2) {// check if the pixel to the right is white
//                                    int rightIndex = ((y) * (MainController.mainController.getWidth())) + x + 1;
//                                    MainController.mainController.getUf().union(currentIndex, rightIndex);
//                                    twoToneUnionedIndices.add(currentIndex);
//                                } else if (y - 1 > MainController.mainController.getHeight() && twoToneTempArray[((y - 1) * (MainController.mainController.getWidth())) - x] == -2) {// check if the pixel below is white
//                                    int belowIndex = (y + 1) * MainController.mainController.getWidth() + x;
//                                    MainController.mainController.getUf().union(currentIndex, belowIndex);
//                                    twoToneUnionedIndices.add(currentIndex);
//                                }
//                            }
//                        }
//                    }
                    int pillNumber = 1;//for counting capsules
                    for (int y = 0; y < MainController.mainController.getHeight(); y++) {
                        for (int x = 0; x < MainController.mainController.getWidth(); x++) {//numbering pills from left to right
                            int root = twoToneTempArray[(y) * MainController.mainController.getWidth() + x];
                            if (!MainController.mainController.getPills().containsKey(root) || twoToneUnionedIndices.contains(root)) {
                                PillDetails pillToAdd = new PillDetails(name.getText(), pillNumber, MainController.mainController.getUf().getNodeSizes()[root], root, color1);
                                if (twoToneUnionedIndices.contains(pillToAdd.getRoot()) &&
                                        pillToAdd.getSize() >= size && pillToAdd.getSize() <= size1 && pillToAdd.getSize()>0) {//only adding sets that have been unioned with a different colour
                                    MainController.mainController.getPills().put(root, pillToAdd);
                                    pillNumber++;
                                } else {
                                    MainController.mainController.getUf().getNodeSizes()[(y) * MainController.mainController.getWidth() + x] = 1;//setting size back to one
                                    MainController.mainController.getUf().getParent()[(y) * MainController.mainController.getWidth() + x] = (y) * MainController.mainController.getWidth() + x;//setting parent back to itself
                                }
                            } else MainController.mainController.getPills().get(root).getIndices().add((y) * MainController.mainController.getWidth() + x);
                        }
                    }
                    MainController.mainController.setImageArray(new int[MainController.mainController.getWidth()* MainController.mainController.getHeight()]);//re-instantiating imageArray
                    for (PillDetails p : MainController.mainController.getPills().values())
                        for (int i : p.getIndices())
                            MainController.mainController.getImageArray()[i] = p.getRoot(); //refilling array with roots
                    cancel();
                }
            }
        }
    }
    @FXML
    public void cancel() {
        colorToBeConfirmed1=false;
        colorToBeConfirmed2=false;
        confirmed1=false;
        confirmed2=false;
        colorView1.setImage(null);
        colorView2.setImage(null);
        bAndWView.setImage(null);
        twoToneTempArray = new int[MainController.mainController.getHeight() * MainController.mainController.getWidth()];
    }
    @FXML
    public void goBack(){
        MainController.mainController.refreshSysDetails();
        HelloApplication.mainStage.setScene(HelloApplication.mainS);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        twoToneController=this;
    }
}
