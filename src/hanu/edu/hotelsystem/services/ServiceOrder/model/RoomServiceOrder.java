package hanu.edu.hotelsystem.services.ServiceOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.Person.model.Employee;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.Service.model.RoomService.RoomService;

import java.util.Date;

@DClass(schema = "hotelsystem" )
public class RoomServiceOrder extends ServiceOrder{
    public static final String C_code = "code";

    @DAttr(name = C_code, type = DAttr.Type.String, auto = true, length = 6,
            mutable = false, optional = false)
    private String code;
    private static int counter;

    @DAttr(name="roomService",type= DAttr.Type.Domain,length = 30, optional = false)
    @DAssoc(ascName="room-service-has-room-service-order",role="room-service-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= RoomService.class,cardMin=1,cardMax=1))
    private RoomService roomService;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public RoomServiceOrder(@AttrRef("createdAt") Date createdAt,
                            @AttrRef("roomService") RoomService roomService,
                            @AttrRef("quantity") Integer quantity,
                            @AttrRef("reservation") Reservation reservation,
                            @AttrRef("employee") Employee employee
    ){
        this(null, createdAt,roomService, quantity, reservation,employee);
    }

    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public RoomServiceOrder(@AttrRef("code") String code,
                            @AttrRef("createdAt") Date createdAt,
                            @AttrRef("roomService") RoomService roomService,
                            @AttrRef("quantity") Integer quantity,
                            @AttrRef("reservation") Reservation reservation,
                            @AttrRef("employee") Employee employee

                            ) throws ConstraintViolationException {
        super(createdAt, quantity, reservation, employee);
        this.code = nextCode(code);
        setRoomService(roomService);
    }

    public String getCode() {
        return code;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    private String nextCode(String code) throws ConstraintViolationException {
        if (code == null) { // generate a new id
            return "RS" + ++counter;
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

}