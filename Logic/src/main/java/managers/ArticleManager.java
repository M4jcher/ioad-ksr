package managers;

import articles.Article;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ArticleManager {
    final private String[] countries = {"usa", "uk", "canada", "west-germany", "france", "japan"};
    private LinkedList<Article> allArticles;
    private LinkedList<Article>[][] confusionMatrix;
    private LinkedList<Article> trainData;
    private LinkedList<Article> testData;

    public ArticleManager() {
        confusionMatrix = new LinkedList[6][];
        for (int i = 0; i < 6; i++) {
            confusionMatrix[i] = new LinkedList[6];
            for (int j = 0; j < 6; j++) {
                confusionMatrix[i][j] = new LinkedList<Article>();
            }
        }
        trainData = new LinkedList<Article>();
        testData = new LinkedList<Article>();
        allArticles = new LinkedList<Article>();
    }

    public void readArticles(String filename) throws IOException {
        String start = "<REUTERS";
        String end = "</REUTERS";
        String placesStart = "<PLACES>";
        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
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
        for (int i = 0; i < testData.size(); i++) {
            Map<Integer, Double> nearest = new HashMap<>();
            ArrayList<Double> distances = new ArrayList<>();
            for (int j = 0; j < trainData.size(); j++) {
                double dist = testData.get(i).getDistanceTo(trainData.get(j), traitsUsed, metricUsed);
                if (nearest.size() < k) {
                    nearest.put(j, dist);
                    distances.add(dist);
                    Collections.sort(distances);
                } else if (distances.get(k - 1) > dist) {
                    nearest.values().remove(distances.remove(k - 1));
                    nearest.put(j, dist);
                    distances.add(dist);
                    Collections.sort(distances);
                }
            }
            int[] numOfNeighbours = new int[countries.length];
            Integer[] keys = nearest.keySet().toArray(new Integer[k]);
            for (int j = 0; j < k; j++) {
                for (int l = 0; l < countries.length; l++) {
                    if (trainData.get(keys[j]).getTrueValue().equals(countries[l])) {
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
            testData.get(i).setPredictedValue(countries[index]);
            putIntoConfusionMatrix(testData.get(i));
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
        confusionMatrix[indexTrue][indexPredicted].add(article);
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArticleManager{");
        sb.append("countries=").append(Arrays.toString(countries)).append("\n");
        for (int i = 0; i < countries.length; i++) {
            for (int j = 0; j < countries.length; j++) {
                sb.append(confusionMatrix[i][j].size()).append("\t\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        ArticleManager articleManager = new ArticleManager();
        articleManager.readArticles("D:\\pulpit\\studia\\semestr6\\KomputeroweSystemyRozpoznawania\\Projekt1\\mavenProject\\Logic\\src\\main\\resources\\exit.txt");
        articleManager.splitDataToTrainAndTest(20);
        boolean[] traitsUsed = new boolean[15];
        Arrays.fill(traitsUsed, true);
        articleManager.kNN(3, traitsUsed, 0);
        System.out.println(articleManager.toString());
    }
}