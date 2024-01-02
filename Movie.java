import java.util.Hashtable;
import java.util.Vector;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.util.LinkedList;

/**
 * Represents an object of type Movie.
 * A Movie object has a title, some Actors, and results for the twelve Bechdel tests.
 *
 * @author (Provided code Stella K., Caroline Jung, Emily Lu, Rachel Suarez)
 * @version (May 12, 2022)
 */
public class Movie implements Comparable<Movie>
{
    private String title;
    private Hashtable<Actor,String> roles;
    private Vector<String> testResults;

    /**
     * Constructor for class Movie.
     */
    public Movie(String title){
        this.title = title;
        //initialize other to empty structures
        roles = new Hashtable<Actor,String>();
        testResults = new Vector<String>();
    }

    /**
     * Compares two movie objects' feminist scores. 
     * 
     * @param movie the movie to compared to
     * @return a negative integer if this movie's feminist score is less than the specified movie's
     * score and a positive integer if this movie's feminist score is more than the specified movie's
     * score. If this movie's feminist score is equal to the specified movie's score, we compare the
     * movies' titles' hashcode to break the tie (smaller hashcode is deemed "smaller than" the other
     * specified movie).
     */
    public int compareTo(Movie movie){
        int result = this.feministScore() - movie.feministScore();
        if (result == 0){
            //hashcodes for titles of movies are all diff
            if (this.title.hashCode() < movie.title.hashCode())
                result = -1;
            else
                result = 1;
        }
        return result;
        //negative if this < movie
        //positive if this > movie
        //breaks ties by comparing hashcode of the movie's titles
    }
    
    /**
     * Getter method for title instance variable.
     * 
     * @return The title of this movie.
     */
    public String getTitle(){
        return title;
    }

    /**
     * Getter method for roles instance variable of a hashtable of an actor and their role.
     * 
     * @return A Hashtable with all actors who played in this movie.
     */
    public Hashtable<Actor,String> getAllActors(){
        return roles;
    }

    /**
     * Returns a LinkedList with names of all actors who played in this movie.
     * 
     * @return A LinkedList with names of all actors who played in this movie.
     */
    public LinkedList<String> getActors(){
        LinkedList<String> allActors = new LinkedList<String>();
        // add each key:value pair in this movie's Hashtable of Actors to a LinkedList
        roles.forEach((key, value) -> {
                allActors.add(key.getName());
            });
        return allActors;
    }

    /**
     * Getter method for result instance variable of vector of all the results.
     * 
     * @return A Vector with the Bechdel test results for this movie, 
     * where "1" and "0" are used to indicate whether this movie passed the corresponding test.
     */
    public Vector<String> getAllTestResult(){
        return testResults;
    }

    /**
     * Takes a String of 1's and 0's and initializes the Vector of test results for this movie.
     * 
     * @param results A String of 1's and 0's, each representing the result of a Bechdel-alternative test on this movie.
     */
    public void setTestResults(String results){
        String[] splitResults = results.split(",");
        for(int i=0; i<splitResults.length; i++){
            testResults.add(splitResults[i]);
        }
    }

    /**
     * Tests this movie object with the input one and determines whether they are equal.
     * 
     * @return true if both objects are movies and have the same title, 
     * false in any other case.
     */
    public boolean equals(Object other) {
        if (other instanceof Movie) {
            return this.title.equals(((Movie) other).title); // Need explicit (Movie) cast to use .title
        } else {
            return false;
        }
    }

    /**
     * Takes a String of data on a role an actor played in the format of
     * '"MOVIE","ACTOR","CHARACTER_NAME","TYPE","BILLING","GENDER"' and
     * adds an entry to the Hashtable of Actors for this movie.
     * 
     * @param line A String of data on a role an actor played.
     * @return An object representing the actor that the input String pertains to.
     */
    public Actor addOneActor(String line){
        String[] splitLine = line.split(",");

        // create an Actor object to be key
        Actor actor = new Actor(splitLine[1].substring(1, splitLine[1].length() - 1), 
                splitLine[5].substring(1, splitLine[5].length() - 1)); 
        // get type of role as value
        String role = splitLine[3].substring(1, splitLine[3].length() - 1); 

        // add actor, 
        roles.put(actor, role); 

        return actor;
    }

    /**
     * Takes the name of an input file and 
     * adds all its Actors to this movie's Hashtable of Actors.
     * 
     * @param actorsFile Input file containing data on actors and their roles.
     */
    public void addAllActors(String actorsFile){
        try{
            Scanner fileScan = new Scanner(new File(actorsFile));

            // skip header line
            fileScan.nextLine(); 

            while(fileScan.hasNextLine()){
                String tempLine = fileScan.nextLine();
                if(tempLine.contains(title)){
                    addOneActor(tempLine);
                }
            }
            fileScan.close();
        }

        catch(IOException ex){
            System.out.println(ex);
        }
    }

    /**
     * Returns a toString() representation of this movie,
     * containing the title of the movie and the number of actors.
     * 
     * @return String representation of this movie.
     */
    public String toString(){
        return title + ": " + roles.size() + " actors";
    }

