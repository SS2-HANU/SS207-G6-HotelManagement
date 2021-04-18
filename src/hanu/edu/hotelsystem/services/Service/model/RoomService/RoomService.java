package hanu.edu.hotelsystem.services.Service.model.RoomService;


import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.Service.model.Service;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RoomServiceOrder;

import java.util.ArrayList;
import java.util.Collection;

@DClass(schema = "hotelsystem")
public class RoomService extends Service {

    @DAttr(name = "roomServiceType", type = DAttr.Type.Domain, length = 15, optional = false, cid = true)
    private RoomServiceType roomServiceType;

    @DAttr(name="roomServiceOrders",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= RoomServiceOrder.class))
    @DAssoc(ascName="room-service-has-room-service-order",role="room-service",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=RoomServiceOrder.class,
                    cardMin=1,cardMax=25))
    private Collection<RoomServiceOrder> roomServiceOrders;

    private int orderCount;


    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    @DOpt(type = DOpt.Type.RequiredConstructor)
    public RoomService(@AttrRef("price") Long price,
                       @AttrRef("roomServiceType") RoomServiceType roomServiceType) {
        this(null, price, roomServiceType);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public RoomService(@AttrRef("id") Integer id,
                       @AttrRef("price") Long price,
                       @AttrRef("roomServiceType") RoomServiceType roomServiceType) {
        super(id, price);
        setRoomServiceType(roomServiceType);

        roomServiceOrders = new ArrayList<>();
        orderCount = 0;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addRoomServiceOrder(RoomServiceOrder order) {
        if (!this.roomServiceOrders.contains(order)) {
            roomServiceOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    public RoomServiceType getRoomServiceType() {
        return roomServiceType;
    }

    public void setRoomServiceType(RoomServiceType roomServiceType) {
        this.roomServiceType = roomServiceType;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewRoomServiceOrder(RoomServiceOrder order) {
        roomServiceOrders.add(order);
        orderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addRoomServiceOrders(Collection<RoomServiceOrder> orders) {
        for (RoomServiceOrder o : orders) {
            if (!this.roomServiceOrders.contains(o)) {
                this.roomServiceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewRoomServiceOrders(Collection<RoomServiceOrder> orders) {
        this.roomServiceOrders.addAll(orders);
        orderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeRoomServiceOrder(RoomServiceOrder o) {
        boolean removed = roomServiceOrders.remove(o);

        if (removed) {
            orderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setRoomServiceOrders(Collection<RoomServiceOrder> orders) {
        this.roomServiceOrders = orders;

        orderCount = orders.size();
    }

    /**
     * @effects
     *  return <tt>orderCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getRoomServiceOrderCount() {
        return orderCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setRoomServiceOrderCount(int count) {
        orderCount = count;
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<RoomServiceOrder> getRoomServiceOrders() {
        return roomServiceOrders;
    }

}
