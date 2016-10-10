package unicorns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for setting up a unicorn database. Not only that,
 * but it can also read from and write to the database. Just wow.
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class Storage {

	private Connection connection = null;

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
	 * Throws the database away and then recreates it.
	 */
	public void setup() {
		System.out.println("In setup");
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("DROP TABLE IF EXISTS restaurants");
			statement.executeUpdate("CREATE TABLE restaurants (id INTEGER PRIMARY KEY, name TEXT, grade INTEGER, description TEXT, reportedBy TEXT, location TEXT, lat REAL, lon REAL, spottedWhen DATETIME CURRENT_TIMESTAMP, image TEXT)");
			statement.executeUpdate("INSERT INTO restaurants VALUES (1, 'Thai wok', 7, 'Billig thai mat men god! ', 'Mike', 'Malmö, Sverige', 55.6092688, 12.9940, '2008-09-23 12:00:00', 'http://www.allakartor.se/venue_images_475/473321_2975148.jpg')");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fetches a list of all unicorns.
	 * 
	 * @return A list of unicorns.
	 */
	public List<Visit> fetchRestaurants() {
        System.out.println("In fetchResturang");

        List<Visit> restaurants = new ArrayList<>();
		
		try {
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT * FROM restaurants");
			while (rs.next()) {
				Visit visit = new Visit();
				
				visit.id = rs.getInt("id");
                visit.name = rs.getString("name");
				visit.grade = rs.getInt("grade");
				visit.description = rs.getString("description");
				visit.reportedBy = rs.getString("reportedBy");
				visit.spottedWhere.name = rs.getString("location");
				visit.spottedWhere.lat = rs.getDouble("lat");
				visit.spottedWhere.lon = rs.getDouble("lon");
				visit.spottedWhen = Timestamp.valueOf(rs.getString("spottedWhen"));
				visit.image = rs.getString("image");
				
				restaurants.add(visit);
			}
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return restaurants;
	}
	
	/**
	 * Fetches a unicorn.
	 * 
	 * @param id The id number of a unicorm.
	 * @return A unicorn object.
	 */
	public Visit fetchResturant(int id) {
		Visit visit = null;
		
		try {
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT * FROM restaurants WHERE id = " + id);
			if (rs.next()) {
				visit = new Visit();
				visit.id = rs.getInt("id");
                visit.name = rs.getString("name");
                visit.grade = rs.getInt("grade");
				visit.description = rs.getString("description");
				visit.reportedBy = rs.getString("reportedBy");
				visit.spottedWhere.name = rs.getString("location");
				visit.spottedWhere.lat = rs.getDouble("lat");
				visit.spottedWhere.lon = rs.getDouble("lon");
				visit.spottedWhen = Timestamp.valueOf(rs.getString("spottedWhen"));
				visit.image = rs.getString("image");
			}
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return visit;
	}
	
	/**
	 * Adds a new visit.
	 * 
	 * @param visit A new visit object.
	 */
	public void addRestaurant(Visit visit) {
		try {
			Statement statement = connection.createStatement();
			
			String sql = "INSERT INTO restaurants (grade, description, reportedBy, "
					   + "location, lat, lon, spottedWhen, image) "
					   + "VALUES ('" + visit.grade + "', "
					   + "'" + visit.description + "', "
					   + "'" + visit.reportedBy + "', "
                       + "'" + visit.name + "', "
					   + "'" + visit.spottedWhere.name + "', "
					   + visit.spottedWhere.lat + ", "
					   + visit.spottedWhere.lon + ", "
					   + "'" + visit.spottedWhen.toString() + "', "
					   + "'" + visit.image + "');";
			
			statement.executeUpdate(sql);
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates an existing visit.
	 * 
	 * @param visit A visit object.
	 */
	public void updateRestaurant(Visit visit) {
		try {
			Statement statement = connection.createStatement();
			
			String sql = "UPDATE restaurants SET id = " + visit.id + ", "
                       + "name = '" + visit.name + "', "
					   + "grade = '" + visit.grade + "', "
					   + "description = '" + visit.description + "', "
					   + "reportedBy = '" + visit.reportedBy + "', "
					   + "location = '" + visit.spottedWhere.name + "', "
					   + "lat = " + visit.spottedWhere.lat + ", "
					   + "lon = " + visit.spottedWhere.lon + ", "
					   + "spottedWhen = '" + visit.spottedWhen.toString() + "', "
					   + "image = '" + visit.image + "' "
					   + "WHERE id = " + visit.id + ";";
			
			statement.executeUpdate(sql);
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a unicorn from the database.
	 * 
	 * @param id The id of a unicorn
	 */
	public void deleteRestaurant(int id) {
		try {
			Statement statement = connection.createStatement();
			
			statement.executeUpdate("DELETE FROM restaurants WHERE id = " + id);
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}