package hanu.edu.hotelsystem.services.Reservation.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.exceptions.DExCode;
import hanu.edu.hotelsystem.services.ServiceOrder.model.ServiceOrder;
import hanu.edu.hotelsystem.services.RoomOrder.model.RoomOrder;
import hanu.edu.hotelsystem.services.Person.model.Customer;
import hanu.edu.hotelsystem.utils.DToolkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@DClass(schema = "hotelsystem")
public class Reservation {

    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "createdAt", type = DAttr.Type.Date, length = 15, optional = false, format= DAttr.Format.Date)
    private Date createdAt;

    @DAttr(name = "startDate", type = DAttr.Type.Date, length = 15, optional = false, format= DAttr.Format.Date)
    private Date startDate;

    @DAttr(name = "endDate", type = DAttr.Type.Date, length = 15, optional = false, format= DAttr.Format.Date)
    private Date endDate;

    @DAttr(name = "customer", type = DAttr.Type.Domain, length = 20, optional = false)
    @DAssoc(ascName = "customer-has-reservation", role = "reservation",
            ascType = DAssoc.AssocType.One2One, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = Customer.class, cardMin = 1, cardMax = 1))
    private Customer customer;


    @DAttr(name = "serviceOrders", type = DAttr.Type.Collection,
            serialisable = false, optional = false,
            filter = @Select(clazz = ServiceOrder.class))
    @DAssoc(ascName = "reservation-has-service-order", role = "reservation",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = ServiceOrder.class,
                    cardMin = 0, cardMax = 25))
    private Collection<ServiceOrder> serviceOrders;
    private int serviceOrderCount;


    @DAttr(name = "roomOrders", type = DAttr.Type.Collection,
            serialisable = false, optional = false,
            filter = @Select(clazz = RoomOrder.class))
    @DAssoc(ascName = "reservation-has-room-order", role = "reservation",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = RoomOrder.class,
                    cardMin = 0, cardMax = 25))
    private Collection<RoomOrder> roomOrders;

    private int roomOrderCount;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public Reservation(@AttrRef("createdAt") Date createdAt,
                       @AttrRef("startDate") Date startDate,
                       @AttrRef("endDate") Date endDate,
                       @AttrRef("customer") Customer customer) {
        this(null,createdAt, startDate, endDate, customer);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Reservation(@AttrRef("id") Integer id,
                       @AttrRef("createdAt") Date createdAt,
                       @AttrRef("startDate") Date startDate,
                       @AttrRef("endDate") Date endDate,
                       @AttrRef("customer") Customer customer) throws ConstraintViolationException {
        this.id = nextID(id);
        setCreatedAt(createdAt);
        setStartDate(startDate);
        setEndDate(endDate);
        this.customer = customer;

        serviceOrders = new ArrayList<>();
        serviceOrderCount = 0;

        roomOrders = new ArrayList<>();
        roomOrderCount = 0;

    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addServiceOrder(ServiceOrder order) {
        if (!this.serviceOrders.contains(order)) {
            serviceOrders.add(order);
        }
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewServiceOrder(ServiceOrder order) {
        serviceOrders.add(order);
        serviceOrderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addServiceOrder(Collection<ServiceOrder> orders) {
        for (ServiceOrder o : orders) {
            if (!this.serviceOrders.contains(o)) {
                this.serviceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewServiceOrder(Collection<ServiceOrder> orders) {
        this.serviceOrders.addAll(orders);
        serviceOrderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeServiceOrder(ServiceOrder o) {
        boolean removed = serviceOrders.remove(o);

        if (removed) {
            serviceOrderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.Setter)
    public void setServiceOrder(Collection<ServiceOrder> orders) {
        this.serviceOrders = orders;

        serviceOrderCount = orders.size();
    }

    /**
     * @effects return <tt>orderCount</tt>
     */
    @DOpt(type = DOpt.Type.LinkCountGetter)
    public Integer getServiceOrderCount() {
        return serviceOrderCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setServiceOrderCount(int count) {
        serviceOrderCount = count;
    }

    @DOpt(type = DOpt.Type.Getter)
    public Collection<ServiceOrder> getServiceOrders() {
        return serviceOrders;
    }

    public boolean addRoomOrder(RoomOrder order) {
        if (!this.roomOrders.contains(order)) {
            roomOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewRoomOrder(RoomOrder order) {
        roomOrders.add(order);
        roomOrderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addRoomOrder(Collection<RoomOrder> orders) {
        for (RoomOrder o : orders) {
            if (!this.roomOrders.contains(o)) {
                this.roomOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewRoomOrder(Collection<RoomOrder> orders) {
        this.roomOrders.addAll(orders);
        roomOrderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeRoomOrder(RoomOrder o) {
        boolean removed = roomOrders.remove(o);

        if (removed) {
            roomOrderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.Setter)
    public void setRoomOrder(Collection<RoomOrder> orders) {
        this.roomOrders = orders;

        roomOrderCount = orders.size();
    }

    /**
     * @effects return <tt>orderCount</tt>
     */
    @DOpt(type = DOpt.Type.LinkCountGetter)
    public Integer getRoomOrderCount() {
        return roomOrderCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setRoomOrderCount(int count) {
        roomOrderCount = count;
    }

    @DOpt(type = DOpt.Type.Getter)
    public Collection<RoomOrder> getRoomOrders() {
        return roomOrders;
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

    public int getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        if (createdAt.before(DToolkit.MIN_DATE)) {
            throw new ConstraintViolationException(DExCode.INVALID_CREATED_DATE, createdAt);
        }
        this.createdAt = createdAt;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate.before(DToolkit.MIN_DATE)) {
            throw new ConstraintViolationException(DExCode.INVALID_START_DATE, startDate);
        }
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate.before(DToolkit.MIN_DATE) && endDate.before(startDate))  {
            throw new ConstraintViolationException(DExCode.INVALID_END_DATE, endDate);
        }
        this.endDate = endDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", customer=" + customer +
                '}';
    }
}
