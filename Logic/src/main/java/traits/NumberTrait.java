package traits;

public class NumberTrait extends Trait {
    private Integer value;

    public NumberTrait(int value) {
        this.value = value;
    }

    public double getDistanceTo(Trait second) {
        NumberTrait trait = (NumberTrait) second;
        return this.value.compareTo(trait.value);
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
