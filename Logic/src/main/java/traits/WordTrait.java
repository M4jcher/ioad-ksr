package traits;

import java.util.ArrayList;
import java.util.List;

public class WordTrait extends Trait {
    private String value;

    public WordTrait(String value) {
        this.value = value;
    }

//    public double getDistanceTo(Trait second) {
//        WordTrait trait = (WordTrait) second;
//        String firstStr = this.value;
//        String secondStr = trait.value;
//        int len = 0;
//        String str, str2;
//        if (firstStr.equals(secondStr)) {
//            return 10;
//        }
//        if (firstStr.length() <= secondStr.length()) {
//            str = firstStr;
//            str2 = secondStr;
//            if (firstStr.length() > 3){
//                len = 3;
//            } else {
//                len = firstStr.length();
//            }
//        } else {
//            str = secondStr;
//            str2 = firstStr;
//            if (secondStr.length() > 3){
//                len = 3;
//            } else {
//                len = secondStr.length();
//            }
//        }
//        List<String> ngrams = ngrams(len,str);
//        int sum = 0;
//        for (int i = 0; i < ngrams.size(); i++) {
//            if (str2.contains(ngrams.get(i))) {
//                sum++;
//            }
//        }
//        return (1 - (sum * 1.0 / ngrams.size())) * 10.0;
//    }

    private List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i < str.length() - n + 1; i++) {
            ngrams.add(str.substring(i, i + n));
        }
        return ngrams;
    }

    public double getDistanceTo(Trait second) {
        WordTrait trait = (WordTrait) second;
        return this.value.compareTo(trait.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
