package hanu.edu.hotelsystem.services.Service.model;

import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.ServiceOrder.model.ServiceOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @overview Represents an accompanied service. The service ID is auto-incremented from 0

 * @author nguyen minh chau
 * @version 2.0
 */

@DClass(schema = "hotelsystem")
public class Service {

    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "name", type = DAttr.Type.String, length = 15, optional = false)
    private String name;

    @DAttr(name = "price", type = DAttr.Type.Long, optional = false)
    private Long price;

    @DAttr(name="serviceOrders",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= ServiceOrder.class))
    @DAssoc(ascName="service-has-service-order",role="service",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=ServiceOrder.class,
                    cardMin=1,cardMax=25))
    private Collection<ServiceOrder> serviceOrders;

    private int orderCount;


    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    @DOpt(type = DOpt.Type.RequiredConstructor)
    public Service(@AttrRef("name") String name,
                              @AttrRef("price") Long price ) {
        this(null, name, price);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Service(@AttrRef("id") Integer id, @AttrRef("name") String name,
                              @AttrRef("price") Long price) {
        this.id = nextID(id);
        setName(name);
        setPrice(price);

        serviceOrders = new ArrayList<>();
        orderCount = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addServiceOrder(ServiceOrder order) {
        if (!this.serviceOrders.contains(order)) {
            serviceOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewServiceOrder(ServiceOrder order) {
        serviceOrders.add(order);
        orderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addServiceOrder(Collection<ServiceOrder> orders) {
        for (ServiceOrder o : orders) {
            if (!this.serviceOrders.contains(o)) {
                this.serviceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewServiceOrder(Collection<ServiceOrder> orders) {
        this.serviceOrders.addAll(orders);
        orderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeServiceOrder(ServiceOrder o) {
        boolean removed = serviceOrders.remove(o);

        if (removed) {
            orderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setServiceOrder(Collection<ServiceOrder> orders) {
        this.serviceOrders = orders;

        orderCount = orders.size();
    }

    /**
     * @effects
     *  return <tt>orderCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getServiceOrderCount() {
        return orderCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setServiceOrderCount(int count) {
        orderCount = count;
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<ServiceOrder> getServiceOrders() {
        return serviceOrders;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service that = (Service) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
