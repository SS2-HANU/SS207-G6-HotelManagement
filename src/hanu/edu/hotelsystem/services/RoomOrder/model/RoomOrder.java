package hanu.edu.hotelsystem.services.RoomOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.Room.model.Room;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;


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


    @DAttr(name="service",type= DAttr.Type.Domain,length = 10, optional = false)
    @DAssoc(ascName="room-has-room-order",role="room-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= Room.class,cardMin=1,cardMax=1))
    private Room room;

    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    public RoomOrder(@AttrRef("reservation") Reservation reservation,
                                   @AttrRef("room") Room room){
        this(null, reservation, room);
    }

    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public RoomOrder(@AttrRef("id") Integer id,
                                   @AttrRef("reservation") Reservation reservation,
                                  @AttrRef("room") Room room) throws ConstraintViolationException {
        this.id = nextId(id);
        this.reservation= reservation;
        this.room = room;
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

}
