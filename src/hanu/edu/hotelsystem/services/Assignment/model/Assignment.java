package hanu.edu.hotelsystem.services.Assignment.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.util.cache.StateHistory;
import hanu.edu.hotelsystem.services.Person.model.Employee;
import hanu.edu.hotelsystem.services.ServiceOrder.model.ServiceOrder;

import java.util.Objects;

@DClass(schema = "hotelsystem")
public class Assignment {
    public static final String Attr_rating = "employeeRating";
    @DAttr(name = "id", id = true, type = DAttr.Type.Integer, auto = true, length = 6,
            mutable = false, optional = false)
    private int id;
    private static int idCounter = 0;

    @DAttr(name="employee",type= DAttr.Type.Domain,length = 10, optional = false)
    @DAssoc(ascName="employee-is-assigned-assignment",role="assignment",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= Employee.class,cardMin=1,cardMax=1))
    private Employee employee;

    @DAttr(name="serviceOrder",type= DAttr.Type.Domain,length = 10, optional = false)
    @DAssoc(ascName="service-order-has-assignment",role="assignment",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= ServiceOrder.class,cardMin=1,cardMax=1))
    private ServiceOrder serviceOrder;

    @DAttr(name = Attr_rating, type = DAttr.Type.Integer, optional = false, min = 1, max = 10)
    private Integer employeeRating;

    private StateHistory<String, Object> stateHist;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public Assignment(@AttrRef("employee") Employee employee,
                      @AttrRef("serviceOrder") ServiceOrder serviceOrder,
                      @AttrRef("employeeRating") Integer employeeRating){
        this(null, employee, serviceOrder, employeeRating);
    };

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Assignment(@AttrRef("id") Integer id,
                      @AttrRef("employee") Employee employee,
                      @AttrRef("serviceOrder") ServiceOrder serviceOrder,
                      @AttrRef("employeeRating") Integer employeeRating)
            throws ConstraintViolationException {
        this.id = nextId(id);
        this.employee = employee;
        this.serviceOrder = serviceOrder;
        this.employeeRating = employeeRating;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public Integer getEmployeeRating() {
        return getEmployeeRating(false);
    }

    public Integer getEmployeeRating(boolean cached) {
        if (cached) {
            Object val = stateHist.get(Attr_rating);

            if (val == null)
                throw new IllegalStateException(
                        "ServiceOrder.getRating: cached value is null");

            return (Integer) val;
        } else {
            if (employeeRating != null)
                return employeeRating;
            else
                return 0;
        }
    }

    public void setEmployeeRating(Integer employeeRating) {
        this.employeeRating = employeeRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", employee=" + employee +
                ", serviceOrder=" + serviceOrder +
                ", rating=" + employeeRating +
                '}';
    }
}
