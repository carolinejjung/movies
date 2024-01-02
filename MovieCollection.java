import java.util.LinkedList;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.Hashtable;
import java.util.Vector;
import java.io.PrintWriter;
import javafoundations.PriorityQueue;

/**
 * Represents an object of type MovieCollection.
 * Manages collections of all movies and all actors given a file with movies with their 
 * test results and actors' information.
 *
 * @author (Caroline Jung, Emily Lu, Rachel Suarez)
 * @version (May 12, 2022)
 */
public class MovieCollection
{
    // instance variables
    private LinkedList<Movie> allMovies;
    private LinkedList<Actor> allActors;

    /**
     * Constructor for class MovieCollection.
     */
    public MovieCollection(String testsFileName, String castsFileName){
        allMovies = new LinkedList<Movie>();
        this.readMovies(testsFileName);
        allActors = new LinkedList<Actor>();
        this.readCasts(castsFileName);
    }
    
    /**
     * Reads the input file, and uses its first column (movie title) to create all movie objects. 
     * Adds the included information on the Bachdel test results to each movie.
     * 
     * @param fName the name of the file to be read
     */
    private void readMovies(String fName){
        try{
            Scanner fileScan = new Scanner(new File(fName));
            fileScan.useDelimiter(",");
            // skip header line
            fileScan.nextLine(); 

            while(fileScan.hasNextLine()){
                String name = fileScan.next();

                String testResult = fileScan.nextLine().substring(1);

                // create new movie object and populate test results
                Movie m = new Movie(name);
                m.setTestResults(testResult);

                allMovies.add(m);
            }
            fileScan.close();  
        }
        catch(IOException ex){
            System.out.println(ex);
        }
    }
    
    /**
     * Reads the casts for each movie, from input casts file; Assume lines in this file are formatted.
     * If movie doesn't have test results, it is not included in the collection. There are no repeats in Movies.
     * 
     * @param fName the name of the file to be read.
     */
    private void readCasts(String fName){
        for (int i=0; i<allMovies.size(); i++){
            if(!allMovies.get(i).getAllTestResult().isEmpty()){ // ignore movies with no test results
                allMovies.get(i).addAllActors(fName); //populates hashtable of actors for a movie
                Hashtable<Actor,String> hash = allMovies.get(i).getAllActors(); //hashtable

                hash.forEach((key, value) -> 
                    {
                        if (!allActors.contains(key)){ //check if Actor is already in the linked list
                            allActors.add(key);
                        }
                    });
            }
        }
    }

    /**
     * Getter method for the allActors Linked List.
     * 
     * @return LinkedList<Actor> linked list of all actors
     */
    public LinkedList<Actor> getActors(){
        return allActors;
    }

    /**
     * Getter method for the allMovies Linked List.
     * 
     * @return LinkedList<Actor> linked list of all movies
     */
    public LinkedList<Movie> getMovies(){
        return allMovies;
    }

    /**
     * Getter method for the accessing all movie titles.
     * 
     * @return LinkedList<String> linked list of all movie titles
     */
    public LinkedList<String> getMovieTitles(){
        LinkedList<String> movieTitles = new LinkedList<String>();
        for (int i = 0; i< allMovies.size(); i++){
            movieTitles.add(allMovies.get(i).getTitle()); // adds movie title for each movie in allMovies linkedlist
        }
        return movieTitles;
    }

    /**
     * Getter method for the accessing all actor names.
     * 
     * @return LinkedList<String> linked list of all actor names
     */
    public LinkedList<String> getActorNames(){
        LinkedList<String> actorNames = new LinkedList<String>();

        for (int i = 0; i<allActors.size(); i++){
            actorNames.add(allActors.get(i).getName()); // adds name for each actor for each actor in allActors linkedlsit
        }
        return actorNames;
    }

