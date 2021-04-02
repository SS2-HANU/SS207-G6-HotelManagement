package hanu.edu.hotelsystem.services.Service.model.RoomService;

import domainapp.basics.model.meta.DAttr;

public enum RoomServiceType {
    MAKEUP_ROOM("Dọn Phòng"),
    TURN_DOWN_SERVICE("Trang trí phòng"),
    SHOESHINE("Đánh giày"),
    LAUNDRY("Giặt là"),
    BELL_SERVICE("Hỗ trợ hành lý");

    private final String name;

    RoomServiceType(String name) {
        this.name = name;
    }

    @DAttr(name = "name", type = DAttr.Type.String, id = true, length = 25)
    public String getName() {
        return name;
    }
}
