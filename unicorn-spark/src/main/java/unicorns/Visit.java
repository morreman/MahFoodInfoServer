package unicorns;

import java.sql.Timestamp;

/**
 * A simple class representing a unicorn.
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class Visit {
	public String name = "";
	public int id = 0;
	public String description = "";
	public int grade = 0;
	public String reportedBy = "";
	public Restaurant spottedWhere = new Restaurant();
	public Timestamp spottedWhen = new Timestamp(0);
	public String image = "";
	
	public Visit() {
		
	}
}
