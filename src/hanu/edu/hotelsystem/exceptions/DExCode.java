package hanu.edu.hotelsystem.exceptions;

import domainapp.basics.util.InfoCode;

import java.text.MessageFormat;



    /**
     * @overview Capture domain-specific exception codes.
     *
     * <p><b>Enum value format</b>: <tt>VALUE("...{0}...{1}...")</tt>
     *  where <t>{0}, {1}, ....</tt> are template variables that will be replaced by run-time values
     *
     * <p><b>Example</b>:
     *
     * <br>enum value: INVALID_DOB("Date of birth {0} is not a valid date")
     *
     * <br>usage: <pre>
     * // an error discovered in some code that validates a dob would throw this exception:
     *
     *    throw new ConstraintViolationException(INVALID_DOB, dob)
     *  </pre>
     *
     *  Here, <tt>dob.toString()</tt> is the run-time value that replaces the template variable {0} in
     *  the enum INVALID_DOB.
     *
     *
     *
     */
    public enum DExCode implements InfoCode {

        /**
         * 0: date of birth
         */
        INVALID_DOB("Date of birth {0} is not a valid date"),

        /**
         * 0: created date
         */
        INVALID_CREATED_DATE("Created date is not a valid date"),
        /**
         * 0: created service order date
         */
        INVALID_CREATED_SERVICE_ORDER_DATE("Created date must be from start date to end date of reservation"),
        /**
         * 0: start date
         */
        INVALID_START_DATE("Start date must be set after created date and before end date"),

        /**
         * 0: end date
         */
        INVALID_END_DATE("End date must be set after created date and start date"),


        ;

        /**
         * THE FOLLOWING CODE (EXCEPT FOR THE CONSTRUCTOR NAME) MUST BE KEPT AS IS
         */
        private final String text;

        /**The {@link MessageFormat} object for formatting {@link #text}
         * using context-specific data arguments
         */
        private MessageFormat messageFormat;

        DExCode(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public MessageFormat getMessageFormat() {
            if (messageFormat == null) {
                messageFormat = new MessageFormat(text);
            }
            return messageFormat;
        }
    }


