package edu.wpi.teamo.database.request;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.database.Database;

import java.time.LocalDateTime;
import java.sql.SQLException;
import java.util.stream.Stream;

public abstract class ExtendedBaseRequest<T> extends RecursiveTreeObject<T> {
    BaseRequest base;

    public ExtendedBaseRequest(BaseRequest base) {
        this.base = base;
    }

    public Stream<String> getLocations() {
        return base.getLocations();
    }

    public void setLocations(Stream<String> locationIDs) throws SQLException {
        base.setLocations(locationIDs);
    }

    public boolean isComplete() {
        return base.isComplete();
    }

    public void setComplete(boolean complete) throws SQLException {
        base.setComplete(complete);
    }

    public String getAssigned() {
        return base.getAssigned();
    }

    public void setAssigned(String assigned) throws SQLException {
        base.setAssigned(assigned);
    }

    public String getDetails() {
        return base.getDetails();
    }

    public void setDetails(String details) throws SQLException {
        base.setDetails(details);
    }

    public String getID() {
        return base.getId();
    }

    public LocalDateTime getDue() {
        return base.getDue();
    }

    public void setDue(LocalDateTime due) throws SQLException {
        base.setDue(due);
    }

    public String getUser() {
        return base.getRequesterUsername();
    }


    public LocalDateTime getTimestamp() {
        return base.getTimestamp();
    }


    public String getRequesterUsername() {
        return base.getRequesterUsername();
    }

    public abstract void delete() throws SQLException;
}
