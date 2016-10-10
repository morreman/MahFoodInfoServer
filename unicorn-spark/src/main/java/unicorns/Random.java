package unicorns;


/**
 * Created by Marten on 2016-09-29.
 */
public class Random {

    public static void main(String args[]){
        double nbr;
        do {
            nbr = Math.random() * 5;
        } while( nbr<2 );
        System.out.println( nbr );
    }
}
