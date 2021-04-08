package hanu.edu.hotelsystem.services.RoomOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.Delivery.model.RoomDelivery;
import hanu.edu.hotelsystem.services.Room.model.Room;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


@DClass(schema = "hotelsystem")
public class RoomOrder {

    @DAttr(name = "id", id = true, type = DAttr.Type.Integer, auto = true, length = 6,
            mutable = false, optional = false)
    private int id;
    private static int idCounter = 0;

    @DAttr(name="reservation",type= DAttr.Type.Domain,length = 10, optional = false)
    @DAssoc(ascName="reservation-has-service-order",role="room-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type=Reservation.class,cardMin=1,cardMax=1))
    private Reservation reservation;


    @DAttr(name="room",type= DAttr.Type.Domain,length = 10, optional = false)
    @DAssoc(ascName="room-has-room-order",role="room-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= Room.class,cardMin=1,cardMax=1))
    private Room room;

    @DAttr(name="roomDeliveries",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= RoomDelivery.class))
    @DAssoc(ascName="room-order-has-room-delivery",role="room-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=RoomDelivery.class,
                    cardMin=0,cardMax=25))
    private Collection<RoomDelivery> roomDeliveries;
    private int roomDeliveryCount; 

    @DAttr(name = "price", type = DAttr.Type.Long, optional = false)
    private Long price;

    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    public RoomOrder(@AttrRef("reservation") Reservation reservation,
                                   @AttrRef("price") Long price,
                                   @AttrRef("room") Room room){
        this(null, reservation,price, room);
    }

    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public RoomOrder(@AttrRef("id") Integer id,
                     @AttrRef("reservation") Reservation reservation,
                     @AttrRef("price") Long price,
                     @AttrRef("room") Room room) throws ConstraintViolationException {
        this.id = nextId(id);
        this.reservation= reservation;
        this.price = price;
        this.room = room;
        
        roomDeliveries = new ArrayList<>();
        roomDeliveryCount = 0;
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomOrder roomOrder = (RoomOrder) o;
        return id == roomOrder.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RoomOrder{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", room=" + room +
                ", price=" + price +
                '}';
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addRoomDelivery(RoomDelivery order) {
        if (!this.roomDeliveries.contains(order)) {
            roomDeliveries.add(order);
        }
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewRoomDelivery(RoomDelivery order) {
        roomDeliveries.add(order);
        roomDeliveryCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addRoomDelivery(Collection<RoomDelivery> orders) {
        for (RoomDelivery o : orders) {
            if (!this.roomDeliveries.contains(o)) {
                this.roomDeliveries.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewRoomDelivery(Collection<RoomDelivery> orders) {
        this.roomDeliveries.addAll(orders);
        roomDeliveryCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeRoomDelivery(RoomDelivery o) {
        boolean removed = roomDeliveries.remove(o);

        if (removed) {
            roomDeliveryCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.Setter)
    public void setRoomDelivery(Collection<RoomDelivery> orders) {
        this.roomDeliveries = orders;

        roomDeliveryCount = orders.size();
    }

    /**
     * @effects return <tt>orderCount</tt>
     */
    @DOpt(type = DOpt.Type.LinkCountGetter)
    public Integer getRoomDeliveryCount() {
        return roomDeliveryCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setRoomDeliveryCount(int count) {
        roomDeliveryCount = count;
    }

    @DOpt(type = DOpt.Type.Getter)
    public Collection<RoomDelivery> getRoomDeliverys() {
        return roomDeliveries;
    }
}
