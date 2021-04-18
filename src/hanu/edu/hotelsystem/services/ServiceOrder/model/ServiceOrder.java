package hanu.edu.hotelsystem.services.ServiceOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.cache.StateHistory;
import hanu.edu.hotelsystem.services.Assignment.model.Assignment;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.Person.model.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;


@DClass(schema = "hotelsystem")
public abstract class ServiceOrder {
    public static final String Attr_rating = "rating";

    @DAttr(name = "id", id = true, type = DAttr.Type.Integer, auto = true, length = 6,
            mutable = false, optional = false)
    private int id;
    private static int idCounter = 0;

    @DAttr(name = "createdAt", type = DAttr.Type.Date, length = 8, optional = false, format= DAttr.Format.Date)
    private Date createdAt;

    @DAttr(name = "quantity", type = DAttr.Type.Integer, optional = false, min = 1)
    private Integer quantity;

    @DAttr(name = "totalPrice", type = DAttr.Type.Long, optional = false, auto = true, mutable = false)
    protected Long totalPrice;

    @DAttr(name = Attr_rating, type = DAttr.Type.Integer, optional = false, min = 1, max = 10)
    private Integer rating;

    private StateHistory<String, Object> stateHist;

    @DAttr(name="reservation",type= DAttr.Type.Domain,length = 10, optional = false)
    @DAssoc(ascName="reservation-has-service-order",role="service-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type=Reservation.class,cardMin=1,cardMax=1))
    private Reservation reservation;

//    @DAttr(name="employee",type= DAttr.Type.Domain,length = 10, optional = false)
//    @DAssoc(ascName="employee-manages-service-order",role="service-order",
//            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
//            associate=@DAssoc.Associate(type=Employee.class,cardMin=1,cardMax=1))
//    private Employee employee;

    @DAttr(name="assignments",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= Assignment.class))
    @DAssoc(ascName="service-order-has-assignment",role="service-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=Assignment.class,
                    cardMin=1,cardMax=25))
    private Collection<Assignment> assignments;
    private int assignmentCount;


    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    public ServiceOrder(@AttrRef("createdAt") Date createdAt,
                        @AttrRef("quantity") Integer quantity,
                        @AttrRef("reservation") Reservation reservation,
                        @AttrRef("rating") Integer rating){
        this(null, createdAt, quantity,0L, reservation,rating);
    }

    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public ServiceOrder(@AttrRef("id") Integer id,
                        @AttrRef("createdAt") Date createdAt,
                        @AttrRef("quantity") Integer quantity,
                        @AttrRef("totalPrice") Long totalPrice,
                        @AttrRef("reservation") Reservation reservation,
                        @AttrRef("rating") Integer rating
    ) throws ConstraintViolationException {
        this.id = nextId(id);
        this.quantity = quantity;
        this.createdAt = createdAt;

        totalPrice = 0L;

        this.reservation= reservation;
        this.rating = rating;

        assignments = new ArrayList<>();
        assignmentCount = 0;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Integer getRating() {
        return getRating(false);
    }

    public Integer getRating(boolean cached) {
        if (cached) {
            Object val = stateHist.get(Attr_rating);

            if (val == null)
                throw new IllegalStateException(
                        "ServiceOrder.getRating: cached value is null");

            return (Integer) val;
        } else {
            if (rating != null)
                return rating;
            else
                return 0;
        }
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addAssignment(Assignment order) {
        if (!this.assignments.contains(order)) {
            assignments.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewAssignment(Assignment order) {
        assignments.add(order);
        assignmentCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addAssignments(Collection<Assignment> orders) {
        for (Assignment o : orders) {
            if (!this.assignments.contains(o)) {
                this.assignments.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewAssignments(Collection<Assignment> orders) {
        this.assignments.addAll(orders);
        assignmentCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeAssignment(Assignment o) {
        boolean removed = assignments.remove(o);

        if (removed) {
            assignmentCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setAssignments(Collection<Assignment> orders) {
        this.assignments = orders;

        assignmentCount = orders.size();
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<Assignment> getAssignments() {
        return assignments;
    }

    /**
     * @effects
     *  return <tt>assignmentCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getAssignmentCount() {
        return assignmentCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setAssignmentCount(int count) {
        assignmentCount = count;
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
                ", createdAt=" + createdAt +
                ", quantity=" + quantity +
                ", rating=" + rating +
                '}';
    }

    abstract Long computeTotalPrice();
}
