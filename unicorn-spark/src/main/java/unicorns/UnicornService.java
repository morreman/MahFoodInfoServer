package unicorns;

import static spark.Spark.*;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Spark;

/**
 * Recensitiondatabasen
 * 
 * @author Mårten, Anton
 */
public class UnicornService {

    public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		Storage storage = new Storage();
        Filter_spark filter = new Filter_spark();
        port(8080);
        filter.apply();
		storage.setup();


        // Hämtar en lista över alla enhörningar
		get("/", (request, response) -> {
            System.out.println("In get!");
            response.type("application/json");
            response.body(gson.toJson(storage.fetchRestaurants()));
            response.status(200);
            return response.body(); // Skicka tillbaka svaret
        });

        get("/:id", (request, response) -> {
            System.out.println("In GET id");
            response.type("application/json");
            response.body(gson.toJson(storage.fetchResturant(Integer.parseInt(request.params(":id")))));
            response.status(200);
            return response.body(); // Skicka tillbaka svaret
        });

        post("/", "application/json", ((request, response) -> {
            Map<String,Object> result = new Gson().fromJson(request.body(), Map.class);
            System.out.println("In POST");
            System.out.println(result.get("grade"));
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


        put("/", (request, response) -> {
            System.out.println("In PUT");
            Calendar calendar = Calendar.getInstance();
            java.util.Date now = calendar.getTime();
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
            Visit visit = new Visit();
            visit.grade = Integer.parseInt(request.params("name"));
            visit.description = request.params("description");
            visit.reportedBy = request.params("reportedBy");
            storage.updateRestaurant(visit);
            response.body("Updaterad");
            return response.body();
        });


        delete("/", (request, response) -> {
            System.out.println("In DELETE");
            int nbr = Integer.parseInt(request.params("id"));
            storage.deleteRestaurant(nbr);
            response.body("Deleted");
            return response.body();
        });
	}



	private static String preferredResponseType(Request request) {
		// Ibland skickar en klient en lista av format som den önskar.
		// Här splittar vi upp listan och tar bort eventuella mellanslag.
		List<String> types = Arrays.asList(request.headers("Accept").split("\\s*,\\s*"));
		
		// Gå igenom listan av format och skicka tillbaka det första som vi stöder
		for (String type: types) {
			switch (type) {
			case "application/json":
			case "application/xml":
			case "text/html":
				return type;
			default:
			}
		}
		// Om vi inte stöder något av formaten, skicka tillbaka det första formatet
		return types.get(0);
	}
}
