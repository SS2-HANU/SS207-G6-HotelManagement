package hanu.edu.hotelsystem.services.Person.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;
import hanu.edu.hotelsystem.services.ServiceOrder.model.ServiceOrder;
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
            filter=@Select(clazz= ServiceOrder.class))
    @DAssoc(ascName="employee-manages-service-order",role="employee",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type= ServiceOrder.class,
                    cardMin=0,cardMax=25))
    private Collection<ServiceOrder> serviceOrders;
    private int orderCount;

    @DAttr(name = "star", type = DAttr.Type.Integer, auto = true, mutable = false, min = 1, max = 5)
    private int star;

    @DAttr(name = "averageRating", type = DAttr.Type.Double, auto = true, mutable = false)
    private double averageRating;

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
        this( null, name, gender,dob, address, phoneNum, email, salary,department, 0D,0);
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
            @AttrRef("department") Department department,
            @AttrRef("averageRating") Double averageRating,
            @AttrRef("star") Integer star
    ) throws ConstraintViolationException {
        super(name, gender, dob, address, phoneNum);
        this.code = nextCode(code);
        setEmail(email);
        setSalary(salary);
        setDepartment(department);

        serviceOrders = new ArrayList<>();
        orderCount = 0;

        averageRating = 0D;
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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public double getAverageRating() {
        return averageRating;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addServiceOrder(ServiceOrder order) {
        if (!this.serviceOrders.contains(order)) {
            serviceOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewServiceOrder(ServiceOrder order) {
        serviceOrders.add(order);
        orderCount++;
        // no other attributes changed

        computeAverageRating();

        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addServiceOrder(Collection<ServiceOrder> orders) {
        for (ServiceOrder o : orders) {
            if (!this.serviceOrders.contains(o)) {
                this.serviceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewServiceOrder(Collection<ServiceOrder> orders) {
        this.serviceOrders.addAll(orders);
        orderCount += orders.size();

        computeAverageRating();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeServiceOrder(ServiceOrder o) {
        boolean removed = serviceOrders.remove(o);

        if (removed) {
            orderCount--;

            computeAverageRating();
        }
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public boolean updateServiceOrder(ServiceOrder s){
        double totalRating = averageRating * orderCount;

        int oldAverageRating = s.getRating();

        int diff = s.getRating() - oldAverageRating;

        totalRating += diff;

        averageRating = totalRating / orderCount;

        return true;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setServiceOrder(Collection<ServiceOrder> orders) {
        this.serviceOrders = orders;

        orderCount = orders.size();

        computeAverageRating();
    }


    @DOpt(type=DOpt.Type.Getter)
    public Collection<ServiceOrder> getServiceOrders() {
        return serviceOrders;
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
    public void setServiceOrderCount(int count) {
        orderCount = count;
    }


    /**
     * @effects
     *  computes {@link #averageRating} of all the {@link ServiceOrder#getRating()}s
     *  (in {@link #serviceOrders}.
     */
    private void computeAverageRating() {
        if (orderCount > 0) {
            double totalRanking = 0d;
            for (ServiceOrder s : serviceOrders) {
                totalRanking += s.getRating();
            }
            averageRating = Math.round(totalRanking / orderCount);


        } else {
            averageRating = 0;
        }
        if (averageRating <= 2)
            star = 1;
        else if (averageRating > 2 && averageRating <= 4)
            star = 2;
        else if (averageRating > 4 && averageRating <= 6)
            star = 3;
        else if (averageRating > 6 && averageRating <= 8)
            star = 4;
        else
            star = 5;
    }

    public double getAverageRanking() {
        return averageRating;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + getId() +
                ", code='" + code +
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
