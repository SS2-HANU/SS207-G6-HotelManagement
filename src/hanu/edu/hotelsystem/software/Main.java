package hanu.edu.hotelsystem.software;

import domainapp.software.SoftwareFactory;
import domainapp.softwareimpl.DomSoftware;
import hanu.edu.hotelsystem.services.Department.model.Department;
import hanu.edu.hotelsystem.services.person.model.Address;
import hanu.edu.hotelsystem.services.person.model.Customer;
import hanu.edu.hotelsystem.services.person.model.Employee;
import hanu.edu.hotelsystem.services.person.model.Person;


/**
 * @overview 
 *  Create and run a UI-based {@link DomSoftware} for a pre-defined model.  
 *  
 * @author dmle
 */
public class Main {
  
  /**
   * @effects 
   *  create and run a UI-based {@link DomSoftware} for a pre-defined model. 
   */
  public static void main(String[] args){
    // 1. initialise the model
    Class[] model = {
            Person.class,
            Employee.class,
            Department.class,
            Address.class,
            Customer.class
    };
    
    // 2. create UI software
    DomSoftware sw = SoftwareFactory.createUIDomSoftware();
    
    // 3. run
    // create in memory configuration
    System.setProperty("domainapp.setup.SerialiseConfiguration", "false");
    
    // 3. run it
    try {
      sw.run(model);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }   
  }

}
