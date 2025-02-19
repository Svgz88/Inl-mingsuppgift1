import com.mongodb.client.*;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class MongoDBAtlasDownloadExample {
    public MongoDBAtlasDownloadExample() {
        //Skriv in rätt url!
        //String uri = "mongodb+srv://sebastianvaldesgomez:Javauser1%21%21@cluster0.yfysc.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        String uri = "mongodb+srv://test:test@cluster0.yfysc.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> moviesCollection = database.getCollection("movies");

            //Get all movies from 1975
            List<Movie> movieList = new ArrayList<>();
            for (Document doc : moviesCollection.find(new Document("year", 1975))) {
                {
                    movieList.add(Movie.fromDocument(doc));
                }
            }

            // Skriver ut alla filmer
            for (Movie movie : movieList) {
                //System.out.println(movie);
            }
            //Här gör du anrop till alla dina funktioner som ska skriva ut svaren på frågorna som
            //efterfrågas i uppgiften


            //• Hur många filmer gjordes 1975 (enligt vårt data). Returnera ett tal
            int svar1 = (int) movieList.stream().mapToInt(Movie::getYear).filter(s->s == 1975).count();
            System.out.println("Svar1: Antal filmer från år 1975: "+svar1);

            //• Hitta längden på den film som var längst (högst runtime). Returnera ett tal.
            int svar2 = movieList.stream().mapToInt(Movie::getRuntime).summaryStatistics().getMax();
            System.out.println("Svar2: Högst runtime: "+svar2);

            //• Hur många UNIKA genrer hade filmerna från 1975. Returnera ett tal.
            List<String> list1 = movieList.stream().filter(s->s.getYear() == 1975).flatMap(s->s.getGenres().stream()).collect(Collectors.toList());
            int svar3 = (int) list1.stream().count();
            //System.out.println("Svar3 "+movieList.stream().flatMap(s->s.getGenres().stream()).collect(Collectors.toSet()));
            System.out.println("Svar3: Unika genrer av filmerna från 1975: "+svar3);

            //• Vilka skådisar som spelade i den film som hade högst imdb-rating. Returnera en List<String> med deras namn.
            double maxRating = movieList.stream().mapToDouble(Movie::getImdbRating).summaryStatistics().getMax();
            List<String> svar4 = movieList.stream().filter(s->s.getImdbRating() == maxRating)
                    .flatMap(s->s.getCast().stream()).collect(Collectors.toList());
            System.out.println("Svar4: Skådisar högst imbt-ratting: "+svar4);

            //• Vad är titeln på den film som hade minst antal skådisar listade? Returnera en String.
            String svar5 = movieList.stream().min(Comparator.comparing(s->s.getCast().size())).map(Movie::getTitle).orElse("");

            System.out.println("Svar5 : Filmen med minst antal skådisar: "+svar5);

            //• Hur många skådisar var med i mer än 1 film? Returnera ett tal.
            List<String> mestEnFilm = movieList.stream().map(Movie::getCast).flatMap(s->s.stream()).toList();
            Map<String,Long> map4 = mestEnFilm.stream().collect(Collectors.groupingBy(s->s,counting()));
            int antal = (int) map4.values().stream().filter(v->v > 1).count();

            System.out.println("Svar6: Så många var med i mer än 1 film: "+antal);

            //• Vad hette den skådis som var med i flest filmer? Returnera en String.
            List <String> lista1 = movieList.stream().map(Movie::getCast).flatMap(s->s.stream()).toList();
            Map<String,Long> map1 = lista1.stream().collect(groupingBy(s->s,counting()));

            Long long1 = map1.values().stream().mapToLong(s->s.intValue()).summaryStatistics().getMax();
            for(Map.Entry<String,Long> entry:map1.entrySet()){
                if(entry.getValue() >= long1){
                    String s1 = entry.getKey();

                    System.out.println("Svar7: "+s1);
                }
            }

            Optional<Long> olong = map1.values().stream().max(Comparator.naturalOrder());
            if(olong.isPresent()){
                long maxCount = olong.get();
                map1.entrySet().stream().filter(s->s.getValue() == maxCount ).map(Map.Entry::getKey).toList()
                        .forEach(s -> System.out.println("svar7: Skådis i flest filmer: "+s));
            }

            //• Hur många UNIKA språk har filmerna? Returnera ett tal.
            List<String> listLanguages = movieList.stream().map(Movie::getLanguages).flatMap(Collection::stream).toList();
            Map<String,Long> mapLanguages = listLanguages.stream().collect(groupingBy(s->s,counting()));
            int mapLanguageSize = mapLanguages.size();

            System.out.println("Svar8: Unika språk "+mapLanguageSize);

            //• Finns det någon titel som mer än en film har? Returnera en bool.


            Map<String,Long> mapTitles = movieList.stream().map(Movie::getTitle).collect(groupingBy(s ->s,counting()));
            //mapTitles.forEach((k,v)-> System.out.println(v+ " " + k ));
            Boolean b1 = mapTitles.values().stream().anyMatch(s->s > 1);


            System.out.println("Svar9: Om det finns likadana titlar? "+b1);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MongoDBAtlasDownloadExample m = new MongoDBAtlasDownloadExample();
    }
}