package hanu.edu.hotelsystem.services.Delivery.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.Person.model.Address;
import hanu.edu.hotelsystem.services.Person.model.Person;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RestaurantServiceOrder;

@DClass(schema = "hotelsystem" )
public abstract class Delivery {
    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.String)
    private String id;

    private static int counter;

    @DAttr(name = "restaurantServiceOrder", type = DAttr.Type.Domain, length = 20, serialisable = false)
    @DAssoc(ascName = "restaurant-service-order-has-delivery", role = "delivery",
            ascType = DAssoc.AssocType.One2One, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = RestaurantServiceOrder.class, cardMin = 1, cardMax = 1, determinant = true))
    private RestaurantServiceOrder restaurantServiceOrder;

    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    @DOpt(type=DOpt.Type.RequiredConstructor)
    public Delivery() {
        this(null, null);
    }

    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    public Delivery(@AttrRef("restaurantServiceOrder") RestaurantServiceOrder restaurantServiceOrder) {
        this(null, restaurantServiceOrder);
    }

    // from data source
    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public Delivery(@AttrRef("id") String id) {
        this(id, null);
    }

    // based constructor (used by others)
    protected Delivery(String id, RestaurantServiceOrder restaurantServiceOrder) {
        this.id = nextID(id);
        this.restaurantServiceOrder = restaurantServiceOrder;
    }

    private String nextID(String currentID) throws ConstraintViolationException {
        if (currentID == null) { // generate a new id

            return "DL" + ++counter;
        } else {
            // update id
            int num;
            try {
                num = Integer.parseInt(currentID.substring(2));
            } catch (RuntimeException e) {
                throw new ConstraintViolationException(
                        ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { currentID});
            }
            if (num > counter) {
                counter = num;
            }
            return currentID;
        }
    }

    public String getId() {
        return id;
    }

    public RestaurantServiceOrder getRestaurantServiceOrder() {
        return restaurantServiceOrder;
    }

    public void setRestaurantServiceOrder(RestaurantServiceOrder restaurantServiceOrder) {
        this.restaurantServiceOrder = restaurantServiceOrder;
    }
}
