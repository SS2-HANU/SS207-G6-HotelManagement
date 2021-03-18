package hanu.edu.hotelsystem.services.Room.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;

import hanu.edu.hotelsystem.services.RoomOrder.model.RoomOrder;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@DClass(schema = "hotelsystem")
public class Room {
    protected static final double MIN_FLOOR = 1;
    protected static final double MAX_FLOOR = 10;

    public static final String R_id = "id";
    public static final String R_name = "name";
    public static final String R_floor = "floorNum";
    public static final String R_price = "price";
    public static final String R_type = "roomType";


    @DAttr(name = R_id, id = true, auto = true, length = 6, mutable = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = R_name, auto = true, type = DAttr.Type.String, length = 6,mutable = false, optional = false,
            serialisable=false,derivedFrom = {R_floor})
    private String name;

    @DAttr(name = R_floor, type = DAttr.Type.Integer, optional = false, min = 1, max = 10)
    private Integer floorNum;

    @DAttr(name = R_price, type = DAttr.Type.Long, optional = false)
    private Long price;

    @DAttr(name = R_type, type = DAttr.Type.Domain, length = 10, optional = false)
    private RoomType roomType;

    @DAttr(name="room",type= DAttr.Type.Collection,
            serialisable=false,optional=false,
            filter=@Select(clazz= RoomOrder.class))
    @DAssoc(ascName="room-has-room-order",role="room",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=RoomOrder.class,
                    cardMin=1,cardMax=25))
    private Collection<RoomOrder> roomOrders;

    private int roomOrderCount;


//    @DAttr(name = "reservations", type = DAttr.Type.Collection,
//            serialisable = false, optional = false,
//            filter = @Select(clazz = Reservation.class))
//    @DAssoc(ascName = "room-has-reservations", role = "room",
//            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
//            associate = @DAssoc.Associate(type = Reservation.class, cardMin = 1, cardMax = MetaConstants.CARD_MORE))
//    private Collection<Reservation> reservations ;

    private static final Map<Tuple, Integer> currNums = new LinkedHashMap<>();

    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    @DOpt(type=DOpt.Type.RequiredConstructor)
    public Room(
            @AttrRef("floorNum") Integer floorNum,
            @AttrRef("price") Long price,
            @AttrRef("roomType") RoomType roomType) throws ConstraintViolationException{
        this(null, null, floorNum, price, roomType );
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Room(
            @AttrRef("id") Integer id,
            @AttrRef("name") String name,
            @AttrRef("floorNum") Integer floorNum,
            @AttrRef("price") Long price,
            @AttrRef("roomType") RoomType roomType
    ) throws ConstraintViolationException {
        this.id = nextId(id);
        this.name = nextName(name, floorNum);

        setFloorNum(floorNum);
        setPrice(price);
        setRoomType(roomType);

    }


    private static int nextId(Integer currID) {
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
    @DOpt(type=DOpt.Type.DerivedAttributeUpdater)
    @AttrRef(value=R_name)
    private static String nextName(String currName, int floorNum) {
        Tuple derivingVal = Tuple.newInstance(floorNum);
        if (currName == null) { // generate one
            Integer currNum = currNums.get(derivingVal);
            if (currNum == null) {
                currNum = floorNum * 100 +1;
            } else {
                currNum++;
            }
            currNums.put(derivingVal, currNum);
            return "R" + currNum;
        } else { // update
            int num;
            try {
                num = Integer.parseInt(currName.substring(1));
            } catch (RuntimeException e) {
                throw new ConstraintViolationException(
                        ConstraintViolationException.Code.INVALID_VALUE, e,
                        "Lỗi giá trị thuộc tính: {0}", currName);
            }

            Integer currMaxVal = currNums.get(derivingVal);
            if (currMaxVal == null || num > currMaxVal) {
                currNums.put(derivingVal, num);
            }

            return currName;
        }
    }
        /**
         * @requires minVal != null /\ maxVal != null
         * @effects update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
         */
        @DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
        public static void updateAutoGeneratedValue(
                DAttr attrib,
                Tuple derivingValue,
                Object minVal,
                Object maxVal) throws ConstraintViolationException {
            if (minVal != null && maxVal != null) {
                if (attrib.name().equals("id")) {
                    int maxIdVal = (Integer) maxVal;
                    if (maxIdVal > idCounter)
                        idCounter = maxIdVal;

                } else if (attrib.name().equals(R_name)) {
                    String maxName = (String) maxVal;

                    try {
                        int maxNameNum = Integer.parseInt(maxName.substring(1));

                        // current max num for the semester
                        Integer currNum = currNums.get(derivingValue);

                        if (currNum == null || maxNameNum > currNum) {
                            currNums.put(derivingValue, maxNameNum);
                        }

                    } catch (RuntimeException e) {
                        throw new ConstraintViolationException(
                                ConstraintViolationException.Code.INVALID_VALUE, e, new Object[]{maxName});
                    }
                }

            }
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

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public String getName() {
        return name;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public boolean addRoomOrder(RoomOrder order) {
        if (!this.roomOrders.contains(order)) {
            roomOrders.add(order);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewRoomOrder(RoomOrder order) {
        roomOrders.add(order);
        roomOrderCount++;
        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addRoomOrder(Collection<RoomOrder> orders) {
        for (RoomOrder o : orders) {
            if (!this.roomOrders.contains(o)) {
                this.roomOrders.add(o);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public boolean addNewRoomOrder(Collection<RoomOrder> orders) {
        this.roomOrders.addAll(orders);
        roomOrderCount += orders.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean removeRoomOrder(RoomOrder o) {
        boolean removed = roomOrders.remove(o);

        if (removed) {
            roomOrderCount--;
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type=DOpt.Type.Setter)
    public void setRoomOrder(Collection<RoomOrder> orders) {
        this.roomOrders = orders;

        roomOrderCount = orders.size();
    }

    /**
     * @effects
     *  return <tt>orderCount</tt>
     */
    @DOpt(type=DOpt.Type.LinkCountGetter)
    public Integer getRoomOrderCount() {
        return roomOrderCount;
    }

    @DOpt(type=DOpt.Type.LinkCountSetter)
    public void setRoomOrderCount(int count) {
        roomOrderCount = count;
    }

    @DOpt(type=DOpt.Type.Getter)
    public Collection<RoomOrder> getRoomOrders() {
        return roomOrders;
    }




    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", price=" + price +
                ", floorNum=" + floorNum +
                ", name='" + name +
                ", roomType=" + roomType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room other = (Room) o;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}

