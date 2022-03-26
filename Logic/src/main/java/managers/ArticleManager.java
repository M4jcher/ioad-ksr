package managers;

import articles.Article;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ArticleManager {
    final private String[] countries = {"usa", "uk", "canada", "west-germany", "france", "japan"};
    private LinkedList<Article> allArticles;
    //private LinkedList<Article>[][] confusionMatrix;
    private int[][] confusionMatrix;
    private LinkedList<Article> trainData;
    private LinkedList<Article> testData;
    private double[][] measures;
    private double accuracy;

    public ArticleManager() {
//        confusionMatrix = new LinkedList[countries.length][];
        confusionMatrix = new int[countries.length][];
        measures = new double[countries.length+1][];
        for (int i = 0; i < countries.length; i++) {
//            confusionMatrix[i] = new LinkedList[countries.length];
            confusionMatrix[i] = new int[countries.length];
            measures[i] = new double[3];
//            for (int j = 0; j < countries.length; j++) {
//                confusionMatrix[i][j] = new LinkedList<>();
//            }
        }
        trainData = new LinkedList<>();
        testData = new LinkedList<>();
        allArticles = new LinkedList<>();
    }

    public void readArticles(File file) throws IOException {
        String start = "<REUTERS";
        String end = "</REUTERS";
        String placesStart = "<PLACES>";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder article = new StringBuilder();
        String trueValue = "None";
        String tmp = reader.readLine();
        while (tmp != null) {
            if (tmp.contains(start)) {
                article = new StringBuilder();
            }
            if (tmp.contains(placesStart)) {
                for (int i = 0; i < 6; i++) {
                    if (tmp.contains(countries[i])) {
                        trueValue = countries[i];
                        break;
                    }
                }
                tmp = reader.readLine();
                continue;
            }
            article.append(tmp).append("\n");
            if (tmp.contains(end)) {
                allArticles.add(new Article(trueValue, TraitManager.extractTraits(article.toString())));
                article = new StringBuilder();
            }
            tmp = reader.readLine();
        }
    }

    public void kNN(int k, boolean[] traitsUsed, int metricUsed) {
        for (Article testDatum : testData) {
            Map<Integer, Double> distances = new HashMap<>();
            for (int j = 0; j < trainData.size(); j++) {
                double dist = testDatum.getDistanceTo(trainData.get(j), traitsUsed, metricUsed);
                distances.put(j,dist);
            }
            List<Map.Entry<Integer, Double>> nearest = distances.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(k).toList();

            int[] numOfNeighbours = new int[countries.length];
            for (int j = 0; j < k; j++) {
                for (int l = 0; l < countries.length; l++) {
                    if (trainData.get(nearest.get(j).getKey()).getTrueValue().equals(countries[l])) {
                        numOfNeighbours[l] += 1;
                        break;
                    }
                }
            }
            int max = 0;
            int index = -1;
            for (int j = 0; j < countries.length; j++) {
                if (numOfNeighbours[j] > max) {
                    max = numOfNeighbours[j];
                    index = j;
                }
            }
            testDatum.setPredictedValue(countries[index]);
            putIntoConfusionMatrix(testDatum);
        }

    }

    private void putIntoConfusionMatrix(Article article) {
        int indexTrue = -1, indexPredicted = -1;
        for (int i = 0; i < 6; i++) {
            if (article.getTrueValue().equals(countries[i])) {
                indexTrue = i;
            }
            if (article.getPredictedValue().equals(countries[i])) {
                indexPredicted = i;
            }
        }
        confusionMatrix[indexTrue][indexPredicted]++;
    }

    public void splitDataToTrainAndTest(int trainPct) {
        testData.addAll(allArticles);
        Random random = new Random();
        int amount = allArticles.size() * trainPct / 100;
        for (int i = 0; i < amount; i++) {
            int index = random.nextInt(testData.size());
            trainData.add(testData.remove(index));
        }
    }

    public void calculateMeasures() {
        this.accuracy = calculateAccuracy();
        calculatePrecision();
        calculateRecall();
        calculateF1();
        calculateForAll();
    }

    private double calculateAccuracy() {
        int sum = 0;
        for (int i = 0; i < countries.length; i++) {
//            sum += confusionMatrix[i][i].size();
            sum += confusionMatrix[i][i];
        }
        return sum * 1.0 / allArticles.size();
    }

    private void calculatePrecision() {
        for (int i = 0; i < countries.length; i++) {
            int sum = 0;
            for (int j = 0; j < countries.length; j++) {
//                sum += confusionMatrix[i][j].size();
                sum += confusionMatrix[i][j];
            }
//            measures[i][0] = confusionMatrix[i][i].size() * 1.0 / sum;
            measures[i][0] = confusionMatrix[i][i] * 1.0 / sum;
        }
    }

    private void calculateRecall() {
        for (int i = 0; i < countries.length; i++) {
            int sum = 0;
            for (int j = 0; j < countries.length; j++) {
//                sum += confusionMatrix[j][i].size();
                sum += confusionMatrix[j][i];
            }
//            measures[i][1] = confusionMatrix[i][i].size() * 1.0 / sum;
            measures[i][1] = confusionMatrix[i][i] * 1.0 / sum;
        }
    }

    private void calculateF1() {
        for (int i = 0; i < countries.length; i++) {
            measures[i][2] = (2 * measures[i][0] * measures[i][1]) / (measures[i][0] + measures[i][1]);
        }
    }

    private void calculateForAll() {
        measures[countries.length] = new double[3];
        for (int i = 0; i < 3; i++) {
            int sumWeight = 0;
            double sumMeasure = 0;
            for (int j = 0; j < countries.length; j++) {
//                sumWeight += confusionMatrix[j][j].size();
//                sumMeasure += measures[j][i] * confusionMatrix[j][j].size();
                sumWeight += confusionMatrix[j][j];
                sumMeasure += measures[j][i] * confusionMatrix[j][j];
            }
            measures[countries.length][i] = sumMeasure / sumWeight;
        }
    }

    public String[] getCountries() {
        return countries;
    }

    public int getConfusionMatrix(int x, int y) {
        return confusionMatrix[x][y];
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double[][] getMeasures() {
        return measures;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("countries=").append(Arrays.toString(countries)).append("\n");
        for (int i = 0; i < countries.length; i++) {
            for (int j = 0; j < countries.length; j++) {
//                sb.append(confusionMatrix[i][j].size()).append("\t\t");
                sb.append(confusionMatrix[i][j]).append("\t\t");
            }
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("Precision\t\tRecall\t\tF1\t\t\n");
        for (int i = 0; i < countries.length; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(measures[i][j]).append("\t\t");
            }
            sb.append("\n");
        }
        sb.append("for whole project:").append("\n");
        for (int i = 0; i < 3; i++) {
            sb.append(measures[countries.length][i]).append("\t\t");
        }
        sb.append("\n");
        sb.append("Accuracy = ").append(this.accuracy).append("\n");
        return sb.toString();
    }
}