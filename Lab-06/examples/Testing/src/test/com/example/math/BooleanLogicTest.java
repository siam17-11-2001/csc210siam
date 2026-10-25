package com.example.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BooleanLogicTest {
    @Test
    void andTruthTable() {
        assertFalse(BooleanLogic.And(false, false));
        assertFalse(BooleanLogic.And(false, true));
        assertFalse(BooleanLogic.And(true, false));
        assertTrue(BooleanLogic.And(true, true));
    }

    @Test
    void orTruthTable() {
        assertFalse(BooleanLogic.Or(false, false));
        assertTrue(BooleanLogic.Or(false, true));
        assertTrue(BooleanLogic.Or(true, false));
        assertTrue(BooleanLogic.Or(true, true));
    }

    @Test
    void notTruthTable() {
        assertTrue(BooleanLogic.Not(false));
        assertFalse(BooleanLogic.Not(true));
    }

    @Test
    void xorTruthTable() {
        assertFalse(BooleanLogic.Xor(false, false));
        assertTrue(BooleanLogic.Xor(false, true));
        assertTrue(BooleanLogic.Xor(true, false));
        assertFalse(BooleanLogic.Xor(true, true));
    }
    @Test
    void deMorganLawsHoldForEveryInputPair() {
        for (boolean left : new boolean[] {false, true}) {
            for (boolean right : new boolean[] {false, true}) {
                assertEquals(BooleanLogic.Not(BooleanLogic.And(left, right)),
                        BooleanLogic.Or(BooleanLogic.Not(left), BooleanLogic.Not(right)));
                assertEquals(BooleanLogic.Not(BooleanLogic.Or(left, right)),
                        BooleanLogic.And(BooleanLogic.Not(left), BooleanLogic.Not(right)));
            }
        }
    }

    @Test
    void doubleNegationRestoresOriginalValue() {
        assertFalse(BooleanLogic.Not(BooleanLogic.Not(false)));
        assertTrue(BooleanLogic.Not(BooleanLogic.Not(true)));
    }
}
