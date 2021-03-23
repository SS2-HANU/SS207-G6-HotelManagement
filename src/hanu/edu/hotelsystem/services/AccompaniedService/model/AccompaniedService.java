package hanu.edu.hotelsystem.services.AccompaniedService.model;

import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.AccompaniedServiceOrder.model.AccompaniedServiceOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@DClass(schema = "hotelsystem")
public class AccompaniedService {

    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "name", type = DAttr.Type.String, length = 15, optional = false)
    private String name;

    @DAttr(name = "price", type = DAttr.Type.Long, optional = false)
    private Long price;

    @DAttr(name="service-order",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= AccompaniedServiceOrder.class))
    @DAssoc(ascName="service-has-service-order",role="service",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=AccompaniedServiceOrder.class,
                    cardMin=1,cardMax=25))
    private Collection<AccompaniedServiceOrder> orders;

    private int orderCount;


    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    @DOpt(type = DOpt.Type.RequiredConstructor)
    public AccompaniedService(@AttrRef("name") String name,
                              @AttrRef("price") Long price ) {
        this(null, name, price);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public AccompaniedService(@AttrRef("id") Integer id, @AttrRef("name") String name,
                              @AttrRef("price") Long price) {
        this.id = nextID(id);
        setName(name);
        setPrice(price);

        orders = new ArrayList<>();
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
    public boolean addAccompaniedServiceOrder(AccompaniedServiceOrder order) {
        if (!this.orders.contains(order)) {
            orders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewAccompaniedServiceOrder(AccompaniedServiceOrder order) {
        orders.add(order);
        orderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addAccompaniedServiceOrder(Collection<AccompaniedServiceOrder> orders) {
        for (AccompaniedServiceOrder o : orders) {
            if (!this.orders.contains(o)) {
                this.orders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewAccompaniedServiceOrder(Collection<AccompaniedServiceOrder> orders) {
        this.orders.addAll(orders);
        orderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeAccompaniedServiceOrder(AccompaniedServiceOrder o) {
        boolean removed = orders.remove(o);

        if (removed) {
            orderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setAccompaniedServiceOrder(Collection<AccompaniedServiceOrder> orders) {
        this.orders = orders;

        orderCount = orders.size();
    }

    /**
     * @effects
     *  return <tt>orderCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getAccompaniedServiceOrderCount() {
        return orderCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setAccompaniedServiceOrderCount(int count) {
        orderCount = count;
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<AccompaniedServiceOrder> getAccompaniedServiceOrders() {
        return orders;
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
        AccompaniedService that = (AccompaniedService) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
