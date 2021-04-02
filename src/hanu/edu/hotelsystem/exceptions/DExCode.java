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
         * 0: address
         */
        INVALID_CREATED_DATE("Date is not a valid date"),

        /**
         * 0: email
         */
        INVALID_START_DATE("Date is not a valid date"),

        /**
         * 0: exam mark
         */
        INVALID_END_DATE("Date is not a valid date"),

        /**
         * 0: internal mark
         */
        INVALID_INTERNAL_MARK("Internal Mark {0} is not a valid internal mark"),

        /**
         * 0: semester
         */
        INVALID_SEMESTER("Semester {0} is not a valid semester"),

        /**
         * 0: credits
         */
        INVALID_CREDITS("Credits {0} is not a valid credits"),

        /**
         * 0: name
         */
        INVALID_NAME("Name {0} is not a valid name"),
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


