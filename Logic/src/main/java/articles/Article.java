package articles;

import traits.Trait;

import java.util.Arrays;

public class Article {
    private Trait[] traits;
    private String trueValue;
    private String predictedValue;

    public Article(String trueValue, Trait[] traits) {
        this.trueValue = trueValue;
        this.traits = traits;
    }

    public String getTrueValue() {
        return trueValue;
    }

    public String getPredictedValue() {
        return predictedValue;
    }

    public void setPredictedValue(String predictedValue) {
        this.predictedValue = predictedValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Article{");
        sb.append(", trueValue='").append(trueValue).append('\'');
        sb.append(", predictedValue='").append(predictedValue).append('\'');
        sb.append("traits=").append(Arrays.toString(traits));
        sb.append("\n");
        return sb.toString();
    }

    double getTaxiDistance(Article article) {
        double sum = 0;
        for (int i = 0; i < traits.length; i++) {
            sum += Math.abs(this.traits[i].getDistanceTo(article.traits[i]));
        }
        return sum;
    }

    double getEuclidesDistance(Article article) {
        double sum = 0;
        for (int i = 0; i < traits.length; i++) {
            double dist = Math.abs(this.traits[i].getDistanceTo(article.traits[i]));
            sum += dist * dist;
        }
        sum = Math.sqrt(sum);
        return sum;
    }

    double getChebyshevDistance(Article article) {
        double max = 0;
        for (int i = 0; i < traits.length; i++) {
            double tmp;
            tmp = Math.abs(this.traits[i].getDistanceTo(article.traits[i]));
            if (tmp > max) {
                max = tmp;
            }
        }
        return max;
    }
}
