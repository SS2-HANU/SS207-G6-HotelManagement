package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;

import java.util.Date;
import java.util.Objects;

@DClass(schema = "hotelsystem" )
public class Customer extends Person {
    public static final String C_code = "code";

    @DAttr(name = C_code, id = true, type = DAttr.Type.String, auto = true, length = 6,
            mutable = false, optional = false)
    private String code;

    private static int counter;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public Customer(
            @AttrRef("name") String name,
            @AttrRef("gender") Gender gender,
            @AttrRef("dob") Date dob,
            @AttrRef("address") Address address,
            @AttrRef("phoneNum") String phoneNum){
        this(null, name, gender,dob, address, phoneNum);
    }

    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Customer(
            @AttrRef("code") String code,
            @AttrRef("name") String name,
            @AttrRef("gender") Gender gender,
            @AttrRef("dob") Date dob,
            @AttrRef("address") Address address,
            @AttrRef("phoneNum") String phoneNum
    ) throws ConstraintViolationException {
        super(name, gender, dob, address, phoneNum);
        this.code = nextCode(code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    private String nextCode(String code) throws ConstraintViolationException {
        if (code == null) { // generate a new id
            if (counter == 0) {
                counter++;
            }
            return "C" + counter;
        } else {
            // update id
            int num;
            try {
                num = Integer.parseInt(code.substring(1));
            } catch (RuntimeException e) {
                throw new ConstraintViolationException(
                        ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { code});
            }
            if (num > counter) {
                counter = num;
            }
            return code;
        }
    }
}
