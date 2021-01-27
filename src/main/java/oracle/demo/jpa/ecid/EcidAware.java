package oracle.demo.jpa.ecid;

import java.sql.Connection;

public interface EcidAware {
    
    public Connection getSqlConnection(); 

}
