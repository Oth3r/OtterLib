package one.oth3r.otterlib;

public record Pair<A, B>(A key, B value) {

    public String toString() {
        return "(" + this.key + ", " + this.value + ")";
    }

    public Pair<B, A> getFlipped() {
        return new Pair<>(value, key);
    }
}
