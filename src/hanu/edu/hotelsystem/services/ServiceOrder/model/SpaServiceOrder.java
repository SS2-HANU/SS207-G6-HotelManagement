package hanu.edu.hotelsystem.services.ServiceOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.Service.model.SpaService.SpaService;

import java.util.Date;
import java.util.Objects;

@DClass(schema = "hotelsystem" )
public class SpaServiceOrder extends ServiceOrder {
    public static final String C_code = "code";

    @DAttr(name = C_code, type = DAttr.Type.String, auto = true, length = 6,
            mutable = false, optional = false)
    private String code;
    private static int counter;

    @DAttr(name="spaService",type= DAttr.Type.Domain,length = 20, optional = false)
    @DAssoc(ascName="spa-service-has-spa-service-order",role="spa-service-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= SpaService.class,cardMin=1,cardMax=1))
    private SpaService spaService;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public SpaServiceOrder(@AttrRef("createdAt") Date createdAt,
                           @AttrRef("quantity") Integer quantity,
                           @AttrRef("reservation") Reservation reservation,
                           @AttrRef("rating") Integer rating,
                           @AttrRef("spaService") SpaService spaService
    ){
        this(createdAt, quantity, 0L,reservation, rating, spaService,null );
    }

    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public SpaServiceOrder(@AttrRef("createdAt") Date createdAt,
                           @AttrRef("quantity") Integer quantity,
                           @AttrRef("totalPrice") Long totalPrice,
                           @AttrRef("reservation") Reservation reservation,
                           @AttrRef("rating") Integer rating,
                           @AttrRef("spaService") SpaService spaService,
                           @AttrRef("code") String code
    ) throws ConstraintViolationException {
        super(createdAt, quantity, reservation,rating);
        this.code = nextCode(code);
        this.spaService = spaService;

        computeTotalPrice();
    }

    public String getCode() {
        return code;
    }

    private String nextCode(String code) throws ConstraintViolationException {
        if (code == null) { // generate a new id
            return "SS" + ++counter;
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

    public SpaService getSpaService() {
        return spaService;
    }

    public void setSpaService(SpaService spaService) {
        this.spaService = spaService;
        computeTotalPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpaServiceOrder that = (SpaServiceOrder) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code);
    }

    @Override
    public String toString() {
        return "SpaServiceOrder{" +
                "totalPrice=" + totalPrice +
                ", code='" + code + '\'' +
                ", spaService=" + spaService +
                '}';
    }

    @Override
    Long computeTotalPrice() {
        if (spaService != null)
            totalPrice = spaService.getPrice() * getQuantity();
        else
            totalPrice = 0L;
        return totalPrice;
    }
}
