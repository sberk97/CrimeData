package CrimeData;

/**
 * Exception thrown when there are not any records with search parameters given
 * @author Sebastian Berk UB:
 */
class NoMatchingInputException extends Exception {
    
    /**
     * Message printed out on the bottom of the screen
     * @return String with the message
     */
    @Override
    public String getMessage() {
        return "There aren't any records matching provided input";
    }
    
}
