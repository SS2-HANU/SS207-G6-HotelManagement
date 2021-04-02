package hanu.edu.hotelsystem.services.Service.model.TransportationService;

import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.ServiceOrder.model.TransportationServiceOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@DClass(schema = "hotelsystem")
public class TransportationService {
    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "vehicle", type = DAttr.Type.String, length = 30, optional = false)
    private String vehicle;

    @DAttr(name = "price", type = DAttr.Type.Long, optional = false)
    private Long price;

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
    @DOpt(type = DOpt.Type.RequiredConstructor)
    public TransportationService(@AttrRef("vehicle") String vehicle,
                      @AttrRef("price") Long price ) {
        this(null, vehicle, price);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public TransportationService(@AttrRef("id") Integer id, @AttrRef("type") String vehicle,
                      @AttrRef("price") Long price) {
        this.id = nextID(id);
        this.vehicle = vehicle;
        setPrice(price);

        transportationServiceOrders = new ArrayList<>();
        orderCount = 0;
    }

    public int getId() {
        return id;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
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

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeTransportationServiceOrder(TransportationServiceOrder o) {
        boolean removed = transportationServiceOrders.remove(o);

        if (removed) {
            orderCount--;
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
    public Integer getTransportationServiceOrderCount() {
        return orderCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setTransportationServiceOrderCount(int count) {
        orderCount = count;
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<TransportationServiceOrder> getTransportationServiceOrders() {
        return transportationServiceOrders;
    }

    public void setTransportationServiceOrders(Collection<TransportationServiceOrder> transportationServiceOrders) {
        this.transportationServiceOrders = transportationServiceOrders;
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

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransportationService that = (TransportationService) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
