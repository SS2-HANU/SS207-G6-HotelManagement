package hanu.edu.hotelsystem.software;

import domainapp.basics.exceptions.DataSourceException;
import domainapp.software.SoftwareFactory;
import domainapp.softwareimpl.DomSoftware;
import hanu.edu.hotelsystem.services.Assignment.model.Assignment;
import hanu.edu.hotelsystem.services.Delivery.model.Delivery;
import hanu.edu.hotelsystem.services.Delivery.model.InplaceDelivery;
import hanu.edu.hotelsystem.services.Delivery.model.RoomDelivery;
import hanu.edu.hotelsystem.services.Department.model.Department;
import hanu.edu.hotelsystem.services.Person.model.Address;
import hanu.edu.hotelsystem.services.Person.model.Customer;
import hanu.edu.hotelsystem.services.Person.model.Employee;
import hanu.edu.hotelsystem.services.Person.model.Gender;
import hanu.edu.hotelsystem.services.Person.model.Person;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.Room.model.Room;
import hanu.edu.hotelsystem.services.Room.model.RoomType;
import hanu.edu.hotelsystem.services.RoomOrder.model.RoomOrder;
import hanu.edu.hotelsystem.services.Service.model.RestaurantService.RestaurantService;
import hanu.edu.hotelsystem.services.Service.model.RoomService.RoomServiceType;
import hanu.edu.hotelsystem.services.Service.model.RoomService.RoomService;
import hanu.edu.hotelsystem.services.Service.model.Service;
import hanu.edu.hotelsystem.services.Service.model.SpaService.Duration;
import hanu.edu.hotelsystem.services.Service.model.SpaService.SpaService;
import hanu.edu.hotelsystem.services.Service.model.TransportationService.TransportationService;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RestaurantServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RoomServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.ServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.SpaServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.TransportationServiceOrder;
import domainapp.basics.util.Toolkit;

import java.util.Date;


/**
 * @author dmle
 * @overview Create and run a UI-based {@link DomSoftware} for a pre-defined model.
 */
public class Main {

