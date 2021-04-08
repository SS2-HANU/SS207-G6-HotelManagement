package hanu.edu.hotelsystem.software;

import domainapp.basics.exceptions.DataSourceException;
import domainapp.software.SoftwareFactory;
import domainapp.softwareimpl.DomSoftware;
import hanu.edu.hotelsystem.services.Delivery.model.Delivery;
import hanu.edu.hotelsystem.services.Delivery.model.InplaceDelivery;
import hanu.edu.hotelsystem.services.Delivery.model.RoomDelivery;
import hanu.edu.hotelsystem.services.Department.model.Department;
import hanu.edu.hotelsystem.services.Person.model.Address;
import hanu.edu.hotelsystem.services.Person.model.Customer;
import hanu.edu.hotelsystem.services.Person.model.Employee;
import hanu.edu.hotelsystem.services.Person.model.Person;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.Room.model.Room;
import hanu.edu.hotelsystem.services.RoomOrder.model.RoomOrder;
import hanu.edu.hotelsystem.services.Service.model.RestaurantService;
import hanu.edu.hotelsystem.services.Service.model.RoomService.RoomService;
import hanu.edu.hotelsystem.services.Service.model.Service;
import hanu.edu.hotelsystem.services.Service.model.SpaService.SpaService;
import hanu.edu.hotelsystem.services.Service.model.TransportationService.TransportationService;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RestaurantServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RoomServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.ServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.SpaServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.TransportationServiceOrder;


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
            Address.class,
            Person.class,
            Employee.class,
            Department.class,
            Customer.class,
            Reservation.class,
            RoomOrder.class,
            Room.class,
            Service.class,
            ServiceOrder.class,
            RoomService.class,
            RoomServiceOrder.class,
            SpaService.class,
            SpaServiceOrder.class,
            TransportationService.class,
            TransportationServiceOrder.class,
            RestaurantService.class,
            RestaurantServiceOrder.class,
            Delivery.class,
            InplaceDelivery.class,
            RoomDelivery.class,
    };

    // delete Domain Model
    deleteDomainModel();

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

  private static void deleteDomainModel() {
    try {
      DomSoftware sw = SoftwareFactory.createDefaultDomSoftware();

      // this should be run subsequent times
      sw.init();
      // delete the domain model
      String modelName = sw.getDomainModelName(Employee.class);
      if (modelName != null) {
        try {
          sw.deleteDomainModel(modelName);
        } catch (DataSourceException e) {
          e.printStackTrace();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
