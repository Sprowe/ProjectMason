package edu.wpi.teamo.database.request;

import java.util.stream.Stream;
import java.sql.SQLException;

public interface IRequestService {

    /* create and store requests */
    String requestMedicine(String type, String amount, Stream<String> locationIDs, String assigned) throws SQLException;
    String requestSanitation(Stream<String> locationID, String assigned, String details) throws SQLException;

    /* get all */
    Stream<SanitationRequest> getAllSanitationRequests() throws SQLException;
    Stream<MedicineRequest> getAllMedicineRequests() throws SQLException;

    /* get one */
    SanitationRequest getSanitationRequest(String id) throws SQLException;
    MedicineRequest getMedicineRequest(String id) throws SQLException;

    /* remove one */
    void removeSanitationRequest(String requestID) throws SQLException;
    void removeMedicineRequest(String requestID) throws SQLException;

    /* mark them as completed */
    void setSanitationCompleted(String requestID) throws SQLException;
    void setMedicineCompleted(String requestID) throws SQLException;

    /* remove them */
    boolean sanitationRequestExists(String requestID);
    boolean medicineRequestExists(String requestID);

    void closeConnection() throws SQLException;
}
