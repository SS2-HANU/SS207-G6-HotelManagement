package hanu.edu.hotelsystem.services.Room.model;

import domainapp.basics.model.meta.DAttr;

public enum RoomType {
    VIP,
    NORMAL;

    @DAttr(name="name", type= DAttr.Type.String, id=true, length=10)
    public String getName() {
        return name();
    }
}
