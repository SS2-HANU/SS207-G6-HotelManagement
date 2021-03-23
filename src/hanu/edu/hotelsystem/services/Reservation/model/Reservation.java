package hanu.edu.hotelsystem.services.Reservation.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.AccompaniedServiceOrder.model.AccompaniedServiceOrder;
import hanu.edu.hotelsystem.services.RoomOrder.model.RoomOrder;
import hanu.edu.hotelsystem.services.person.model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@DClass(schema = "hotelsystem")
public class Reservation {

    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "customer", type = DAttr.Type.Domain, length = 20, optional = false)
    @DAssoc(ascName = "customer-has-reservation", role = "reservation",
            ascType = DAssoc.AssocType.One2One, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = Customer.class, cardMin = 1, cardMax = 1))
    private Customer customer;


    @DAttr(name = "service-order", type = DAttr.Type.Collection,
            serialisable = false, optional = false,
            filter = @Select(clazz = AccompaniedServiceOrder.class))
    @DAssoc(ascName = "reservation-has-service-order", role = "reservation",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = AccompaniedServiceOrder.class,
                    cardMin = 0, cardMax = 25))
    private Collection<AccompaniedServiceOrder> serviceOrders;
    private int serviceOrderCount;


    @DAttr(name = "room-order", type = DAttr.Type.Collection,
            serialisable = false, optional = false,
            filter = @Select(clazz = RoomOrder.class))
    @DAssoc(ascName = "reservation-has-room-order", role = "reservation",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = RoomOrder.class,
                    cardMin = 0, cardMax = 25))
    private Collection<RoomOrder> roomOrders;

    private int roomOrderCount;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public Reservation(@AttrRef("customer") Customer customer) {
        this(null, customer);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Reservation(@AttrRef("id") Integer id,
                       @AttrRef("customer") Customer customer) throws ConstraintViolationException {
        this.id = nextID(id);
        this.customer = customer;

        serviceOrders = new ArrayList<>();
        serviceOrderCount = 0;

        roomOrders = new ArrayList<>();
        roomOrderCount = 0;

    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addAccompaniedServiceOrder(AccompaniedServiceOrder order) {
        if (!this.serviceOrders.contains(order)) {
            serviceOrders.add(order);
        }
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewAccompaniedServiceOrder(AccompaniedServiceOrder order) {
        serviceOrders.add(order);
        serviceOrderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addAccompaniedServiceOrder(Collection<AccompaniedServiceOrder> orders) {
        for (AccompaniedServiceOrder o : orders) {
            if (!this.serviceOrders.contains(o)) {
                this.serviceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewAccompaniedServiceOrder(Collection<AccompaniedServiceOrder> orders) {
        this.serviceOrders.addAll(orders);
        serviceOrderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeAccompaniedServiceOrder(AccompaniedServiceOrder o) {
        boolean removed = serviceOrders.remove(o);

        if (removed) {
            serviceOrderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.Setter)
    public void setAccompaniedServiceOrder(Collection<AccompaniedServiceOrder> orders) {
        this.serviceOrders = orders;

        serviceOrderCount = orders.size();
    }

    /**
     * @effects return <tt>orderCount</tt>
     */
    @DOpt(type = DOpt.Type.LinkCountGetter)
    public Integer getAccompaniedServiceOrderCount() {
        return serviceOrderCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setAccompaniedServiceOrderCount(int count) {
        serviceOrderCount = count;
    }

    @DOpt(type = DOpt.Type.Getter)
    public Collection<AccompaniedServiceOrder> getAccompaniedServiceOrders() {
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
                ", customer=" + customer +
                ", serviceOrders=" + serviceOrders +
                ", serviceOrderCount=" + serviceOrderCount +
                ", roomOrders=" + roomOrders +
                ", roomOrderCount=" + roomOrderCount +
                '}';
    }
}
