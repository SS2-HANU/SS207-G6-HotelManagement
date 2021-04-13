package hanu.edu.hotelsystem.services.Delivery.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.ServiceOrder.model.RestaurantServiceOrder;

@DClass(schema = "hotelsystem")
public class InplaceDelivery extends Delivery {
    @DAttr(name = "tableNo", type = DAttr.Type.Integer, optional = false, min = 1, max = 100)
    private Integer tableNo;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public InplaceDelivery(
            @AttrRef("tableNo") Integer tableNo,
            @AttrRef("restaurantServiceOrder") RestaurantServiceOrder restaurantServiceOrder) {
        this(null, tableNo, restaurantServiceOrder);
    }

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    @DOpt(type = DOpt.Type.RequiredConstructor)
    public InplaceDelivery(
            @AttrRef("tableNo") Integer tableNo) {
        this(null, tableNo, null);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public InplaceDelivery(@AttrRef("id") String id,
                           @AttrRef("tableNo") Integer tableNo) {
        this(id, tableNo, null);
    }

    public InplaceDelivery(String id, Integer tableNo, RestaurantServiceOrder restaurantServiceOrder) {
        super(id, restaurantServiceOrder);
        this.tableNo = tableNo;
    }


    public Integer getTableNo() {
        return tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }
}
