
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.lang.reflect.Array;
import java.util.*;
import java.io.File;

//creates BST using movieslends data as input and prints out subsets between two songs as examples of the tree
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner file = new Scanner(new File("../data/movies.csv"));
        //ignores header of csv file
        file.nextLine();

        //creates new map by reading in file and maps title names to corresponding nodes
        HashMap<String, Movie> map = readFile(file);

        //map with genre as key and movie nodes associated with key in arraylist as value
        HashMap<String, ArrayList<Movie>> genres = countGenres(map);

        //new hashmap with genre count associated with value
        HashMap<String, Integer> genreCount = genreCount(genres);

        //new LinkedHashMap that is sorted in descending order
        LinkedHashMap<String, Integer> sortedDescendingMap = sortValueDescending(genreCount);


        System.out.println("enter ' r ' to see genres and their movie count in descending order");
        System.out.println("enter ' q ' to quit");

        Scanner in = new Scanner(System.in);
        String userInput;


        printMap(genreCount);

        HashMap<Integer, Integer> mapCount =releaseYearCount(map);

        mapTest(mapCount);
        System.out.println(mapCount);




        /*
        do {
            userInput = in.next();
            if(userInput.equals("r"))
                printMap(sortedDescendingMap);
            if(userInput.equals("t")){
                System.out.println("How many years recent?");
                int yearAfter = in.nextInt();
                printMap(genreCount(genres , yearAfter));
            }



        }while(!userInput.equals("q"));
        */

    }










    public static void printMap(LinkedHashMap<String, Integer> map){
        Set<String> genreCounts = map.keySet();
        for (String genre : genreCounts){
            System.out.println(genre + " " + map.get(genre));
        }
    }
    public static void printMap(HashMap<String, Integer> map){
        Set<String> genreCounts = map.keySet();
        for (String genre : genreCounts){
            System.out.println(genre + " " + map.get(genre));
        }
    }


    //returns map with values sorted in descending order
    public static LinkedHashMap<String, Integer> sortValueDescending (HashMap<String, Integer> genreCount){
        //LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();

        genreCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        return reverseSortedMap;
    }







    //return hashmap with genre title as key and amount of movies in genre as value
    public static HashMap<String, Integer> genreCount(HashMap<String, ArrayList<Movie>> genres){
        //create new hashmap
        HashMap<String, Integer> newMap = new HashMap<>();
        //parsing through old map and adding genre name and size to new hashmap
        Set<String> genreSet = genres.keySet();
        for(String genre : genreSet){
            int size = genres.get(genre).size();
            newMap.put(genre, size);

        }
        return newMap;
    }

    public static HashMap<Integer, Integer> releaseYearCount(HashMap<String, Movie> map){

        HashMap<Integer,Integer> releaseCount = new HashMap<>();
        Set<String> set = map.keySet();
        for(String key: set){
            if(releaseCount.containsKey(map.get(key).getReleaseYear())){
                Integer value = releaseCount.get(map.get(key).getReleaseYear());
                value+=1;
                releaseCount.put(map.get(key).getReleaseYear(),value);
            }else{
                releaseCount.put(map.get(key).getReleaseYear(),1);
            }

        }

        return releaseCount;

    }



    public static HashMap<String, Integer> genreCount(HashMap<String, ArrayList<Movie>> genres , int years){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int afterYear = currentYear - years;


        //cloned param HashMap
        HashMap<String, ArrayList<Movie>> copy = clone(genres);


        //create new hashmap
        HashMap<String, Integer> newMap = new HashMap<>();
        //parsing through old map and adding genre name and size to new hashmap
        Set<String> genreSet = genres.keySet();
        //iterating through each genre
        for(String genre : genreSet){
            ArrayList<Movie> movies = copy.get(genre);
            ArrayList<Movie> toDelete = new ArrayList<>();
            //iterating through arraylist
            for(Movie movie : movies){
                if(movie.getReleaseYear() < afterYear){
                    toDelete.add(movie);
                }
            }
            movies.removeAll(toDelete);
            newMap.put(genre,movies.size());
        }
        return newMap;
    }







    /*
    public static HashMap<String, Integer> genreCount(HashMap<String, ArrayList<Movie>> genres , int years){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int afterYear = currentYear - years;
        //clone original map
        HashMap<String, ArrayList<Movie>> genresClone =  cloneMap(genres);
        //create new hashmap
        HashMap<String, Integer> newMap = new HashMap<>();
        //parsing through old map and adding genre name and size to new hashmap
        Set<String> genreSet = genresClone.keySet();
        //iterating through each genre
        for(String genre : genreSet){
            ArrayList<Movie> movies = genresClone.get(genre);
            ArrayList<Movie> toDelete = new ArrayList<>();
            //iterating through arraylist
            for(Movie movie : movies){
                if(movie.getReleaseYear() < afterYear){
                    toDelete.add(movie);
                }
            }
            movies.removeAll(toDelete);
            newMap.put(genre,movies.size());
        }
        return newMap;
    }

     */

    public static HashMap<String, ArrayList<Movie>> cloneMap(HashMap<String, ArrayList<Movie>> original){
        Set<String> set = original.keySet();
        HashMap<String, ArrayList<Movie>> newMap = new  HashMap<>();
        for(String movieNames : set){
            newMap.put(movieNames, original.get(movieNames));
        }
        return newMap;
    }


    //accepts scanner and returns hashmap with movie titles as key and movie node as value
    public static HashMap<String, Movie> readFile (Scanner file) {
        ArrayList<Movie> movies = new ArrayList<>();
        while (file.hasNextLine()) {
            movies.add(Movie.readMovie(file));
        }
        //removes quotation marks from song title
        for (Movie movie : movies) {
            if (movie.getTitle().charAt(0) == '"') {
                movie.setTitle(movie.getTitle().substring(1));
            }
        }

        HashMap<String, Movie> map = new HashMap<>();
        //adds movies into HashMap with Movie title as key and Movie node as value
        for (Movie movie : movies) {
            map.put(movie.getTitle(), movie);
        }
        return map;
    }

    //adds occurances of genres to the hashmap key = name of genre & value = arraylist of movies
    public static HashMap<String, ArrayList<Movie>> countGenres(HashMap<String, Movie> map) {
        HashMap<String, ArrayList<Movie>> genreCount = new HashMap<>();
        Set<String> keySet = map.keySet();

        for (String key : keySet) {
            String[] genres = map.get(key).getGenres();
            for (int i = 0; i < genres.length; i++) {
                String genre = genres[i];
                if (genre.charAt(0) == ',') {
                    genre = genre.substring(1);
                }
                if (genreCount.containsKey(genre)) {
                    ArrayList<Movie> list = genreCount.get(genre);
                    list.add(map.get(key));
                    genreCount.put(genre, list);
                } else {
                    ArrayList<Movie> list = new ArrayList<>();
                    list.add(map.get(key));
                    genreCount.put(genre, list);
                }


            }
        }
        genreCount.remove(" ");
        return genreCount;
    }

    //deep copy of hashmap
    public static <Movie> HashMap<String, ArrayList<Movie>> clone(HashMap<String, ArrayList<Movie>> original)
    {
        HashMap<String, ArrayList<Movie>> copy = new HashMap<>();
        for (Map.Entry<String, ArrayList<Movie>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }


    public static void mapTest(HashMap<Integer,Integer> map)throws Exception {
        Set<Integer> songYear = map.keySet();
        ArrayList<Integer> arrayList = new ArrayList<>();
        int[] xArray = new int[songYear.size()];
        int[] yArray = new int[songYear.size()];
        int count =0;
        for(Integer ints : songYear){
            if(ints==0) {
                continue;
            }else{
                xArray[count] = ints;
                count++;
                yArray[count] = map.get(ints);
            }
        }


        int[] xData = xArray;

        System.out.println(Arrays.toString(xArray));
        int[] yData = yArray;


// Create Chart
        //XYChart chart = QuickChart.getChart("Release Year", "X", "Y", "y(x)", xData, yData);

        /*
        XYChart chart = new XYChart(500, 400);
        chart.setTitle("Sample Chart");
        chart.setXAxisTitle("X");
        chart.setXAxisTitle("Y");
        XYSeries series = chart.addSeries("y(x)", xData, yData);
        series.setMarker(SeriesMarkers.CIRCLE);
        //chart.getStyler().setDecimalPattern(" ");
        */

        /*
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Test").xAxisTitle("Age").yAxisTitle("Amount").build();
        chart.addSeries("test", yData, xData);
        */

        //create chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Releases by year").yAxisTitle("Movies released").xAxisTitle("Years").build();
        //chart.addSeries(xData,yData);
        chart.getStyler().setXAxisMin(1900.0);
        chart.getStyler().setXAxisDecimalPattern("####");

        chart.addSeries("series",xData,yData);





// Show it
        new SwingWrapper(chart).displayChart();

// Save it
        BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapEncoder.BitmapFormat.PNG);




}
}