    /**
     * Calculates the feminist score for a movie.
     * 
     * @return int the number representing the feminist score
     */
    public int feministScore(){
        //count: add more points if they passed depending on weight of the test, 100 point scale
        int count = 0;

        //go thru vector. if 0, then add score. if 1, do nothing

        //bechdel: i=0        weight: 12
        //rees-davies: i=12   weight: 14
        //ko: i=6             weight: 25
        //pierce: i=1         weight: 28
        //koeze-dottle: i=9   weight: 21

        if (testResults.get(0).equals("0"))//bechdel
            count += 12;
        if (testResults.get(12).equals("0"))//rees-davies
            count += 14;
        if (testResults.get(6).equals("0"))//ko
            count += 25;
        if (testResults.get(1).equals("0"))//pierce
            count += 28;
        if (testResults.get(9).equals("0"))//koeze-dottle
            count += 21;

        for (int i=0; i<testResults.size(); i++){
            //for each additional test that it passes, add 1 point
            //max score possible: 108
            boolean additional = (i==2) ||(i==3) ||(i==4) ||(i==5) ||(i==7) ||(i==8) ||(i==10) ||(i==11);
            if (additional && testResults.get(i).equals("0"))
                count += 1;
        }
        
        return count;
    }

    /**
     * Setter method for title instance variable.
     * 
     * @param t the string to set the title to
     */
    public void setTitle(String t){
        title = t;
    }

    /**
     * Setter method for roles instance variable of a hashtable of an actor and their role.
     * 
     * @param r a hashtable of actors and their role to set the hashtable to
     */
    public void setRole(Hashtable<Actor,String> r){
        roles = r;
    }

    /**
     * Setter method for result instance variable of vector of all the results.
     * 
     * @param res a vector of strings to set the vector of all results to
     */
    public void setResult(Vector<String> res){
        testResults = res;
    }

    /**
     * Main method for testing.
     */
    public static void main(String[] args){
        System.out.println("Testing addOneActor() and addAllActors()");
        Movie alpha = new Movie("Alpha");
        alpha.addAllActors("data/small_castGender.txt");
        System.out.println("\nPrinting roles in Alpha:" 
            + "\nEXPECTED: " + 
            "{Patrice Lovely | Female=Supporting, Cassi Davis | Female=Supporting, Stella | Male=Leading, Tyler Perry | Male=Leading}" 
            + "\nACTUAL: " + alpha.roles);
        Movie beta = new Movie("Beta");
        beta.addAllActors("data/small_castGender.txt");
        System.out.println("\nPrinting roles in Beta:"
            + "\nEXPECTED: " +
            "{Takis | Female=Supporting, Patrice Lovely | Female=Supporting, Cassi Davis | Female=Supporting, Tyler Perry | Male=Leading}"
            + "\nACTUAL: " + beta.roles);
        Movie gamma = new Movie("Gamma");
        gamma.addAllActors("data/small_castGender.txt");
        System.out.println("\nPrinting roles in Gamma:"
            + "\nEXPECTED: " +
            "{Cassi Davis | Female=Supporting, Tyler Perry | Male=Leading}"
            + "\nACTUAL: " + gamma.roles);
        Movie lambda = new Movie("Lambda");
        lambda.addAllActors("data/small_castGender.txt");
        System.out.println("\nPrinting roles in Lambda:"
            + "\nEXPECTED: {}" +
            "\nACTUAL: " + lambda.roles);

        System.out.println("\nReading from \"nonexistent.txt\"");
        System.out.print("EXPECTED: FileNotFoundException" + "\nACTUAL: ");
        lambda.addAllActors("data/nonexistent.txt");

        System.out.println("\nTesting toString()");
        System.out.println("\nCalling toString() on Alpha:" + "\nEXPECTED: " + "Alpha | 4 actors" + "\nACTUAL: " + alpha);
        System.out.println("\nCalling toString() on Beta:" + "\nEXPECTED: " + "Beta | 4 actors" + "\nACTUAL: " + beta);
        System.out.println("\nCalling toString() on Gamma:" + "\nEXPECTED: " + "Gamma | 2 actors" + "\nACTUAL: " + gamma);

        System.out.println("\nTesting getActors()");
        System.out.println("\nCalling getActors() on Alpha:" + "\nEXPECTED: " + "[Tyler Perry, Stella, Cassi Davis, Patrice Lovely]" + "\nACTUAL: " + alpha.getActors());
        System.out.println("\nCalling getActors() on Gamma:" + "\nEXPECTED: " + "[Tyler Perry, Cassi Davis]" + "\nACTUAL: " + gamma.getActors());

        System.out.println("\nTesting setTestResults()");
        alpha.setTestResults("0,0,0,1,0,0,0,1,0,0,1,1,1");
        System.out.println("Printing test results for Alpha:" + "\nEXPECTED: [0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1]" + "\nACTUAL: " + alpha.testResults);
        beta.setTestResults("1,1,0,1,0,1,0,1,1,0,1,0,0");
        System.out.println("Printing test results for Beta:" + "\nEXPECTED: [1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0]" + "\nACTUAL: " + beta.testResults);
    
        System.out.println("\nTesting feministScore()");
        System.out.println("Testing Alpha: EXPECTED: 90" + " ACTUAL: " + alpha.feministScore());
        Movie fem = new Movie("fem");
        System.out.println("Test results for fem: [0,0,0,0,0,0,0,0,0,0,0,0,0]");
        fem.setTestResults("0,0,0,0,0,0,0,0,0,0,0,0,0");
        System.out.println("Testing fem: EXPECTED: 108" + " ACTUAL: " + fem.feministScore());
        
        
        System.out.println("\nTesting compareTo()");
        System.out.println("Testing alpha and fem scores: EXPECTED: -18 | ACTUAL: " + alpha.compareTo(fem));
    }
}
