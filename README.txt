Meeting Scheduling System

Application acts as an appointment scheduling system for an internationally operating business that
conforms to American Eastern business hours of 8:00 to 22:00. Users possessing valid login credentials may
add new, modify, or delete Customers and Appointments within the connected SQL database whose structure and
entries are otherwise restricted as they are used in other systems for the business and so may not be modified.
Users will only be presented with their local System time and at no point will have to make conversions themselves,
and the login form specifically will load French translations for all labels if the User's primary system language
is set to French.

Making use of the operation is dependent on the inclusion of the proper Java SDK and JavaFx versions as seen above and a
connection to an already structured and populated MySQL database through a mysql Java connector as seen below.
Once all of these necessary libraries and tools are accounted for (and are actually loaded, the virtual machine during
development held the CLI arguments "--module-path ${PATH_TO_FX} --add-modules javafx.fxml,javafx.controls,javafx.graphics"
sans quotations prepopulated for operational testing) the application may be compiled and launched with a call to
method main in Main.Java, within the project at location "ScheduleSystemJFX.zip/HelloWorldJFX/src/sample/Main.java"

Once run, the User will be faced with a login screen where they must provide a valid Username and Password to continue,
a combination which exists within the connected database users Table such as username:"test" and password:"test".
If successfully logged in, the User may review all Customers or Appointments with their times adjusted to be shown
in the local System's equivalent timezone values of the appointments, as well as add, modify, and remove them.
A Reports page may also be brought up by the User which showcases a breakdown of all Appointments by month of occurrence,
Type descriptor value, both Month and Type description, all appointments for an individually selected Contact, and all
appointments for an individually selected Customer.
