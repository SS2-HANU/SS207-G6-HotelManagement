package hanu.edu.hotelsystem.services.ServiceOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.Person.model.Employee;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.Service.model.Service;
import hanu.edu.hotelsystem.services.Service.model.SpaService.SpaService;

import java.util.Date;

@DClass(schema = "hotelsystem" )
public class SpaServiceOrder extends ServiceOrder {
    public static final String C_code = "code";

    @DAttr(name = C_code, type = DAttr.Type.String, auto = true, length = 6,
            mutable = false, optional = false)
    private String code;
    private static int counter;

    @DAttr(name="spaService",type= DAttr.Type.Domain,length = 30, optional = false)
    @DAssoc(ascName="spa-service-has-spa-service-order",role="spa-service-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= SpaService.class,cardMin=1,cardMax=1))
    private SpaService spaService;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public SpaServiceOrder(@AttrRef("createdAt") Date createdAt,
                            @AttrRef("spaService") SpaService spaService,
                            @AttrRef("quantity") Integer quantity,
                            @AttrRef("reservation") Reservation reservation,
                           @AttrRef("rating") Integer rating,
                           @AttrRef("employee") Employee employee
    ){
        this(null, createdAt, spaService, quantity, reservation,rating,employee);
    }

    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public SpaServiceOrder(@AttrRef("code") String code,
                            @AttrRef("createdAt") Date createdAt,
                            @AttrRef("spaService") SpaService spaService,
                            @AttrRef("quantity") Integer quantity,
                            @AttrRef("reservation") Reservation reservation,
                           @AttrRef("rating") Integer rating,
                           @AttrRef("employee") Employee employee

    ) throws ConstraintViolationException {
        super(createdAt, quantity, reservation,rating, employee);
        this.code = nextCode(code);
        this.spaService = spaService;

    }

    public String getCode() {
        return code;
    }

    public SpaService getSpaService() {
        return spaService;
    }

    public void setSpaService(SpaService spaService) {
        this.spaService = spaService;
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

}
