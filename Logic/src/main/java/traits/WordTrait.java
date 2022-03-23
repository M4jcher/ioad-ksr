package traits;

public class WordTrait extends Trait {
    private String value;

    public WordTrait(String value) {
        this.value = value;
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
