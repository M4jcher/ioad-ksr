package traits;

import java.util.ArrayList;
import java.util.List;

public class WordTrait extends Trait {
    private String value;

    public WordTrait(String value) {
        this.value = value;
    }

    public double getDistanceTo(Trait second) {
        WordTrait trait = (WordTrait) second;
        String firstStr = this.value;
        String secondStr = trait.value;
        int len;
        String shortStr, longStr;
        if (firstStr.equals(secondStr)) {
            return 0.0;
        } else if (firstStr.isEmpty() || secondStr.isEmpty()) {
            return 1.0;
        }
        if (firstStr.length() <= secondStr.length()) {
            shortStr = firstStr;
            longStr = secondStr;
        } else {
            shortStr = secondStr;
            longStr = firstStr;
        }
        len = Math.min(shortStr.length(), 3);
        List<String> ngrams = ngrams(len,shortStr);
        double sum = 0.0;
        for (String ngram : ngrams) {
            if (longStr.contains(ngram)) {
                sum++;
            }
        }
        double closeness = sum / ((longStr.length() - len) + 1);
        return 1 - closeness;
    }

    private List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i < str.length() - n + 1; i++) {
            ngrams.add(str.substring(i, i + n));
        }
        return ngrams;
    }

//    public double getDistanceTo(Trait second) {
//        WordTrait trait = (WordTrait) second;
//        return this.value.compareTo(trait.value);
//    }

    @Override
    public String toString() {
        return value;
    }
}
