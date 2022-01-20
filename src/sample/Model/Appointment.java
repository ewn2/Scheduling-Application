package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.Vector;

public class Appointment {
    private int AppointmentID;
    private String AppointmentTitle;
    private String AppointmentDesc;
    private String AppointmentLocation;
    private String AppointmentContact;
    private String AppointmentType;
    private String AppointmentStartDateTime;//YYYY-MM-DD hh:mm:ss
    private String AppointmentEndDateTime;//YYYY-MM-DD hh:mm:ss
    private int AppointmentCustomerID;
    private int AppointmentUserID;
    //SQL DATETIME format that database will expect: "YYYY-MM-DD hh:mm:ss"
    //minus quotation marks.


    public Appointment(int AppointmentID, String AppointmentTitle, String AppointmentDesc, String AppointmentLocation, String AppointmentContact, String AppointmentType, String AppointmentStartDateTime, String AppointmentEndDateTime, int AppointmentCustomerID, int AppointmentUserID) {
        this.AppointmentID = AppointmentID;
        this.AppointmentTitle = AppointmentTitle;
        this.AppointmentDesc = AppointmentDesc;
        this.AppointmentLocation = AppointmentLocation;
        this.AppointmentContact = AppointmentContact;
        this.AppointmentType = AppointmentType;
        this.AppointmentStartDateTime = AppointmentStartDateTime;
        this.AppointmentEndDateTime = AppointmentEndDateTime;
        this.AppointmentCustomerID = AppointmentCustomerID;
        this.AppointmentUserID = AppointmentUserID;
    }

    public int getAppointmentID() {
        return AppointmentID;
    }
    public void setAppointmentID(int AppointmentID) {
        this.AppointmentID = AppointmentID;
    }

    public String getAppointmentTitle() {
        return AppointmentTitle;
    }
    public void setAppointmentTitle(String AppointmentTitle) {
        this.AppointmentTitle = AppointmentTitle;
    }

    public String getAppointmentDesc() {
        return AppointmentDesc;
    }
    public void setAppointmentDesc(String AppointmentDesc) {
        this.AppointmentDesc = AppointmentDesc;
    }

    public String getAppointmentLocation() {
        return AppointmentLocation;
    }
    public void setAppointmentLocation(String AppointmentLocation) {
        this.AppointmentLocation = AppointmentLocation;
    }

    public String getAppointmentContact() {
        return AppointmentContact;
    }
    public void setAppointmentContact(String AppointmentContact) {
        this.AppointmentContact = AppointmentContact;
    }

    public String getAppointmentType() {
        return AppointmentType;
    }
    public void setAppointmentType(String AppointmentType) {
        this.AppointmentType = AppointmentType;
    }

    public String getAppointmentStartDateTime() {
        return AppointmentStartDateTime;
    }
    public void setAppointmentStartDateTime(String AppointmentStartDateTime) {
        this.AppointmentStartDateTime = AppointmentStartDateTime;
    }

    public String getAppointmentEndDateTime() {
        return AppointmentEndDateTime;
    }
    public void setAppointmentEndDateTime(String AppointmentEndDateTime) {
        this.AppointmentEndDateTime = AppointmentEndDateTime;
    }

    public int getAppointmentCustomerID() {
        return AppointmentCustomerID;
    }
    public void setAppointmentCustomerID(int AppointmentCustomerID) {
        this.AppointmentCustomerID = AppointmentCustomerID;
    }

    public int getAppointmentUserID() {
        return AppointmentUserID;
    }
    public void setAppointmentUserID(int AppointmentUserID) {
        this.AppointmentUserID = AppointmentUserID;
    }

    private static int appointmentIDGenerated = -1;

    public static Vector<Integer> usedAppointmentIDs = new Vector<>(0);

    public static int uniqueAppointmentID() {
        appointmentIDGenerated++;
        while (usedAppointmentIDs.contains(appointmentIDGenerated)) {
            appointmentIDGenerated++;
        }
        usedAppointmentIDs.add(appointmentIDGenerated);
        return appointmentIDGenerated;
    }

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    public static ObservableList<Appointment> appointmentPopulation() {
        return allAppointments;
    }
    public static void addAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    }
    public static Appointment lookupAppointment(int appointmentID) {
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentID() == appointmentID) {
                return appointment;
            }
        }
        return null;
    }
    public static ObservableList<Appointment> lookupAppointment(String appointmentTitle) {
        ObservableList<Appointment> matches = FXCollections.observableArrayList();
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentTitle().toUpperCase().contains((String.valueOf(appointment)).toUpperCase())) {
                matches.add(appointment);
            }
        }
        return matches;
    }
    public static void updateAppointment(int index, Appointment selectedAppointment) {
        allAppointments.set(index, selectedAppointment);
    }
    public static boolean deleteAppointment(Appointment selectedAppointment) {
        if (allAppointments.contains(selectedAppointment)) {
            allAppointments.remove(selectedAppointment);
            return true;
        }
        return false;
    }
}
