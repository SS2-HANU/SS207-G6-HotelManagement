package hanu.edu.hotelsystem.services.Service.model.SpaService;

import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.Service.model.Service;
import hanu.edu.hotelsystem.services.ServiceOrder.model.SpaServiceOrder;

import java.util.ArrayList;
import java.util.Collection;

@DClass(schema = "hotelsystem")
public class SpaService extends Service {


    @DAttr(name = "type", type = DAttr.Type.Domain, length = 30, optional = false, cid = true)
    private Duration type;


    @DAttr(name="spaServiceOrders",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= SpaServiceOrder.class))
    @DAssoc(ascName="spa-service-has-spa-service-order",role="spa-service",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=SpaServiceOrder.class,
                    cardMin=1,cardMax=25))
    private Collection<SpaServiceOrder> spaServiceOrders;

    private int orderCount;


    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public SpaService(@AttrRef("type") Duration type,
                       @AttrRef("price") Long price ) {
        this(null, type, price);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public SpaService(@AttrRef("id") Integer id, @AttrRef("type") Duration type,
                       @AttrRef("price") Long price) {
        super(id, price);
        this.type = type;

        spaServiceOrders = new ArrayList<>();
        orderCount = 0;
    }


    public Duration getType() {
        return type;
    }

    public void setType(Duration type) {
        this.type = type;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addSpaServiceOrder(SpaServiceOrder order) {
        if (!this.spaServiceOrders.contains(order)) {
            spaServiceOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewSpaServiceOrder(SpaServiceOrder order) {
        spaServiceOrders.add(order);
        orderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addSpaServiceOrders(Collection<SpaServiceOrder> orders) {
        for (SpaServiceOrder o : orders) {
            if (!this.spaServiceOrders.contains(o)) {
                this.spaServiceOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewSpaServiceOrders(Collection<SpaServiceOrder> orders) {
        this.spaServiceOrders.addAll(orders);
        orderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeSpaServiceOrder(SpaServiceOrder o) {
        boolean removed = spaServiceOrders.remove(o);

        if (removed) {
            orderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setSpaServiceOrders(Collection<SpaServiceOrder> orders) {
        this.spaServiceOrders = orders;

        orderCount = orders.size();
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<SpaServiceOrder> getSpaServiceOrders() {
        return spaServiceOrders;
    }

    /**
     * @effects
     *  return <tt>orderCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getSpaServiceOrderCount() {
        return orderCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setSpaServiceOrderCount(int count) {
        orderCount = count;
    }




}
