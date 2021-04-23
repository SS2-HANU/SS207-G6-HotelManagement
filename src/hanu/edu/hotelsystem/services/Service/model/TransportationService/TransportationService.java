package hanu.edu.hotelsystem.services.Service.model.TransportationService;

import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.Service.model.Service;
import hanu.edu.hotelsystem.services.ServiceOrder.model.SpaServiceOrder;
import hanu.edu.hotelsystem.services.ServiceOrder.model.TransportationServiceOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@DClass(schema = "hotelsystem")
public class TransportationService extends Service {


    @DAttr(name = "vehicle", type = DAttr.Type.String, length = 30, optional = false, cid = true)
    private String vehicle;


    @DAttr(name="transportationServiceOrders",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= TransportationServiceOrder.class))
    @DAssoc(ascName="transportation-service-has-transportation-service-order",role="transportation-service",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=TransportationServiceOrder.class,
                    cardMin=1,cardMax=25))
    private Collection<TransportationServiceOrder> transportationServiceOrders;

    private int orderCount;


    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public TransportationService(@AttrRef("price") Long price,
                                 @AttrRef("vehicle") String vehicle) {
        this(null, price,0D, vehicle);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public TransportationService(@AttrRef("id") Integer id,
                                 @AttrRef("price") Long price,
                                 @AttrRef("averageRating") Double averageRating,
                                 @AttrRef("vehicle") String vehicle) {
        super(id, price, averageRating);
        this.vehicle = vehicle;

        transportationServiceOrders = new ArrayList<>();
        orderCount = 0;
    }


    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * @effects
     *  computes {@link #averageRating} of all the {@link TransportationServiceOrder#getRating()} ()}s
     *  (in {@link #transportationServiceOrders}.
     */
    @Override
    public Double computeAverageMark() {
        if (orderCount > 0) {
            double totalRating = 0d;
            for (TransportationServiceOrder t : transportationServiceOrders) {
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
    public boolean updateTransportationServiceOrder(TransportationServiceOrder t){
        double totalRating = averageRating * orderCount;

        int oldAverageRating = t.getRating(true);

        int diff = t.getRating() - oldAverageRating;

        totalRating += diff;

        averageRating = totalRating / orderCount;

        return true;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addTransportationServiceOrder(TransportationServiceOrder order) {
        if (!this.transportationServiceOrders.contains(order)) {
            transportationServiceOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewTransportationServiceOrder(TransportationServiceOrder order) {
        transportationServiceOrders.add(order);
        orderCount++;
        // no other attributes changed
        computeAverageMark();

        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addTransportationServiceOrder(Collection<TransportationServiceOrder> orders) {
        for (TransportationServiceOrder o : orders) {
            if (!this.transportationServiceOrders.contains(o)) {
                this.transportationServiceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewTransportationServiceOrder(Collection<TransportationServiceOrder> orders) {
        this.transportationServiceOrders.addAll(orders);
        orderCount += orders.size();

        computeAverageMark();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    public boolean removeTransportationServiceOrder(TransportationServiceOrder o) {
        boolean removed = transportationServiceOrders.remove(o);

        if (removed) {
            orderCount--;
            computeAverageMark();
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setTransportationServiceOrders(Collection<TransportationServiceOrder> transportationServiceOrders) {
        this.transportationServiceOrders = transportationServiceOrders;
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<TransportationServiceOrder> getTransportationServiceOrders() {
        return transportationServiceOrders;
    }

        /**
         * @effects
         *  return <tt>orderCount</tt>
         */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getTransportationServiceOrderCount() {
        return orderCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setTransportationServiceOrderCount(int count) {
        orderCount = count;
    }

}
