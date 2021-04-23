package hanu.edu.hotelsystem.services.Service.model.RestaurantService;

import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.Service.model.Service;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RestaurantServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.TransportationServiceOrder;

import java.util.ArrayList;
import java.util.Collection;

@DClass(schema = "hotelsystem")
public class RestaurantService extends Service {

    @DAttr(name = "dishName", type = DAttr.Type.String, length = 30, optional = false, cid = true)
    private String dishName;

    @DAttr(name = "restaurantServiceOrders", type = DAttr.Type.Collection,
            serialisable = false, optional = false,
            filter = @Select(clazz = RestaurantServiceOrder.class))
    @DAssoc(ascName = "restaurant-service-has-restaurant-service-order", role = "restaurant-service",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = RestaurantServiceOrder.class,
                    cardMin = 0, cardMax = 25))
    private Collection<RestaurantServiceOrder> restaurantServiceOrders;
    private int orderCount;


    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public RestaurantService(@AttrRef("price") Long price,
                             @AttrRef("dishName") String dishName) {
        this(null, price, 0D, dishName);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public RestaurantService(@AttrRef("id") Integer id,
                             @AttrRef("price") Long price,
                             @AttrRef("averageRating") Double averageRating,
                             @AttrRef("dishName") String dishName) {
        super(id, price, averageRating);
        this.dishName = dishName;

        restaurantServiceOrders = new ArrayList<>();
        orderCount = 0;

        averageRating = 0D;

    }

    /**
     * @effects
     *  computes {@link #averageRating} of all the {@link RestaurantServiceOrder#getRating()} ()}s
     *  (in {@link #restaurantServiceOrders}.
     */
    @Override
    public Double computeAverageMark() {
        if (orderCount > 0) {
            double totalRating = 0d;
            for (RestaurantServiceOrder t : restaurantServiceOrders) {
                totalRating += t.getRating();
            }
            totalRating = Math.round(totalRating * 100) ;
            averageRating = totalRating / (100 * orderCount);
        } else {
            averageRating = 0;
        }
        return averageRating;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public boolean updateRestaurantServiceOrder(RestaurantServiceOrder r){
        double totalRating = averageRating * orderCount;

        int oldAverageRating = r.getRating(true);

        int diff = r.getRating() - oldAverageRating;

        totalRating += diff;

        averageRating = totalRating / orderCount;

        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addRestaurantServiceOrder(RestaurantServiceOrder order) {
        if (!this.restaurantServiceOrders.contains(order)) {
            restaurantServiceOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewRestaurantServiceOrder(RestaurantServiceOrder order) {
        restaurantServiceOrders.add(order);
        orderCount++;
        // no other attributes changed
        computeAverageMark();
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addRestaurantServiceOrder(Collection<RestaurantServiceOrder> orders) {
        for (RestaurantServiceOrder o : orders) {
            if (!this.restaurantServiceOrders.contains(o)) {
                this.restaurantServiceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewRestaurantServiceOrder(Collection<RestaurantServiceOrder> orders) {
        this.restaurantServiceOrders.addAll(orders);
        orderCount += orders.size();

        computeAverageMark();
        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeRestaurantServiceOrder(RestaurantServiceOrder o) {
        boolean removed = restaurantServiceOrders.remove(o);

        if (removed) {
            orderCount--;
            computeAverageMark();
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.Getter)
    public Collection<RestaurantServiceOrder> getRestaurantServiceOrders() {
        return restaurantServiceOrders;
    }

    @DOpt(type = DOpt.Type.Setter)
    public void setRestaurantServiceOrders(Collection<RestaurantServiceOrder> restaurantServiceOrders) {
        this.restaurantServiceOrders = restaurantServiceOrders;
    }

    /**
     * @effects return <tt>orderCount</tt>
     */
    @DOpt(type = DOpt.Type.LinkCountGetter)
    public Integer getRestaurantServiceOrderCount() {
        return orderCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setRestaurantServiceOrderCount(int count) {
        orderCount = count;
    }


    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
}