    /**
     * Returns a list of all Movies that pass the n-th Bechdel test
     * 
     * @param n an integer indicating which nth Bechdel test to examine
     * @return LinkedList<Movie> linked list of movies that passed the nth Bechdel test
     */
    public LinkedList<Movie> findAllMoviesPassedTestNum(int n){
        LinkedList<Movie> passed = new LinkedList<Movie>();
        for (int i = 0; i < allMovies.size(); i++){
            if (allMovies.get(i).getAllTestResult().get(n).equals("0")){
                //each movie's test result for n is considered to pass (0)
                passed.add(allMovies.get(i));
            }  
        }

        return passed;
    }

    /**
     * Writes all the movies that passed the following criteria to a separate file: 1) Bechdel test 2) Peirce or Landau test 
     * 3) passed White test but failed Rees-Davies test. File contains the number of movies from each result and the movie titles.
     * 
     * @param outFileName the name of the file to write the results to
     */
    private void writeAllMoviesPassedCriteria(String outFileName){
        try{
            PrintWriter writer = new PrintWriter(new File(outFileName));
            /*
             * write all movies that passed Bechdel test to file
             */
            LinkedList<Movie> passedBechdel = findAllMoviesPassedTestNum(0);
            writer.println("Movies that passed the Bechdel test (" + passedBechdel.size() + " total):");
            for (int i = 0; i < passedBechdel.size(); i++){
                writer.println(passedBechdel.get(i).getTitle()); // write title of current movie to file
            }

            /*
             * write all movies that passed Peirce or Landau tests to file
             */
            LinkedList<Movie> passedPeirceOrLandau = findAllMoviesPassedPeirceOrLandau();
            writer.println("\nMovies that passed the Peirce or Landau test (" + passedPeirceOrLandau.size() + " total):");
            for (int i = 0; i< passedPeirceOrLandau.size(); i++){
                writer.println(passedPeirceOrLandau.get(i).getTitle()); // write title of current movie to file
            }

            /*
             * write all movies that passed the White test but failed the Rees-Davies test to file
             */
            LinkedList<Movie> passedWhiteFailedReesDavies = findAllMoviesPassedWhiteFailedReesDavies();
            writer.println("\nMovies that passed the White test but failed the Rees-Davies test (" + passedWhiteFailedReesDavies.size() + " total):");
            for (int i = 0; i< passedWhiteFailedReesDavies.size(); i++){
                writer.println(passedWhiteFailedReesDavies.get(i).getTitle()); // write title of current movie to file
            }
            writer.close();
        }
        catch(IOException ex){
            System.out.println(ex);
        }
    }

    /**
     * Returns all the movies (in a linked list) that passed the Peirce or Landau test.
     * 
     * @return  LinkedList<Movie> the movies that passed the Peirce or Landau test
     */
    private LinkedList<Movie> findAllMoviesPassedPeirceOrLandau(){
        LinkedList<Movie> passed = new LinkedList<Movie>();
        for (int i = 0; i < allMovies.size(); i++){
            Vector<String> aTestResult = allMovies.get(i).getAllTestResult(); // get allTestResult for current movie
            if (aTestResult.get(1).equals("0") || aTestResult.get(2).equals("0")){ // if movie passed Peirce or Landau,
                passed.add(allMovies.get(i)); // then add it to LinkedList 
            }
        }

        return passed;
    }
    
    /**
     * Returns all the movies (in a linked list) that passed the White test but failed the Rees-Davies.
     * 
     * @return  LinkedList<Movie> the movies that passed the White test but failed the Rees-Davies
     */
    private LinkedList<Movie> findAllMoviesPassedWhiteFailedReesDavies(){
        LinkedList<Movie> passed = new LinkedList<Movie>();
        for (int i = 0; i < allMovies.size(); i++){
            Vector<String> aTestResult = allMovies.get(i).getAllTestResult(); // get allTestResult for current movie
            if (aTestResult.get(11).equals("0") && !aTestResult.get(12).equals("0")){ // if movie passed White but failed Rees-Davies,
                passed.add(allMovies.get(i)); // then add it to LinkedList 
            }
        }

        return passed;
    }

