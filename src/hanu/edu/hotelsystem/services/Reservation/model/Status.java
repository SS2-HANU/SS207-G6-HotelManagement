package hanu.edu.hotelsystem.services.Reservation.model;

import domainapp.basics.model.meta.DAttr;

public enum Status {
    CANCELLED,
    COMPLETED,
    SERVING,
    RESERVED,
    UNDEFINED;

    @DAttr(name = "name", type = DAttr.Type.String, id = true, length = 10)
    public String getName() {
        return name();
    }
}
