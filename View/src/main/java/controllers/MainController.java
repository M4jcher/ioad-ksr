package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import managers.ArticleManager;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;


public class MainController {
    ArticleManager articleManager;
    FileChooser fileChooser;
    @FXML
    public Text accuracyText;
    public ChoiceBox metricListChoice;
    public GridPane confusionMatrixGrid;
    public TextField kNeighboursInput;
    public CheckBox importantPeopleCheckBox;
    public CheckBox citiesCheckBox;
    public CheckBox continentsCheckBox;
    public CheckBox measuresCheckBox;
    public CheckBox currencyCheckBox;
    public CheckBox firmsCheckBox;
    public CheckBox countryCheckBox;
    public CheckBox buildingCheckBox;
    public CheckBox usaCheckBox;
    public CheckBox ukCheckBox;
    public CheckBox canadaCheckBox;
    public CheckBox germanyCheckBox;
    public CheckBox franceCheckBox;
    public CheckBox japanCheckBox;
    public CheckBox textLenCheckBox;
    public GridPane measuresGrid;
    public TextField splitFactor;
    public TextField loadArticlesPathInput;
    public Button runClassificationButton;
    public Button loadArticlesButton;
    public VBox traitsList;

    @FXML
    public void initialize() {
        prepareWindow();
    }

    public void prepareWindow() {
        articleManager = new ArticleManager();
        String[] countries = articleManager.getCountries();
        for (int i = 0; i < countries.length; i++) {
            if (countries[i].equals("west-germany")) {
                countries[i] = "germany";
            }
            Text tmp = new Text(countries[i]);
            confusionMatrixGrid.add(tmp, 0, i + 1);
            GridPane.setHalignment(tmp, HPos.CENTER);
            GridPane.setValignment(tmp, VPos.CENTER);

            Text tmp2 = new Text(countries[i]);
            confusionMatrixGrid.add(tmp2, i + 1, 0);
            GridPane.setHalignment(tmp2, HPos.CENTER);
            GridPane.setValignment(tmp2, VPos.CENTER);

            Text tmp3 = new Text(countries[i]);
            measuresGrid.add(tmp3,0, i+1);
            GridPane.setHalignment(tmp3, HPos.CENTER);
            GridPane.setValignment(tmp3, VPos.CENTER);
        }
        LinkedList<String> metrics = new LinkedList<>();
        metrics.add("Euklidesowa");
        metrics.add("Taksówkowa");
        metrics.add("Czebyszewa");
        metricListChoice.setItems(FXCollections.observableList(metrics));
        metricListChoice.setValue("Euklidesowa");
    }

    public void loadArticles() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik z artykułami");
        File file = fileChooser.showOpenDialog(null);
        loadArticlesPathInput.setText(file.getPath());
        try {
            articleManager.readArticles(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runClassification() {
        int k = Integer.parseInt(kNeighboursInput.getText());
        int splitPrc = Integer.parseInt(splitFactor.getText());
        int metricNumber = metricListChoice.getSelectionModel().getSelectedIndex();
        articleManager.splitDataToTrainAndTest(splitPrc);
        boolean[] traitsUsed = new boolean[15];
        for (int i = 0; i < 15; i++) {
            CheckBox tmp = (CheckBox) traitsList.getChildren().get(i);
            traitsUsed[i] = tmp.isSelected();
        }
        articleManager.kNN(k, traitsUsed, metricNumber);
        articleManager.calculateMeasures();
        updateConfusionMatrix();
        updateMeasures();
    }

    public void updateConfusionMatrix() {
        int amount = articleManager.getCountries().length;
        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                int tmp = articleManager.getConfusionMatrix(i,j);
                Text text = new Text(String.valueOf(tmp));
                confusionMatrixGrid.add(text,i+1,j+1);
                GridPane.setHalignment(text, HPos.CENTER);
                GridPane.setValignment(text, VPos.CENTER);
            }
        }
    }

    public void updateMeasures() {
        accuracyText.setText(String.format("%.4g%n",articleManager.getAccuracy()));
        accuracyText.setTextAlignment(TextAlignment.CENTER);
        int amount = articleManager.getCountries().length;
        double[][] measures = articleManager.getMeasures();
        for (int i = 0; i < amount+1; i++) {
            for (int j = 0; j < 3; j++) {
                double value = measures[i][j];
                String tmp = String.format("%.4g%n", value);
                Text text = new Text();
                measuresGrid.add(text,j+1,i+1);
                GridPane.setHalignment(text, HPos.CENTER);
                GridPane.setValignment(text, VPos.CENTER);
                text.setText(tmp);
            }
        }
    }
}
