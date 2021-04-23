package hanu.edu.hotelsystem.services.Service.model.SpaService;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import hanu.edu.hotelsystem.services.Assignment.model.Assignment;
import hanu.edu.hotelsystem.services.Service.model.Service;
import hanu.edu.hotelsystem.services.ServiceOrder.model.SpaServiceOrder;

import java.util.ArrayList;
import java.util.Collection;

@DClass(schema = "hotelsystem")
public class SpaService extends Service {
    public static final String A_name = "name";

    @DAttr(name = "duration", type = DAttr.Type.Domain, length = 30, optional = false)
    private Duration duration;

//    @DAttr(name = "name", type = DAttr.Type.Domain, length = 30, optional = false, cid = true, virtual = true, auto = true, derivedFrom = {"duration"})
//    private String name;

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
    public SpaService(@AttrRef("price") Long price,
                      @AttrRef("duration") Duration duration) {
        this(null, price, 0D, duration);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public SpaService(@AttrRef("id") Integer id,
                      @AttrRef("price") Long price,
                      @AttrRef("averageRating") Double averageRating,
                      @AttrRef("duration") Duration duration
    ) throws ConstraintViolationException {
        super(id, price, averageRating);
        this.duration = duration;
//        this.name = nextName(duration);

        spaServiceOrders = new ArrayList<>();
        orderCount = 0;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

//    @DOpt(type = DOpt.Type.DerivedAttributeUpdater)
//    @AttrRef(value = A_name)
//    private String nextName(Duration duration) {
//        return duration.getName();
//    }
//
//    public String getName() {
//        return name;
//    }


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
        computeAverageMark();

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

        computeAverageMark();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    public boolean removeSpaServiceOrder(SpaServiceOrder o) {
        boolean removed = spaServiceOrders.remove(o);

        if (removed) {
            orderCount--;

            computeAverageMark();
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setSpaServiceOrders(Collection<SpaServiceOrder> orders) {
        this.spaServiceOrders = orders;

//        orderCount = orders.size();
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


    /**
     * @effects
     *  computes {@link #averageRating} of all the {@link SpaServiceOrder#getRating()} ()}s
     *  (in {@link #spaServiceOrders}.
     */
    @Override
    public Double computeAverageMark() {
        if (orderCount > 0) {
            double totalRating = 0d;
            for (SpaServiceOrder s : spaServiceOrders) {
                totalRating += s.getRating();
            }
            totalRating = Math.round(totalRating * 100) ;
            averageRating = totalRating / (100 * orderCount);
        } else {
            averageRating = 0;
        }
        return averageRating;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public boolean updateSpaServiceOrder(SpaServiceOrder s){
        double totalRating = averageRating * orderCount;

        int oldAverageRating = s.getRating(true);

        int diff = s.getRating() - oldAverageRating;

        totalRating += diff;

        averageRating = totalRating / orderCount;

        return true;
    }
}