    public static Class[] models = {
            Address.class,
            Person.class,
            Employee.class,
            Department.class,
            Customer.class,
            Reservation.class,
            Assignment.class,
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

    /**
     * @effects create and run a UI-based {@link DomSoftware} for a pre-defined model.
     */
    public static void main(String[] args) throws DataSourceException {

        DomSoftware sw;

        sw = SoftwareFactory.createDefaultDomSoftware();
        sw.init();

        deleteDomainModel(sw);

        sw.addClasses(models);

        createAddress(sw);
        createCustomer(sw);

        createReservation(sw);

        createDepartment(sw);
        createEmployee(sw);
        createRoom(sw);

        createSpaService(sw);
        createRoomService(sw);
        createTransportationService(sw);
        createRestaurantService(sw);

        // 2. create UI software
        sw = SoftwareFactory.createUIDomSoftware();

        // 3. run
        // create in memory configuration
        System.setProperty("domainapp.setup.SerialiseConfiguration", "false");

        // 3. run it
        try {
            sw.run(models);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void deleteDomainModel(DomSoftware sw) {
        try {
            // delete the domain model
            for (Class model : models) {
                String modelName = sw.getDomainModelName(model);
                if (modelName != null)
                    sw.deleteDomainModel(modelName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createAddress(DomSoftware sw) throws DataSourceException {
        Address address1 = new Address("VietNam", "Thai Binh", "TP Thai Binh", "112A");
        sw.addObject(Address.class, address1);

        Address address2 = new Address("VietNam", "Ha Noi", "Dong Da", "Thai Thinh");
        sw.addObject(Address.class, address2);

//        Address address3 = new Address("Ho Chi Minh");
//        sw.addObject(Address.class, address3);
//
//        Address address4 = new Address("Da Nang");
//        sw.addObject(Address.class, address4);
    }

    private static void createCustomer(DomSoftware sw) throws DataSourceException {
        Address address = sw.retrieveObjectById(Address.class, 1);
        assert address != null;

        Date dob = Toolkit.getDateZeroTime(8, 8, 2000);

        Customer customer = new Customer("Nguyen Thi Thuy Duong", Gender.Female, dob, address, "0969696969");
        sw.addObject(Customer.class, customer);
    }

    private static void createReservation(DomSoftware sw) throws DataSourceException {

        Customer customer = sw.retrieveObjectById(Customer.class, 1);
        assert customer != null;
        //Boolean isCancel = false;

        Date createdAt = Toolkit.getDateZeroTime(1, 1, 2021);
        Date startDate = Toolkit.getDateZeroTime(2, 1, 2021);
        Date endDate = Toolkit.getDateZeroTime(10, 1, 2021);

        Reservation reservation = new Reservation(createdAt, startDate, endDate, customer, false);

        sw.addObject(Reservation.class, reservation);
    }

    private static void createDepartment(DomSoftware sw) throws DataSourceException {
        Department department = new Department("Manager");
        sw.addObject(Department.class, department);
    }

    private static void createEmployee(DomSoftware sw) throws DataSourceException {
        Department department = sw.retrieveObjectById(Department.class, 1);
        Address address = sw.retrieveObjectById(Address.class, 2);

        assert department != null;
        assert address != null;

        Employee employee = new Employee("Tang Ba Minh", Gender.Male, new Date(), address, "0904842084", "tangbamiinh@gmail.com", 1_000_000_000L, department);

        sw.addObject(Employee.class, employee);
    }

    private static void createRoom(DomSoftware sw) throws DataSourceException{
        Room room1 = new Room(3, RoomType.VIP);
        sw.addObject(Room.class, room1);

        Room room2 = new Room(3, RoomType.VIP);
        sw.addObject(Room.class, room2);
    }


    private static void createSpaService(DomSoftware sw) throws DataSourceException {
        SpaService spaService1 = new SpaService(100L, Duration.C60_MINUTES);
        sw.addObject(SpaService.class, spaService1);

        SpaService spaService2 = new SpaService(120L, Duration.C90_MINUTES);
        sw.addObject(SpaService.class, spaService2);
    }
//
//    private static void createSpaServiceOrder(DomSoftware sw) throws DataSourceException {
//        SpaService spaService = sw.retrieveObjectById(SpaService.class, 1);
//        Employee employee = sw.retrieveObjectById(Employee.class, 1);
//        Reservation reservation = sw.retrieveObjectById(Reservation.class, 1);
//
//        SpaServiceOrder spaServiceOrder1 = new SpaServiceOrder(new Date(), 1, reservation, null, employee, spaService);
//        sw.addObject(SpaServiceOrder.class, spaServiceOrder1);
//
//        SpaServiceOrder spaServiceOrder2 = new SpaServiceOrder();
//        sw.addObject(SpaServiceOrder.class, spaServiceOrder2);
//    }

    private static void createRoomService(DomSoftware sw) throws DataSourceException {
        RoomService roomService = new RoomService(100L, RoomServiceType.BELL_SERVICE);

        sw.addObject(RoomService.class, roomService);
    }
//
//    private static void createRoomServiceOrder(DomSoftware sw) throws DataSourceException {
//        RoomServiceOrder roomService = new RoomServiceOrder();
//
//        sw.addObject(RoomServiceOrder.class, roomService);
//    }
//
    private static void createRestaurantService(DomSoftware sw) throws DataSourceException {
        RestaurantService restaurantService = new RestaurantService(100L, "fish");

        sw.addObject(RestaurantService.class, restaurantService);
    }

//
//    private static void createRestaurantServiceOrder(DomSoftware sw) throws DataSourceException {
//        RestaurantServiceOrder restaurantServiceOrder = new RestaurantServiceOrder();
//
//        sw.addObject(RestaurantServiceOrder.class, restaurantServiceOrder);
//    }
private static void createTransportationService(DomSoftware sw) throws DataSourceException {
    TransportationService transportationService = new TransportationService(100L, "fish");

    sw.addObject(TransportationService.class, transportationService);
}
}
