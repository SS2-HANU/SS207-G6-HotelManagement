package hanu.edu.hotelsystem.services.Delivery.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.RoomOrder.model.RoomOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RestaurantServiceOrder;

import java.util.Date;

@DClass(schema = "hotelsystem" )
public class RoomDelivery extends Delivery{

    @DAttr(name = "roomOrder", type = DAttr.Type.Domain, length = 10, optional = false)
    @DAssoc(ascName = "room-order-has-room-delivery", role = "room-delivery",
            ascType = DAssoc.AssocType.One2One, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = RoomOrder.class, cardMin = 1, cardMax = 1))
    private RoomOrder roomOrder;

    @DAttr(name = "servedAt", type = DAttr.Type.Date, length = 10, optional = false, format= DAttr.Format.Date)
    private Date servedAt;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    protected RoomDelivery(
            @AttrRef("roomOrder") RoomOrder roomOrder,
            @AttrRef("servedAt") Date servedAt,
            @AttrRef("restaurantServiceOrder") RestaurantServiceOrder restaurantServiceOrder){
        this(null, roomOrder, servedAt, restaurantServiceOrder);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    protected RoomDelivery(@AttrRef("id") String id,
                           @AttrRef("roomOrder") RoomOrder roomOrder,
                           @AttrRef("servedAt") Date servedAt,
                           @AttrRef("restaurantServiceOrder") RestaurantServiceOrder restaurantServiceOrder)
            throws ConstraintViolationException {
        super(id, restaurantServiceOrder);
        this.roomOrder = roomOrder;
        this.servedAt = servedAt;
    }

    public RoomOrder getRoomOrder() {
        return roomOrder;
    }

    public void setRoomOrder(RoomOrder roomOrder) {
        this.roomOrder = roomOrder;
    }

    public Date getServedAt() {
        return servedAt;
    }

    public void setServedAt(Date servedAt) {
        this.servedAt = servedAt;
    }
}
