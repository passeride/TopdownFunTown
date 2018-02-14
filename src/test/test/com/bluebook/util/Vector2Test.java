package test.com.bluebook.util;

import com.bluebook.util.Vector2;

import static org.junit.jupiter.api.Assertions.*;

class Vector2Test {

    @org.junit.jupiter.api.Test
    void vector2FromAgnleInRadians() {
        // Arrange
        Vector2 v = Vector2.Vector2FromAgnleInRadians(1);
        // Act
        double returnValue = v.getAngleInRadians();
        // Assert
        assertEquals(1, v.getLength(), "Length was not a match");
        assertEquals(1, returnValue, "Angle in radians was not a match");
    }

    @org.junit.jupiter.api.Test
    void vector2FromAngleInDegrees() {
        // Arrange
        Vector2 v = Vector2.Vector2FromAngleInDegrees(45);
        // Act
        double returnValue = v.getAngleInDegrees();
        // Assert
        assertEquals(1, v.getLength(), "Length was not a match");
        assertEquals(45, returnValue, "Angle in degree was not a match");
    }

    @org.junit.jupiter.api.Test
    void getLength() {
        // Arrange
        Vector2 v = new Vector2(1, 1);
        // Act
        double returnValue = v.getLength();
        // Assert
        assertEquals(1.4142135623730951, returnValue, "Length was not a match");
    }

    @org.junit.jupiter.api.Test
    void distance() {
        // Arrange
        Vector2 v1 = new Vector2(5, 5);
        Vector2 v2 = new Vector2(5, 6);
        // Act
        double distance = v1.distance(v2);
        // Assert
        assertEquals(1, distance, "Distance between vectors was not expected");
    }

    @org.junit.jupiter.api.Test
    void getNormalizedVector() {
        // Arrange
        Vector2 v1 = new Vector2(5, 5);
        //  Act
        Vector2 v2 = v1.getNormalizedVector();
        double distance = v2.getLength();
        // Assert
        assertEquals(1, distance, 0.001, "Length of normalized vector was not excpedted");

    }

    @org.junit.jupiter.api.Test
    void normalize() {
        // Arrange
        Vector2 v1 = new Vector2(5, 5);
        //  Act
        v1.normalize();
        double distance = v1.getLength();
        // Assert
        assertEquals(1, distance, 0.001,"Length of normalized vector was not excpedted");
    }

}