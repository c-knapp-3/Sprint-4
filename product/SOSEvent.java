package sprint4;

import java.util.Objects;

import sprint4.Board.Cell;

// Represents an event in the SOS game, capturing details about a move
public class SOSEvent {
	
    private Cell cell;
    private int row;
    private int column;
    private String direction;

    public SOSEvent(Cell cell, int row, int column, String direction) {
        this.cell = cell;
        this.row = row;
        this.column = column;
        this.direction = direction;  // The direction in which the event occurred
    }
    
    public Cell getCell() {
    	return cell;
    }
    
    public void setCell(Cell cell) {
    	this.cell = cell;
    }
    
    public int getRow() {
    	return row;
    }
    
    public void setRow(int row) {
    	this.row = row;
    }
    
    public int getColumn() {
    	return column;
    }
    
    public void setColumn(int column) {
    	this.column = column;
    }  
    
    public String getDirection() {
        return direction;
    }
    
    // Provides a string representation of the event
    @Override
    public String toString() {
        return String.format("Cell: %s, Row: %d, Column: %d, Direction: %s", cell, row, column, direction);
    }
     
    // Compares event based on cell, row, column and direction
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SOSEvent otherEvent = (SOSEvent) obj;
        return row == otherEvent.row && column == otherEvent.column && Objects.equals(cell, otherEvent.cell)
                && Objects.equals(direction, otherEvent.direction);
    }

    // Generate a hash code for the event based on cell, row, column and direction
    @Override
    public int hashCode() {
        return Objects.hash(cell, row, column, direction);
    }
}
