package hanu.edu.hotelsystem.services.ServiceOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.Person.model.Employee;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.Service.model.TransportationService.TransportationService;

import java.util.Date;

@DClass(schema = "hotelsystem" )
public class TransportationServiceOrder extends ServiceOrder{
    public static final String C_code = "code";

    @DAttr(name = C_code, type = DAttr.Type.String, auto = true, length = 6,
            mutable = false, optional = false)
    private String code;
    private static int counter;

    @DAttr(name="transportationService",type= DAttr.Type.Domain,length = 30, optional = false)
    @DAssoc(ascName="transportation-service-has-transportation-service-order",role="transportation-service-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= TransportationService.class,cardMin=1,cardMax=1))
    private TransportationService transportationService;

    @DAttr(name = "distance", type = DAttr.Type.Integer, optional = false, min = 1)
    private Integer distance;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public TransportationServiceOrder(@AttrRef("createdAt") Date createdAt,
                                      @AttrRef("quantity") Integer quantity,
                                      @AttrRef("reservation") Reservation reservation,
                                      @AttrRef("rating") Integer rating,
                                      @AttrRef("transportationService") TransportationService transportationService,
                                      @AttrRef("distance") Integer distance){
        this(createdAt, quantity, 0L, reservation, rating, transportationService, distance, null);
    }

    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public TransportationServiceOrder(@AttrRef("createdAt") Date createdAt,
                                      @AttrRef("quantity") Integer quantity,
                                      @AttrRef("totalPrice") Long totalPrice,
                                      @AttrRef("reservation") Reservation reservation,
                                      @AttrRef("rating") Integer rating,
                                      @AttrRef("transportationService") TransportationService transportationService,
                                      @AttrRef("distance") Integer distance,
                                      @AttrRef("code") String code
    ) throws ConstraintViolationException {
        super(createdAt, quantity, reservation,rating);
        this.code = nextCode(code);
        this.transportationService = transportationService;
        this.distance = distance;

        computeTotalPrice();
    }

    public String getCode() {
        return code;
    }

    private String nextCode(String code) throws ConstraintViolationException {
        if (code == null) { // generate a new id
            return "TS" + ++counter;
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

    public TransportationService getTransportationService() {
        return transportationService;
    }

    public void setTransportationService(TransportationService transportationService) {
        this.transportationService = transportationService;
        computeTotalPrice();
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }



    @Override
    Long computeTotalPrice() {
        if (transportationService != null)
            totalPrice = transportationService.getPrice() * getQuantity() * getDistance();
        else
            totalPrice = 0L;
        return totalPrice;
    }
}
