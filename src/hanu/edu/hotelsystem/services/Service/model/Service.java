package hanu.edu.hotelsystem.services.Service.model;

import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;

import java.util.Objects;

@DClass(schema = "hotelsystem")
public abstract class Service {
    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "price", type = DAttr.Type.Long, optional = false)
    private Long price;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public Service(@AttrRef("price") Long price ) {
        this(null, price);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Service(@AttrRef("id") Integer id,
                   @AttrRef("price") Long price) {
        this.id = nextID(id);
        this.price = price;
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
        Service service = (Service) o;
        return id == service.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", price=" + price +
                '}';
    }
}
