package hanu.edu.hotelsystem.services.Person.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RestaurantServiceOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@DClass(schema = "hotelsystem" )
public class Customer extends Person{
    public static final String C_code = "code";

    @DAttr(name = C_code, type = DAttr.Type.String, auto = true, length = 6,
            mutable = false, optional = false)
    private String code;
    private static int counter;

    @DAttr(name="reservations",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= Reservation.class))
    @DAssoc(ascName="customer-has-reservation",role="customer",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=Reservation.class,
                    cardMin=0,cardMax=25))
    private Collection<Reservation> reservations;
    private int reservationsCount;

    @DAttr(name="restaurantServiceOrders",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= RestaurantServiceOrder.class))
    @DAssoc(ascName="customer-has-restaurant-service-order",role="customer",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=RestaurantServiceOrder.class,
                    cardMin=1,cardMax=25))
    private Collection<RestaurantServiceOrder> restaurantServiceOrders;
    private int restaurantServiceOrderCount;

    
    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public Customer(
            @AttrRef("name") String name,
            @AttrRef("gender") Gender gender,
            @AttrRef("dob") Date dob,
            @AttrRef("address") Address address,
            @AttrRef("phoneNum") String phoneNum){
        this(null, name, gender,dob, address, phoneNum);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Customer(
            @AttrRef("code") String code,
            @AttrRef("name") String name,
            @AttrRef("gender") Gender gender,
            @AttrRef("dob") Date dob,
            @AttrRef("address") Address address,
            @AttrRef("phoneNum") String phoneNum
    ) throws ConstraintViolationException {
        super(name, gender, dob, address, phoneNum);
        this.code = nextCode(code);
        
        reservations = new ArrayList<>();
        reservationsCount =0;

        restaurantServiceOrders = new ArrayList<>();
        restaurantServiceOrderCount = 0;
        
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    private String nextCode(String code) throws ConstraintViolationException {
        if (code == null) { // generate a new id

            return "C" + ++counter;
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

    @DOpt(type=DOpt.Type.LinkAdder)
    //only need to do this for reflexive association: @MemberRef(name="reservations")  
    public boolean addReservation(Reservation r) {
        if (!this.reservations.contains(r)) {
            reservations.add(r);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewReservation(Reservation r) {
        reservations.add(r);
        reservationsCount++;

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addReservation(Collection<Reservation> reservations) {
        for (Reservation r : reservations) {
            if (!this.reservations.contains(r)) {
                this.reservations.add(r);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewReservation(Collection<Reservation> reservations) {
        this.reservations.addAll(reservations);
        reservationsCount += reservations.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="reservations")
    public boolean removeReservation(Reservation r) {
        boolean removed = reservations.remove(r);

        if (removed) {
            reservationsCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setReservations(Collection<Reservation> reservations) {
        this.reservations = reservations;

        reservationsCount = reservations.size();
    }

    /**
     * @effects
     *  return <tt>reservationsCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getReservationsCount() {
        return reservationsCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setReservationsCount(int count) {
        reservationsCount = count;
    }

    public String getCode() {
        return code;
    }

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addRestaurantServiceOrder(RestaurantServiceOrder order) {
        if (!this.restaurantServiceOrders.contains(order)) {
            restaurantServiceOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewRestaurantServiceOrder(RestaurantServiceOrder order) {
        restaurantServiceOrders.add(order);
        restaurantServiceOrderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addRestaurantServiceOrder(Collection<RestaurantServiceOrder> orders) {
        for (RestaurantServiceOrder o : orders) {
            if (!this.restaurantServiceOrders.contains(o)) {
                this.restaurantServiceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewRestaurantServiceOrder(Collection<RestaurantServiceOrder> orders) {
        this.restaurantServiceOrders.addAll(orders);
        restaurantServiceOrderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeRestaurantServiceOrder(RestaurantServiceOrder o) {
        boolean removed = restaurantServiceOrders.remove(o);

        if (removed) {
            restaurantServiceOrderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)

    /**
     * @effects
     *  return <tt>orderCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getRestaurantServiceOrderCount() {
        return restaurantServiceOrderCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setRestaurantServiceOrderCount(int count) {
        restaurantServiceOrderCount = count;
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<RestaurantServiceOrder> getRestaurantServiceOrders() {
        return restaurantServiceOrders;
    }

    public void setRestaurantServiceOrders(Collection<RestaurantServiceOrder> restaurantServiceOrders) {
        this.restaurantServiceOrders = restaurantServiceOrders;
    }
}
