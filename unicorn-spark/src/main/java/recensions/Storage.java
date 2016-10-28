package recensions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author "Anton & Mårten"
 */
public class Storage {

	private Connection connection = null;

    private String createStringRestaurant =
            "create table RESTAURANT " +
            "(RESTAURANT_NAME varchar(32)," +
            "primary key(RESTAURANT_NAME))";


    private String createStringComment =
            "create table COMMENT " +
            "(COMMENT_ID int AUTO INCREMENT, " +
            "RESTAURANT_FK varchar(32)," +
            "GRADE int," +
            "REPORTED_BY varchar(32)," +
            "DESCRIPTION varchar(140)," +
            "primary key(COMMENT_ID)," +
            "foreign key(RESTAURANT_FK) references RESTAURANT(RESTAURANT_NAME))";

	/**
	 * Sets up the database connection.
	 *
	 * @throws ClassNotFoundException
	 */
	public Storage() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		 try {
			connection = DriverManager.getConnection("jdbc:sqlite:restaurants.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the database connection.
	 */
	protected void finalize() throws Throwable {
		try {
			connection.close();
		} catch (SQLException e) {
		}
		super.finalize();
	}
	
	/**
	 * Nollställer databasen när servern startar.
     * Fyller på med de 5 restauranger som Tibblins API tillhandahåller.
	 */
	public void setup() {
		System.out.println("In setup");
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("DROP TABLE IF EXISTS RESTAURANT");
            statement.executeUpdate("DROP TABLE IF EXISTS COMMENT");
            statement.executeUpdate(createStringComment);
            statement.executeUpdate(createStringRestaurant);
			statement.executeUpdate("INSERT INTO RESTAURANT VALUES ('Restaurang Niagara')");
			statement.executeUpdate("INSERT INTO RESTAURANT VALUES ('Mia Maria')");
			statement.executeUpdate("INSERT INTO RESTAURANT VALUES ('Välfärden')");
			statement.executeUpdate("INSERT INTO RESTAURANT VALUES ('Lilla Köket')");
			statement.executeUpdate("INSERT INTO RESTAURANT VALUES ('La Bonne Vie')");

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public List<Visit> fetchRestaurants() {

        List<Visit> recension = new ArrayList<>();
		
		try {
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT RESTAURANT.RESTAURANT_NAME, COMMENT.GRADE, COMMENT.DESCRIPTION, COMMENT.REPORTED_BY FROM RESTAURANT INNER " +
                    "JOIN COMMENT ON RESTAURANT.RESTAURANT_NAME=COMMENT.RESTAURANT_FK ");

            while (rs.next()) {
				Visit visit = new Visit();
				
				visit.grade = rs.getInt("GRADE");
				visit.description = rs.getString("DESCRIPTION");
				visit.reportedBy = rs.getString("REPORTED_BY");
                visit.restaurant = rs.getString("RESTAURANT_NAME");
				recension.add(visit);
			}
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recension;
	}
	
	/**
	 * Hämtar alla besök från en restaurang.
	 * 
	 * @param id Id:t är restaurangens namn.
	 * @return returnernar alla besök för den givna restaurangen.
	 */
	public Visit fetchResturant(int id) {
		Visit visit = null;
		
		try {
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT * FROM restaurants WHERE id = " + id);
			if (rs.next()) {
				visit = new Visit();
                visit.grade = rs.getInt("grade");
				visit.description = rs.getString("description");
				visit.reportedBy = rs.getString("reportedBy");
                visit.restaurant = rs.getString("restaurant_name");
			}
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return visit;
	}

	
	/**
	 * Skapar ett nytt besök.
	 * 
	 * @param visit A new visit object.
	 */
	public void addRestaurant(Visit visit) {
        System.out.println(visit.restaurant + " VIlken resturang");
		try {
			Statement statement = connection.createStatement();
			
			String sql = "INSERT INTO COMMENT (GRADE, DESCRIPTION, REPORTED_BY, RESTAURANT_FK) "
					   + "VALUES ('" + visit.grade + "', "
					   + "'" + visit.description + "', "
					   + "'" + visit.reportedBy + "', "
                       + "'" + visit.restaurant + "');";
			System.out.println(sql);
			statement.executeUpdate(sql);
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Uppdaterar ett besök.
     * Används inte för tillfället (Kod inte fullständig).
     * @param visit A visit object.
	 */
//	public void updateRestaurant(Visit visit) {
//		try {
//			Statement statement = connection.createStatement();
//
//			String sql = "UPDATE restaurants SET id = " + visit.id + ", "
//                       + "restaurant_name = '" + visit.restaurant + "', "
//					   + "grade = '" + visit.grade + "', "
//					   + "description = '" + visit.description + "', "
//					   + "reportedBy = '" + visit.reportedBy + "', "
//					   + "WHERE id = " + visit.id + ";";
//
//			statement.executeUpdate(sql);
//
//			statement.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Tar bort ett besök tillhörande en restaurang.
	 *  Används inte för tillfället (Kod inte fullständig).
	 * @param id Id:t för ett besök.
	 */
	public void deleteVisit(int id) {
		try {
			Statement statement = connection.createStatement();
			
			statement.executeUpdate("DELETE FROM restaurants WHERE id = " + id);
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}