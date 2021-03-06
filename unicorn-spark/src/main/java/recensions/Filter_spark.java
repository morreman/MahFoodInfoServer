package recensions;
import java.util.HashMap;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Created by Marten & Anton on 2016-10-25.
 * En filterklass för att slippa komma runt cross origin
 * anrop från Websidan.
 */
public final class Filter_spark {


    /**
     * Really simple helper for enabling CORS in a spark application;
     */

        private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

        static {
            corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            corsHeaders.put("Access-Control-Allow-Origin", "*");
            corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
            corsHeaders.put("Access-Control-Allow-Credentials", "true");
        }

        public final static void apply() {
            Filter filter = new Filter() {
                @Override
                public void handle(Request request, Response response) throws Exception {
                    corsHeaders.forEach((key, value) -> {
                        response.header(key, value);
                    });
                }
            };
            Spark.after(filter);
        }
    }
