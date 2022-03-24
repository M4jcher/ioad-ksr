package managers;

import traits.NumberTrait;
import traits.Trait;
import traits.WordTrait;

public class TraitManager {
    final static String[] people = {"Ken Cherney", "John Middleton", "Joe Clark", "Joe Riccardo",
            "Joe Carson", "Joel Graff", "Joe Plocek", "Joe Foster", "Joe Gold", "Joel Reed",
            "Joe Shaw", "Jack Norman", "Martin S. Ackerman", "Mary Anne Cossa", "Thomas C. Amendola",
            "Joel Yonover", "Karen Gibbs", "Freddie Mac", "Robert deRose", "Patrick Tyler",
            "Kiichi Miyazawa, Jennie Kantyka", "Yasuhiro Nakasone", "Jean Mantelet", "Roland Darneau",
            "De la Serre", "Jeremy Morse, Brian Pomeroy", "Robin Leigh-Pemberton", "Walt Disney", "Normal Paul",
            "Hudson's Bay", "Peter Miller", "Karl Otto Poehl", "Alfred Herrhausen",
            "Josef Strenger", "Gerhard Stoltenberg"};
    final static String[] regions = {"Tokyo", "Paris", "Lyon", "Toronto", "Ottawa", "New York", "Vancouver",
            "London", "Boston", "Frankfurt", "Bonn", "Leverkusen", "Berlin", "Wiesbaden",
            "Hanover", "Nuremberg", "Yokohama", "Montreal"};
    final static String[] continents = {"European", "Europe", "American", "America", "Asian", "Asia"};
    final static String[] currency = {"dlrs", "dlr", "$", "yen", "francs", "franc", "stg", "mark", "marks"};
    final static String[] units = {"ounces", "pct", "cts", "tones", "metres", "kilos", "kg",
            "pound", "foot", "feet", "miles", "yards"};
    final static String[] firms = {"ltd", "Inc", "Corp", "Saint", "de", "des", "Ltd"};
    final static String[] countries1 = {"Germany", "German", "West-Germany", "East-Germany", "DE",
            "America", "American", "USA", "US", "United states", "France", "French", "FR",
            "United Kingdoms", "English", "Scotish", "British", "Irish", "UK", "Scotland", "Great Britain",
            "Wales", "Ireland", "Canada", "Canadian", "CA", "Japan", "Japanese", "JPN"};
    final static String[] buildings = {"Commerzbank", "Norfolk", "Southern Tower", "Senate and House of Representatives",
            "White House", "The House Banking Committee", "Golden Gate", "George Washington Bridge",
            "Windsor", "Eiffel tower", "Westminister Bank"};
    final static String[][] countries2 = {{"Germany", "German", "West-Germany", "East-Germany", "DE"},
            {"America", "American", "USA", "US", "United states"},
            {"France", "French", "FR"},
            {"United Kingdoms", "English", "Scotish", "British", "Irish", "UK", "Scotland", "Great Britain", "Wales", "Ireland"},
            {"Canada", "Canadian", "CA"},
            {"Japan", "Japanese", "JPN"}};

    public static Trait[] extractTraits(String text) {
        Trait[] exit = new Trait[15];
        exit[0] = new WordTrait(getFirstOccurrence(text, people));
        exit[1] = new WordTrait(getFirstOccurrence(text, regions));
        exit[2] = new WordTrait(getFirstOccurrence(text, continents));
        exit[3] = new WordTrait(getFirstOccurrence(text, currency));
        exit[4] = new WordTrait(getFirstOccurrence(text, units));
        exit[5] = new WordTrait(getFirstOccurrence(text, firms));
        exit[6] = new WordTrait(getFirstOccurrence(text, countries1));
        exit[7] = new WordTrait(getFirstOccurrence(text, buildings));
        for (int i = 0; i < 6; i++) {
            exit[8+i] = new NumberTrait(countOccurrences(text,countries2[i]));
        }
        exit[14] = new NumberTrait(text.split(" ").length);
        return exit;
    }

    private static String getFirstOccurrence(String text, String[] dict) {
        int min = text.length() + 1;
        int index = -1;
        for (int i = 0; i < dict.length; i++) {
            int tmp = text.toLowerCase().indexOf(dict[i].toLowerCase());
            if ((tmp >= 0) && (tmp < min)) {
                min = tmp;
                index = i;
            }
        }
        if (index == -1) {
            return "";
        }
        return dict[index];
    }

    private static int countOccurrences(String text, String[] dict) {
        text = text.toLowerCase();
        int sum = 0;
        for (String s : dict) {
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = text.indexOf(s.toLowerCase(), lastIndex);
                if (lastIndex != -1) {
                    sum++;
                    lastIndex += s.length();
                }
            }
        }
        return sum;
    }
}
