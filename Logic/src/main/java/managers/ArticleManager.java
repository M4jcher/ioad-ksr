package managers;

import articles.Article;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class ArticleManager {
    final private String[] countries = {"west-germany", "usa", "france", "uk", "canada", "japan"};
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArticleManager{");
        sb.append("allArticles=").append(allArticles);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        ArticleManager articleManager = new ArticleManager();
        articleManager.readArticles("D:/pulpit/studia/semestr6/KomputeroweSystemyRozpoznawania" +
                "/Projekt1/mavenProject/Logic/exit.txt");
        System.out.println(articleManager.toString());
    }
}