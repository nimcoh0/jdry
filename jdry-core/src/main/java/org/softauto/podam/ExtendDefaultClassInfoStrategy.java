package org.softauto.podam;



public class ExtendDefaultClassInfoStrategy extends ExtendAbstractClassInfoStrategy {


    // ------------------->> Constants

    /** The singleton instance of this implementation */
    private static final ExtendDefaultClassInfoStrategy SINGLETON = new ExtendDefaultClassInfoStrategy();

    // ------------------->> Instance / Static variables

    // ------------------->> Constructors

    /**
     * Implementation of the Singleton pattern
     */
    private ExtendDefaultClassInfoStrategy() {
        super();
    }

    // ------------------->> Public methods

    /**
     * Implementation of the Singleton pattern
     *
     * @return A singleton instance of this class
     */
    public static ExtendDefaultClassInfoStrategy getInstance() {
        return SINGLETON;
    }

    /**
     * Other factory method which assigns a default number of collection
     * elements before returning the singleton.
     *
     * @param nbrCollectionElements
     *            The number of collection elements
     * @return The Singleton, set with the number of collection elements set as
     *         parameter
     */
    public static ExtendDefaultClassInfoStrategy getInstance(
            int nbrCollectionElements) {

        return SINGLETON;

    }

    // ------------------->> Getters / Setters

    // ------------------->> Private methods

    // ------------------->> equals() / hashcode() / toString()

    // ------------------->> Inner classes
}
