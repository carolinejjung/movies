/**
 * Represents an object of type Actor. An Actor has a name and a gender.
 *
 * @author (Provided code Stella K., Caroline Jung)
 * @version (May 12, 2022)
 */
public class Actor
{
    private String name;
    private String gender;
    
    /**
     * Constructor for class Actor.
     */
    public Actor(String name, String gender){
        this.name = name;
        this.gender = gender;
    }

    /**
     * This method is defined here because Actor (mutable) is used as a key in a Hashtable.
     * It makes sure that same Actors have always the same hash code.
     * So, the hash code of any object that is used as key in a hash table,
     * has to be produced on an *immutable* quantity,
     * like a String (such a string is the name of the actor in our case)
     * 
     * @return an integer, which is the hash code for the name of the actor
     */
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Tests this actor against the input one and determines whether they are equal.
     * Two actors are considered equal if they have the same name and gender.
     * 
     * @return true if both objects are of type Actor, 
     * and have the same name and gender, false in any other case.
     */
    public boolean equals(Object other) {
        if (other instanceof Actor) {
            return this.name.equals(((Actor) other).name) && 
            this.gender.equals(((Actor) other).gender); // Need explicit (Actor) cast to use .name
        } else {
            return false;
        }
    }

    /**
     * Getter method for gender instance variable.
     * 
     * @return The gender of this actor.
     */
    public String getGender(){
        return gender;
    }
    
    /**
     * Getter method for name instance variable.
     * 
     * @result The name of this actor.
     */
    public String getName(){
        return name;
    }
    
    /**
     * Setter method for gender instance variable.
     * 
     * @param g The gender of this actor.
     */
    public void setGender(String g){
        gender = g;
    }
    
    /**
     * Setter method for name instance variable.
     * 
     * @param n The name of this actor.
     */
    public void setName(String n){
        name = n;
    }
    
    /**
     * Returns a String representation of object Actor.
     * 
     * @return A String representation of this actor, containing the actor's name and gender.
     */ 
    public String toString(){
        return name + ": " + gender;
    }
    
    /**
     * Main method for testing.
     */
    public static void main(String[] args){
        System.out.println("Testing constructor, toString(), hashCode():");
        Actor a1 = new Actor("Scarlett Johansson", "Female");
        System.out.println("Actor 1: " + a1);
        System.out.println("Hashcode: " + a1.hashCode());
        System.out.println();
        
        System.out.println("Testing equals():");
        Actor a2 = new Actor("Scarlett Johansson", "Female"); //same as a1
        Actor a3 = new Actor("Ryan Gosling", "Male");
        System.out.println("Actor 1: " + a1);
        System.out.println("Actor 2: " + a2);
        System.out.println("Actor 3: " + a3);
        System.out.println("Comparing actor 1 & 2. EXPECTED: true | ACTUAL: " + a1.equals(a2));
        System.out.println("Comparing actor 2 & 3. EXPECTED: false | ACTUAL: " + a2.equals(a3));
    }
}
