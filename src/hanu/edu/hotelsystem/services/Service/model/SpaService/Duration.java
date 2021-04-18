package hanu.edu.hotelsystem.services.Service.model.SpaService;

import domainapp.basics.model.meta.DAttr;

public enum Duration {
    C60_MINUTES,
    C90_MINUTES,
    C120_MINUTES;


    @DAttr(name="name", type= DAttr.Type.String, id=true, length=20)
    public String getName() {
        return this.name();
    }
}
