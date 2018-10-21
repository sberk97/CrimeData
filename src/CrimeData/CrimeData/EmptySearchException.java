package CrimeData;

/**
 * Exception thrown when user did not input any values to search fields
 * @author Sebastian Berk UB:
 */
class EmptySearchException extends Exception {
      
     /**
     * Message printed out on the bottom of the screen
     * @return String with the message
     */
    @Override
    public String getMessage() {
        return "You haven't input anything to search for";
    }
    
}
