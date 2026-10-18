/**
 * Every class in the simulation implements TurnTaker.
 * The World is passed in as context so a TurnTaker can look at
 * its surroundings when it acts. Do not change this signature.
 */
public interface TurnTaker {
    void takeTurn(World world);
}
