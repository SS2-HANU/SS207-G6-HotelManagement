package hanu.edu.hotelsystem.services.Department.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;
import hanu.edu.hotelsystem.services.person.model.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@DClass(schema = "hotelsystem")
public class Department {

    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    // candidate identifier
    @DAttr(name = "name", length = 20, type = DAttr.Type.String, optional = false, cid = true)
    private String name;

    @DAttr(name = "employees", type = DAttr.Type.Collection,
            serialisable = false, optional = false,
            filter = @Select(clazz = Employee.class))
    @DAssoc(ascName = "department-has-employee", role = "department",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = Employee.class,
                    cardMin = 1, cardMax = 25))
    private Collection<Employee> employees;

    // derived attributes
    private int employeesCount;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    @DOpt(type = DOpt.Type.RequiredConstructor)
    public Department(@AttrRef("name") String name) {
        this(null, name);
    }

    // constructor to create objects from data source
    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Department(@AttrRef("id") Integer id, @AttrRef("name") String name) {
        this.id = nextID(id);
        this.name = name;

        employees = new ArrayList<>();
        employeesCount = 0;
    }

    @DOpt(type = DOpt.Type.Setter)
    public void setName(String name) {
        this.name = name;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addEmployee(Employee e) {
        if (!this.employees.contains(e)) {
            employees.add(e);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewEmployee(Employee e) {
        employees.add(e);
        employeesCount++;

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addEmployee(Collection<Employee> employees) {
        for (Employee e : employees) {
            if (!this.employees.contains(e)) {
                this.employees.add(e);
            }
        }
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewEmployee(Collection<Employee> employees) {
        this.employees.addAll(employees);
        employeesCount += employees.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeEmployee(Employee s) {
        boolean removed = employees.remove(s);

        if (removed) {
            employeesCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.Setter)
    public void setEmployees(Collection<Employee> employees) {
        this.employees = employees;

        employeesCount = employees.size();
    }

    /**
     * @effects return <tt>studentsCount</tt>
     */
    @DOpt(type = DOpt.Type.LinkCountGetter)
    public Integer getEmployeesCount() {
        return employeesCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setEmployeesCount(int count) {
        employeesCount = count;
    }

    @DOpt(type = DOpt.Type.Getter)
    public String getName() {
        return name;
    }

    @DOpt(type = DOpt.Type.Getter)
    public Collection<Employee> getStudents() {
        return employees;
    }

    @DOpt(type = DOpt.Type.Getter)
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Department(" + getId() + "," + getName() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Department other = (Department) obj;
        if (id != other.id)
            return false;
        return true;
    }

    private static int nextID(Integer currID) {
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

    /**
     * @requires minVal != null /\ maxVal != null
     * @effects update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
     */
    @DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
    public static void updateAutoGeneratedValue(
            DAttr attrib,
            Tuple derivingValue,
            Object minVal,
            Object maxVal) throws ConstraintViolationException {

        if (minVal != null && maxVal != null) {
            if (attrib.name().equals("id")) {
                int maxIdVal = (Integer) maxVal;
                if (maxIdVal > idCounter)
                    idCounter = maxIdVal;
            }
        }
    }
}

