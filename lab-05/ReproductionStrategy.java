/** Composition: a Plant contains one or more ways of making offspring. */
public interface ReproductionStrategy {
    boolean reproduce(Plant parent, World world);
}
