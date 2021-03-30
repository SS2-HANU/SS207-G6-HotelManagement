package hanu.edu.hotelsystem.services.ServiceOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.Service.model.Service;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.person.model.Employee;

import java.util.Objects;


@DClass(schema = "hotelsystem")
public class ServiceOrder {

    @DAttr(name = "id", id = true, type = DAttr.Type.Integer, auto = true, length = 6,
            mutable = false, optional = false)
    private int id;
    private static int idCounter = 0;

    @DAttr(name="reservation",type= DAttr.Type.Domain,length = 10, optional = false)
    @DAssoc(ascName="reservation-has-service-order",role="serviceOrder",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type=Reservation.class,cardMin=1,cardMax=1))
    private Reservation reservation;

    @DAttr(name="employee",type= DAttr.Type.Domain,length = 10, optional = false)
    @DAssoc(ascName="employee-manages-service-order",role="serviceOrder",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type=Employee.class,cardMin=1,cardMax=1))
    private Employee employee;

    @DAttr(name="service",type= DAttr.Type.Domain,length = 10, optional = false)
    @DAssoc(ascName="service-has-service-order",role="serviceOrder",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= Service.class,cardMin=1,cardMax=1))
    private Service service;

    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    public ServiceOrder(@AttrRef("reservation") Reservation reservation,
                        @AttrRef("employee") Employee employee ,
                        @AttrRef("service") Service service){
        this(null, reservation,employee, service);
    }

    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public ServiceOrder(@AttrRef("id") Integer id,
                        @AttrRef("reservation") Reservation reservation,
                        @AttrRef("employee") Employee employee ,
                        @AttrRef("service") Service service) throws ConstraintViolationException {
        this.id = nextId(id);
        this.reservation= reservation;
        this.employee = employee;
        this.service = service;
    }

    private static int nextId(Integer currID) {
        if (currID == null) {
            idCounter++;
            return idCounter;
        } else {
            int num = currID.intValue();
            if (num > idCounter)
                idCounter = num;

            return currID;
        }
    }

    public int getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceOrder that = (ServiceOrder) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ServiceOrder{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", employee=" + employee +
                ", service=" + service +
                '}';
    }
}