    /**
     * returns a PriorityQueue of movies in the provided data based on their feminist score. 
     * If all movies are enqueued, they will be dequeued in priority order: from most 
     * feminist to least feminist. 
     * 
     * @return priority queue of movies in order of their feminist score
     */
    public PriorityQueue<Movie> prioritizeMovies(){
        PriorityQueue<Movie> pq = new PriorityQueue<Movie>();
        
        for (Movie movie : allMovies){
            //ties broken by the compareTo method
            pq.enqueue(movie);
        }
        return pq;
    }
    
    /**
     * returns a PriorityQueue of feminist scores for all movies in the provided data in order
     * of most feminist (higher score) to least feminist. Used for testing purposes.
     * 
     * @return priority queue of feminist scores in order
     */
    private PriorityQueue<Integer> testingPQ(){
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
        
        for (Movie movie : allMovies){
            //ties broken by the compareTo method
            pq.enqueue(movie.feministScore());
        }
        return pq;
    }
    
    /**
     * String representation of a MovieCollection which includes the number of movies and the movies themselves.
     * 
     * @return String string representation of a MovieCollection
     */
    public String toString(){
        
        String s = "This MovieCollection has " + allMovies.size() + " movies: \n";
        for (int i=0; i<allMovies.size(); i++){
            s += allMovies.get(i) + "\n";
        }
        
        return s;
    }
    
    /**
     * Main method for testing.
     */
    public static void main(String[] args){
        System.out.println("Testing readMovies() and readCast()");
        MovieCollection m1 = new MovieCollection("data/small_allTests_Copy.txt", "data/small_castGender.txt");
        //MovieCollection m1 = new MovieCollection("data/nextBechdel_allTests.txt", "data/nextBechdel_castGender.txt");
        System.out.println("\nPrinting all movies in small MovieCollection: \nEXPECTED: [Alpha: 4 actors, Beta: 4 actors, Gamma: 4 actors]" + 
                            "\nACTUAL: " + m1.allMovies);
        System.out.println("\nPrinting all actors in small MovieCollection: \nEXPECTED: [Tyler Perry: Male, Stella: Male, Cassi Davis: Female, Patrice Lovely: Female, Takis: Female]" + 
        "\nACTUAL: " + m1.allActors);
        
        System.out.println("\nTesting toString(): ");
        System.out.println(m1);

        System.out.println("\nTesting findAllMoviesPassedTestNum()");
        System.out.println("\nPrinting all movies that passed test no. 1" 
                            + "\nEXPECTED: [Alpha: 4 actors, Beta: 4 actors]"
                            + "\nACTUAL: " + m1.findAllMoviesPassedTestNum(0));
        System.out.println("\nPrinting all movies that passed test no. 13" 
                            + "\nEXPECTED: []"
                            + "\nACTUAL: " + m1.findAllMoviesPassedTestNum(12));
        
        System.out.println("\nTesting findAllMoviesPassedPeirceOrLandau()");                 
        System.out.println("\nEXPECTED: [Beta: 4 actors, Gamma: 2 actors]" + 
                            "\nACTUAL: " + m1.findAllMoviesPassedPeirceOrLandau());
        System.out.println("\nTesting findAllMoviesPassedWhiteFailedReesDavies()"); 
        System.out.println("\nEXPECTED: [Beta: 4 actors]" +
                            "\nACTUAL: " + m1.findAllMoviesPassedWhiteFailedReesDavies());
        m1.writeAllMoviesPassedCriteria("data/write_Test.txt");
        

        MovieCollection m2 = new MovieCollection("data/nextBechdel_allTests.txt", "data/nextBechdel_castGender.txt");
        m2.writeAllMoviesPassedCriteria("data/bechdelProject_testing.txt");
        
        System.out.println("\nPriority queue of movies:\n" + m2.prioritizeMovies());
        System.out.println("Order of the feminist scores for all movies in the priority queue:\n" + m2.testingPQ());
    }
}
