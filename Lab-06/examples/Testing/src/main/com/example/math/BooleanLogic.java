package com.example.math;

/** Four elementary Boolean operations on Java truth values. */
public final class BooleanLogic {
    private BooleanLogic() { } // Utility class; no instances.

    /** @return true only when both arguments are true. */
    public static boolean And(boolean arg1, boolean arg2) { return arg1 && arg2; }

    /** @return true when either argument is true. */
    public static boolean Or(boolean arg1, boolean arg2) { return arg1 || arg2; }

    /** @return the inverse of the argument. */
    public static boolean Not(boolean arg1) { return !arg1; }

    /** @return true when exactly one argument is true. */
    public static boolean Xor(boolean arg1, boolean arg2) { return arg1 ^ arg2; }
}
