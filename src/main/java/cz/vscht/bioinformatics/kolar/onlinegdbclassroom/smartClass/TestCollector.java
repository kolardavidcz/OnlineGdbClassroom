package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass;

import java.util.ArrayList;

/**
 * Abstract base class for test source implementations that generate lists of test cases.
 * This class provides a framework for creating and managing test lists from different sources.
 */
public abstract class TestCollector {
    protected int numberOfTests;
    public abstract ArrayList<String> makeTestList();
}
