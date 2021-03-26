package hanu.edu.hotelsystem.services.person.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;
import hanu.edu.hotelsystem.services.AccompaniedServiceOrder.model.AccompaniedServiceOrder;
import hanu.edu.hotelsystem.services.Department.model.Department;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@DClass(schema = "hotelsystem" )
public class Employee extends Person {
    public static final String E_code = "code";
    public static final String A_email = "email";
    public static final String E_salary = "salary";


    @DAttr(name = E_code, type = DAttr.Type.String, auto = true, length = 6,
            mutable = false, optional = false)
    private String code;

    //static variable to keep track of employee id
    private static int counter = 0;

    @DAttr(name = A_email, type = DAttr.Type.String, length = 30, optional = false)
    private String email;

    @DAttr(name = E_salary, type = DAttr.Type.Long, optional = false)
    private Long salary;

    @DAttr(name="department",type= DAttr.Type.Domain,length = 20, optional = false)
    @DAssoc(ascName="department-has-employee",role="employee",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= Department.class,cardMin=1,cardMax=1))
    private Department department;

    @DAttr(name="serviceOrders",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= AccompaniedServiceOrder.class))
    @DAssoc(ascName="employee-manages-service-order",role="employee",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=AccompaniedServiceOrder.class,
                    cardMin=0,cardMax=25))
    private Collection<AccompaniedServiceOrder> serviceOrders;

    private int orderCount;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public Employee(
//            @AttrRef("id") Integer id,
            @AttrRef("name") String name,
            @AttrRef("gender") Gender gender,
            @AttrRef("dob") Date dob,
            @AttrRef("address") Address address,
            @AttrRef("phoneNum") String phoneNum,
            @AttrRef("email") String email,
            @AttrRef("salary") Long salary,
            @AttrRef("department") Department department){
        this( null, name, gender,dob, address, phoneNum, email, salary,department);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Employee(
//            @AttrRef("id") Integer id,
            @AttrRef("code") String code,
            @AttrRef("name") String name,
            @AttrRef("gender") Gender gender,
            @AttrRef("dob") Date dob,
            @AttrRef("address") Address address,
            @AttrRef("phoneNum") String phoneNum,
            @AttrRef("email") String email,
            @AttrRef("salary") Long salary,
            @AttrRef("department") Department department
    ) throws ConstraintViolationException {
        super(name, gender, dob, address, phoneNum);
        this.code = nextCode(code);
        setEmail(email);
        setSalary(salary);
        setDepartment(department);

        serviceOrders = new ArrayList<>();
        orderCount = 0;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ConstraintViolationException {
        if (!email.contains("@")) {
            throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE,
                    new Object[] {"'" + email + "' (does not have '@') "});
        }
        this.email = email;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addAccompaniedServiceOrder(AccompaniedServiceOrder order) {
        if (!this.serviceOrders.contains(order)) {
            serviceOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewAccompaniedServiceOrder(AccompaniedServiceOrder order) {
        serviceOrders.add(order);
        orderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addAccompaniedServiceOrder(Collection<AccompaniedServiceOrder> orders) {
        for (AccompaniedServiceOrder o : orders) {
            if (!this.serviceOrders.contains(o)) {
                this.serviceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewAccompaniedServiceOrder(Collection<AccompaniedServiceOrder> orders) {
        this.serviceOrders.addAll(orders);
        orderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeAccompaniedServiceOrder(AccompaniedServiceOrder o) {
        boolean removed = serviceOrders.remove(o);

        if (removed) {
            orderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setAccompaniedServiceOrder(Collection<AccompaniedServiceOrder> orders) {
        this.serviceOrders = orders;

        orderCount = orders.size();
    }

    /**
     * @effects
     *  return <tt>orderCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getAccompaniedServiceOrderCount() {
        return orderCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setAccompaniedServiceOrderCount(int count) {
        orderCount = count;
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<AccompaniedServiceOrder> getAccompaniedServiceOrders() {
        return serviceOrders;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + getId() +
                ", name='" + code +
                ", name='" + getName() +
                ", gender='" + getGender() +
                ", dob='" + getDob() +
                ", address='" + getAddress() +
                ", phoneNum='" + getPhoneNum() +
                ", email='" + email +
                ", salary=" + salary +
                ", department=" + department +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee other = (Employee) o;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    private String nextCode(String code) throws ConstraintViolationException {
        if (code == null) { // generate a new id
            return "E" + ++counter;
        } else {
            // update id
            int num;
            try {
                num = Integer.parseInt(code.substring(1));
            } catch (RuntimeException e) {
                throw new ConstraintViolationException(
                        ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { code});
            }
            if (num > counter) {
                counter = num;
            }
            return code;
        }
    }
    /**
     * @requires
     *  minVal != null /\ maxVal != null
     * @effects
     *  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
     */
    @DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
    public static void updateAutoGeneratedValue(
            DAttr attrib,
            Tuple derivingValue,
            Object minVal,
            Object maxVal) throws ConstraintViolationException {

        if (minVal != null && maxVal != null) {
            if (attrib.name().equals("id")) {
                int maxIdVal = (Integer) maxVal;
                if (maxIdVal > counter)
                    counter = maxIdVal;
            }
        }
    }

}
