package hanu.edu.hotelsystem.services.Reservation.report;

import domainapp.basics.core.dodm.dsm.DSMBasic;
import domainapp.basics.core.dodm.qrm.QRM;
import domainapp.basics.exceptions.DataSourceException;
import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.model.Oid;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.MetaConstants;
import domainapp.basics.model.meta.Select;
import domainapp.basics.model.query.Expression;
import domainapp.basics.model.query.Query;
import domainapp.basics.model.query.QueryToolKit;
import domainapp.basics.modules.report.model.meta.Output;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.Reservation.model.Status;

import java.util.Collection;
import java.util.Map;

@DClass(schema = "hotelsystem", serialisable = false)
public class ReservationByStatusReport {
    @DAttr(name = "id", id = true, auto = true, type = DAttr.Type.Integer, length = 5, optional = false, mutable = false)
    private int id;
    private static int idCounter = 0;

    @DAttr(name = "status", type = DAttr.Type.Domain, length = 8, serialisable = false, optional = false)
    private Status status;


    /**output: students whose names match {@link #status} */
    @DAttr(name="students",type= DAttr.Type.Collection,optional=false, mutable=false,
            serialisable=false,filter=@Select(clazz= Reservation.class,
            attributes={Reservation.Attr_ID, Reservation.Attr_customer, Reservation.Attr_CreatedAt,
                    Reservation.Attr_StartDate, Reservation.Attr_EndDate, Reservation.AttributeName_Status, })
            ,derivedFrom={"status"}
    )
    @DAssoc(ascName="reservations-by-name-report-has-reservations",role="report",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type=Reservation.class,cardMin=0,cardMax= MetaConstants.CARD_MORE))
    @Output
    private Collection<Reservation> reservations;

    @DAttr(name = "numReservations", type = DAttr.Type.Integer, length = 20, auto=true, mutable=false)
    @Output
    private int numReservations;

    /**
     * @effects
     *  initialise this with <tt>status</tt> and use {@link QRM} to retrieve from data source
     *  all {@link Reservation} whose status match <tt>status</tt>.
     *  initialise {@link #reservations} with the result if any.
     *
     *  <p>throws NotPossibleException if failed to generate data source query;
     *  DataSourceException if fails to read from the data source
     *
     */
    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    @DOpt(type=DOpt.Type.RequiredConstructor)
    public ReservationByStatusReport(@AttrRef("status") Status status) throws NotPossibleException, DataSourceException {
        this.id=++idCounter;

        this.status = status;

        doReportQuery();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) throws NotPossibleException, DataSourceException{
        this.status = status;

        doReportQuery();
    }

    @DOpt(type=DOpt.Type.DerivedAttributeUpdater)
    @AttrRef(value="reservations")
    public void doReportQuery() throws NotPossibleException, DataSourceException {
        // the query manager instance

        QRM qrm = QRM.getInstance();

        // create a query to look up Reservation from the data source
        // and then populate the output attribute (reservations) with the result
        DSMBasic dsm = qrm.getDsm();

        //TODO: to conserve memory cache the query and only change the query parameter value(s)
        Query q = QueryToolKit.createSearchQuery(dsm, Reservation.class,
                new String[]{Reservation.AttributeName_Status},
                new Expression.Op[]{Expression.Op.MATCH},
                new Object[]{status});

        Map<Oid, Reservation> result = qrm.getDom().retrieveObjects(Reservation.class, q);

        if (result != null) {
            // update the main output data
            reservations = result.values();

            // update other output (if any)
            numReservations = reservations.size();
        } else {
            // no data found: reset output
            resetOutput();
        }
    }
    /**
     * @effects
     *  reset all output attributes to their initial values
     */
    private void resetOutput() {
        reservations = null;
        numReservations = 0;
    }

    /**
     * A link-adder method for {@link #reservations}, required for the object form to function.
     * However, this method is empty because reservations have already be recorded in the attribute {@link #reservations}.
     */
    @DOpt(type=DOpt.Type.LinkAdder)
    public boolean addReservation(Collection<Reservation> reservations) {
        // do nothing
        return false;
    }

    /**
     * @effects return reservations
     */
    public Collection<Reservation> getStudents() {
        return reservations;
    }

    /**
     * @effects return numReservations
     */
    public int getNumReservations() {
        return numReservations;
    }

    /**
     * @effects return id
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReservationByStatusReport other = (ReservationByStatusReport) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ReservationByStatusReport{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
