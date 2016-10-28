package recensions;

import static spark.Spark.*;
import java.util.*;
import com.google.gson.Gson;

/**
 * Recensionsdatabasen
 * 
 * @author Mårten, Anton
 */
public class RecensionService {

    public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		Storage storage = new Storage();
        Filter_spark filter = new Filter_spark();
        port(8080);
        filter.apply();
		storage.setup();


        // Hämtar en lista över alla besök.
		get("/", (request, response) -> {
            response.type("application/json");
            response.body(gson.toJson(storage.fetchRestaurants()));
            response.status(200);
            return response.body();
        });

        // Hämta ut ett specifikt besök för en restaurang.
        get("/:id", (request, response) -> {
            response.type("application/json");
            response.body(gson.toJson(storage.fetchResturant(Integer.parseInt(request.params(":id")))));
            response.status(200);
            return response.body();
        });

        // Skapar ett nytt besök.
        post("/", "application/json", ((request, response) -> {
            Map<String,Object> result = new Gson().fromJson(request.body(), Map.class);
            Visit visit = new Visit();
            visit.grade = Integer.parseInt(result.get("grade").toString());
            visit.restaurant = result.get("restaurant").toString();
            visit.description = result.get("description").toString();
            visit.reportedBy = result.get("reportedBy").toString();
            storage.addRestaurant(visit);
            System.out.println(visit);
            return "hello World";
//            response.body(); // Sätt ett tomt svar
//            response.status(200);
//            return response.body(); // Skicka tillbaka svaret
    }));

        // Metod för att uppdatera ett besök i databasen.
        put("/", (request, response) -> {
            System.out.println("In PUT");
            Calendar calendar = Calendar.getInstance();
            java.util.Date now = calendar.getTime();
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
            Visit visit = new Visit();
            visit.grade = Integer.parseInt(request.params("name"));
            visit.description = request.params("description");
            visit.reportedBy = request.params("reportedBy");
            //storage.updateRestaurant(visit);
            response.body("Updaterad");
            return response.body();
        });

        // Metod för att ta bort ett besök i databasen.
        delete("/", (request, response) -> {
            System.out.println("In DELETE");
            int nbr = Integer.parseInt(request.params("id"));
            storage.deleteVisit(nbr);
            response.body("Deleted");
            return response.body();
        });
	}
}
