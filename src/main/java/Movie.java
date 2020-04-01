import java.util.Scanner;

public class Movie implements Comparable<Movie> {
    private String title;
    private int releaseYear;
    private String[] genres; // optional
    private int movieId; //optional
    public Movie left;
    public Movie right;

    //no args constructor
    public Movie(){

    }

    //Constructor that accepts string and assigns to title
    public Movie(String title) {
        this.title = title;
    }
    //constructor for all fields
    public Movie(int ID, String title, int releaseYear, String[] genres){
        this.movieId = ID;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genres = genres;
    }

    //getter for movieID
    public int getMovieID(){
        return movieId;
    }
    //getter for Movie title
    public String getTitle(){
        return title;
    }
    //setter for Movie title
    public void setTitle(String title){
        this.title = title;
    }
    //getter for release year
    public int getReleaseYear(){
        return releaseYear;
    }
    //getter for generes
    public String[] getGenres(){
        return genres;
    }

    //accepts scanner and returns new Movie object
    public static Movie readMovie(Scanner in){
        if(!(in.hasNextLine()))
            return null;
        String line = in.nextLine();
        //gets movie ID by creating substring up to first comma
        String stringID = line.substring(0,line.indexOf(','));
        int ID = Integer.parseInt(stringID);
        //removes the substring that was taken out
        line = line.substring(line.indexOf(',')+1);
        //gets movie title name

        String titleTest = line.substring(0,line.lastIndexOf(","));
        String title;
        int releaseDate = 0000;
        if(!titleTest.contains("(")){
            title = titleTest;
            line=line.substring(line.lastIndexOf(",")+1);
        }
        else {
            boolean correctSpot = false;
            int parenthesisSpot = line.indexOf('(');
            while (!correctSpot) {
                if (Character.isDigit(line.charAt(parenthesisSpot + 1)) && Character.isDigit(line.charAt(parenthesisSpot + 2)) && Character.isDigit(line.charAt(parenthesisSpot + 3)) && Character.isDigit(line.charAt(parenthesisSpot + 4))) {
                    correctSpot = true;
                } else {
                    parenthesisSpot = line.indexOf('(', parenthesisSpot + 1);
                }
            }
            title = line.substring(0, parenthesisSpot-1);
            //removes substring that was taken out
            line = line.substring(parenthesisSpot+1);
            //gets release year of movie
            String release = line.substring(0,line.indexOf(')'));
            //if there are two years for release data, chooses first date
            if(release.contains("–")) {
                release = release.substring(0, 4);
            }
            releaseDate = Integer.parseInt(release);
            //removes substring that was taken out
            line = line.substring(line.indexOf(')')+2);
        }

        //gets movie generes and stores into an array
        String [] genres = new String[1];
        if(line.contains("no genres"))
            genres[0] = " ";
        else if (!(line.contains("|")))
            genres[0] = line;
        else
            genres = line.split("\\|");
        return new Movie(ID, title, releaseDate, genres);
    }

    //returns name and release year
    public String toString(){
        return title;

    }

    //compareTo implemented in order to compare objects by title
    @Override
    public int compareTo(Movie other){
        return title.compareToIgnoreCase(other.title);
    }
}
