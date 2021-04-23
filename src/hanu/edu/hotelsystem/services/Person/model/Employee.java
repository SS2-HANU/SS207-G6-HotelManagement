package hanu.edu.hotelsystem.services.Person.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;
import hanu.edu.hotelsystem.services.Assignment.model.Assignment;
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

    @DAttr(name="assignments",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= Assignment.class))
    @DAssoc(ascName="employee-is-assigned-assignment",role="employee",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type= Assignment.class,
                    cardMin=0,cardMax=25))
    private Collection<Assignment> assignments;
    private int assignmentCount;

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
        this( null, name, gender,dob, address, phoneNum, null, email, salary,department, 0,0D);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Employee(
            @AttrRef("id") Integer id,
            @AttrRef("name") String name,
            @AttrRef("gender") Gender gender,
            @AttrRef("dob") Date dob,
            @AttrRef("address") Address address,
            @AttrRef("phoneNum") String phoneNum,
            @AttrRef("code") String code,
            @AttrRef("email") String email,
            @AttrRef("salary") Long salary,
            @AttrRef("department") Department department,
            @AttrRef("star") Integer star,
            @AttrRef("averageRating") Double averageRating
            ) throws ConstraintViolationException {
        super(id, name, gender, dob, address, phoneNum);
        this.code = nextCode(code);
        setEmail(email);
        setSalary(salary);
        setDepartment(department);

        assignments = new ArrayList<>();
        assignmentCount = 0;

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
    public boolean addAssignment(Assignment assignment) {
        if (!this.assignments.contains(assignment)) {
            assignments.add(assignment);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewAssignment(Assignment assignment) {
        assignments.add(assignment);
        assignmentCount++;
        // no other attributes changed

        computeAverageRating();

        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addAssignment(Collection<Assignment> assignments) {
        for (Assignment a : assignments) {
            if (!this.assignments.contains(a)) {
                this.assignments.add(a);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewAssignment(Collection<Assignment> assignments) {
        this.assignments.addAll(assignments);
        assignmentCount += assignments.size();

        computeAverageRating();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeAssignment(Assignment assignment) {
        boolean removed = assignments.remove(assignment);

        if (removed) {
            assignmentCount--;
            computeAverageRating();
        }
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public boolean updateAssignment(Assignment a){
        double totalRating = averageRating * assignmentCount;
        System.out.println(totalRating);
        int oldAverageRating = a.getEmployeeRating(true);
        System.out.println(oldAverageRating);
        int diff = a.getEmployeeRating() - oldAverageRating;
        System.out.println(diff);
        totalRating += diff;
        System.out.println(totalRating);
        averageRating = totalRating / assignmentCount;
        System.out.println(averageRating);
        return true;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setAssignment(Collection<Assignment> orders) {
        this.assignments = orders;

        assignmentCount = orders.size();

        computeAverageRating();
    }


    @DOpt(type=DOpt.Type.Getter)
    public Collection<Assignment> getAssignments() {
        return assignments;
    }

    /**
     * @effects
     *  return <tt>orderCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getAssignmentCount() {
        return assignmentCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setAssignmentCount(int count) {
        assignmentCount = count;
    }


    /**
     * @effects
     *  computes {@link #averageRating} of all the {@link Assignment#getEmployeeRating()}s
     *  (in {@link #assignments}.
     */
    private void computeAverageRating() {
        if (assignmentCount > 0) {
            double totalRating = 0d;
            for (Assignment s : assignments) {
                totalRating += s.getEmployeeRating();
            }
            totalRating = Math.round(totalRating * 100) ;
            averageRating = totalRating / (100 * assignmentCount);
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
