package hanu.edu.hotelsystem.services.ServiceOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.util.cache.StateHistory;
import hanu.edu.hotelsystem.services.Delivery.model.Delivery;
import hanu.edu.hotelsystem.services.Person.model.Customer;
import hanu.edu.hotelsystem.services.Service.model.RestaurantService.RestaurantService;

import java.util.Date;
import java.util.Objects;

@DClass(schema = "hotelsystem" )
public class RestaurantServiceOrder {
    public static final String Attr_rating = "rating";

    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "createdAt", type = DAttr.Type.Date, length = 15, optional = false, format= DAttr.Format.Date)
    private Date createdAt;

    @DAttr(name = "customer", type = DAttr.Type.Domain, length = 20, optional = false)
    @DAssoc(ascName = "customer-has-restaurant-service-order", role = "restaurant-service-order",
            ascType = DAssoc.AssocType.One2One, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = Customer.class, cardMin = 1, cardMax = 1))
    private Customer customer;

    @DAttr(name = "restaurantService", type = DAttr.Type.Domain, length = 20, optional = false)
    @DAssoc(ascName = "restaurant-service-has-restaurant-service-order", role = "restaurant-service-order",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.Many,
            associate = @DAssoc.Associate(type = RestaurantService.class, cardMin = 1, cardMax = 1))
    private RestaurantService restaurantService;

    @DAttr(name = "delivery", type = DAttr.Type.Domain, length = 20, optional = false)
    @DAssoc(ascName = "restaurant-service-order-has-delivery", role = "restaurant-service-order",
            ascType = DAssoc.AssocType.One2One, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = Delivery.class, cardMin = 1, cardMax = 1))
    private Delivery delivery;

    @DAttr(name = "quantity", type = DAttr.Type.Integer, optional = false, min = 1, max = 10)
    private Integer quantity;

    @DAttr(name = Attr_rating, type = DAttr.Type.Integer, optional = false, min = 1, max = 10)
    private Integer rating;

    private StateHistory<String, Object> stateHist;

    @DAttr(name = "totalPrice", type = DAttr.Type.Long, optional = false)
    private Long totalPrice;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public RestaurantServiceOrder(@AttrRef("customer") Customer customer,
                                  @AttrRef("createdAt") Date createdAt,
                                  @AttrRef("restaurantService") RestaurantService restaurantService,
                                  @AttrRef("quantity") Integer quantity,
                                  @AttrRef("delivery") Delivery delivery,
                                  @AttrRef("rating") Integer rating,
                                  @AttrRef("totalPrice") Long totalPrice){
        this(null, customer,createdAt, restaurantService, quantity,delivery, rating, totalPrice);

    }
    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public RestaurantServiceOrder(@AttrRef("id") Integer id,
                                  @AttrRef("customer") Customer customer,
                                  @AttrRef("createdAt") Date createdAt,
                                  @AttrRef("restaurantService") RestaurantService restaurantService,
                                  @AttrRef("quantity") Integer quantity,
                                  @AttrRef("delivery") Delivery delivery,
                                  @AttrRef("rating") Integer rating,
                                  @AttrRef("totalPrice") Long totalPrice)
            throws ConstraintViolationException {
        this.id = nextID(id);
        this.customer = customer;
        this.createdAt = createdAt;
        this.restaurantService = restaurantService;
        this.quantity = quantity;
        this.delivery = delivery;
        this.rating = rating;
        this.totalPrice = totalPrice;
    }

    private static int nextID(Integer currID) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public RestaurantService getRestaurantService() {
        return restaurantService;
    }

    public void setRestaurantService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRating() {
        return getRating(false);
    }

    public Integer getRating(boolean cached) {
        if (cached) {
            Object val = stateHist.get(Attr_rating);

            if (val == null)
                throw new IllegalStateException(
                        "ServiceOrder.getRating: cached value is null");

            return (Integer) val;
        } else {
            if (rating != null)
                return rating;
            else
                return 0;
        }
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantServiceOrder that = (RestaurantServiceOrder) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
