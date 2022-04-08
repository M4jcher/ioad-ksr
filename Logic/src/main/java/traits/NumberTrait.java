package traits;

public class NumberTrait extends Trait {
    private Double value;

    public NumberTrait(Double value) {
        this.value = value;
    }

    public double getDistanceTo(Trait second) {
        NumberTrait trait = (NumberTrait) second;
        return (this.value -  trait.value);
    }

//    public double getDistanceTo(Trait second) {
//        NumberTrait trait = (NumberTrait) second;
//        return this.value.compareTo(trait.value);
//    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
