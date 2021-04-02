package hanu.edu.hotelsystem.services.Service.model.TransportationService;

import domainapp.basics.model.meta.DAttr;

public enum Vehicle {
    MOTORBIKE,
    CAR_4_SEATER,
    ;

    @DAttr(name="name", type= DAttr.Type.String, id=true, length=20)
    public String getName() {
        return name();
    }
}
