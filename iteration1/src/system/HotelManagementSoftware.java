package system;

import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.software.DomainAppToolSoftware;
import domainapp.software.SoftwareFactory;
import domainapp.softwareimpl.DomSoftware;
import model.Address;
import model.Customer;
import model.Department;
import model.Employee;
import model.Person;


public class HotelManagementSoftware {



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

