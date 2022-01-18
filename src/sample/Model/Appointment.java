package sample.Model;

import java.time.LocalDateTime;

public class Appointment {
    private int AppointmentID;
    private String AppointmentTitle;
    private String AppointmentDesc;
    private String AppointmentLocation;
    private String AppointmentContact;
    private String AppointmentType;
    private String AppointmentStartDate;//YYYY-MM-DD
    private String AppointmentStartTime;//hh:mm:ss
    private String AppointmentEndDate;//YYYY-MM-DD
    private String AppointmentEndTime;//hh:mm:ss
    private int AppointmentCustomerID;
    private int AppointmentUserID;
    //SQL DATETIME format that database will expect: "YYYY-MM-DD hh:mm:ss"
    //minus quotation marks.
}
